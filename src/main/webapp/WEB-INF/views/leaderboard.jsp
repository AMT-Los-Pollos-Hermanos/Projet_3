<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="fragments/header.jsp" %>

<div class="container">
    <h1>Classement</h1>
    <hr>
    <table class="table">
        <tr>
            <th>#</th>
            <th>Nom</th>
            <th>Points</th>
        </tr>
        <c:forEach items="${requestScope.leaderboard}" var="user" varStatus="loop">
            <tr>
                <td>${loop.index + 1}</td>
                <td>${user.username}</td>
                <td>${user.points}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<%@include file="fragments/footer.jsp" %>
