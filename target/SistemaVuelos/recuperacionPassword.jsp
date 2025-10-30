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
    <title>Recuperar Contraseña - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .recovery-container {
            max-width: 500px;
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
        <div class="recovery-container">
            <div class="text-center">
                <i class="fas fa-key form-icon"></i>
                <h2>Recuperar Contraseña</h2>
                <p class="text-muted">Sigue los pasos para restablecer tu contraseña</p>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success">${mensaje}</div>
            </c:if>
            
            <c:choose>
                <c:when test="${paso == 'solicitar' || empty paso}">
                    <!-- Paso 1: Solicitar recuperación -->
                    <form action="recuperacionPassword" method="post">
                        <input type="hidden" name="paso" value="solicitar">
                        <div class="mb-3">
                            <label class="form-label">Email de tu cuenta *</label>
                            <input type="email" class="form-control" name="email" required
                                   pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
                                   title="Ingresa el email de tu cuenta">
                        </div>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-primary w-100 btn-lg">Enviar Enlace de Recuperación</button>
                        </div>
                    </form>
                </c:when>
                
                <c:when test="${paso == 'restablecer'}">
                    <!-- Paso 2: Restablecer contraseña -->
                    <form action="recuperacionPassword" method="post" id="formRestablecer">
                        <input type="hidden" name="paso" value="restablecer">
                        <input type="hidden" name="token" value="${token}">
                        
                        <div class="mb-3">
                            <label class="form-label">Nueva Contraseña *</label>
                            <input type="password" class="form-control" name="nuevaPassword" id="nuevaPassword" required
                                   pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$"
                                   title="Mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial">
                            <div class="form-text">
                                La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y caracteres especiales.
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Confirmar Nueva Contraseña *</label>
                            <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" required>
                            <div class="form-text" id="passwordMatch"></div>
                        </div>
                        
                        <div class="mb-3">
                            <button type="submit" class="btn btn-success w-100 btn-lg">Restablecer Contraseña</button>
                        </div>
                    </form>
                </c:when>
                
                <c:when test="${paso == 'completado'}">
                    <!-- Paso 3: Completado -->
                    <div class="text-center">
                        <i class="fas fa-check-circle fa-3x text-success mb-3"></i>
                        <h4>Contraseña Restablecida</h4>
                        <p class="text-muted">Tu contraseña ha sido actualizada exitosamente.</p>
                        <a href="login.jsp" class="btn btn-primary">Iniciar Sesión</a>
                    </div>
                </c:when>
            </c:choose>
            
            <div class="text-center mt-3">
                <a href="login.jsp" class="text-decoration-none">
                    <i class="fas fa-arrow-left me-1"></i>Volver al inicio de sesión
                </a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación de contraseñas coincidentes
        const confirmPassword = document.getElementById('confirmPassword');
        if (confirmPassword) {
            confirmPassword.addEventListener('input', function() {
                const nuevaPassword = document.getElementById('nuevaPassword').value;
                const confirmPassword = this.value;
                const matchText = document.getElementById('passwordMatch');
                
                if (confirmPassword === '') {
                    matchText.textContent = '';
                    matchText.className = 'form-text';
                } else if (nuevaPassword === confirmPassword) {
                    matchText.textContent = '✓ Las contraseñas coinciden';
                    matchText.className = 'form-text text-success';
                } else {
                    matchText.textContent = '✗ Las contraseñas no coinciden';
                    matchText.className = 'form-text text-danger';
                }
            });
        }

        // Validación del formulario de restablecimiento
        const formRestablecer = document.getElementById('formRestablecer');
        if (formRestablecer) {
            formRestablecer.addEventListener('submit', function(e) {
                const nuevaPassword = document.getElementById('nuevaPassword').value;
                const confirmPassword = document.getElementById('confirmPassword').value;
                
                if (nuevaPassword !== confirmPassword) {
                    e.preventDefault();
                    alert('Las contraseñas no coinciden');
                    return;
                }
            });
        }
    </script>
</body>
</html>