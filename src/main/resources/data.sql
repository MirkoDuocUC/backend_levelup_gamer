-- Regiones
INSERT INTO regiones (id, nombre, codigo, created_at) VALUES 
(1, 'Región Metropolitana', 'RM', NOW()),
(2, 'Valparaíso', 'V', NOW());

-- Comunas
INSERT INTO comunas (id, nombre, region_id, created_at) VALUES 
(1, 'Santiago', 1, NOW()),
(2, 'Providencia', 1, NOW()),
(3, 'Viña del Mar', 2, NOW());

-- Usuarios
-- Se crearán mediante el endpoint POST /auth/registro
-- No se insertan aquí para evitar problemas con hashes BCrypt


-- Categorias
INSERT INTO categorias (id, nombre, descripcion, activo, created_at) VALUES 
(1, 'Consolas', 'Consolas de videojuegos', true, NOW()),
(2, 'Juegos', 'Videojuegos físicos y digitales', true, NOW());

-- Productos
INSERT INTO productos (id, codigo, nombre, descripcion, precio, stock, categoria_id, activo, created_at) VALUES 
(1, 'PS5-001', 'PlayStation 5', 'Consola de última generación', 549990, 50, 1, true, NOW()),
(2, 'NSW-001', 'Nintendo Switch OLED', 'Consola híbrida', 349990, 100, 1, true, NOW());
