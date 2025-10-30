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
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<title>Sistema de Reservas de Vuelos</title> 
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"> 
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"> 
<style> 
.hero-section { 
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
color: white; 
padding: 80px 0; 
} 
.search-box { 
background: white; 
border-radius: 10px; 
padding: 30px; 
box-shadow: 0 10px 30px rgba(0,0,0,0.2); 
margin-top: -50px; 
}
.user-menu {
    position: absolute;
    top: 20px;
    right: 20px;
}
.features-section {
    padding: 60px 0;
    background-color: #f8f9fa;
}
.feature-card {
    text-align: center;
    padding: 30px 20px;
    border-radius: 10px;
    transition: transform 0.3s ease;
}
.feature-card:hover {
    transform: translateY(-5px);
}
.feature-icon {
    font-size: 3rem;
    margin-bottom: 20px;
    color: #667eea;
}
</style> 
</head> 
<body> 
<!-- User Menu -->
<div class="user-menu">
    <c:choose>
        <c:when test="${not empty sessionScope.usuarioNombre}">
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

<!-- Hero Section --> 
<div class="hero-section"> 
<div class="container"> 
<div class="row"> 
<div class="col-md-8 mx-auto text-center"> 
<h1 class="display-4 mb-4"> 
<i class="fas fa-plane-departure me-3"></i> 
Sistema de Reservas de Vuelos
</h1> 
<p class="lead">Encuentra y reserva los mejores vuelos al mejor precio</p> 
</div> 
</div> 
</div> 
</div> 

<!-- Formulario de Búsqueda --> 
<div class="container"> 
<div class="row justify-content-center"> 
<div class="col-lg-10"> 
<div class="search-box"> 
    
    <form action="buscarVuelos" method="get" id="formBusqueda">
    <div class="row g-3"> 
    <div class="col-md-6"> 
    <label class="form-label">Origen</label> 
    <input type="text" class="form-control" name="origen" placeholder="Ej: Ciudad de México" 
           required list="ciudades-origen">
    <datalist id="ciudades-origen">
        <option value="Ciudad de México">
        <option value="Guadalajara">
        <option value="Monterrey">
        <option value="Cancún">
        <option value="Tijuana">
        <option value="Los Cabos">
        <option value="Puerto Vallarta">
        <option value="Mérida">
        <option value="Oaxaca">
    </datalist>
    </div> 
    <div class="col-md-6"> 
    <label class="form-label">Destino</label> 
    <input type="text" class="form-control" name="destino" placeholder="Ej: Cancún" 
           required list="ciudades-destino">
    <datalist id="ciudades-destino">
        <option value="Cancún">
        <option value="Ciudad de México">
        <option value="Guadalajara">
        <option value="Monterrey">
        <option value="Tijuana">
        <option value="Los Cabos">
        <option value="Puerto Vallarta">
        <option value="Mérida">
        <option value="Oaxaca">
    </datalist>
    </div> 
    <div class="col-md-4"> 
    <label class="form-label">Fecha Ida</label> 
    <input type="date" class="form-control" name="fechaIda" id="fechaIda" required>
    </div> 
    <div class="col-md-4"> 
    <label class="form-label">Fecha Vuelta (Opcional)</label> 
    <input type="date" class="form-control" name="fechaVuelta" id="fechaVuelta">
    </div> 
    <div class="col-md-4"> 
    <label class="form-label">Pasajeros</label> 
    <select class="form-select" name="pasajeros"> 
    <option value="1">1 pasajero</option> 
    <option value="2">2 pasajeros</option> 
    <option value="3">3 pasajeros</option> 
    <option value="4">4 pasajeros</option> 
    </select> 
    </div> 
    <div class="col-12 text-center"> 
    <button type="submit" class="btn btn-primary btn-lg w-50"> 
    <i class="fas fa-search me-2"></i> Buscar Vuelos 
    </button> 
    </div> 
    </div> 
    </form> 
</div> 
</div> 
</div> 
</div> 

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Establecer fecha mínima (hoy) y fecha por defecto (mañana)
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    
    const todayFormatted = today.toISOString().split('T')[0];
    const tomorrowFormatted = tomorrow.toISOString().split('T')[0];
    
    document.getElementById('fechaIda').min = todayFormatted;
    document.getElementById('fechaIda').value = tomorrowFormatted;
    document.getElementById('fechaVuelta').min = tomorrowFormatted;

    // Validación del formulario
    document.getElementById('formBusqueda').addEventListener('submit', function(e) {
        const origen = document.querySelector('input[name="origen"]');
        const destino = document.querySelector('input[name="destino"]');
        const fechaIda = document.getElementById('fechaIda');
        
        // Validar que origen y destino sean diferentes
        if (origen.value.toLowerCase() === destino.value.toLowerCase()) {
            e.preventDefault();
            alert('El origen y destino deben ser diferentes');
            origen.focus();
            return;
        }
        
        // Validar fecha no sea en el pasado
        if (fechaIda.value < todayFormatted) {
            e.preventDefault();
            alert('La fecha de ida no puede ser en el pasado');
            fechaIda.focus();
            return;
        }
    });

    // Sincronizar fecha de vuelta
    document.getElementById('fechaIda').addEventListener('change', function() {
        const fechaVuelta = document.getElementById('fechaVuelta');
        if (fechaVuelta.value && fechaVuelta.value < this.value) {
            fechaVuelta.value = this.value;
        }
        fechaVuelta.min = this.value;
    });
</script>
</body> 
</html>