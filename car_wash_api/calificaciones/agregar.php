<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->id_cotizacion) &&
    isset($data->puntuacion) &&
    isset($data->comentario)
) {
    $id = intval($data->id_cotizacion);
    $puntuacion = intval($data->puntuacion);
    $comentario = $conn->real_escape_string($data->comentario);

    $sql = "INSERT INTO calificaciones (id_cotizacion, puntuacion, comentario)
            VALUES ($id, $puntuacion, '$comentario')";

    if ($conn->query($sql)) {
        jsonResponse(true, "Calificación guardada");
    } else {
        jsonResponse(false, "Error al guardar: " . $conn->error);
    }
} else {
    jsonResponse(false, "Datos incompletos");
}
?>