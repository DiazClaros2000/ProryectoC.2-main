<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->id_usuario) &&
    isset($data->tipo) &&
    isset($data->fecha_programada)
) {
    $id_usuario = intval($data->id_usuario);
    $tipo = $conn->real_escape_string($data->tipo);
    $fecha = $conn->real_escape_string($data->fecha_programada);

    $sql = "INSERT INTO recordatorios (id_usuario, tipo, fecha_programada, estado)
            VALUES ($id_usuario, '$tipo', '$fecha', 'pendiente')";

    if ($conn->query($sql)) {
        jsonResponse(true, "Recordatorio programado");
    } else {
        jsonResponse(false, "Error al programar: " . $conn->error);
    }
} else {
    jsonResponse(false, "Datos incompletos");
}
?>
