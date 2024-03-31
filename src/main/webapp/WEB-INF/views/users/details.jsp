<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp"%>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Details de l'utilisateur
            </h1>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <div class="box-body no-padding">
                            <table class="table table-striped">
                                <tr>
                                    <th>ID</th>
                                    <th>Nom</th>
                                    <th>Prenom</th>
                                    <th>Email</th>
                                    <th>Date de naissance</th>
                                </tr>
                                <tr>
                                    <td>${client.id()}</td>
                                    <td>${client.nom()}</td>
                                    <td>${client.prenom()}</td>
                                    <td>${client.email()}</td>
                                    <td>${client.naissance()}</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">Reservations</h3>
                        </div>
                        <div class="box-body no-padding">
                            <table class="table table-striped">
                                <tr>
                                    <th>ID</th>
                                    <th>Date</th>
                                    <th>Heure</th>
                                    <th>Model</th>
                                    <th>Constructeur</th>
                                    <th>Nb de places</th>
                                </tr>
                                <c:forEach items="${reservations}" var="reservation">
                                    <tr>
                                        <td>${reservation.id()}</td>
                                        <td>${reservation.debut()}</td>
                                        <td>${reservation.fin()}</td>
                                        <td>${reservation.vehicle().modele()}</td>
                                        <td>${reservation.vehicle().constructeur()}</td>
                                        <td>${reservation.vehicle().nbPlaces()}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>

<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
</body>
</html>