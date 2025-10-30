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
    <title>¡Reserva Confirmada! - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .confirmation-card {   
            border: 3px solid #28a745;   
            border-radius: 15px;   
        }
        .reservation-number {   
            font-size: 2rem;   
            font-weight: bold;   
            color: #28a745;   
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <c:choose>
                    <c:when test="${empty sessionScope.numeroReserva}">
                        <!-- NO HAY DATOS DE RESERVA -->
                        <div class="card border-danger">
                            <div class="card-header bg-danger text-white text-center">
                                <h3 class="card-title mb-0">
                                    <i class="fas fa-exclamation-triangle me-2"></i>Error en la Reserva
                                </h3>
                            </div>
                            <div class="card-body text-center">
                                <i class="fas fa-times-circle fa-5x text-danger mb-3"></i>
                                <h4>No se encontraron datos de reserva</h4>
                                <p class="text-muted">La sesión pudo haber expirado o hubo un error en el proceso.</p>
                                <div class="mt-4">
                                    <a href="index.jsp" class="btn btn-primary btn-lg me-3">
                                        <i class="fas fa-home me-2"></i>Volver al Inicio
                                    </a>
                                    <a href="formularioPago.jsp" class="btn btn-warning btn-lg">
                                        <i class="fas fa-arrow-left me-2"></i>Reintentar Pago
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- RESERVA EXITOSA -->
                        <div class="card confirmation-card">
                            <div class="card-header bg-success text-white text-center">
                                <h3 class="card-title mb-0">
                                    <i class="fas fa-check-circle me-2"></i>¡Reserva Confirmada!
                                </h3>
                            </div>
                            <div class="card-body text-center">
                                <div class="mb-4">
                                    <i class="fas fa-plane fa-5x text-success mb-3"></i>
                                    <h4>Tu vuelo ha sido reservado exitosamente</h4>
                                    <div class="reservation-number">${sessionScope.numeroReserva}</div>
                                </div>
                                
                                <div class="row text-start mb-4">
                                    <div class="col-md-6">
                                        <h6><i class="fas fa-info-circle me-2"></i>Detalles de la Reserva</h6>
                                        <p class="mb-1"><strong>Fecha de Reserva:</strong>    
                                            <fmt:formatDate value="${sessionScope.fechaReserva}" pattern="dd/MM/yyyy HH:mm"/>
                                        </p>
                                        <p class="mb-1"><strong>Total Pagado:</strong>  
                                            $${sessionScope.total} MXN
                                        </p>
                                        <p class="mb-1"><strong>Pasajeros:</strong>  
                                            ${sessionScope.numPasajeros}
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <h6><i class="fas fa-users me-2"></i>Información de Contacto</h6>
                                        <p class="mb-1"><strong>Email:</strong>  
                                            ${sessionScope.emailContacto}
                                        </p>
                                        <p class="mb-1"><strong>Teléfono:</strong>  
                                            ${sessionScope.telefonoContacto}
                                        </p>
                                    </div>
                                </div>
                                
                                <div class="alert alert-info">
                                    <h6><i class="fas fa-road me-2"></i>¿Qué sigue?</h6>
                                    <ul class="mb-0">
                                        <li>Recibirás un email de confirmación</li>
                                        <li>Presenta tu identificación y número de reserva en el aeropuerto</li>
                                        <li>Realiza el check-in 2 horas antes del vuelo</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="card-footer text-center">
                                <a href="index.jsp" class="btn btn-primary btn-lg me-3">
                                    <i class="fas fa-home me-2"></i>Volver al Inicio
                                </a>
                                <a href="historial" class="btn btn-info btn-lg me-3">
                                    <i class="fas fa-history me-2"></i>Ver en Historial
                                </a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>