<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/header.jsp" %>
<html>
<head>
    <title>Title</title>
    <script src="/static/js/communities.js"></script>
</head>
<body>
    <label for="area">지역</label>
    <form id="area" name="area" method="post">
        <input type="checkbox" value="SEOUL">서울</input>
        <input type="checkbox" value="BUSAN">부산</input>
        <input type="checkbox" value="DAEGU">대구</input>
        <input type="checkbox" value="INCHEON">인천</input>
        <input type="checkbox" value="GWANGJU">광주</input>
        <input type="checkbox" value="DAEJEON">대전</input>
        <input type="checkbox" value="ULSAN">울산</input>
        <input type="checkbox" value="SEJONG">세종</input>
        <input type="checkbox" value="GYEONGGI">경기</input>
        <input type="checkbox" value="GANGWON">강원</input>
        <input type="checkbox" value="CHUNGCHEONGBUK">충청북도</input>
        <input type="checkbox" value="CHUNGCHEONGNAM">충청남도</input>
        <input type="checkbox" value="JEOLLABUK">전라북도</input>
        <input type="checkbox" value="JEOLLANAM">전라남도</input>
        <input type="checkbox" value="GYEONGSANGBUK">경상북도</input>
        <input type="checkbox" value="GYEONGSANGNAM">경상남도</input>
        <input type="checkbox" value="JEJU">제주</input>
    </form>
    <button id="allClick">전체선택</button>
    <button id="noClick">전체취소</button>
    <button id="apply">적용</button>
    <div id="community-list"></div>
    <button id="btn-prev"><</button>
    <button id="btn-next">></button>
    <button id="new-board">new</button>

</body>
</html>
