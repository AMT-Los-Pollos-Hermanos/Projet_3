<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="fragments/header.jsp" %>

<div class="container">
    <div class="row gutters-sm">
        <div class="col-md-4 mb-3">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex flex-column align-items-center text-center">
                        <img src="${pageContext.request.contextPath}/assets/img/avatar.png" alt="Admin" class="rounded-circle" width="150">
                        <div class="mt-3">
                            <h4>${requestScope.user.username}</h4>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card mb-3">
                <div class="card-body">
                    <div class="row">
                        <div class="col-sm-3">
                            <p class="font-weight-bold">Nom</p>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${requestScope.user.firstName} ${requestScope.user.lastName}"/>
                        </div>
                        <div class="col-sm-3">
                            <p class="font-weight-bold">Points</p>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${requestScope.user.points}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="fragments/footer.jsp" %>
