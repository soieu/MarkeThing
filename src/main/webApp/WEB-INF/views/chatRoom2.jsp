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
    <div id="messages"></div>
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
  var nickname = '';
  var chatRoomId = 1; // Default chat room ID
  var senderId = 3; // Default sender ID

  function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
      console.log('Connected: ' + frame);
      stompClient.subscribe('/sub/chat/room', function(userInfo) {
        var user = JSON.parse(userInfo.body);
        nickname = user.nickname;
        senderId = user.senderId;
        console.log('Nickname: ' + nickname + ', Sender ID: ' + senderId);
      });
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
    if (message.senderId === senderId) {
      messageElement.classList.add('own-message');
      messageElement.innerText = message.content;
    } else {
      messageElement.classList.add('other-message');
      messageElement.innerText = message.content;
    }
    document.getElementById('messages').appendChild(messageElement);
    document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;
  }
  function handleKeyPress(event) {
    if (event.keyCode === 13) { // 13 is the key code for Enter
      sendMsg();
    }
  }

  $(document).ready(function() {
    connect();
  });
</script>
</body>
</html>
