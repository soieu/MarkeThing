<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="java.util.Enumeration" %>
<%@ include file="../common/header.jsp" %>
<%
    String savePath = application.getRealPath("./");
    String file = "";
    String oriFile = "";
    int sizeLimit = 5 * 1024 * 1024;

    MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, "UTF-8", new DefaultFileRenamePolicy());
    Enumeration files = multi.getFileNames();
    String name = (String) files.nextElement();

    file = multi.getFilesystemName(name);
    oriFile = multi.getOriginalFileName(name);
%>
<html>
<head>
    <title>Title</title>
    <script src="/static/js/community.js"></script>
</head>
<body>
    <div id="community-detail"></div>
    <h2>파일 업로드 성공</h2><br/>
    파일 저장 위치 : <%= savePath%><br/>
    파일 저장 이름 : <%= file%><br/>
    파일 원본 이름 : <%= oriFile%>
</body>
</html>
