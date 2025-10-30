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
    <title>Resultados de Búsqueda</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .filtro-activo {
            background-color: #e3f2fd;
            border-left: 4px solid #2196F3;
        }
        .vuelo-card {
            transition: all 0.3s ease;
            border: 1px solid #dee2e6;
        }
        .vuelo-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .precio-destacado {
            font-size: 1.5rem;
            font-weight: bold;
            color: #28a745;
        }
    </style>
</head>
<body>
    <!-- Cabecero -->
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container mt-4">
        <div class="row mb-4">
            <div class="col-12">
                <h2><i class="fas fa-search me-2"></i>Resultados para: ${origen} → ${destino}</h2>
                <p class="text-muted">
                    <strong>Fecha:</strong> ${fechaIda} 
                    <c:if test="${not empty fechaVuelta}">| <strong>Vuelta:</strong> ${fechaVuelta}</c:if>
                    | <strong>Pasajeros:</strong> ${pasajeros}
                </p>
                <p class="text-success">
                    <strong>${vuelos.size()} vuelos encontrados</strong>
                </p>
            </div>
        </div>
         
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <!-- Sección de Filtros -->
        <div class="card mb-4">
            <div class="card-header bg-light">
                <h5 class="card-title mb-0">
                    <i class="fas fa-filter me-2"></i>Filtrar Resultados
                </h5>
            </div>
            <div class="card-body">
                <form method="get" action="buscarVuelos" class="row g-3">
                    <input type="hidden" name="origen" value="${origen}">
                    <input type="hidden" name="destino" value="${destino}">
                    <input type="hidden" name="fechaIda" value="${fechaIda}">
                    <input type="hidden" name="pasajeros" value="${pasajeros}">
                    
                    <div class="col-md-3">
                        <label class="form-label">Aerolínea</label>
                        <select class="form-select" name="aerolinea">
                            <option value="">Todas</option>
                            <option value="Aeroméxico" ${param.aerolinea == 'Aeroméxico' ? 'selected' : ''}>Aeroméxico</option>
                            <option value="Volaris" ${param.aerolinea == 'Volaris' ? 'selected' : ''}>Volaris</option>
                            <option value="Viva Aerobus" ${param.aerolinea == 'Viva Aerobus' ? 'selected' : ''}>Viva Aerobus</option>
                            <option value="American Airlines" ${param.aerolinea == 'American Airlines' ? 'selected' : ''}>American Airlines</option>
                        </select>
                    </div>
                    
                    <div class="col-md-2">
                        <label class="form-label">Precio Máx.</label>
                        <input type="number" class="form-control" name="precioMaximo" 
                               value="${param.precioMaximo}" placeholder="5000" min="0" step="100">
                    </div>
                    
                    <div class="col-md-2">
                        <label class="form-label">Escalas Máx.</label>
                        <select class="form-select" name="escalas">
                            <option value="">Cualquiera</option>
                            <option value="0" ${param.escalas == '0' ? 'selected' : ''}>Directo</option>
                            <option value="1" ${param.escalas == '1' ? 'selected' : ''}>1 escala</option>
                            <option value="2" ${param.escalas == '2' ? 'selected' : ''}>2 escalas</option>
                        </select>
                    </div>
                    
                    <div class="col-md-2">
                        <label class="form-label">Horario</label>
                        <select class="form-select" name="horario">
                            <option value="">Cualquiera</option>
                            <option value="manana" ${param.horario == 'manana' ? 'selected' : ''}>Mañana (6:00-12:00)</option>
                            <option value="tarde" ${param.horario == 'tarde' ? 'selected' : ''}>Tarde (12:00-18:00)</option>
                            <option value="noche" ${param.horario == 'noche' ? 'selected' : ''}>Noche (18:00-24:00)</option>
                        </select>
                    </div>
                    
                    <div class="col-md-2">
                        <label class="form-label">Ordenar por</label>
                        <select class="form-select" name="ordenarPor">
                            <option value="">Predeterminado</option>
                            <option value="precio" ${param.ordenarPor == 'precio' ? 'selected' : ''}>Precio (menor)</option>
                            <option value="precio_desc" ${param.ordenarPor == 'precio_desc' ? 'selected' : ''}>Precio (mayor)</option>
                            <option value="duracion" ${param.ordenarPor == 'duracion' ? 'selected' : ''}>Duración</option>
                            <option value="salida" ${param.ordenarPor == 'salida' ? 'selected' : ''}>Hora salida</option>
                        </select>
                    </div>
                    
                    <div class="col-md-1 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100" title="Aplicar filtros">
                            <i class="fas fa-filter"></i>
                        </button>
                    </div>
                </form>
                
                <!-- Mostrar filtros activos -->
                <c:set var="filtrosAplicados" value="false"/>
                <c:if test="${not empty param.aerolinea || not empty param.precioMaximo || not empty param.escalas || not empty param.horario || not empty param.ordenarPor}">
                    <c:set var="filtrosAplicados" value="true"/>
                    <div class="mt-3 p-2 filtro-activo">
                        <small class="text-muted">
                            <strong>Filtros aplicados:</strong> 
                            <c:if test="${not empty param.aerolinea}">
                                <span class="badge bg-info me-1">Aerolínea: ${param.aerolinea}</span>
                            </c:if>
                            <c:if test="${not empty param.precioMaximo}">
                                <span class="badge bg-info me-1">Precio máximo: $${param.precioMaximo}</span>
                            </c:if>
                            <c:if test="${not empty param.escalas}">
                                <span class="badge bg-info me-1">Escalas: ${param.escalas}</span>
                            </c:if>
                            <c:if test="${not empty param.horario}">
                                <span class="badge bg-info me-1">Horario: ${param.horario}</span>
                            </c:if>
                            <c:if test="${not empty param.ordenarPor}">
                                <span class="badge bg-info me-1">Orden: ${param.ordenarPor}</span>
                            </c:if>
                            <a href="buscarVuelos?origen=${origen}&destino=${destino}&fechaIda=${fechaIda}&pasajeros=${pasajeros}" 
                               class="text-danger ms-2">
                                <i class="fas fa-times"></i> Limpiar filtros
                            </a>
                        </small>
                    </div>
                </c:if>
            </div>
        </div>
         
        <c:choose>
            <c:when test="${not empty vuelos}">
                <div class="row">
                    <c:forEach var="vuelo" items="${vuelos}">
                        <div class="col-md-6 mb-4">
                            <div class="card h-100 vuelo-card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="card-title mb-0">
                                        <i class="fas fa-plane me-2"></i>${vuelo.aerolinea} - ${vuelo.numeroVuelo}
                                    </h5>
                                    <c:if test="${vuelo.escalas == 0}">
                                        <span class="badge bg-success">Vuelo Directo</span>
                                    </c:if>
                                    <c:if test="${vuelo.escalas > 0}">
                                        <span class="badge bg-warning">${vuelo.escalas} escala(s)</span>
                                    </c:if>
                                </div>
                                <div class="card-body">
                                    <div class="row mb-3">
                                        <div class="col-6">
                                            <strong><i class="fas fa-plane-departure text-success me-1"></i>Salida:</strong><br>
                                            <fmt:formatDate value="${vuelo.fechaSalida}" pattern="dd/MM/yyyy HH:mm" />
                                        </div>
                                        <div class="col-6">
                                            <strong><i class="fas fa-plane-arrival text-primary me-1"></i>Llegada:</strong><br>
                                            <fmt:formatDate value="${vuelo.fechaLlegada}" pattern="dd/MM/yyyy HH:mm" />
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-6">
                                            <strong><i class="fas fa-clock me-1"></i>Duración:</strong><br>
                                            <c:set var="horas" value="${vuelo.duracionMinutos / 60}"/>
                                            <c:set var="minutos" value="${vuelo.duracionMinutos % 60}"/>
                                            <fmt:formatNumber value="${horas}" maxFractionDigits="0"/>h ${minutos}min
                                        </div>
                                        <div class="col-6">
                                            <strong><i class="fas fa-chair me-1"></i>Asientos:</strong><br>
                                            <c:choose>
                                                <c:when test="${vuelo.asientosDisponibles > 10}">
                                                    <span class="text-success">${vuelo.asientosDisponibles} disponibles</span>
                                                </c:when>
                                                <c:when test="${vuelo.asientosDisponibles > 0}">
                                                    <span class="text-warning">Solo ${vuelo.asientosDisponibles}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-danger">Agotado</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="row align-items-center">
                                        <div class="col-8">
                                            <small class="text-muted">
                                                <i class="fas fa-route me-1"></i>${vuelo.origen} → ${vuelo.destino}
                                            </small>
                                        </div>
                                        <div class="col-4 text-end">
                                            <div class="precio-destacado">
                                                $<fmt:formatNumber value="${vuelo.precio}" pattern="#,##0.00"/>
                                            </div>
                                            <small class="text-muted">por pasajero</small>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <form action="seleccionarVuelo" method="post">
                                        <input type="hidden" name="vueloId" value="${vuelo.id}">
                                        <input type="hidden" name="pasajeros" value="${pasajeros}">
                                        <c:choose>
                                            <c:when test="${vuelo.asientosDisponibles > 0}">
                                                <button type="submit" class="btn btn-success w-100">
                                                    <i class="fas fa-check me-2"></i>Seleccionar Vuelo
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" class="btn btn-secondary w-100" disabled>
                                                    <i class="fas fa-times me-2"></i>No Disponible
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Estadísticas de precios -->
                <c:set var="precioMin" value="${999999}"/>
                <c:set var="precioMax" value="${0}"/>
                <c:set var="precioTotal" value="${0}"/>
                
                <c:forEach var="vuelo" items="${vuelos}">
                    <c:if test="${vuelo.precio < precioMin}">
                        <c:set var="precioMin" value="${vuelo.precio}"/>
                    </c:if>
                    <c:if test="${vuelo.precio > precioMax}">
                        <c:set var="precioMax" value="${vuelo.precio}"/>
                    </c:if>
                    <c:set var="precioTotal" value="${precioTotal + vuelo.precio}"/>
                </c:forEach>
                
                <c:set var="precioPromedio" value="${precioTotal / vuelos.size()}"/>
                
                <div class="card mt-4">
                    <div class="card-header bg-info text-white">
                        <h6 class="card-title mb-0">
                            <i class="fas fa-chart-bar me-2"></i>Estadísticas de Precios
                        </h6>
                    </div>
                    <div class="card-body">
                        <div class="row text-center">
                            <div class="col-md-3">
                                <h5 class="text-success">$<fmt:formatNumber value="${precioMin}" pattern="#,##0.00"/></h5>
                                <small class="text-muted">Precio Mínimo</small>
                            </div>
                            <div class="col-md-3">
                                <h5 class="text-warning">$<fmt:formatNumber value="${precioPromedio}" pattern="#,##0.00"/></h5>
                                <small class="text-muted">Precio Promedio</small>
                            </div>
                            <div class="col-md-3">
                                <h5 class="text-danger">$<fmt:formatNumber value="${precioMax}" pattern="#,##0.00"/></h5>
                                <small class="text-muted">Precio Máximo</small>
                            </div>
                            <div class="col-md-3">
                                <h5 class="text-primary">${vuelos.size()}</h5>
                                <small class="text-muted">Vuelos Encontrados</small>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning text-center">
                    <i class="fas fa-exclamation-triangle fa-2x mb-3"></i>
                    <h4>No se encontraron vuelos</h4>
                    <p>No hay vuelos disponibles para la ruta y fecha seleccionadas.</p>
                    <p class="mb-3">Intenta con otras fechas o rutas diferentes.</p>
                    
                    <c:if test="${filtrosAplicados}">
                        <div class="mt-3">
                            <p>Los filtros aplicados pueden estar restringiendo demasiado los resultados.</p>
                            <a href="buscarVuelos?origen=${origen}&destino=${destino}&fechaIda=${fechaIda}&pasajeros=${pasajeros}" 
                               class="btn btn-primary">
                                <i class="fas fa-times me-2"></i>Limpiar Filtros
                            </a>
                        </div>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
         
        <div class="text-center mt-4">
            <a href="index.jsp" class="btn btn-secondary me-2">
                <i class="fas fa-arrow-left me-2"></i>Nueva Búsqueda
            </a>
            <a href="historial" class="btn btn-outline-primary">
                <i class="fas fa-history me-2"></i>Ver Mi Historial
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>