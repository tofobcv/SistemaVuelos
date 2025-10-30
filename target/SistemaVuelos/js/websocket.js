class ClienteVuelosWebSocket {
    constructor() {
        this.socket = null;
        this.conectado = false;
        this.reconectarIntentos = 0;
        this.maxReconexiones = 5;
    }
    
    conectar() {
        try {
            console.log('🔗 Conectando al servidor de vuelos...');
            this.socket = new WebSocket('ws://localhost:9090');
            
            this.socket.onopen = (event) => {
                console.log('✅ Conectado al servidor de vuelos');
                this.conectado = true;
                this.reconectarIntentos = 0;
                this.mostrarEstado('Conectado');
                this.mostrarNotificacion('Conectado al servidor', 'success');
            };
            
            this.socket.onmessage = (event) => {
                console.log('📨 Mensaje recibido:', event.data);
                this.procesarMensaje(event.data);
            };
            
            this.socket.onclose = (event) => {
                console.log('🔌 Conexión cerrada');
                this.conectado = false;
                this.mostrarEstado('Desconectado');
                this.intentarReconexion();
            };
            
            this.socket.onerror = (error) => {
                console.error('❌ Error en WebSocket:', error);
                this.mostrarEstado('Error de conexión');
                this.mostrarNotificacion('Error de conexión', 'error');
            };
            
        } catch (error) {
            console.error('❌ Error al conectar:', error);
        }
    }
    
    procesarMensaje(mensaje) {
        try {
            if (mensaje.startsWith('BIENVENIDA:')) {
                const texto = mensaje.substring(11);
                this.mostrarNotificacion(texto, 'success');
            } else if (mensaje.startsWith('RESULTADOS_VUELOS:')) {
                this.mostrarResultadosVuelos(mensaje);
            } else if (mensaje.startsWith('ESTADO_RESERVA:')) {
                this.mostrarEstadoReserva(mensaje);
            } else if (mensaje.startsWith('RESPUESTA:')) {
                const texto = mensaje.substring(10);
                this.mostrarNotificacion(texto, 'info');
            } else if (mensaje.startsWith('ERROR:')) {
                const texto = mensaje.substring(6);
                this.mostrarNotificacion('Error: ' + texto, 'error');
            } else if (mensaje.startsWith('ECHO:')) {
                const texto = mensaje.substring(5);
                this.mostrarNotificacion('Echo: ' + texto, 'info');
            } else {
                console.log('📝 Mensaje de texto:', mensaje);
                this.mostrarNotificacion(mensaje, 'info');
            }
        } catch (e) {
            console.log('❌ Error procesando mensaje:', e);
        }
    }
    
    // Método para buscar vuelos via WebSocket
    buscarVuelos(origen, destino, fecha) {
        if (!this.conectado) {
            this.mostrarNotificacion('No conectado al servidor', 'error');
            return;
        }
        
        const mensaje = `BUSCAR_VUELOS:${origen},${destino},${fecha}`;
        this.enviarMensaje(mensaje);
        this.mostrarNotificacion('Buscando vuelos...', 'info');
    }
    
    // Método para consultar estado de reserva
    consultarEstadoReserva(codigoReserva) {
        if (!this.conectado) {
            this.mostrarNotificacion('No conectado al servidor', 'error');
            return;
        }
        
        const mensaje = `ESTADO_RESERVA:${codigoReserva}`;
        this.enviarMensaje(mensaje);
    }
    
    // Método para enviar mensaje simple
    enviarMensajeSimple(texto) {
        if (!this.conectado) {
            this.mostrarNotificacion('No conectado al servidor', 'error');
            return;
        }
        
        const mensaje = `MENSAJE:${texto}`;
        this.enviarMensaje(mensaje);
    }
    
    enviarMensaje(mensaje) {
        if (this.socket && this.conectado) {
            this.socket.send(mensaje);
            console.log('📤 Mensaje enviado:', mensaje);
        } else {
            console.error('❌ No se puede enviar - Socket no conectado');
        }
    }
    
    mostrarResultadosVuelos(mensaje) {
        try {
            // Formato: RESULTADOS_VUELOS:aerolinea,numero,precio,hora,duracion|...
            const datos = mensaje.substring(18);
            const vuelos = datos.split('|');
            
            let mensajeNotificacion = `Encontrados ${vuelos.length} vuelos`;
            this.mostrarNotificacion(mensajeNotificacion, 'success');
            
            // Mostrar detalles en consola
            console.log('🎯 Resultados de vuelos:');
            vuelos.forEach((vuelo, index) => {
                const partes = vuelo.split(',');
                console.log(`  ${index + 1}. ${partes[0]} ${partes[1]} - $${partes[2]} - ${partes[3]} (${partes[4]})`);
            });
            
        } catch (e) {
            console.error('Error mostrando resultados:', e);
        }
    }
    
    mostrarEstadoReserva(mensaje) {
        try {
            // Formato: ESTADO_RESERVA:codigo,estado,vuelo,fecha,pasajeros
            const datos = mensaje.substring(15);
            const partes = datos.split(',');
            
            const mensajeNotificacion = `Reserva ${partes[0]}: ${partes[1]} - ${partes[2]}`;
            this.mostrarNotificacion(mensajeNotificacion, 'info');
            
        } catch (e) {
            console.error('Error mostrando estado de reserva:', e);
        }
    }
    
    mostrarEstado(estado) {
        const estadoElemento = document.getElementById('estado-conexion');
        if (estadoElemento) {
            estadoElemento.textContent = `Socket: ${estado}`;
            estadoElemento.className = `badge ${estado === 'Conectado' ? 'bg-success' : 'bg-danger'}`;
        }
    }
    
    mostrarNotificacion(mensaje, tipo = 'info') {
        // Crear notificación bonita
        const notificacion = document.createElement('div');
        notificacion.className = `alert alert-${tipo} alert-dismissible fade show`;
        notificacion.innerHTML = `
            ${mensaje}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        // Agregar al contenedor de notificaciones
        const contenedor = document.getElementById('notificaciones-socket') || this.crearContenedorNotificaciones();
        contenedor.appendChild(notificacion);
        
        // Auto-eliminar después de 5 segundos
        setTimeout(() => {
            if (notificacion.parentElement) {
                notificacion.remove();
            }
        }, 5000);
    }
    
    crearContenedorNotificaciones() {
        const contenedor = document.createElement('div');
        contenedor.id = 'notificaciones-socket';
        contenedor.style.position = 'fixed';
        contenedor.style.top = '20px';
        contenedor.style.right = '20px';
        contenedor.style.zIndex = '9999';
        contenedor.style.maxWidth = '400px';
        document.body.appendChild(contenedor);
        return contenedor;
    }
    
    intentarReconexion() {
        if (this.reconectarIntentos < this.maxReconexiones) {
            this.reconectarIntentos++;
            const tiempo = Math.min(1000 * this.reconectarIntentos, 10000);
            
            console.log(`🔄 Reconectando en ${tiempo/1000} segundos... (intento ${this.reconectarIntentos})`);
            
            setTimeout(() => {
                this.conectar();
            }, tiempo);
        } else {
            console.error('❌ Máximo de reconexiones alcanzado');
            this.mostrarNotificacion('No se pudo reconectar al servidor', 'error');
        }
    }
    
    desconectar() {
        if (this.socket) {
            this.socket.close();
            this.conectado = false;
            this.mostrarEstado('Desconectado');
        }
    }
}

// Instancia global
const clienteVuelos = new ClienteVuelosWebSocket();

// Conectar cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    clienteVuelos.conectar();
    
    // Agregar botones de prueba
    agregarBotonesPrueba();
});

// Función para agregar botones de prueba
function agregarBotonesPrueba() {
    const contenedor = document.createElement('div');
    contenedor.className = 'position-fixed bottom-0 end-0 p-3';
    contenedor.style.zIndex = '9998';
    contenedor.innerHTML = `
        <div class="btn-group-vertical">
            <button class="btn btn-primary btn-sm mb-1" onclick="clienteVuelos.buscarVuelos('CDMX', 'Cancún', '2024-06-15')">
                🔍 Buscar Vuelos
            </button>
            <button class="btn btn-info btn-sm mb-1" onclick="clienteVuelos.consultarEstadoReserva('RES12345')">
                📋 Estado Reserva
            </button>
            <button class="btn btn-secondary btn-sm mb-1" onclick="clienteVuelos.enviarMensajeSimple('Hola Servidor')">
                💬 Mensaje Test
            </button>
        </div>
    `;
    document.body.appendChild(contenedor);
}