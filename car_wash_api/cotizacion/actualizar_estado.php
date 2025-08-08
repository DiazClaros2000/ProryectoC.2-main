<?php
require_once __DIR__ . '/../../../config/db.php';
require_once __DIR__ . '/../../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->id_cotizacion) && isset($data->estado)) {
    $id = intval($data->id_cotizacion);
    $estado = strtolower($conn->real_escape_string($data->estado));

    // Validar estado permitido
    $estados_validos = ['pendiente', 'aceptada', 'rechazada', 'completada'];
    if (!in_array($estado, $estados_validos)) {
        jsonResponse(false, "Estado inválido. Usa: pendiente, aceptada, rechazada o completada.");
    }

    $sql = "UPDATE cotizaciones SET estado = '$estado' WHERE id_cotizacion = $id";
    if ($conn->query($sql)) {
        jsonResponse(true, "Estado actualizado correctamente");
    } else {
        jsonResponse(false, "Error al actualizar estado: " . $conn->error);
    }
} else {
    jsonResponse(false, "Faltan datos: id_cotizacion o estado");
}