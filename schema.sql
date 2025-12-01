-- ============================================
-- LEVEL-UP GAMER - MYSQL DATABASE SCHEMA
-- E-Commerce Gaming Platform
-- ============================================

-- Crear la base de datos
DROP DATABASE IF EXISTS levelup_gamer;
CREATE DATABASE levelup_gamer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE levelup_gamer;

-- ============================================
-- TABLA: regiones
-- Regiones de Chile
-- ============================================
CREATE TABLE regiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- TABLA: comunas
-- Comunas de Chile asociadas a regiones
-- ============================================
CREATE TABLE comunas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    region_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (region_id) REFERENCES regiones(id) ON DELETE CASCADE,
    INDEX idx_region (region_id)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: usuarios
-- Usuarios del sistema (clientes y administradores)
-- ============================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    edad INT,
    direccion TEXT,
    comuna_id INT,
    region_id INT,
    rol ENUM('admin', 'cliente') DEFAULT 'cliente',
    estado ENUM('activo', 'inactivo', 'suspendido') DEFAULT 'activo',
    descuento_duoc DECIMAL(5,2) DEFAULT 0.00,
    newsletter BOOLEAN DEFAULT FALSE,
    notificaciones BOOLEAN DEFAULT TRUE,
    tema VARCHAR(20) DEFAULT 'dark',
    email_verificado BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_conexion TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (comuna_id) REFERENCES comunas(id) ON DELETE SET NULL,
    FOREIGN KEY (region_id) REFERENCES regiones(id) ON DELETE SET NULL,
    INDEX idx_email (email),
    INDEX idx_rol (rol),
    INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: categorias
-- Categorías de productos
-- ============================================
CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    imagen_url VARCHAR(500),
    activo BOOLEAN DEFAULT TRUE,
    orden INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_activo (activo),
    INDEX idx_orden (orden)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: productos
-- Catálogo de productos
-- ============================================
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    categoria_id INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    stock_minimo INT DEFAULT 10,
    imagen_url VARCHAR(500),
    imagenes_adicionales JSON,
    especificaciones JSON,
    activo BOOLEAN DEFAULT TRUE,
    destacado BOOLEAN DEFAULT FALSE,
    vistas INT DEFAULT 0,
    ventas_total INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT,
    INDEX idx_codigo (codigo),
    INDEX idx_categoria (categoria_id),
    INDEX idx_activo (activo),
    INDEX idx_destacado (destacado),
    INDEX idx_precio (precio)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: carritos
-- Carritos de compra activos
-- ============================================
CREATE TABLE carritos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    estado ENUM('activo', 'abandonado', 'convertido') DEFAULT 'activo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_usuario (usuario_id),
    INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: carrito_items
-- Items individuales en el carrito
-- ============================================
CREATE TABLE carrito_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carrito_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (carrito_id) REFERENCES carritos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_carrito (carrito_id),
    INDEX idx_producto (producto_id),
    UNIQUE KEY unique_carrito_producto (carrito_id, producto_id)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: ordenes
-- Órdenes de compra finalizadas
-- ============================================
CREATE TABLE ordenes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_orden VARCHAR(50) NOT NULL UNIQUE,
    usuario_id INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL,
    estado ENUM('pendiente', 'pagado', 'preparando', 'enviado', 'entregado', 'cancelado') DEFAULT 'pendiente',
    metodo_pago VARCHAR(50),
    direccion_envio TEXT NOT NULL,
    comuna_envio VARCHAR(100),
    region_envio VARCHAR(100),
    telefono_contacto VARCHAR(20),
    notas TEXT,
    fecha_pago TIMESTAMP NULL,
    fecha_envio TIMESTAMP NULL,
    fecha_entrega TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    INDEX idx_numero_orden (numero_orden),
    INDEX idx_usuario (usuario_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha (created_at)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: orden_items
-- Items de cada orden
-- ============================================
CREATE TABLE orden_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orden_id INT NOT NULL,
    producto_id INT NOT NULL,
    producto_nombre VARCHAR(255) NOT NULL,
    producto_codigo VARCHAR(50) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (orden_id) REFERENCES ordenes(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT,
    INDEX idx_orden (orden_id),
    INDEX idx_producto (producto_id)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: resenas
-- Reseñas de productos por usuarios
-- ============================================
CREATE TABLE resenas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    usuario_id INT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    titulo VARCHAR(200),
    comentario TEXT,
    verificado BOOLEAN DEFAULT FALSE,
    likes INT DEFAULT 0,
    estado ENUM('pendiente', 'aprobado', 'rechazado') DEFAULT 'pendiente',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_producto (producto_id),
    INDEX idx_usuario (usuario_id),
    INDEX idx_calificacion (calificacion),
    INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: contacto
-- Mensajes del formulario de contacto
-- ============================================
CREATE TABLE contacto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    asunto VARCHAR(200),
    mensaje TEXT NOT NULL,
    estado ENUM('nuevo', 'leido', 'respondido', 'archivado') DEFAULT 'nuevo',
    respuesta TEXT,
    fecha_respuesta TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_estado (estado),
    INDEX idx_fecha (created_at)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: blog_posts
-- Entradas del blog
-- ============================================
CREATE TABLE blog_posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    contenido TEXT NOT NULL,
    resumen TEXT,
    imagen_destacada VARCHAR(500),
    autor_id INT NOT NULL,
    categoria VARCHAR(100),
    tags JSON,
    publicado BOOLEAN DEFAULT FALSE,
    vistas INT DEFAULT 0,
    fecha_publicacion TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    INDEX idx_slug (slug),
    INDEX idx_publicado (publicado),
    INDEX idx_categoria (categoria),
    INDEX idx_fecha_publicacion (fecha_publicacion)
) ENGINE=InnoDB;

-- ============================================
-- TABLA: sesiones
-- Sesiones de usuario para manejo de autenticación
-- ============================================
CREATE TABLE sesiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    ip_address VARCHAR(45),
    user_agent TEXT,
    expira_en TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_usuario (usuario_id),
    INDEX idx_expira (expira_en)
) ENGINE=InnoDB;

-- ============================================
-- POBLADO DE DATOS
-- ============================================

-- Insertar Regiones de Chile
INSERT INTO regiones (nombre, codigo) VALUES
('Arica y Parinacota', 'AP'),
('Tarapacá', 'TA'),
('Antofagasta', 'AN'),
('Atacama', 'AT'),
('Coquimbo', 'CO'),
('Valparaíso', 'VA'),
('Región Metropolitana', 'RM'),
('O''Higgins', 'OH'),
('Maule', 'MA'),
('Ñuble', 'NU'),
('Biobío', 'BI'),
('La Araucanía', 'AR'),
('Los Ríos', 'LR'),
('Los Lagos', 'LL'),
('Aysén', 'AY'),
('Magallanes', 'MG');

-- Insertar Comunas principales (muestra de las más importantes)
INSERT INTO comunas (nombre, region_id) VALUES
-- Región Metropolitana (id 7)
('Santiago', 7),
('Providencia', 7),
('Las Condes', 7),
('Vitacura', 7),
('Ñuñoa', 7),
('La Reina', 7),
('Macul', 7),
('Peñalolén', 7),
('La Florida', 7),
('Maipú', 7),
('Pudahuel', 7),
('Cerrillos', 7),
('Estación Central', 7),
('Quinta Normal', 7),
('Recoleta', 7),
('Independencia', 7),
('Conchalí', 7),
('Huechuraba', 7),
('La Cisterna', 7),
('San Miguel', 7),
-- Valparaíso (id 6)
('Valparaíso', 6),
('Viña del Mar', 6),
('Concón', 6),
('Quilpué', 6),
('Villa Alemana', 6),
-- Biobío (id 11)
('Concepción', 11),
('Talcahuano', 11),
('Los Angeles', 11),
('Chillán', 11),
('San Pedro de la Paz', 11);

-- Insertar Categorías
INSERT INTO categorias (nombre, descripcion, orden) VALUES
('Juegos de Mesa', 'Juegos de mesa clásicos y modernos para toda la familia', 1),
('Accesorios', 'Accesorios gaming de alta calidad', 2),
('Consolas', 'Las mejores consolas del mercado', 3),
('Computadores Gamers', 'PCs gaming de alto rendimiento', 4),
('Sillas Gamers', 'Sillas ergonómicas para largas sesiones de juego', 5),
('Mouse', 'Mouses gaming de precisión', 6),
('Mousepad', 'Mousepads para máximo control', 7),
('Poleras Personalizadas', 'Poleras con diseños gaming exclusivos', 8),
('Polerones Personalizados', 'Polerones gaming cómodos y con estilo', 9);

-- Insertar Productos (basados en los del proyecto original)
INSERT INTO productos (codigo, categoria_id, nombre, descripcion, precio, stock, imagen_url, activo, destacado) VALUES
('JM001', 1, 'Catan', 'El clásico juego de estrategia donde construyes asentamientos y ciudades en la isla de Catán', 29990.00, 50, 'assets/img/8436017220100-1200-face3d-copy3162-removebg-preview.png', TRUE, TRUE),
('JM002', 1, 'Carcassonne', 'Construye tu reino medieval colocando losetas y estratégicamente', 24990.00, 35, 'assets/img/carcassonne-removebg-preview.png', TRUE, TRUE),
('AC001', 2, 'Controlador Inalámbrico Xbox Series X', 'Control inalámbrico de última generación con tecnología Bluetooth', 59990.00, 100, 'assets/img/D_970114-MLA45317791910_032021-O_500x-removebg-preview.png', TRUE, TRUE),
('AC002', 2, 'Auriculares Gamer HyperX Cloud II', 'Auriculares gaming con sonido envolvente 7.1 y micrófono extraíble', 79990.00, 75, 'assets/img/cloud-ii-1_2-removebg-preview.png', TRUE, TRUE),
('CO001', 3, 'PlayStation 5', 'La consola de nueva generación de Sony con gráficos 4K y SSD ultra rápido', 549990.00, 25, 'assets/img/ps5.png', TRUE, TRUE),
('CG001', 4, 'PC Gamer ASUS ROG Strix', 'Computador gaming de alto rendimiento con RTX 4070 y procesador Intel i7', 1299990.00, 15, 'assets/img/52-removebg-preview.png', TRUE, TRUE),
('SG001', 5, 'Silla Gamer Secretlab Titan', 'Silla ergonómica premium con soporte lumbar ajustable', 349990.00, 30, 'assets/img/minecra-removebg-preview.png', TRUE, FALSE),
('MS001', 6, 'Mouse Gamer Logitech G502 HERO', 'Mouse gaming con sensor HERO 25K y pesos ajustables', 49990.00, 120, 'assets/img/mouse.png', TRUE, TRUE),
('MP001', 7, 'Mousepad Razer Goliathus Extended Chroma', 'Mousepad XXL con iluminación RGB personalizable', 29990.00, 80, 'assets/img/mousepadconcaca.png', TRUE, FALSE),
('PP001', 8, 'Polera Gamer Personalizada ''Level-Up''', 'Polera 100% algodón con diseño exclusivo Level-Up', 14990.00, 200, 'assets/img/polera.png', TRUE, FALSE),
('PL001', 9, 'Polerón Gamer con Logo Level-Up', 'Polerón con capucha y logo bordado Level-Up', 24990.00, 150, 'assets/img/ChatGPT_Image_5_sept_2025__11_42_48_p.m.-removebg-preview.png', TRUE, FALSE);

-- Insertar Usuario Administrador
-- Password: 1234 (en producción usar bcrypt)
-- Hash bcrypt de '1234': $2a$10$rOyalXKbfk8W5Y3l5rN9ReYn7TrVKcP.L6hBLfGp8UjLqB2oWFiYC
INSERT INTO usuarios (email, password_hash, nombre, apellido, rol, edad, descuento_duoc, email_verificado, estado) VALUES
('admin@levelup.com', '$2a$10$rOyalXKbfk8W5Y3l5rN9ReYn7TrVKcP.L6hBLfGp8UjLqB2oWFiYC', 'Admin', 'Level-Up', 'admin', 30, 0.00, TRUE, 'activo');

-- Insertar Usuarios de Ejemplo
INSERT INTO usuarios (email, password_hash, nombre, apellido, telefono, edad, direccion, comuna_id, region_id, rol, descuento_duoc, email_verificado) VALUES
('juan.perez@duocuc.cl', '$2a$10$rOyalXKbfk8W5Y3l5rN9ReYn7TrVKcP.L6hBLfGp8UjLqB2oWFiYC', 'Juan', 'Pérez', '+56912345678', 22, 'Av. Libertador 123', 1, 7, 'cliente', 20.00, TRUE),
('maria.gonzalez@gmail.com', '$2a$10$rOyalXKbfk8W5Y3l5rN9ReYn7TrVKcP.L6hBLfGp8UjLqB2oWFiYC', 'María', 'González', '+56987654321', 25, 'Calle Principal 456', 2, 7, 'cliente', 0.00, TRUE),
('carlos.silva@duocuc.cl', '$2a$10$rOyalXKbfk8W5Y3l5rN9ReYn7TrVKcP.L6hBLfGp8UjLqB2oWFiYC', 'Carlos', 'Silva', '+56923456789', 28, 'Pasaje Los Aromos 789', 21, 6, 'cliente', 20.00, TRUE);

-- Insertar Carritos de Ejemplo
INSERT INTO carritos (usuario_id, estado) VALUES
(2, 'activo'),
(3, 'activo');

-- Insertar Items en Carritos
INSERT INTO carrito_items (carrito_id, producto_id, cantidad, precio_unitario) VALUES
(1, 1, 2, 29990.00),
(1, 8, 1, 49990.00),
(2, 5, 1, 549990.00);

-- Insertar Órdenes de Ejemplo
INSERT INTO ordenes (numero_orden, usuario_id, subtotal, descuento, total, estado, metodo_pago, direccion_envio, comuna_envio, region_envio, telefono_contacto, fecha_pago) VALUES
('ORD-2025-0001', 2, 109970.00, 21994.00, 87976.00, 'entregado', 'Tarjeta de Crédito', 'Av. Libertador 123', 'Santiago', 'Región Metropolitana', '+56912345678', '2025-11-15 14:30:00'),
('ORD-2025-0002', 3, 549990.00, 109998.00, 439992.00, 'enviado', 'Transferencia', 'Calle Principal 456', 'Providencia', 'Región Metropolitana', '+56987654321', '2025-11-28 10:15:00');

-- Insertar Items de Órdenes
INSERT INTO orden_items (orden_id, producto_id, producto_nombre, producto_codigo, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 'Catan', 'JM001', 2, 29990.00, 59980.00),
(1, 8, 'Mouse Gamer Logitech G502 HERO', 'MS001', 1, 49990.00, 49990.00),
(2, 5, 'PlayStation 5', 'CO001', 1, 549990.00, 549990.00);

-- Insertar Reseñas de Ejemplo
INSERT INTO resenas (producto_id, usuario_id, calificacion, titulo, comentario, verificado, estado) VALUES
(1, 2, 5, '¡Excelente juego!', 'Catan es un clásico que nunca falla. Perfecto para reuniones familiares.', TRUE, 'aprobado'),
(8, 2, 4, 'Muy buen mouse', 'El sensor es muy preciso, aunque es un poco pesado para mi gusto.', TRUE, 'aprobado'),
(5, 3, 5, 'La mejor consola', 'Gráficos increíbles y carga rapidísima. Vale cada peso.', TRUE, 'aprobado');

-- Insertar Posts de Blog
INSERT INTO blog_posts (titulo, slug, contenido, resumen, autor_id, categoria, publicado, fecha_publicacion) VALUES
('Las mejores estrategias para Catan', 'mejores-estrategias-catan', 'Aquí te compartimos las mejores estrategias para dominar en Catan...', 'Descubre cómo ganar en Catan con estas estrategias probadas', 1, 'Juegos de Mesa', TRUE, '2025-11-01 10:00:00'),
('Guía de compra: Auriculares Gaming 2025', 'guia-compra-auriculares-gaming-2025', 'Todo lo que necesitas saber antes de comprar tus auriculares gaming...', 'Encuentra los mejores auriculares gaming según tu presupuesto', 1, 'Accesorios', TRUE, '2025-11-15 12:00:00');

-- Insertar Mensajes de Contacto
INSERT INTO contacto (nombre, email, telefono, asunto, mensaje, estado) VALUES
('Pedro Ramírez', 'pedro@example.com', '+56956781234', 'Consulta sobre envíos', '¿Realizan envíos a regiones?', 'nuevo'),
('Ana Torres', 'ana@example.com', '+56945678901', 'Problema con mi orden', 'Mi orden no ha llegado aún', 'leido');

-- ============================================
-- VISTAS ÚTILES
-- ============================================

-- Vista: Productos con información de categoría
CREATE VIEW vista_productos AS
SELECT 
    p.id,
    p.codigo,
    p.nombre,
    p.descripcion,
    p.precio,
    p.stock,
    p.imagen_url,
    p.activo,
    p.destacado,
    p.ventas_total,
    c.nombre AS categoria_nombre,
    c.id AS categoria_id,
    CASE 
        WHEN p.stock <= p.stock_minimo THEN 'Bajo'
        WHEN p.stock <= p.stock_minimo * 2 THEN 'Medio'
        ELSE 'Alto'
    END AS nivel_stock,
    COALESCE(AVG(r.calificacion), 0) AS calificacion_promedio,
    COUNT(r.id) AS total_resenas
FROM productos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN resenas r ON p.id = r.producto_id AND r.estado = 'aprobado'
GROUP BY p.id, c.id, c.nombre;

-- Vista: Órdenes con información del usuario
CREATE VIEW vista_ordenes AS
SELECT 
    o.id,
    o.numero_orden,
    o.total,
    o.estado,
    o.created_at AS fecha_orden,
    u.nombre AS usuario_nombre,
    u.apellido AS usuario_apellido,
    u.email AS usuario_email,
    COUNT(oi.id) AS total_items
FROM ordenes o
JOIN usuarios u ON o.usuario_id = u.id
LEFT JOIN orden_items oi ON o.id = oi.orden_id
GROUP BY o.id, u.id;

-- Vista: Estadísticas de productos
CREATE VIEW vista_estadisticas_productos AS
SELECT 
    c.nombre AS categoria,
    COUNT(p.id) AS total_productos,
    SUM(p.stock) AS stock_total,
    AVG(p.precio) AS precio_promedio,
    SUM(p.ventas_total) AS ventas_totales
FROM categorias c
LEFT JOIN productos p ON c.id = p.categoria_id AND p.activo = TRUE
GROUP BY c.id, c.nombre;

-- ============================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- ============================================

-- Índice compuesto para búsqueda de productos
CREATE INDEX idx_producto_busqueda ON productos(nombre, activo, categoria_id);

-- Índice para órdenes por fecha y estado
CREATE INDEX idx_orden_fecha_estado ON ordenes(created_at DESC, estado);

-- Índice para reseñas aprobadas
CREATE INDEX idx_resenas_aprobadas ON resenas(producto_id, estado, calificacion);

-- ============================================
-- PROCEDIMIENTOS ALMACENADOS
-- ============================================

DELIMITER //

-- Procedimiento: Crear orden desde carrito
CREATE PROCEDURE crear_orden_desde_carrito(
    IN p_usuario_id INT,
    IN p_direccion_envio TEXT,
    IN p_comuna_envio VARCHAR(100),
    IN p_region_envio VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_metodo_pago VARCHAR(50)
)
BEGIN
    DECLARE v_carrito_id INT;
    DECLARE v_subtotal DECIMAL(10,2);
    DECLARE v_descuento DECIMAL(10,2);
    DECLARE v_total DECIMAL(10,2);
    DECLARE v_numero_orden VARCHAR(50);
    DECLARE v_orden_id INT;
    DECLARE v_descuento_porc DECIMAL(5,2);
    
    -- Obtener carrito activo del usuario
    SELECT id INTO v_carrito_id 
    FROM carritos 
    WHERE usuario_id = p_usuario_id AND estado = 'activo' 
    LIMIT 1;
    
    IF v_carrito_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se encontró carrito activo';
    END IF;
    
    -- Calcular subtotal
    SELECT SUM(precio_unitario * cantidad) INTO v_subtotal
    FROM carrito_items
    WHERE carrito_id = v_carrito_id;
    
    -- Obtener descuento del usuario
    SELECT descuento_duoc INTO v_descuento_porc
    FROM usuarios
    WHERE id = p_usuario_id;
    
    -- Calcular descuento y total
    SET v_descuento = v_subtotal * (v_descuento_porc / 100);
    SET v_total = v_subtotal - v_descuento;
    
    -- Generar número de orden
    SET v_numero_orden = CONCAT('ORD-', YEAR(NOW()), '-', LPAD((SELECT COUNT(*) + 1 FROM ordenes), 4, '0'));
    
    -- Crear orden
    INSERT INTO ordenes (numero_orden, usuario_id, subtotal, descuento, total, direccion_envio, comuna_envio, region_envio, telefono_contacto, metodo_pago)
    VALUES (v_numero_orden, p_usuario_id, v_subtotal, v_descuento, v_total, p_direccion_envio, p_comuna_envio, p_region_envio, p_telefono, p_metodo_pago);
    
    SET v_orden_id = LAST_INSERT_ID();
    
    -- Copiar items del carrito a orden_items
    INSERT INTO orden_items (orden_id, producto_id, producto_nombre, producto_codigo, cantidad, precio_unitario, subtotal)
    SELECT 
        v_orden_id,
        ci.producto_id,
        p.nombre,
        p.codigo,
        ci.cantidad,
        ci.precio_unitario,
        ci.cantidad * ci.precio_unitario
    FROM carrito_items ci
    JOIN productos p ON ci.producto_id = p.id
    WHERE ci.carrito_id = v_carrito_id;
    
    -- Actualizar stock de productos
    UPDATE productos p
    JOIN carrito_items ci ON p.id = ci.producto_id
    SET p.stock = p.stock - ci.cantidad,
        p.ventas_total = p.ventas_total + ci.cantidad
    WHERE ci.carrito_id = v_carrito_id;
    
    -- Marcar carrito como convertido
    UPDATE carritos SET estado = 'convertido' WHERE id = v_carrito_id;
    
    -- Crear nuevo carrito activo para el usuario
    INSERT INTO carritos (usuario_id, estado) VALUES (p_usuario_id, 'activo');
    
    SELECT v_numero_orden AS numero_orden, v_orden_id AS orden_id;
END //

-- Procedimiento: Actualizar calificación promedio de producto
CREATE PROCEDURE actualizar_calificacion_producto(IN p_producto_id INT)
BEGIN
    UPDATE productos
    SET especificaciones = JSON_SET(
        COALESCE(especificaciones, '{}'),
        '$.calificacion_promedio',
        (SELECT COALESCE(AVG(calificacion), 0) 
         FROM resenas 
         WHERE producto_id = p_producto_id AND estado = 'aprobado')
    )
    WHERE id = p_producto_id;
END //

DELIMITER ;

-- ============================================
-- TRIGGERS
-- ============================================

DELIMITER //

-- Trigger: Calcular edad al insertar/actualizar usuario
CREATE TRIGGER trg_calcular_edad_insert
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.fecha_nacimiento IS NOT NULL THEN
        SET NEW.edad = TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE());
    END IF;
END //

CREATE TRIGGER trg_calcular_edad_update
BEFORE UPDATE ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.fecha_nacimiento IS NOT NULL THEN
        SET NEW.edad = TIMESTAMPDIFF(YEAR, NEW.fecha_nacimiento, CURDATE());
    END IF;
END //

-- Trigger: Validar stock antes de añadir al carrito
CREATE TRIGGER trg_validar_stock_carrito
BEFORE INSERT ON carrito_items
FOR EACH ROW
BEGIN
    DECLARE v_stock_disponible INT;
    
    SELECT stock INTO v_stock_disponible
    FROM productos
    WHERE id = NEW.producto_id;
    
    IF v_stock_disponible < NEW.cantidad THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Stock insuficiente para este producto';
    END IF;
END //

-- Trigger: Actualizar timestamp de última conexión
CREATE TRIGGER trg_actualizar_ultima_conexion
BEFORE UPDATE ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.ultima_conexion IS NULL OR OLD.ultima_conexion != NEW.ultima_conexion THEN
        SET NEW.ultima_conexion = CURRENT_TIMESTAMP;
    END IF;
END //

DELIMITER ;

-- ============================================
-- DATOS DE PRUEBA ADICIONALES
-- ============================================

-- Más productos en diferentes categorías para testing
INSERT INTO productos (codigo, categoria_id, nombre, descripcion, precio, stock, activo, destacado) VALUES
('JM003', 1, 'Pandemic', 'Juego cooperativo donde debes salvar al mundo de enfermedades mortales', 34990.00, 45, TRUE, FALSE),
('AC003', 2, 'Teclado Mecánico Corsair K70', 'Teclado mecánico RGB con switches Cherry MX', 89990.00, 60, TRUE, TRUE),
('MS002', 6, 'Razer DeathAdder V2', 'Mouse ergonómico con sensor óptico de 20K DPI', 44990.00, 90, TRUE, FALSE);

-- ============================================
-- CONFIGURACIÓN FINAL
-- ============================================

-- Configurar zona horaria
SET time_zone = '-03:00'; -- Chile Continental

-- Mostrar resumen de la base de datos
SELECT 'Base de datos creada exitosamente' AS Mensaje;
SELECT TABLE_NAME, TABLE_ROWS 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'levelup_gamer' 
ORDER BY TABLE_NAME;
