<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">

    <script src="js/app.js"></script>
</head>

<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}">Home</a>
                <a class="nav-link" href="matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Matches</h1>
        <div class="input-container">
            <input class="input-filter" placeholder="Filter by name" type="text" />
            <div>
                <a href="#">
                    <button class="btn-filter">Reset Filter</button>
                </a>
            </div>
        </div>


        </table>
        <div class="pagination">

            <c:if test="${matchesPage.hasPrevious}">
                <a class="prev"
                   href="${pageContext.request.contextPath}/matches?page=${matchesPage.currentPage - 1}">
                    &lt;
                </a>
            </c:if>
         <table class="table-matches">
            <tr>
                <th>Player One</th>
                <th>Player Two</th>
                <th>Winner</th>
            </tr>
            <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                <c:choose>
                    <c:when test="${pageNumber == matchesPage.currentPage}">
                        <a class="num-page current"
                           href="${pageContext.request.contextPath}/matches?page=${pageNumber}">
                            ${pageNumber}
                        </a>
                        <c:forEach var="match" items="${matchesList}">
                            <tr>
                                <td>${match.firstPlayerName}</td>
                                <td>${match.secondPlayerName}</td>
                                <td><span class="winner-name-td">${match.winnerName}</span></td>
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <a class="num-page"
                           href="${pageContext.request.contextPath}/matches?page=${pageNumber}">
                            ${pageNumber}
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${pageDto.hasNext}">
                <a class="next"
                   href="${pageContext.request.contextPath}/matches?page=${pageDto.currentPage + 1}">
                    &gt;
                </a>
            </c:if>

        </div>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>
