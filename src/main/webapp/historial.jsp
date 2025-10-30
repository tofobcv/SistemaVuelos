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
    <title>Mi Historial - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .history-section {
            margin-bottom: 30px;
        }
        .reserva-card {
            border-left: 4px solid #28a745;
            transition: all 0.3s ease;
        }
        .reserva-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .busqueda-card {
            border-left: 4px solid #007bff;
        }
        .estado-confirmada {
            color: #28a745;
        }
        .estado-cancelada {
            color: #dc3545;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container mt-4">
        <div class="row">
            <div class="col-12">
                <h2><i class="fas fa-history me-2"></i>Mi Historial</h2>
                <p class="text-muted">Revisa tu historial de búsquedas y reservas</p>
            </div>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <!-- Pestañas -->
        <ul class="nav nav-tabs mb-4" id="myTab" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active" id="reservas-tab" data-bs-toggle="tab" data-bs-target="#reservas" type="button" role="tab">
                    <i class="fas fa-ticket-alt me-1"></i>Mis Reservas
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="busquedas-tab" data-bs-toggle="tab" data-bs-target="#busquedas" type="button" role="tab">
                    <i class="fas fa-search me-1"></i>Búsquedas Recientes
                </button>
            </li>
        </ul>
        
        <div class="tab-content" id="myTabContent">
            <!-- Pestaña de Reservas -->
            <div class="tab-pane fade show active" id="reservas" role="tabpanel">
                <c:choose>
                    <c:when test="${not empty historialReservas}">
                        <div class="row">
                            <c:forEach var="reserva" items="${historialReservas}">
                                <div class="col-md-6 mb-3">
                                    <div class="card reserva-card">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <h5 class="card-title">${reserva[1]} - ${reserva[2]}</h5>
                                                <span class="badge ${reserva[7] == 'confirmada' ? 'bg-success' : 'bg-danger'}">
                                                    ${reserva[7]}
                                                </span>
                                            </div>
                                            <p class="card-text mb-1">
                                                <strong>Ruta:</strong> ${reserva[3]} → ${reserva[4]}
                                            </p>
                                            <p class="card-text mb-1">
                                                <strong>Salida:</strong> 
                                                <fmt:formatDate value="${reserva[5]}" pattern="dd/MM/yyyy HH:mm" />
                                            </p>
                                            <p class="card-text mb-1">
                                                <strong>Total:</strong> $<fmt:formatNumber value="${reserva[6]}" pattern="#,##0.00" />
                                            </p>
                                            <p class="card-text mb-2">
                                                <strong>Código:</strong> <code>${reserva[0]}</code>
                                            </p>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <small class="text-muted">
                                                    Reserva: <fmt:formatDate value="${reserva[8]}" pattern="dd/MM/yyyy" />
                                                </small>
                                                <button class="btn btn-outline-primary btn-sm" 
                                                        onclick="verDetallesReserva('${reserva[0]}')">
                                                    <i class="fas fa-eye me-1"></i>Ver Detalles
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="fas fa-ticket-alt fa-3x text-muted mb-3"></i>
                            <h4>No tienes reservas aún</h4>
                            <p class="text-muted">Realiza tu primera reserva para verla aquí</p>
                            <a href="index.jsp" class="btn btn-primary">Buscar Vuelos</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <!-- Pestaña de Búsquedas -->
            <div class="tab-pane fade" id="busquedas" role="tabpanel">
                <c:choose>
                    <c:when test="${not empty historialBusquedas}">
                        <div class="row">
                            <c:forEach var="busqueda" items="${historialBusquedas}">
                                <div class="col-md-6 mb-3">
                                    <div class="card busqueda-card">
                                        <div class="card-body">
                                            <h6 class="card-title">${busqueda[1]} → ${busqueda[2]}</h6>
                                            <p class="card-text mb-1">
                                                <strong>Fecha ida:</strong> 
                                                <fmt:formatDate value="${busqueda[3]}" pattern="dd/MM/yyyy" />
                                            </p>
                                            <c:if test="${not empty busqueda[4]}">
                                                <p class="card-text mb-1">
                                                    <strong>Fecha vuelta:</strong> 
                                                    <fmt:formatDate value="${busqueda[4]}" pattern="dd/MM/yyyy" />
                                                </p>
                                            </c:if>
                                            <p class="card-text mb-1">
                                                <strong>Pasajeros:</strong> ${busqueda[5]}
                                            </p>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <small class="text-muted">
                                                    Buscado: <fmt:formatDate value="${busqueda[6]}" pattern="dd/MM/yyyy HH:mm" />
                                                </small>
                                                <a href="index.jsp?origen=${busqueda[1]}&destino=${busqueda[2]}&fechaIda=<fmt:formatDate value='${busqueda[3]}' pattern='yyyy-MM-dd' />&pasajeros=${busqueda[5]}" 
                                                   class="btn btn-outline-success btn-sm">
                                                    <i class="fas fa-redo me-1"></i>Buscar Nuevamente
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="fas fa-search fa-3x text-muted mb-3"></i>
                            <h4>No hay búsquedas recientes</h4>
                            <p class="text-muted">Tus búsquedas aparecerán aquí</p>
                            <a href="index.jsp" class="btn btn-primary">Realizar Búsqueda</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Modal para detalles de reserva -->
    <div class="modal fade" id="detallesReservaModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-info-circle me-2"></i>Detalles de la Reserva
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body" id="detallesReservaContent">
                    <!-- Contenido cargado por JavaScript -->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    <button type="button" class="btn btn-success" onclick="window.print()">
                        <i class="fas fa-print me-2"></i>Imprimir
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function verDetallesReserva(codigoReserva) {
            const fechaActual = new Date();
            const fechaFormateada = fechaActual.toLocaleDateString('es-ES');
            
            const contenido = `
                <div class="reserva-detalles">
                    <div class="text-center mb-4">
                        <h4 class="text-primary">
                            <i class="fas fa-ticket-alt me-2"></i>Reserva: \${codigoReserva}
                        </h4>
                        <p class="text-muted">Información completa de tu reserva de vuelo</p>
                    </div>
                     
                    <div class="row">
                        <div class="col-md-6">
                            <div class="card mb-3">
                                <div class="card-header bg-light">
                                    <h6 class="card-title mb-0">
                                        <i class="fas fa-plane me-2"></i>Información del Vuelo
                                    </h6>
                                </div>
                                <div class="card-body">
                                    <p class="mb-2"><strong>Aerolínea:</strong> Sistema de Vuelos</p>
                                    <p class="mb-2"><strong>Ruta:</strong> Origen → Destino</p>
                                    <p class="mb-2"><strong>Fecha:</strong> \${fechaFormateada}</p>
                                    <p class="mb-0"><strong>Estado:</strong> <span class="badge bg-success">Confirmada</span></p>
                                </div>
                            </div>
                        </div>
                         
                        <div class="col-md-6">
                            <div class="card mb-3">
                                <div class="card-header bg-light">
                                    <h6 class="card-title mb-0">
                                        <i class="fas fa-user me-2"></i>Información de Pago
                                    </h6>
                                </div>
                                <div class="card-body">
                                    <p class="mb-2"><strong>Método:</strong> Tarjeta de Crédito/Débito</p>
                                    <p class="mb-2"><strong>Fecha de Pago:</strong> \${fechaFormateada}</p>
                                    <p class="mb-0"><strong>Total:</strong> <span class="text-success fw-bold">$1,500.00 MXN</span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                     
                    <div class="card">
                        <div class="card-header bg-light">
                            <h6 class="card-title mb-0">
                                <i class="fas fa-users me-2"></i>Pasajeros
                            </h6>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-sm">
                                    <thead>
                                        <tr>
                                            <th>Nombre</th>
                                            <th>Tipo</th>
                                            <th>Documento</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Pasajero Principal</td>
                                            <td>Adulto</td>
                                            <td>INE/Passport</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                     
                    <div class="alert alert-info mt-3">
                        <h6 class="alert-heading">
                            <i class="fas fa-info-circle me-2"></i>Información Importante
                        </h6>
                        <ul class="mb-0">
                            <li>Presenta este comprobante y tu identificación en el aeropuerto</li>
                            <li>Realiza el check-in 2 horas antes del vuelo</li>
                            <li>Para cambios o cancelaciones, contacta a nuestro servicio al cliente</li>
                        </ul>
                    </div>
                </div>
            `;
            
            document.getElementById('detallesReservaContent').innerHTML = contenido;
            new bootstrap.Modal(document.getElementById('detallesReservaModal')).show();
        }
    </script>
</body>
</html>