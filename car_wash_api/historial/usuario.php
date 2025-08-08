<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$id_usuario = isset($_GET['id_usuario']) ? intval($_GET['id_usuario']) : 0;

if ($id_usuario > 0) {
    $sql = "SELECT hs.*, c.fecha_servicio, c.hora_servicio, s.nombre_servicio
            FROM historial_servicios hs
            JOIN cotizaciones c ON hs.id_cotizacion = c.id_cotizacion
            JOIN servicios s ON c.id_servicio = s.id_servicio
            WHERE c.id_usuario = $id_usuario
            ORDER BY c.fecha_servicio DESC";

    $result = $conn->query($sql);
    $historial = [];

    while ($row = $result->fetch_assoc()) {
        $historial[] = $row;
    }

    jsonResponse(true, "Historial cargado", $historial);
} else {
    jsonResponse(false, "ID de usuario no válido");
}
?>
