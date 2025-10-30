<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%   
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Verificación de Email - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .verification-container {
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #dee2e6;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            text-align: center;
        }
        .icon-success {
            font-size: 4rem;
            color: #28a745;
            margin-bottom: 20px;
        }
        .icon-error {
            font-size: 4rem;
            color: #dc3545;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container">
        <div class="verification-container">
            <c:choose>
                <c:when test="${not empty mensaje}">
                    <i class="fas fa-check-circle icon-success"></i>
                    <h2>¡Verificación Exitosa!</h2>
                    <p class="text-muted">${mensaje}</p>
                     
                    <c:if test="${not empty usuarioNombre}">
                        <div class="alert alert-info">
                            <strong>Usuario:</strong> ${usuarioNombre}<br>
                            <strong>Email:</strong> ${usuarioEmail}
                        </div>
                    </c:if>
                     
                    <a href="login.jsp" class="btn btn-success btn-lg">
                        <i class="fas fa-sign-in-alt me-2"></i>Iniciar Sesión
                    </a>
                </c:when>
                 
                <c:when test="${not empty error}">
                    <i class="fas fa-times-circle icon-error"></i>
                    <h2>Error de Verificación</h2>
                    <p class="text-muted">${error}</p>
                    <div class="mt-3">
                        <a href="registro.jsp" class="btn btn-primary me-2">Intentar Registrarse Nuevamente</a>
                        <a href="login.jsp" class="btn btn-outline-secondary">Ir al Login</a>
                    </div>
                </c:when>
                 
                <c:otherwise>
                    <i class="fas fa-envelope icon-success"></i>
                    <h2>Verificación de Email</h2>
                    <p class="text-muted">Procesando tu verificación...</p>
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Cargando...</span>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>