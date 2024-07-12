<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.chat.dto.ChatMessageResponseDto" %>
<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Pub/Sub Chat</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
        background-color: #f2f2f2;
      }
      #chat-container {
        width: 80%;
        max-width: 800px;
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        padding: 20px;
      }
      #messages {
        border: 1px solid #ccc;
        padding: 10px;
        height: 500px; /* 채팅창 높이 설정 */
        overflow-y: scroll; /* 스크롤 가능하도록 설정 */
        display: flex;
        flex-direction: column;
      }
      .message {
        margin-bottom: 10px;
        padding: 10px;
        border-radius: 10px;
        max-width: 80%;
        word-wrap: break-word;
      }
      .own-message {
        background-color: #dcf8c6;
        align-self: flex-end;
        text-align: right;
      }
      .other-message {
        background-color: #e3e3e3;
        align-self: flex-start;
        text-align: left;
      }
      .message-time {
        font-size: 0.8em;
        color: #888;
        margin-top: 5px;
      }
      #msg {
        width: calc(100% - 70px);
        padding: 10px;
        margin-right: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 14px;
      }
      #send-btn {
        padding: 10px 20px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 14px;
      }
    </style>
</head>
<body>
<div id="chat-container">
    <div id="messages">
        <%
            List<ChatMessageResponseDto> chatMessages = (List<ChatMessageResponseDto>) request.getAttribute("chatMessages");
            Long userId = (Long) request.getAttribute("userId");
            for (ChatMessageResponseDto chatMessage : chatMessages) {
                String messageClass = chatMessage.getSenderId().equals(userId) ? "own-message" : "other-message";
        %>
        <div class="message <%= messageClass %>">
            <%= chatMessage.getContent() %>
            <div class="message-time"><%= chatMessage.getTime() %></div>
        </div>
        <%
            }
        %>
    </div>
    <div style="display: flex; align-items: center; margin-top: 10px;">
        <input type="text" id="msg" placeholder="Enter your message" onkeypress="handleKeyPress(event)">
        <button id="send-btn" onclick="sendMsg()">Send</button>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script>
  var stompClient = null;
  var chatRoomId = "<%= request.getAttribute("chatRoomId") %>";
  var senderId = "<%= request.getAttribute("userId") %>";

  function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
      console.log('Connected: ' + frame);
      stompClient.subscribe('/sub/chat/room/' + chatRoomId, function(messageOutput) {
        showMessage(JSON.parse(messageOutput.body));
      });
    }, function(error) {
      console.error('STOMP error:', error);
    });
  }

  function sendMsg() {
    var message = {
      chatRoomId: chatRoomId,
      senderId: senderId,
      content: $('#msg').val()
    };
    stompClient.send("/pub/sendMessage", {}, JSON.stringify(message));
    $('#msg').val('');
  }

  function showMessage(message) {
    var messageElement = document.createElement('div');
    messageElement.className = 'message';
    var messageTime = document.createElement('div');
    messageTime.className = 'message-time';

    if (message.senderId == senderId) {
      messageElement.classList.add('own-message');
    } else {
      messageElement.classList.add('other-message');
    }
    messageElement.innerText = message.content;
    messageTime.innerText = message.time ? message.time : formatAMPM(new Date());

    messageElement.appendChild(messageTime);
    document.getElementById('messages').appendChild(messageElement);
    document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;
  }

  function handleKeyPress(event) {
    if (event.keyCode === 13) { // 13 is the key code for Enter
      sendMsg();
    }
  }

  function formatAMPM(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? '오후' : '오전';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var strTime = hours + ':' + minutes + ' ' + ampm;
    return strTime;
  }

  $(document).ready(function() {
    connect();
    document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;
  });
</script>
</body>
</html>
