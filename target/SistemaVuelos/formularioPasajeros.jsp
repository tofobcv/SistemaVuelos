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
    <title>Datos de Pasajeros - Sistema de Vuelos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .pasajero-section {
            border: 1px solid #dee2e6;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            background: #f8f9fa;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/paginas/comunes/cabecero.jsp"/>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-10">
                
                <!-- Progreso -->
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div class="text-center">
                                <span class="badge bg-primary rounded-circle p-2">1</span>
                                <div class="mt-1"><small>Datos Pasajeros</small></div>
                            </div>
                            <div class="text-center">
                                <span class="badge bg-secondary rounded-circle p-2">2</span>
                                <div class="mt-1"><small>Pago</small></div>
                            </div>
                            <div class="text-center">
                                <span class="badge bg-secondary rounded-circle p-2">3</span>
                                <div class="mt-1"><small>Confirmación</small></div>
                            </div>
                        </div>
                    </div>
                </div>

                <h2><i class="fas fa-users me-2"></i>Datos de los Pasajeros</h2>
                <p class="text-muted">Ingresa la información de todos los pasajeros</p>

                <!-- Información del Vuelo -->
                <div class="card mb-4">
                    <div class="card-header bg-info text-white">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-plane me-2"></i>Vuelo Seleccionado
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-8">
                                <h5>${vuelo.aerolinea} - ${vuelo.numeroVuelo}</h5>
                                <p class="mb-1"><strong>Ruta:</strong> ${vuelo.origen} → ${vuelo.destino}</p>
                                <p class="mb-1"><strong>Fecha:</strong> ${vuelo.fechaSalida}</p>
                                <p class="mb-0"><strong>Pasajeros:</strong> ${pasajeros}</p>
                            </div>
                            <div class="col-md-4 text-end">
                                <h4 class="text-success">$${total} MXN</h4>
                                <small class="text-muted">Total para ${pasajeros} pasajero(s)</small>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Formulario de Pasajeros -->
                <form action="procesarReserva" method="post" id="formPasajeros">
                    <input type="hidden" name="vueloId" value="${vuelo.id}">
                    <input type="hidden" name="total" value="${total}">
                    
                    <c:forEach var="i" begin="1" end="${pasajeros}">
                        <div class="pasajero-section">
                            <h5><i class="fas fa-user me-2"></i>Pasajero ${i}</h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Nombre *</label>
                                    <input type="text" class="form-control" name="nombre${i}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Apellido *</label>
                                    <input type="text" class="form-control" name="apellido${i}" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Fecha de Nacimiento</label>
                                    <input type="date" class="form-control" name="fechaNacimiento${i}">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Género</label>
                                    <select class="form-select" name="genero${i}">
                                        <option value="">Seleccionar</option>
                                        <option value="M">Masculino</option>
                                        <option value="F">Femenino</option>
                                        <option value="O">Otro</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Tipo Documento</label>
                                    <select class="form-select" name="tipoDocumento${i}">
                                        <option value="">Seleccionar</option>
                                        <option value="INE">INE</option>
                                        <option value="Pasaporte">Pasaporte</option>
                                        <option value="Licencia">Licencia</option>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Número de Documento</label>
                                    <input type="text" class="form-control" name="numeroDocumento${i}">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control" name="email${i}">
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Teléfono de Contacto</label>
                                    <input type="tel" class="form-control" name="telefono${i}">
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Requerimientos Especiales</label>
                                    <textarea class="form-control" name="requerimientos${i}" rows="2" 
                                              placeholder="Alergias, discapacidades, etc."></textarea>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Información de Contacto -->
                    <div class="card mb-4">
                        <div class="card-header bg-warning text-dark">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-envelope me-2"></i>Información de Contacto
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Email de Contacto *</label>
                                    <input type="email" class="form-control" name="emailContacto" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Teléfono de Contacto *</label>
                                    <input type="tel" class="form-control" name="telefonoContacto" required>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="d-flex justify-content-between">
                        <a href="javascript:history.back()" class="btn btn-outline-secondary btn-lg">
                            <i class="fas fa-arrow-left me-2"></i>Volver Atrás
                        </a>
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-arrow-right me-2"></i>Continuar al Pago
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>