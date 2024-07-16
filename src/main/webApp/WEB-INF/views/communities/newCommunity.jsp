<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ include file="../common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Community</title>
    <script src="/static/js/newCommunity.js"></script>
</head>
<body>
<h2>Create a New Community Post</h2>
<form id="create-community-form" enctype="multipart/form-data" action="community.jsp">
    <div>
        <label for="area">Area:</label>
        <select id="area" name="area">
            <option value="SEOUL">서울</option>
            <option value="BUSAN">부산</option>
            <option value="DAEGU">대구</option>
            <option value="INCHEON">인천</option>
            <option value="GWANGJU">광주</option>
            <option value="DAEJEON">대전</option>
            <option value="ULSAN">울산</option>
            <option value="SEJONG">세종</option>
            <option value="GYEONGGI">경기</option>
            <option value="GANGWON">강원</option>
            <option value="CHUNGCHEONGBUK">충청북도</option>
            <option value="CHUNGCHEONGNAM">충청남도</option>
            <option value="JEOLLABUK">전라북도</option>
            <option value="JEOLLANAM">전라남도</option>
            <option value="GYEONGSANGBUK">경상북도</option>
            <option value="GYEONGSANGNAM">경상남도</option>
            <option value="JEJU">제주</option>
        </select>
    </div>
    <div>
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" required>
    </div>
    <div>
        <label for="content">Content:</label>
        <textarea id="content" name="content" required></textarea>
    </div>
    <div>
        <label for="postImg">Post Image URL:</label>
        <input type="file" id="postImg" name="postImg">
    </div>
    <button type="button" id="btn-submit">Submit</button>
</form>
<div id="response-message"></div>
</body>
</html>

