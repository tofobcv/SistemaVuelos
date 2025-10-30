CREATE DATABASE IF NOT EXISTS sistema_vuelos;
USE sistema_vuelos;

CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email_verificado BOOLEAN DEFAULT FALSE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_verificacion DATETIME NULL
);

CREATE TABLE tokens_verificacion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    token VARCHAR(100) UNIQUE NOT NULL,
    tipo ENUM('verificacion', 'recuperacion') NOT NULL,
    expiracion DATETIME NOT NULL,
    utilizado BOOLEAN DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE historial_busquedas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    origen VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    fecha_ida DATE NOT NULL,
    fecha_vuelta DATE NULL,
    pasajeros INT NOT NULL,
    fecha_busqueda DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE reservas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo_reserva VARCHAR(10) UNIQUE NOT NULL,
    usuario_id INT NOT NULL,
    vuelo_id INT NOT NULL,
    fecha_reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('confirmada', 'cancelada', 'pendiente') DEFAULT 'confirmada',
    total_pagado DECIMAL(10,2) NOT NULL,
    email_contacto VARCHAR(255) NOT NULL,
    telefono_contacto VARCHAR(20) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (vuelo_id) REFERENCES vuelos(id)
);

ALTER TABLE reservas 
MODIFY COLUMN codigo_reserva VARCHAR(20) UNIQUE NOT NULL;

-- Tabla aerolíneas
CREATE TABLE aerolineas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    codigo VARCHAR(3) UNIQUE NOT NULL
);

-- Tabla vuelos
CREATE TABLE vuelos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    aerolinea_id INT NOT NULL,
    numero_vuelo VARCHAR(10) NOT NULL,
    origen VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    fecha_salida DATETIME NOT NULL,
    fecha_llegada DATETIME NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL,
    asientos_disponibles INT NOT NULL,
    escalas INT DEFAULT 0,
    duracion_minutos INT NOT NULL,
    FOREIGN KEY (aerolinea_id) REFERENCES aerolineas(id)
);

-- Insertar datos de ejemplo
INSERT INTO aerolineas (nombre, codigo) VALUES 
('Aeroméxico', 'AM'),
('Volaris', 'Y4'),
('Viva Aerobus', 'VB'),
('American Airlines', 'AA');

-- Insertar vuelos de ejemplo
INSERT INTO vuelos (aerolinea_id, numero_vuelo, origen, destino, fecha_salida, fecha_llegada, precio_base, asientos_disponibles, escalas, duracion_minutos) VALUES
(1, 'AM500', 'Ciudad de México', 'Cancún', '2024-06-15 08:00:00', '2024-06-15 10:30:00', 2500.00, 150, 0, 150),
(2, 'Y4123', 'Guadalajara', 'Tijuana', '2024-06-15 14:20:00', '2024-06-15 16:45:00', 1800.00, 120, 0, 145),
(3, 'VB789', 'Monterrey', 'Los Cabos', '2024-06-16 09:30:00', '2024-06-16 11:15:00', 2200.00, 80, 0, 105),
(1, 'AM702', 'Ciudad de México', 'Cancún', '2024-06-15 14:00:00', '2024-06-15 16:30:00', 2700.00, 140, 0, 150),
(4, 'AA123', 'Ciudad de México', 'Cancún', '2024-06-15 11:30:00', '2024-06-15 14:00:00', 3000.00, 100, 1, 150);

-- Verificar datos
SELECT v.*, a.nombre as aerolinea_nombre 
FROM vuelos v 
JOIN aerolineas a ON v.aerolinea_id = a.id;