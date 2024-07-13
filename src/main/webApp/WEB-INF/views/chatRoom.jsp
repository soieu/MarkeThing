<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.chat.dto.ChatMessageResponseDto" %>
<!DOCTYPE html>
<html>
<head>
    <title>ChatRoom</title>
    <link rel="stylesheet" type="text/css" href="/static/css/chatRoom.css">
</head>
<body>
<div id="chat-container">
    <div id="messages">
        <% List<ChatMessageResponseDto> chatMessages = (List<ChatMessageResponseDto>) request.getAttribute("chatMessages");
            Long userId = (Long) request.getAttribute("userId");
            for (ChatMessageResponseDto chatMessage : chatMessages) {
                String messageClass = chatMessage.getSenderId().equals(userId) ? "own-message" : "other-message";
        %>
        <div class="message <%= messageClass %>">
            <%= chatMessage.getContent() %>
            <div class="message-time"><%= chatMessage.getTime() %></div>
        </div>
        <% } %>
    </div>

    <div style="display: flex; align-items: center; margin-top: 10px;">
        <input type="text" id="msg" placeholder="Enter your message" onkeypress="handleKeyPress(event)">
        <button id="send-btn">Send</button>
    </div>

    <div id="left-chat-room-msg" class="left-chat-room">상대방이 나갔습니다.</div>
</div>

<script>
  var chatRoomId = "<%= request.getAttribute("chatRoomId") %>";
  var senderId = "<%= request.getAttribute("userId") %>";
  var chatRoomStatus = <%= request.getAttribute("chatRoomStatus") %>; // chatRoomStatus 값을 JSP에서 가져옵니다.
</script>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="/static/js/chatRoom.js"></script>
</body>
</html>
