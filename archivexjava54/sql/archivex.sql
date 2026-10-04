-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 30-09-2026 a las 22:39:24
-- Versión del servidor: 8.0.17
-- Versión de PHP: 7.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `archivex`
--
CREATE DATABASE IF NOT EXISTS `archivex` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `archivex`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `almacenes`
--

CREATE TABLE `almacenes` (
  `id_almacen` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `ubicacion` varchar(250) COLLATE utf8mb4_general_ci NOT NULL,
  `ciudad` varchar(120) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `capacidad_maxima` int(10) UNSIGNED DEFAULT NULL,
  `estado` enum('activo','inactivo') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'activo',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `almacenes`
--

INSERT INTO `almacenes` (`id_almacen`, `nombre`, `ubicacion`, `ciudad`, `capacidad_maxima`, `estado`, `fecha_creacion`) VALUES
(1, 'Local 1', 'AV 11 Cl.5 Parque Santander', 'Cucuta', 1000, 'activo', '2026-09-16 15:37:50'),
(3, 'Almacen 1', 'Av 3 Cl 5 barrio aeropuerto', 'Cucuta', 700, 'activo', '2026-09-16 15:39:57');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id_categoria` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `descripcion` varchar(300) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado` enum('activo','inactivo') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'activo',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id_categoria`, `nombre`, `descripcion`, `estado`, `fecha_creacion`) VALUES
(1, 'Materias Primas', 'Insumos y materiales base utilizados para la fabricación de productos', 'activo', '2026-09-16 15:19:10'),
(2, 'Tecnologías', 'Equipos, componentes y productos tecnológicos', 'activo', '2026-09-16 15:19:10');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compras`
--

CREATE TABLE `compras` (
  `id_compra` int(10) UNSIGNED NOT NULL,
  `numero_pedido` int(10) UNSIGNED NOT NULL,
  `id_proveedor` int(10) UNSIGNED NOT NULL,
  `id_almacen` int(10) UNSIGNED NOT NULL,
  `fecha_pedido` datetime NOT NULL,
  `fecha_entrega` datetime DEFAULT NULL,
  `valor_total` decimal(12,2) NOT NULL,
  `estado` enum('pendiente','entregado','cancelado') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'pendiente',
  `comentarios` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `compras`
--

INSERT INTO `compras` (`id_compra`, `numero_pedido`, `id_proveedor`, `id_almacen`, `fecha_pedido`, `fecha_entrega`, `valor_total`, `estado`, `comentarios`) VALUES
(1, 1, 2, 1, '2026-09-26 20:30:46', '2026-09-28 05:00:00', '1200000.00', 'pendiente', 'Producto delicado'),
(2, 2, 3, 3, '2026-09-26 20:31:56', NULL, '3000000.00', 'cancelado', 'Producto delicado'),
(3, 3, 1, 1, '2026-09-26 20:33:10', '2026-09-26 15:33:24', '5000000.00', 'entregado', 'Producto delicado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion`
--

CREATE TABLE `configuracion` (
  `id_configuracion` tinyint(3) UNSIGNED NOT NULL DEFAULT '1',
  `nombre_sistema` varchar(100) NOT NULL DEFAULT 'ArchiveX',
  `nombre_empresa` varchar(150) NOT NULL,
  `correo_contacto` varchar(150) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `ciudad` varchar(120) DEFAULT NULL,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallecompra`
--

CREATE TABLE `detallecompra` (
  `id_detalle` int(10) UNSIGNED NOT NULL,
  `id_compra` int(10) UNSIGNED NOT NULL,
  `id_producto` int(10) UNSIGNED NOT NULL,
  `cantidad` int(10) UNSIGNED NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(12,2) GENERATED ALWAYS AS ((`cantidad` * `precio_unitario`)) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detallecompra`
--

INSERT INTO `detallecompra` (`id_detalle`, `id_compra`, `id_producto`, `cantidad`, `precio_unitario`) VALUES
(1, 1, 2, 2, '600000.00'),
(2, 2, 1, 3, '1000000.00'),
(3, 3, 1, 5, '1000000.00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historialinventario`
--

CREATE TABLE `historialinventario` (
  `id_historial` int(10) UNSIGNED NOT NULL,
  `id_producto` int(10) UNSIGNED NOT NULL,
  `id_almacen` int(10) UNSIGNED NOT NULL,
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `cantidad_anterior` int(10) UNSIGNED NOT NULL,
  `cantidad_nueva` int(10) UNSIGNED NOT NULL,
  `diferencia` int(11) NOT NULL,
  `fecha_cambio` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `historialinventario`
--

INSERT INTO `historialinventario` (`id_historial`, `id_producto`, `id_almacen`, `id_usuario`, `cantidad_anterior`, `cantidad_nueva`, `diferencia`, `fecha_cambio`) VALUES
(1, 2, 3, 1, 10, 8, -2, '2026-09-26 15:27:32'),
(2, 1, 1, 1, 0, 5, 5, '2026-09-26 15:33:24');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historialusuarios`
--

CREATE TABLE `historialusuarios` (
  `id_historial_usuario` int(10) UNSIGNED NOT NULL,
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `nombre_anterior` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nombre_nuevo` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apellido_anterior` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apellido_nuevo` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `correo_anterior` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `correo_nuevo` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telefono_anterior` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telefono_nuevo` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado_anterior` enum('activo','inactivo') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado_nuevo` enum('activo','inactivo') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `accion` enum('registro','actualizacion','cambio_correo','cambio_telefono','cambio_contrasena','activacion','desactivacion') COLLATE utf8mb4_general_ci NOT NULL,
  `fecha_accion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `historialusuarios`
--

INSERT INTO `historialusuarios` (`id_historial_usuario`, `id_usuario`, `nombre_anterior`, `nombre_nuevo`, `apellido_anterior`, `apellido_nuevo`, `correo_anterior`, `correo_nuevo`, `telefono_anterior`, `telefono_nuevo`, `estado_anterior`, `estado_nuevo`, `accion`, `fecha_accion`) VALUES
(1, 1, NULL, 'Miguel', NULL, 'Correa', NULL, 'mdch2077@gmail.com', NULL, '3115861318', NULL, 'activo', 'registro', '2026-09-16 12:05:59'),
(2, 3, NULL, 'David', NULL, 'Torres', NULL, 'torres123@gmail.com', NULL, '3305462989', NULL, 'activo', 'registro', '2026-09-19 20:42:27');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventarioporalmacen`
--

CREATE TABLE `inventarioporalmacen` (
  `id_inventario` int(10) UNSIGNED NOT NULL,
  `id_producto` int(10) UNSIGNED NOT NULL,
  `id_almacen` int(10) UNSIGNED NOT NULL,
  `cantidad_disponible` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `cantidad_reservada` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `fecha_de_actualizacion` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `inventarioporalmacen`
--

INSERT INTO `inventarioporalmacen` (`id_inventario`, `id_producto`, `id_almacen`, `cantidad_disponible`, `cantidad_reservada`, `fecha_de_actualizacion`) VALUES
(1, 1, 3, 5, 3, NULL),
(2, 2, 3, 8, 7, '2026-09-26 15:27:32'),
(3, 3, 1, 60, 0, NULL),
(4, 1, 1, 5, 0, '2026-09-26 15:33:24');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientoinventario`
--

CREATE TABLE `movimientoinventario` (
  `id_movimiento` int(10) UNSIGNED NOT NULL,
  `id_producto` int(10) UNSIGNED NOT NULL,
  `id_almacen` int(10) UNSIGNED NOT NULL,
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `tipo_movimiento` enum('entrada','salida','ajuste','devolucion') COLLATE utf8mb4_general_ci NOT NULL,
  `cantidad` int(11) NOT NULL,
  `id_compra` int(10) UNSIGNED DEFAULT NULL,
  `motivo` varchar(230) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_movimiento` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `movimientoinventario`
--

INSERT INTO `movimientoinventario` (`id_movimiento`, `id_producto`, `id_almacen`, `id_usuario`, `tipo_movimiento`, `cantidad`, `id_compra`, `motivo`, `fecha_movimiento`) VALUES
(1, 2, 3, 1, 'salida', 2, NULL, 'Pocas existencias', '2026-09-26 15:27:32'),
(2, 1, 1, 1, 'entrada', 5, 3, 'Entrada por compra, pedido N° 3', '2026-09-26 15:33:24');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productoproveedor`
--

CREATE TABLE `productoproveedor` (
  `id_producto_proveedor` int(10) UNSIGNED NOT NULL,
  `id_producto` int(10) UNSIGNED NOT NULL,
  `id_proveedor` int(10) UNSIGNED NOT NULL,
  `precio_compra` decimal(10,2) DEFAULT NULL,
  `codigo_proveedor` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado` enum('activo','inactivo') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'activo',
  `fecha_registro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productoproveedor`
--

INSERT INTO `productoproveedor` (`id_producto_proveedor`, `id_producto`, `id_proveedor`, `precio_compra`, `codigo_proveedor`, `estado`, `fecha_registro`) VALUES
(1, 1, 1, '1000000.00', NULL, 'activo', '2026-09-26 15:33:24');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `descripcion` varchar(300) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_categoria` int(10) UNSIGNED NOT NULL,
  `precio_compra` decimal(10,2) NOT NULL,
  `precio_venta` decimal(10,2) NOT NULL,
  `cantidad` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `estado` enum('activo','descontinuado') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'activo',
  `imagen` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre`, `descripcion`, `id_categoria`, `precio_compra`, `precio_venta`, `cantidad`, `estado`, `imagen`, `fecha_creacion`) VALUES
(1, 'Iphone 18', 'Telefono inteligente', 2, '1000000.00', '2000000.00', 10, 'activo', 'telefonoCelular.png', '2026-09-16 15:31:51'),
(2, 'Televisor Smart 50” Crystal UHD 4K U8000H', 'Televisor', 2, '600000.00', '1400000.00', 8, 'activo', NULL, '2026-09-16 15:50:55'),
(3, 'PONY MALTA R 330 ML', 'Bebida Gaseosa', 1, '1500.00', '3500.00', 60, 'activo', 'comida_y_bebida.png', '2026-09-16 15:54:28');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedores`
--

CREATE TABLE `proveedores` (
  `id_proveedor` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `correo` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `direccion` varchar(230) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ciudad` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pais` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado` enum('activo','inactivo','suspendido') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'activo',
  `fecha_registro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `proveedores`
--

INSERT INTO `proveedores` (`id_proveedor`, `nombre`, `correo`, `telefono`, `direccion`, `ciudad`, `pais`, `estado`, `fecha_registro`) VALUES
(1, 'World Phone', 'wpcenter@gmail.com', '3206651484', 'Av 7 Cl.8 CC Alejandria', 'Cucuta', 'Colombia', 'activo', '2026-09-16 15:44:46'),
(2, 'Palacio Conde', 'palacioconde1@gmail.com', '3133458876', 'Av 6 CL.8 Barrio Centro', 'Cucuta', 'Colombia', 'activo', '2026-09-16 15:47:07'),
(3, 'Bavaria SA', 'protecciondedatos@co.ab-inbev.com', '+57 (1) 6389000', 'Av. 3 #7-2 a 7-78 Barrio Motilones', 'Cucuta', 'Colombia', 'activo', '2026-09-16 15:58:33');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reportes`
--

CREATE TABLE `reportes` (
  `id_reporte` int(10) UNSIGNED NOT NULL,
  `titulo` varchar(150) NOT NULL,
  `tipo_reporte` enum('inventario','compras','movimientos','proveedores','productos','general') NOT NULL,
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `id_almacen` int(10) UNSIGNED DEFAULT NULL,
  `id_categoria` int(10) UNSIGNED DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `formato` enum('pdf','excel','csv') NOT NULL DEFAULT 'pdf',
  `ruta_archivo` varchar(255) DEFAULT NULL,
  `estado` enum('generado','procesando','error') NOT NULL DEFAULT 'procesando',
  `fecha_generacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `reportes`
--

INSERT INTO `reportes` (`id_reporte`, `titulo`, `tipo_reporte`, `id_usuario`, `id_almacen`, `id_categoria`, `fecha_inicio`, `fecha_fin`, `formato`, `ruta_archivo`, `estado`, `fecha_generacion`) VALUES
(1, 'Inventario por almacén', 'inventario', 1, NULL, NULL, NULL, NULL, 'excel', 'C:\\Users\\migue\\OneDrive\\Escritorio\\Inventario.xls', 'generado', '2026-09-27 15:31:56'),
(2, 'Historial de usuarios', 'general', 1, NULL, NULL, NULL, NULL, 'excel', 'C:\\Users\\migue\\OneDrive\\Escritorio\\Usuarios.xls', 'generado', '2026-09-27 15:32:32');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `apellido` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `correo_electronico` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contrasena` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado` enum('activo','inactivo') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'activo',
  `rol` enum('usuario','administrador') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'usuario',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre`, `apellido`, `correo_electronico`, `contrasena`, `telefono`, `estado`, `rol`, `fecha_creacion`) VALUES
(1, 'Miguel', 'Correa', 'mdch2077@gmail.com', '9b99dd0263701b58eda966615dd04436c83544620800587125175eaf73562a1d', '3115861318', 'activo', 'usuario', '2026-09-16 12:05:59'),
(3, 'David', 'Torres', 'torres123@gmail.com', '5b300191bdce1d281e08935f6ffa96b694c7f9e4bc1cae0f4f72a0907e33234c', '3305462989', 'activo', 'usuario', '2026-09-19 20:42:27');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `almacenes`
--
ALTER TABLE `almacenes`
  ADD PRIMARY KEY (`id_almacen`),
  ADD UNIQUE KEY `uk_almacenes_nombre` (`nombre`),
  ADD KEY `idx_almacenes_estado` (`estado`),
  ADD KEY `idx_almacenes_ciudad` (`ciudad`);

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id_categoria`),
  ADD UNIQUE KEY `uk_categorias_nombre` (`nombre`),
  ADD KEY `idx_categorias_estado` (`estado`);

--
-- Indices de la tabla `compras`
--
ALTER TABLE `compras`
  ADD PRIMARY KEY (`id_compra`),
  ADD UNIQUE KEY `uk_compras_numero_pedido` (`numero_pedido`),
  ADD KEY `idx_compras_proveedor` (`id_proveedor`),
  ADD KEY `idx_compras_almacen` (`id_almacen`),
  ADD KEY `idx_compras_estado` (`estado`),
  ADD KEY `idx_compras_fecha_pedido` (`fecha_pedido`);

--
-- Indices de la tabla `configuracion`
--
ALTER TABLE `configuracion`
  ADD PRIMARY KEY (`id_configuracion`);

--
-- Indices de la tabla `detallecompra`
--
ALTER TABLE `detallecompra`
  ADD PRIMARY KEY (`id_detalle`),
  ADD KEY `idx_detalle_compra` (`id_compra`),
  ADD KEY `idx_detalle_producto` (`id_producto`);

--
-- Indices de la tabla `historialinventario`
--
ALTER TABLE `historialinventario`
  ADD PRIMARY KEY (`id_historial`),
  ADD KEY `idx_historial_producto` (`id_producto`),
  ADD KEY `idx_historial_almacen` (`id_almacen`),
  ADD KEY `idx_historial_usuario` (`id_usuario`),
  ADD KEY `idx_historial_fecha` (`fecha_cambio`);

--
-- Indices de la tabla `historialusuarios`
--
ALTER TABLE `historialusuarios`
  ADD PRIMARY KEY (`id_historial_usuario`),
  ADD KEY `idx_historialusuarios_usuario` (`id_usuario`),
  ADD KEY `idx_historialusuarios_fecha` (`fecha_accion`),
  ADD KEY `idx_historialusuarios_accion` (`accion`);

--
-- Indices de la tabla `inventarioporalmacen`
--
ALTER TABLE `inventarioporalmacen`
  ADD PRIMARY KEY (`id_inventario`),
  ADD UNIQUE KEY `uk_producto_almacen` (`id_producto`,`id_almacen`),
  ADD KEY `idx_inventario_almacen` (`id_almacen`);

--
-- Indices de la tabla `movimientoinventario`
--
ALTER TABLE `movimientoinventario`
  ADD PRIMARY KEY (`id_movimiento`),
  ADD KEY `idx_movimiento_compra` (`id_compra`),
  ADD KEY `idx_movimiento_producto` (`id_producto`),
  ADD KEY `idx_movimiento_almacen` (`id_almacen`),
  ADD KEY `idx_movimiento_tipo` (`tipo_movimiento`),
  ADD KEY `idx_movimiento_fecha` (`fecha_movimiento`),
  ADD KEY `idx_movimiento_usuario` (`id_usuario`);

--
-- Indices de la tabla `productoproveedor`
--
ALTER TABLE `productoproveedor`
  ADD PRIMARY KEY (`id_producto_proveedor`),
  ADD UNIQUE KEY `uk_producto_proveedor` (`id_producto`,`id_proveedor`),
  ADD KEY `idx_producto_proveedor_producto` (`id_producto`),
  ADD KEY `idx_producto_proveedor_proveedor` (`id_proveedor`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`),
  ADD KEY `idx_productos_categoria` (`id_categoria`),
  ADD KEY `idx_productos_estado` (`estado`);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD PRIMARY KEY (`id_proveedor`),
  ADD KEY `idx_proveedores_estado` (`estado`),
  ADD KEY `idx_proveedores_pais` (`pais`);

--
-- Indices de la tabla `reportes`
--
ALTER TABLE `reportes`
  ADD PRIMARY KEY (`id_reporte`),
  ADD KEY `idx_reportes_usuario` (`id_usuario`),
  ADD KEY `idx_reportes_almacen` (`id_almacen`),
  ADD KEY `idx_reportes_categoria` (`id_categoria`),
  ADD KEY `idx_reportes_tipo` (`tipo_reporte`),
  ADD KEY `idx_reportes_fecha` (`fecha_generacion`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `uk_usuarios_correo` (`correo_electronico`),
  ADD KEY `idx_usuarios_estado` (`estado`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `almacenes`
--
ALTER TABLE `almacenes`
  MODIFY `id_almacen` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id_categoria` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `compras`
--
ALTER TABLE `compras`
  MODIFY `id_compra` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `detallecompra`
--
ALTER TABLE `detallecompra`
  MODIFY `id_detalle` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `historialinventario`
--
ALTER TABLE `historialinventario`
  MODIFY `id_historial` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `historialusuarios`
--
ALTER TABLE `historialusuarios`
  MODIFY `id_historial_usuario` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `inventarioporalmacen`
--
ALTER TABLE `inventarioporalmacen`
  MODIFY `id_inventario` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `movimientoinventario`
--
ALTER TABLE `movimientoinventario`
  MODIFY `id_movimiento` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `productoproveedor`
--
ALTER TABLE `productoproveedor`
  MODIFY `id_producto_proveedor` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  MODIFY `id_proveedor` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `reportes`
--
ALTER TABLE `reportes`
  MODIFY `id_reporte` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `compras`
--
ALTER TABLE `compras`
  ADD CONSTRAINT `fk_compras_almacen` FOREIGN KEY (`id_almacen`) REFERENCES `almacenes` (`id_almacen`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_compras_proveedor` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedores` (`id_proveedor`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `detallecompra`
--
ALTER TABLE `detallecompra`
  ADD CONSTRAINT `fk_detallecompra_compra` FOREIGN KEY (`id_compra`) REFERENCES `compras` (`id_compra`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_detallecompra_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `historialinventario`
--
ALTER TABLE `historialinventario`
  ADD CONSTRAINT `fk_historialinv_almacen` FOREIGN KEY (`id_almacen`) REFERENCES `almacenes` (`id_almacen`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_historialinv_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_historialinv_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `historialusuarios`
--
ALTER TABLE `historialusuarios`
  ADD CONSTRAINT `fk_historialusr_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `inventarioporalmacen`
--
ALTER TABLE `inventarioporalmacen`
  ADD CONSTRAINT `fk_inventario_almacen` FOREIGN KEY (`id_almacen`) REFERENCES `almacenes` (`id_almacen`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventario_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `movimientoinventario`
--
ALTER TABLE `movimientoinventario`
  ADD CONSTRAINT `fk_movimiento_almacen` FOREIGN KEY (`id_almacen`) REFERENCES `almacenes` (`id_almacen`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_movimiento_compra` FOREIGN KEY (`id_compra`) REFERENCES `compras` (`id_compra`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_movimiento_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_movimiento_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `productoproveedor`
--
ALTER TABLE `productoproveedor`
  ADD CONSTRAINT `fk_productoproveedor_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_productoproveedor_proveedor` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedores` (`id_proveedor`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `fk_productos_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `reportes`
--
ALTER TABLE `reportes`
  ADD CONSTRAINT `fk_reportes_almacen` FOREIGN KEY (`id_almacen`) REFERENCES `almacenes` (`id_almacen`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_reportes_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_reportes_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
