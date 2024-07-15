
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
  if (chatRoomStatus === 1) {
    $('#msg').prop('disabled', true);
    $('#send-btn').addClass('disabled-btn').prop('disabled', true);
    $('.left-chat-room').show(); // 상대방이 나갔다는 메시지 보이기
  } else {
    connect();
    document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;
  }
});
