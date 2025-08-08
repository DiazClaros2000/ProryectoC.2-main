<?php
require_once __DIR__ . '/../../../config/db.php';
require_once __DIR__ . '/../../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->id_cotizacion) && isset($data->id_tecnico)) {
    $id_cotizacion = intval($data->id_cotizacion);
    $id_tecnico = intval($data->id_tecnico);
    $fecha_asignacion = date('Y-m-d');

    // Insertar asignación
    $sql = "INSERT INTO asignaciones (id_cotizacion, id_tecnico, fecha_asignacion) VALUES ($id_cotizacion, $id_tecnico, '$fecha_asignacion')";

    if ($conn->query($sql)) {
        // Opcional: actualizar disponibilidad del técnico
        $conn->query("UPDATE personal_tecnico SET disponibilidad = FALSE WHERE id_tecnico = $id_tecnico");
        jsonResponse(true, "Técnico asignado correctamente");
    } else {
        jsonResponse(false, "Error al asignar: " . $conn->error);
    }
} else {
    jsonResponse(false, "Datos incompletos");
}