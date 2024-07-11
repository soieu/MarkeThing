<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Chat Rooms</title>
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
      .chat-room {
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 10px;
        border-radius: 10px;
        background-color: #e3e3e3;
        cursor: pointer;
      }
      .chat-room:hover {
        background-color: #ddd;
      }
      .chat-room-title {
        font-weight: bold;
        margin-bottom: 5px;
      }
      .chat-room-message {
        color: #555;
      }
      .chat-room-time {
        text-align: right;
        color: #999;
        font-size: 12px;
      }
    </style>
    <script>
      function navigateToChatRoom(chatRoomId) {
        window.location.href = 'http://localhost:8080/test/chat1/' + chatRoomId;
      }
    </script>
</head>
<body>
<div id="chat-container">
    <h1>Chat Rooms</h1>
    <c:forEach var="chatRoom" items="${chatRooms}">
        <div class="chat-room" onclick="navigateToChatRoom(${chatRoom.chatRoomId})">
            <div class="chat-room-title">${chatRoom.title}</div>
            <div class="chat-room-message">${chatRoom.lastChatMessage}</div>
            <div class="chat-room-time">${chatRoom.time}</div>
        </div>
    </c:forEach>
</div>
</body>
</html>
