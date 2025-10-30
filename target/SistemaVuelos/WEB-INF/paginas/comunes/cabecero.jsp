<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%  
    // Configurar encoding para caracteres especiales  
    request.setCharacterEncoding("UTF-8");  
    response.setCharacterEncoding("UTF-8");  
    response.setContentType("text/html; charset=UTF-8");  
%>  
<header class="bg-primary text-white py-3">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-6">
                <h1><i class="fas fa-plane"></i> Sistema de Vuelos</h1>
            </div>
            <div class="col-md-6 text-end">
                <div class="d-flex align-items-center justify-content-end">
                    <span class="badge bg-light text-dark me-3" id="estado-conexion">Conectando...</span>
                     
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuarioNombre}">
                            <!-- Usuario logueado -->
                            <div class="dropdown">
                                <button class="btn btn-outline-light dropdown-toggle" type="button"  
                                        data-bs-toggle="dropdown">
                                    <i class="fas fa-user me-1"></i>${sessionScope.usuarioNombre}
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li><a class="dropdown-item" href="historial">
                                        <i class="fas fa-history me-2"></i>Mi Historial
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="logout">
                                        <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión
                                    </a></li>
                                </ul>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!-- Usuario no logueado -->
                            <div>
                                <a href="login.jsp" class="btn btn-outline-light me-2">
                                    <i class="fas fa-sign-in-alt me-1"></i>Iniciar Sesión
                                </a>
                                <a href="registro.jsp" class="btn btn-light">
                                    <i class="fas fa-user-plus me-1"></i>Registrarse
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</header>