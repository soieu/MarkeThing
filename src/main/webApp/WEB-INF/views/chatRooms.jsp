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
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .chat-room:hover {
        background-color: #ddd;
      }
      .chat-room-info {
        flex-grow: 1;
        margin-right: 10px;
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
      .leave-button {
        background-color: #ff4d4d;
        color: white;
        border: none;
        border-radius: 5px;
        padding: 5px 10px;
        cursor: pointer;
      }
      .leave-button:hover {
        background-color: #ff1a1a;
      }
    </style>
    <script>
      function navigateToChatRoom(chatRoomId, userId) {
        window.location.href = "http://localhost:8080/view/chat/rooms/"+chatRoomId+"/user/"+userId;
      }

      function leaveChatRoom(chatRoomId, userId) {
        const encodedChatRoomId = encodeURIComponent(chatRoomId);
        const encodedUserId = encodeURIComponent(userId);

        fetch("/api/chat/rooms/" + encodedChatRoomId + "/user/" + encodedUserId, {
          method: 'DELETE'
        })
        .then(response => {
          if (response.ok) {
            alert("채팅방 나가기 완료")
            console.log("Left chat room with ID: " + chatRoomId);
            // Remove the chat room element from the DOM
            document.getElementById("chat-room-" + chatRoomId).remove();
            // Reload the current page
            window.location.reload();
          } else {
            console.error("Failed to leave chat room with ID: " + chatRoomId);
          }
        })
        .catch(error => {
          console.error("Error leaving chat room with ID: " + chatRoomId, error);
        });
      }


    </script>
</head>
<body>
<div id="chat-container">
    <h1>Chat Rooms</h1>
    <c:forEach var="chatRoom" items="${chatRooms}">
        <div id="chat-room-${chatRoom.chatRoomId}" class="chat-room" onclick="navigateToChatRoom(${chatRoom.chatRoomId}, ${userId})">
            <div class="chat-room-info">
                <div class="chat-room-title">${chatRoom.title}</div>
                <div class="chat-room-message">${chatRoom.lastChatMessage}</div>
                <div class="chat-room-time">${chatRoom.time}</div>
            </div>
            <button class="leave-button" onclick="event.stopPropagation(); leaveChatRoom(${chatRoom.chatRoomId}, ${userId});">Leave</button>
        </div>
    </c:forEach>
</div>
</body>
</html>
