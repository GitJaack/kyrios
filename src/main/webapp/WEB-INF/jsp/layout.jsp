<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle != null ? pageTitle : 'Kyrios'}</title>
    <link href="/css/base.css" rel="stylesheet">
    <link href="/css/sidebar.css" rel="stylesheet">
    <link href="/css/home.css" rel="stylesheet">
    <link href="/css/main-content.css" rel="stylesheet">
    <c:if test="${not empty pageCss}">
        <link href="/css/${pageCss}.css" rel="stylesheet">
    </c:if>
    <script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js"></script>
</head>

<body>
    <jsp:include page="sidebar.jsp"/>
    
    <main class="main-content">
        <jsp:include page="${contentPage}"/>
    </main>
</body>
</html>
