<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%   
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Pago - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .card-input {
            background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"><path fill="%23999" d="M0 0h24v24H0z" fill="none"/><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/></svg>');
            background-repeat: no-repeat;
            background-position: right 10px center;
            background-size: 20px;
        }
        .payment-security {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            border-left: 4px solid #28a745;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container mt-4">
        <div class="row">
            <div class="col-md-8">
                <!-- Progreso -->
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div class="text-center">
                                <span class="badge bg-success rounded-circle p-2">1</span>
                                <div class="mt-1"><small>Datos Pasajeros</small></div>
                            </div>
                            <div class="text-center">
                                <span class="badge bg-primary rounded-circle p-2">2</span>
                                <div class="mt-1"><small>Pago</small></div>
                            </div>
                            <div class="text-center">
                                <span class="badge bg-secondary rounded-circle p-2">3</span>
                                <div class="mt-1"><small>Confirmación</small></div>
                            </div>
                        </div>
                    </div>
                </div>

                <h2><i class="fas fa-credit-card me-2"></i>Información de Pago</h2>
                <p class="text-muted">Completa los datos de tu tarjeta para finalizar tu reserva</p>

                <!-- Mostrar errores -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <i class="fas fa-exclamation-triangle me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Formulario de Pago CORREGIDO -->
                <form action="procesarPago" method="post" id="formPago">
                    <!-- Información de Seguridad -->
                    <div class="card mb-4 payment-security">
                        <div class="card-body">
                            <div class="d-flex align-items-center">
                                <i class="fas fa-shield-alt fa-2x text-success me-3"></i>
                                <div>
                                    <h5 class="card-title mb-1">Pago Seguro</h5>
                                    <p class="card-text mb-0">Tus datos están protegidos con encriptación SSL de 256 bits</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Datos de Tarjeta -->
                    <div class="card mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-credit-card me-2"></i>Datos de la Tarjeta
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-12">
                                    <label class="form-label">Número de Tarjeta *</label>
                                    <input type="text" class="form-control card-input" name="numeroTarjeta" 
                                           placeholder="1234 5678 9012 3456" required maxlength="19"
                                           pattern="[0-9\s]{13,19}">
                                    <div class="form-text">Ingresa los 16 dígitos de tu tarjeta</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Nombre del Titular *</label>
                                    <input type="text" class="form-control" name="nombreTitular" 
                                           placeholder="Como aparece en la tarjeta" required
                                           pattern="[A-Za-záéíóúÁÉÍÓÚñÑ\s]{2,50}">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Fecha Expiración *</label>
                                    <input type="month" class="form-control" name="fechaExpiracion" required
                                           min="2024-06">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">CVV *</label>
                                    <input type="text" class="form-control" name="cvv" 
                                           placeholder="123" required maxlength="4"
                                           pattern="[0-9]{3,4}">
                                    <div class="form-text">3 o 4 dígitos en la parte trasera</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Información de Contacto -->
                    <div class="card mb-4">
                        <div class="card-header bg-info text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-user me-2"></i>Información de Contacto
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Email para confirmación *</label>
                                    <input type="email" class="form-control" name="emailConfirmacion" 
                                           value="${sessionScope.emailContacto}" required
                                           pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Teléfono de contacto *</label>
                                    <input type="tel" class="form-control" name="telefonoConfirmacion"
                                           value="${sessionScope.telefonoContacto}" required
                                           pattern="[0-9+\-\s]{10,15}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Términos y Condiciones -->
                    <div class="card mb-4">
                        <div class="card-body">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="terminos" name="terminos" required>
                                <label class="form-check-label" for="terminos">
                                    Acepto los <a href="#" data-bs-toggle="modal" data-bs-target="#terminosModal">términos y condiciones</a>    
                                    y la <a href="#" data-bs-toggle="modal" data-bs-target="#privacidadModal">política de privacidad</a>
                                </label>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="d-flex justify-content-between">
                        <a href="javascript:history.back()" class="btn btn-outline-secondary btn-lg">
                            <i class="fas fa-arrow-left me-2"></i>Volver Atrás
                        </a>
                        <button type="submit" class="btn btn-success btn-lg" id="btnPagar">
                            <i class="fas fa-lock me-2"></i>Pagar y Confirmar Reserva
                        </button>
                    </div>
                </form>
            </div>

            <!-- Resumen -->
            <div class="col-md-4">
                <div class="sticky-top" style="top: 20px;">
                    <div class="card">
                        <div class="card-header bg-primary text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-receipt me-2"></i>Resumen de Pago
                            </h5>
                        </div>
                        <div class="card-body">
                            <%   
                                // Obtener datos de la sesión
                                HttpSession currentSession = request.getSession();
                                String totalStr = (String) currentSession.getAttribute("total");
                                Integer numPasajeros = (Integer) currentSession.getAttribute("numPasajeros");
                                Double total = 0.0;
                                if (totalStr != null) {
                                    total = Double.parseDouble(totalStr);
                                }
                                if (numPasajeros == null) numPasajeros = 1;
                                  
                                double impuestos = total * 0.16;
                                double totalFinal = total + impuestos;
                                pageContext.setAttribute("totalFinal", totalFinal);
                                pageContext.setAttribute("impuestos", impuestos);
                                pageContext.setAttribute("numPasajeros", numPasajeros);
                            %>
                              
                            <table class="table table-sm">
                                <tr>
                                    <td>Pasajeros:</td>
                                    <td class="text-end">${numPasajeros}</td>
                                </tr>
                                <tr>
                                    <td>Tarifa base:</td>
                                    <td class="text-end">$<fmt:formatNumber value="${total}" pattern="#,##0.00"/></td>
                                </tr>
                                <tr>
                                    <td>Impuestos (16%):</td>
                                    <td class="text-end">$<fmt:formatNumber value="${impuestos}" pattern="#,##0.00"/></td>
                                </tr>
                                <tr class="table-active">
                                    <td><strong>Total a Pagar:</strong></td>
                                    <td class="text-end"><strong>$<fmt:formatNumber value="${totalFinal}" pattern="#,##0.00"/></strong></td>
                                </tr>
                            </table>
                              
                            <div class="alert alert-success mt-3">
                                <small>
                                    <i class="fas fa-shield-alt me-1"></i>
                                    <strong>Pago 100% Seguro</strong><br>
                                    Tus datos están protegidos con encriptación SSL
                                </small>
                            </div>

                            <!-- Información de la Reserva -->
                            <div class="alert alert-info mt-3">
                                <h6><i class="fas fa-info-circle me-1"></i>Información Importante</h6>
                                <small>
                                    <ul class="mb-0 ps-3">
                                        <li>Recibirás un email de confirmación</li>
                                        <li>Guarda tu número de reserva</li>
                                        <li>Cancelaciones hasta 24 horas antes</li>
                                    </ul>
                                </small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Términos y Condiciones -->
    <div class="modal fade" id="terminosModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Términos y Condiciones</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p><strong>TÉRMINOS Y CONDICIONES DE RESERVA</strong></p>
                    <p>1. Las tarifas mostradas incluyen impuestos aplicables.</p>
                    <p>2. Las cancelaciones deben realizarse con 24 horas de anticipación.</p>
                    <p>3. Los cambios de fecha están sujetos a disponibilidad y cargos adicionales.</p>
                    <p>4. El check-in debe realizarse 2 horas antes del vuelo.</p>
                    <p>5. Se requiere identificación oficial en el aeropuerto.</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Política de Privacidad -->
    <div class="modal fade" id="privacidadModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Política de Privacidad</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p><strong>POLÍTICA DE PRIVACIDAD</strong></p>
                    <p>1. Tus datos personales están protegidos y no se compartirán con terceros.</p>
                    <p>2. Utilizamos encriptación SSL para proteger tu información de pago.</p>
                    <p>3. Solo utilizamos tu información para procesar tu reserva y enviar confirmaciones.</p>
                    <p>4. Puedes solicitar la eliminación de tus datos en cualquier momento.</p>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Formatear número de tarjeta
        document.querySelector('[name="numeroTarjeta"]').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
            let formattedValue = value.match(/.{1,4}/g)?.join(' ');
            e.target.value = formattedValue || value;
        });

        // Validación del formulario
        document.getElementById('formPago').addEventListener('submit', function(e) {
            const terminos = document.getElementById('terminos');
            const btnPagar = document.getElementById('btnPagar');
            
            if (!terminos.checked) {
                e.preventDefault();
                alert('Debes aceptar los términos y condiciones para continuar');
                terminos.focus();
                return;
            }

            // Mostrar loader en el botón
            btnPagar.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Procesando pago...';
            btnPagar.disabled = true;

            // Simular validación de tarjeta
            const numeroTarjeta = document.querySelector('[name="numeroTarjeta"]').value.replace(/\s/g, '');
            if (numeroTarjeta.length !== 16) {
                e.preventDefault();
                alert('El número de tarjeta debe tener 16 dígitos');
                btnPagar.innerHTML = '<i class="fas fa-lock me-2"></i>Pagar y Confirmar Reserva';
                btnPagar.disabled = false;
                return;
            }

            // Validar fecha de expiración
            const fechaExpiracion = document.querySelector('[name="fechaExpiracion"]').value;
            const hoy = new Date();
            const fechaSeleccionada = new Date(fechaExpiracion);
            
            if (fechaSeleccionada < hoy) {
                e.preventDefault();
                alert('La fecha de expiración no puede ser en el pasado');
                btnPagar.innerHTML = '<i class="fas fa-lock me-2"></i>Pagar y Confirmar Reserva';
                btnPagar.disabled = false;
                return;
            }

            // Si todo está bien, el formulario se envía normalmente
            console.log('Procesando pago con tarjeta...');
        });

        // Validar CVV en tiempo real
        document.querySelector('[name="cvv"]').addEventListener('input', function(e) {
            const cvv = e.target.value;
            if (cvv.length === 3 || cvv.length === 4) {
                e.target.classList.remove('is-invalid');
                e.target.classList.add('is-valid');
            } else {
                e.target.classList.remove('is-valid');
                e.target.classList.add('is-invalid');
            }
        });

        // Validar número de tarjeta en tiempo real
        document.querySelector('[name="numeroTarjeta"]').addEventListener('input', function(e) {
            const numero = e.target.value.replace(/\s/g, '');
            if (numero.length === 16) {
                e.target.classList.remove('is-invalid');
                e.target.classList.add('is-valid');
            } else {
                e.target.classList.remove('is-valid');
                e.target.classList.add('is-invalid');
            }
        });
    </script>
</body>
</html>