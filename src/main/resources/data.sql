-- ========================================
-- ADMINISTRADOR
-- ========================================
INSERT INTO administrador (id_administrador, correo, contrasena, fecha_registro)
VALUES (1, 'admin@marbellin.com', 'admin123', CURRENT_DATE);

-- ========================================
-- CATEGORÍAS
-- ========================================
INSERT INTO categoria_producto (id_categoria, nombre_categoria)
VALUES (1, 'Bikini'),
       (2, 'Cachetero'),
       (3, 'Semi Hilo'),
       (4, 'Topsito');

-- ========================================
-- PRODUCTOS (grupo por categoría)
-- ========================================
INSERT INTO producto (id_producto, id_categoria, id_administrador, fecha_registro)
VALUES (1, 1, 1, CURRENT_DATE), -- Bikinis
       (2, 2, 1, CURRENT_DATE), -- Cacheteros
       (3, 3, 1, CURRENT_DATE), -- Semi Hilos
       (4, 4, 1, CURRENT_DATE);
-- Topsitos

-- ========================================
-- MODELOS (21 registros del JSON)
-- ========================================
INSERT INTO modelo_producto (id_modelo, id_producto, nombre_modelo, precio, descripcion)
VALUES (1020, 1, 'Bikini Animal Print', 61.00,
        'Bikini con estampado animal print, ideal para destacar en la playa con estilo y confianza.'),
       (1024, 1, 'Bikini Blonda', 58.00,
        'Bikini con detalles de blonda delicada, combinación perfecta de sensualidad y elegancia.'),
       (1034, 1, 'Bikina Blondita Completa', 65.00,
        'Conjunto completo de bikini con diseño suave y femenino, para un look delicado.'),
       (1022, 1, 'Bikini Clasico', 56.00,
        'Diseño clásico y atemporal, ideal para quienes prefieren un estilo tradicional.'),
       (1019, 1, 'Bikini Estrella', 63.00, 'Bikini con detalles de estrella que aportan un toque juvenil y divertido.'),
       (1033, 1, 'Bikini Pretina Ancha', 62.00, 'Cómodo bikini con pretina ancha que brinda mejor ajuste y soporte.'),

       (1028, 2, 'Cachetero Blonda con Logo', 70.00, 'Cachetero con elegantes acabados en blonda y logo decorativo.'),
       (1029, 2, 'Cachetero Blonda', 64.00, 'Cachetero clásico de blonda, suave y cómodo para el uso diario.'),
       (1072, 2, 'Cachetero Corazon Estampado', 70.00,
        'Cachetero con estampado de corazones, ideal para un look tierno y coqueto.'),
       (1027, 2, 'Cachetero Corazon', 65.00,
        'Cachetero con detalles románticos en forma de corazón, perfecto para ocasiones especiales.'),
       (1052, 2, 'Cachetero Dije Estampado', 64.00, 'Cachetero con pequeño dije decorativo y estampados modernos.'),
       (1025, 2, 'Cachetero Dije', 57.00, 'Cachetero elegante con dije en el frente, para un detalle extra de estilo.'),
       (1050, 2, 'Cachetero Encaje Atras', 70.00,
        'Cachetero con diseño de encaje en la parte posterior, sexy y sofisticado.'),
       (1049, 2, 'Cachetero Señorial Floreado', 76.00,
        'Diseño floreado con estilo señorial, mezcla de tradición y elegancia.'),

       (1026, 3, 'Semi Hilo Clasico', 54.00,
        'Diseño clásico de semi hilo, para quienes buscan comodidad y sensualidad.'),
       (1031, 3, 'Semi Hilo Dije', 55.00, 'Semi hilo decorado con dije, moderno y atractivo para cualquier ocasión.'),
       (1032, 3, 'Semi Hilo Pretina Ancha', 59.00,
        'Semi hilo con pretina ancha que proporciona ajuste ideal y confort.'),
       (1048, 3, 'Semi Hilo Señorial Juvenil', 73.00, 'Estilo que combina elegancia señorial con frescura juvenil.'),

       (1001, 4, 'Topsito Clasico', 67.00, 'Topsito de corte clásico, esencial para cualquier guardarropa íntimo.'),
       (1002, 4, 'Topsito con Tirante', 67.00, 'Topsito cómodo con tirantes ajustables, ideal para uso diario.'),
       (1003, 4, 'Topsito Olimpico', 67.00, 'Bikini estilo deportivo tipo topsito olímpico, brinda soporte y estilo.');

-- ========================================
-- IMÁGENES con rutas reales
-- ========================================
INSERT INTO imagen_modelo (id_imagen, id_modelo, url, orden)
VALUES (1, 1020, '/images/productos/bikinis/bikini-animal-print1.jpg', 1),
       (2, 1024, '/images/productos/bikinis/bikini-blonda.jpg', 1),
       (3, 1034, '/images/productos/bikinis/bikini-blondita-completa.jpg', 1),
       (4, 1022, '/images/productos/bikinis/bikini-clasico.jpg', 1),
       (5, 1019, '/images/productos/bikinis/bikini-estrella.jpg', 1),
       (6, 1033, '/images/productos/bikinis/bikini-pretina-ancha.jpg', 1),

       (7, 1028, '/images/productos/cacheteros/cachetero-blonda-con-logo.jpg', 1),
       (8, 1029, '/images/productos/cacheteros/cachetero-blonda.jpg', 1),
       (9, 1072, '/images/productos/cacheteros/cachetero-corazon-estampado-1.jpg', 1),
       (10, 1027, '/images/productos/cacheteros/cachetero-corazon-1.jpg', 1),
       (11, 1052, '/images/productos/cacheteros/cachetero-dije-estampado-1.jpeg', 1),
       (12, 1025, '/images/productos/cacheteros/cachetero-dije-1.jpeg', 1),
       (13, 1050, '/images/productos/cacheteros/cachetero-encaje-atras-1.jpeg', 1),
       (14, 1049, '/images/productos/cacheteros/cachetero-señorial-floreado.jpg', 1),

       (15, 1026, '/images/productos/semi/semi-hilo-clasico.jpg', 1),
       (16, 1031, '/images/productos/semi/semi-hilo-dije.jpg', 1),
       (17, 1032, '/images/productos/semi/semi-hilo-pretina-ancha.jpg', 1),
       (18, 1048, '/images/productos/semi/semi-señorial-juvenil-1.jpg', 1),

       (19, 1001, '/images/productos/topsitos/topsito-clasico.jpg', 1),
       (20, 1002, '/images/productos/topsitos/topsito-con-tirante.jpg', 1),
       (21, 1003, '/images/productos/topsitos/topsito-olimpico.jpg', 1);
