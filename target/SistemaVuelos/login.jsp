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
    <title>Iniciar Sesión - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .login-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #dee2e6;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .form-icon {
            font-size: 4rem;
            color: #667eea;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container">
        <div class="login-container">
            <div class="text-center">
                <i class="fas fa-sign-in-alt form-icon"></i>
                <h2>Iniciar Sesión</h2>
                <p class="text-muted">Accede a tu cuenta para gestionar tus reservas</p>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle me-2"></i>${error}
                </div>
            </c:if>
            
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle me-2"></i>${mensaje}
                </div>
            </c:if>
            
            <form action="login" method="post" id="formLogin">
                <div class="mb-3">
                    <label class="form-label">Email *</label>
                    <input type="email" class="form-control" name="email" required
                           pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
                           title="Ingresa un email válido">
                </div>
                
                <div class="mb-3">
                    <label class="form-label">Contraseña *</label>
                    <input type="password" class="form-control" name="password" required
                           minlength="6">
                    <div class="form-text">
                        <a href="recuperacionPassword">¿Olvidaste tu contraseña?</a>
                    </div>
                </div>
                
                <div class="mb-3">
                    <button type="submit" class="btn btn-primary w-100 btn-lg">Iniciar Sesión</button>
                </div>
                
                <div class="text-center">
                    <p class="mb-0">¿No tienes cuenta? <a href="registro.jsp">Regístrate aquí</a></p>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación básica del formulario
        document.getElementById('formLogin').addEventListener('submit', function(e) {
            const email = document.querySelector('input[name="email"]');
            const password = document.querySelector('input[name="password"]');
            
            if (!email.checkValidity()) {
                e.preventDefault();
                alert('Por favor ingresa un email válido');
                email.focus();
                return;
            }
            
            if (password.value.length < 6) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 6 caracteres');
                password.focus();
                return;
            }
        });
    </script>
</body>
</html>