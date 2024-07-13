function navigateToChatRoom(chatRoomId, userId) {
  window.location.href = "/view/chat/rooms/" + chatRoomId + "/user/" + userId;
}

function leaveChatRoom(chatRoomId, userId) {
  const encodedChatRoomId = encodeURIComponent(chatRoomId);
  const encodedUserId = encodeURIComponent(userId);

  fetch("/api/chat/rooms/" + encodedChatRoomId + "/user/" + encodedUserId, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.ok) {
      alert("채팅방 나가기 완료");
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