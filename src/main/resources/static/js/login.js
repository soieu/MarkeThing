$(document).ready(function () {
  $("#loginForm").submit(function (event) {
    event.preventDefault(); // 기본 동작 중단

    var formData = {
      username: $("#email").val(),
      password: $("#password").val()
    };

    $.ajax({
      url: "/login",
      type: 'POST',
      data: $.param(formData), // 데이터를 URL-encoded string 형식으로 전송
      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
      success: function (data, textStatus, jqXHR) {
        var authToken = jqXHR.getResponseHeader('Authorization');
        localStorage.setItem('AuthToken', authToken);
        // localStorage.setItem('authToken', authToken);
        // 로그인 성공 시 처리
        $("#message").text("로그인에 성공했습니다.").css("color", "green");
        // 리디렉션 또는 추가적인 성공 처리 로직 작성
      },
      error: function (jqXHR, textStatus, errorThrown) {
        $("#message").text("로그인에 실패했습니다: " + errorThrown).css("color", "red");
      }
    });
  });
});
