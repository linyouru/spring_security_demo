<%--
  Created by IntelliJ IDEA.
  User: LB
  Date: 2018-12-04
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>锅锅听歌~</title>
</head>
<body>
    <audio src="${pageContext.request.contextPath}/player/getAudio" controls="controls">
        播放一个音乐试试
    </audio>
</body>
</html>
