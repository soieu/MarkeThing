<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Chat Rooms</title>
    <link rel="stylesheet" type="text/css" href="/static/css/chatRooms.css">
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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="/static/js/chatRooms.js"></script>
</body>
</html>
