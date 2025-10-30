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
    <title>Confirmación de Selección</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .vuelo-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .price-highlight {
            font-size: 2rem;
            font-weight: bold;
            color: #28a745;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-10">
                
                <!-- Alert de éxito -->
                <div class="alert alert-success">
                    <h4 class="alert-heading">
                        <i class="fas fa-check-circle me-2"></i>¡Vuelo Seleccionado Correctamente!
                    </h4>
                    <p class="mb-0">Tu vuelo ha sido reservado exitosamente. A continuación los detalles:</p>
                </div>

                <!-- Información del Vuelo -->
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white">
                        <h4 class="card-title mb-0">
                            <i class="fas fa-info-circle me-2"></i>Detalles del Vuelo
                        </h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h5>${vuelo.aerolinea} - ${vuelo.numeroVuelo}</h5>
                                <p class="mb-1">
                                    <strong>Ruta:</strong> ${vuelo.origen} → ${vuelo.destino}
                                </p>
                                <p class="mb-1">
                                    <strong>Duración:</strong> ${vuelo.duracionMinutos} minutos
                                </p>
                                <p class="mb-0">
                                    <strong>Pasajeros:</strong> ${pasajeros}
                                </p>
                            </div>
                            <div class="col-md-6 text-end">
                                <div class="price-highlight">
                                    $<fmt:formatNumber value="${vuelo.precio}" pattern="#,##0.00"/> MXN
                                </div>
                                <p class="text-muted">por pasajero</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Resumen de Pago -->
                <div class="card mb-4">
                    <div class="card-header bg-info text-white">
                        <h4 class="card-title mb-0">
                            <i class="fas fa-receipt me-2"></i>Resumen de Pago
                        </h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-8">
                                <p class="mb-1">${vuelo.aerolinea} - ${vuelo.numeroVuelo}</p>
                                <p class="mb-1">${pasajeros} pasajero(s) x $<fmt:formatNumber value="${vuelo.precio}" pattern="#,##0.00"/></p>
                            </div>
                            <div class="col-md-4 text-end">
                                <h5 class="text-success">
                                    Total: $<fmt:formatNumber value="${total}" pattern="#,##0.00"/> MXN
                                </h5>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Próximos Pasos -->
                <div class="card">
                    <div class="card-header bg-warning text-dark">
                        <h4 class="card-title mb-0">
                            <i class="fas fa-road me-2"></i>Próximos Pasos
                        </h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-4 text-center">
                                <div class="mb-3">
                                    <i class="fas fa-user-friends fa-2x text-primary mb-2"></i>
                                    <h6>Datos de Pasajeros</h6>
                                    <small class="text-muted">Ingresa información de los viajeros</small>
                                </div>
                            </div>
                            <div class="col-md-4 text-center">
                                <div class="mb-3">
                                    <i class="fas fa-credit-card fa-2x text-primary mb-2"></i>
                                    <h6>Pago Seguro</h6>
                                    <small class="text-muted">Procesamiento seguro de pago</small>
                                </div>
                            </div>
                            <div class="col-md-4 text-center">
                                <div class="mb-3">
                                    <i class="fas fa-ticket-alt fa-2x text-primary mb-2"></i>
                                    <h6>Confirmación</h6>
                                    <small class="text-muted">Recibe tu boleto electrónico</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Botones de Acción -->
                <div class="text-center mt-4">
                    <a href="index.jsp" class="btn btn-primary btn-lg me-3">
                        <i class="fas fa-home me-2"></i>Volver al Inicio
                    </a>
                    <button class="btn btn-success btn-lg me-3" disabled>
                        <i class="fas fa-credit-card me-2"></i>Continuar al Pago (Próximamente)
                    </button>
                    <a href="javascript:history.back()" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Volver a Resultados
                    </a>
                </div>

                <!-- Información Adicional -->
                <div class="alert alert-info mt-4">
                    <h6><i class="fas fa-clock me-2"></i>Información Importante</h6>
                    <ul class="mb-0">
                        <li>Tu reserva está <strong>pendiente de pago</strong></li>
                        <li>Tienes 24 horas para completar el proceso de pago</li>
                        <li>Recibirás un email de confirmación al completar la reserva</li>
                    </ul>
                </div>

            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>