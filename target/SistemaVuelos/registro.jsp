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
    <title>Registro - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .register-container {
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
        .password-requirements {
            font-size: 0.85rem;
            color: #6c757d;
        }
        .requirement {
            margin-bottom: 3px;
        }
        .requirement.valid {
            color: #28a745;
        }
        .requirement.invalid {
            color: #dc3545;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container">
        <div class="register-container">
            <div class="text-center">
                <i class="fas fa-user-plus form-icon"></i>
                <h2>Crear Cuenta</h2>
                <p class="text-muted">Completa tus datos para registrarte</p>
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
            
            <form action="registro" method="post" id="formRegistro">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Nombre *</label>
                        <input type="text" class="form-control" name="nombre" required  
                               pattern="[A-Za-záéíóúÁÉÍÓÚñÑ\s]{2,50}"  
                               title="Solo letras y espacios (2-50 caracteres)">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Apellido *</label>
                        <input type="text" class="form-control" name="apellido" required
                               pattern="[A-Za-záéíóúÁÉÍÓÚñÑ\s]{2,50}"
                               title="Solo letras y espacios (2-50 caracteres)">
                    </div>
                    <div class="col-12">
                        <label class="form-label">Email *</label>
                        <input type="email" class="form-control" name="email" required
                               pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
                               title="Ingresa un email válido">
                    </div>
                    <div class="col-12">
                        <label class="form-label">Contraseña *</label>
                        <input type="password" class="form-control" name="password" id="password" required
                               minlength="8">
                        
                        <!-- Requisitos de contraseña -->
                        <div class="password-requirements mt-2">
                            <strong>La contraseña debe tener:</strong>
                            <div class="requirement" id="reqLength">
                                <i class="fas fa-circle me-1" style="font-size: 0.5rem;"></i>
                                Al menos 8 caracteres
                            </div>
                            <div class="requirement" id="reqUpper">
                                <i class="fas fa-circle me-1" style="font-size: 0.5rem;"></i>
                                Una letra mayúscula
                            </div>
                            <div class="requirement" id="reqLower">
                                <i class="fas fa-circle me-1" style="font-size: 0.5rem;"></i>
                                Una letra minúscula
                            </div>
                            <div class="requirement" id="reqDigit">
                                <i class="fas fa-circle me-1" style="font-size: 0.5rem;"></i>
                                Un número
                            </div>
                            <div class="requirement" id="reqSpecial">
                                <i class="fas fa-circle me-1" style="font-size: 0.5rem;"></i>
                                Un carácter especial (cualquier símbolo)
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Confirmar Contraseña *</label>
                        <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" required>
                        <div class="form-text" id="passwordMatch"></div>
                    </div>
                    <div class="col-12">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="terminos" required>
                            <label class="form-check-label" for="terminos">
                                Acepto los <a href="#" data-bs-toggle="modal" data-bs-target="#terminosModal">términos y condiciones</a>
                            </label>
                        </div>
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary w-100 btn-lg" id="submitBtn">Registrarse</button>
                    </div>
                    <div class="col-12 text-center">
                        <p class="mb-0">¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión aquí</a></p>
                    </div>
                </div>
            </form>
            
            <!-- Mostrar enlace de verificación para desarrollo -->
            <c:if test="${not empty enlaceVerificacion}">
                <div class="alert alert-warning mt-4">
                    <h6><i class="fas fa-exclamation-triangle me-2"></i>No se pudo enviar el email automáticamente</h6>
                    <p class="mb-2">Por favor, usa el siguiente enlace para verificar tu cuenta:</p>
                    <div class="bg-light p-3 rounded">
                        <strong>Enlace de verificación:</strong><br>
                        <a href="${enlaceVerificacion}" target="_blank" class="small">${enlaceVerificacion}</a>
                    </div>
                    <p class="mt-2 mb-0"><small>Haz clic en el enlace para verificar tu cuenta inmediatamente.</small></p>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Modal Términos -->
    <div class="modal fade" id="terminosModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Términos y Condiciones</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <h6>1. Registro de Usuario</h6>
                    <p>Al registrarte, aceptas proporcionar información veraz y actualizada.</p>
                    
                    <h6>2. Uso del Servicio</h6>
                    <p>El servicio está destinado para la reserva de vuelos personales.</p>
                    
                    <h6>3. Privacidad</h6>
                    <p>Tu información personal será protegida según nuestra política de privacidad.</p>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validación en tiempo real de la contraseña
        document.getElementById('password').addEventListener('input', function() {
            const password = this.value;
            validatePasswordRequirements(password);
        });

        // Validación de contraseñas coincidentes
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            const matchText = document.getElementById('passwordMatch');
            
            if (confirmPassword === '') {
                matchText.textContent = '';
                matchText.className = 'form-text';
            } else if (password === confirmPassword) {
                matchText.textContent = '✓ Las contraseñas coinciden';
                matchText.className = 'form-text text-success';
            } else {
                matchText.textContent = '✗ Las contraseñas no coinciden';
                matchText.className = 'form-text text-danger';
            }
        });

        // Función para validar requisitos de contraseña - ACEPTA TODOS LOS CARACTERES ESPECIALES
        function validatePasswordRequirements(password) {
            const hasMinLength = password.length >= 8;
            const hasUpperCase = /[A-Z]/.test(password);
            const hasLowerCase = /[a-z]/.test(password);
            const hasDigit = /[0-9]/.test(password);
            
            // ACEPTA CUALQUIER CARÁCTER ESPECIAL (excepto letras, números y espacios)
            const hasSpecialChar = /[^A-Za-z0-9\s]/.test(password);

            updateRequirement('reqLength', hasMinLength);
            updateRequirement('reqUpper', hasUpperCase);
            updateRequirement('reqLower', hasLowerCase);
            updateRequirement('reqDigit', hasDigit);
            updateRequirement('reqSpecial', hasSpecialChar);

            // Habilitar/deshabilitar botón de envío
            const isValid = hasMinLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
            document.getElementById('submitBtn').disabled = !isValid;
        }

        function updateRequirement(elementId, isValid) {
            const element = document.getElementById(elementId);
            const icon = element.querySelector('i');
            
            if (isValid) {
                element.classList.add('valid');
                element.classList.remove('invalid');
                icon.className = 'fas fa-check-circle me-1 text-success';
            } else {
                element.classList.add('invalid');
                element.classList.remove('valid');
                icon.className = 'fas fa-times-circle me-1 text-danger';
            }
        }

        // Validación inicial
        validatePasswordRequirements('');

        // Validación del formulario - ACEPTA TODOS LOS CARACTERES ESPECIALES
        document.getElementById('formRegistro').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const terminos = document.getElementById('terminos');
            
            // Validar contraseña nuevamente por si acaso
            const hasMinLength = password.length >= 8;
            const hasUpperCase = /[A-Z]/.test(password);
            const hasLowerCase = /[a-z]/.test(password);
            const hasDigit = /[0-9]/.test(password);
            
            // ACEPTA CUALQUIER CARÁCTER ESPECIAL
            const hasSpecialChar = /[^A-Za-z0-9\s]/.test(password);

            if (!hasMinLength || !hasUpperCase || !hasLowerCase || !hasDigit || !hasSpecialChar) {
                e.preventDefault();
                alert('La contraseña no cumple con todos los requisitos');
                return;
            }
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Las contraseñas no coinciden');
                return;
            }
            
            if (!terminos.checked) {
                e.preventDefault();
                alert('Debes aceptar los términos y condiciones');
                return;
            }
        });
    </script>
</body>
</html>