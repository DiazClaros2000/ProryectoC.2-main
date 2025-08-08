<?php
require_once __DIR__ . '/../../../config/db.php';
require_once __DIR__ . '/../../../helpers/jsonResponse.php';

$sql = "
SELECT c.id_cotizacion, u.nombre AS cliente, v.marca, v.modelo, s.nombre_servicio, 
       c.fecha_servicio, c.hora_servicio, c.ubicacion, c.estado
FROM cotizaciones c
JOIN usuarios u ON u.id_usuario = c.id_usuario
JOIN vehiculos v ON v.id_vehiculo = c.id_vehiculo
JOIN servicios s ON s.id_servicio = c.id_servicio
WHERE c.estado = 'pendiente'
ORDER BY c.fecha_servicio ASC
";

$result = $conn->query($sql);

$datos = [];
while ($row = $result->fetch_assoc()) {
    $datos[] = $row;
}

jsonResponse(true, "Cotizaciones pendientes", $datos);
