$(document).ready(function () {
  // 파일 선택 시 파일명 표시
  $(".custom-file-input").on("change", function() {
    var fileName = $(this).val().split("\\").pop();
    $(this).siblings(".custom-file-label").addClass("selected").text(fileName);
  });

  // 업로드 버튼 클릭 시 파일 업로드 실행
  $("#uploadButton").click(function () {
    var file = $("#profilePicture")[0].files[0];
    if (file) {
      uploadProfilePicture(file);
    } else {
      alert("파일을 선택하세요.");
    }
  });

  function uploadProfilePicture(file) {
    var formData = new FormData();
    formData.append("file", file);

    $.ajax({
      url: "/api/upload", // 파일 업로드 API 엔드포인트
      type: 'POST',
      data: formData,
      async: false,
      cache: false,
      contentType: false,
      processData: false,
      success: function (data) {
        alert("프로필 사진 업로드 성공: "); // 업로드된 URL을 경고창에 표시
        $("#profileImg").val(data); // 업로드된 프로필 사진 링크를 숨겨진 필드에 설정
        $("#profilePictureDisplay").attr("src", data).show(); // 프로필 사진 미리보기 설정
      },
      error: function (error) {
        alert("프로필 사진 업로드에 실패했습니다: " + error.responseText);
      }
    });
  }

  // 회원 가입 폼 제출 시 Ajax 요청
  $("#signupForm").submit(function (event) {
    event.preventDefault(); // 기본 동작 중단
    var userData = {
      email: $("#email").val(),
      password: $("#password").val(),
      name: $("#name").val(),
      nickname: $("#nickname").val(),
      phoneNumber: $("#phoneNumber").val(),
      address: $("#address").val(),
      profileImg: $("#profileImg").val() // 프로필 링크는 숨겨진 필드에서 가져옴
    };
    // Ajax 요청
    $.ajax({
      url: "/api/users/sign-up",
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(userData),
      success: function (response) {
        // 회원 가입 성공 시 처리
        $("#message").text("회원 가입이 완료되었습니다.");
      },
      error: function (error) {
        $("#message").text("회원 가입에 실패했습니다: " + error.responseText);
      }
    });
  });
});