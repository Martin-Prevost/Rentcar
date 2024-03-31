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
                Details du vehicule : ${vehicle.constructeur()} ${vehicle.modele()}
            </h1>
            <p>
                ID : ${vehicle.id()}, nombre de places : ${vehicle.nbPlaces()}.
            </p>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div>

                    </div>
                    <div class="box">
                        <div class="box-header">
                            <h3>Reservations</h3>
                        </div>
                        <div class="box-body no-padding">
                            <table class="table table-striped">
                                <tr>
                                    <th>ID</th>
                                    <th>Client</th>
                                    <th>Date de debut</th>
                                    <th>Date de fin</th>
                                </tr>
                                <c:forEach items="${reservations}" var="reservation">
                                    <tr>
                                        <td>${reservation.id()}</td>
                                        <td>${reservation.client().nom()} ${reservation.client().prenom()}</td>
                                        <td>${reservation.debut()}</td>
                                        <td>${reservation.fin()}</td>
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