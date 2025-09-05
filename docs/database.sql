drop table UsuariosPerfiles;
drop table Usuarios;
drop table Perfiles;
drop table Productos;

-- Tabla de Perfiles
CREATE TABLE Perfiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de Usuarios
CREATE TABLE Usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    githubLogin VARCHAR(255) NOT NULL UNIQUE,
    nombreCompleto VARCHAR(255),
    email VARCHAR(255),
    estatus INT NOT NULL
);

-- Tabla intermedia UsuariosPerfiles
CREATE TABLE UsuariosPerfiles (
    idUsuario BIGINT NOT NULL,
    idPerfil INT NOT NULL,
    PRIMARY KEY (idUsuario, idPerfil),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(id),
    FOREIGN KEY (idPerfil) REFERENCES Perfiles(id)
);

-- Tabla Productos (CRUD)
CREATE TABLE Productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DOUBLE,
    cantidad INT,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO Perfiles (perfil) VALUES ('ADMIN'), ('SUPERVISOR'), ('USUARIO');

-- Insert de productos
INSERT INTO Productos (nombre, descripcion, precio, cantidad, fechaCreacion) VALUES
('Laptop', 'Laptop profesional', 18500.00, 10, NOW()),
('Mouse', 'Mouse inalámbrico', 350.00, 50, NOW()),
('Teclado', 'Teclado mecánico', 1200.00, 30, NOW()),
('Monitor', 'Monitor 24 pulgadas', 3200.00, 15, NOW()),
('Impresora', 'Impresora multifuncional', 2500.00, 8, NOW()),
('Tablet', 'Tablet Android', 4200.00, 12, NOW()),
('Smartphone', 'Teléfono inteligente', 7800.00, 20, NOW()),
('Auriculares', 'Auriculares Bluetooth', 650.00, 40, NOW()),
('Cámara', 'Cámara digital', 5400.00, 7, NOW()),
('Disco Duro', 'Disco duro externo 1TB', 1100.00, 25, NOW()),
('Memoria USB', 'Memoria USB 64GB', 180.00, 60, NOW()),
('Router', 'Router WiFi', 900.00, 18, NOW()),
('Proyector', 'Proyector portátil', 3500.00, 5, NOW()),
('Silla Gamer', 'Silla ergonómica', 2900.00, 6, NOW()),
('Microfono', 'Micrófono profesional', 800.00, 14, NOW()),
('Webcam', 'Webcam HD', 450.00, 22, NOW()),
('Fuente Poder', 'Fuente de poder 600W', 950.00, 11, NOW()),
('Tarjeta Video', 'Tarjeta gráfica 8GB', 7200.00, 4, NOW()),
('SSD', 'Disco SSD 512GB', 1300.00, 16, NOW()),
('Altavoces', 'Altavoces estéreo', 650.00, 13, NOW());

-- Inserts para Usuarios
INSERT INTO Usuarios (githubLogin, nombreCompleto, email, estatus) VALUES
('user01', 'Juan Pérez', 'juan.perez@example.com', 1),
('user02', 'María López', 'maria.lopez@example.com', 1),
('user03', 'Carlos Sánchez', 'carlos.sanchez@example.com', 1),
('user04', 'Ana Torres', 'ana.torres@example.com', 1),
('user05', 'Luis Gómez', 'luis.gomez@example.com', 1),
('user06', 'Sofía Ramírez', 'sofia.ramirez@example.com', 1),
('user07', 'Miguel Díaz', 'miguel.diaz@example.com', 1),
('user08', 'Laura Castillo', 'laura.castillo@example.com', 1),
('user09', 'Jorge Ruiz', 'jorge.ruiz@example.com', 1),
('user10', 'Patricia Herrera', 'patricia.herrera@example.com', 1),
('user11', 'Ricardo Mendoza', 'ricardo.mendoza@example.com', 1),
('user12', 'Gabriela Silva', 'gabriela.silva@example.com', 1),
('user13', 'Fernando Vargas', 'fernando.vargas@example.com', 1),
('user14', 'Valeria Jiménez', 'valeria.jimenez@example.com', 1),
('user15', 'Andrés Castro', 'andres.castro@example.com', 1),
('user16', 'Paola Morales', 'paola.morales@example.com', 1),
('user17', 'Diego Navarro', 'diego.navarro@example.com', 1),
('user18', 'Claudia Ríos', 'claudia.rios@example.com', 1),
('user19', 'Roberto Paredes', 'roberto.paredes@example.com', 1),
('user20', 'Mónica Salinas', 'monica.salinas@example.com', 1);

-- Inserts para UsuariosPerfiles (asumiendo que los IDs generados son del 1 al 20)
INSERT INTO UsuariosPerfiles (idUsuario, idPerfil) VALUES
(1, 3),
(2, 3),
(3, 3),
(4, 3),
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(9, 3),
(10, 3),
(11, 3),
(12, 3),
(13, 3),
(14, 3),
(15, 3),
(16, 3),
(17, 3),
(18, 3),
(19, 3),
(20, 3);