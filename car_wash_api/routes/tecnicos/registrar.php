<?php
require_once __DIR__ . '/../../../config/db.php';
require_once __DIR__ . '/../../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->nombre) && isset($data->zona_asignada)) {
    $nombre = $conn->real_escape_string($data->nombre);
    $zona = $conn->real_escape_string($data->zona_asignada);
    $sql = "INSERT INTO personal_tecnico (nombre, zona_asignada, disponibilidad) VALUES ('$nombre', '$zona', TRUE)";

    if ($conn->query($sql)) {
        jsonResponse(true, "Técnico registrado correctamente");
    } else {
        jsonResponse(false, "Error al registrar: " . $conn->error);
    }
} else {
    jsonResponse(false, "Datos incompletos");
}