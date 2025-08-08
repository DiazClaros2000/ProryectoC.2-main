<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->id_vehiculo)) {
    $sql = "DELETE FROM vehiculos WHERE id_vehiculo = $data->id_vehiculo";

    if ($conn->query($sql)) {
        jsonResponse(true, "Vehículo eliminado");
    } else {
        jsonResponse(false, "Error al eliminar: " . $conn->error);
    }
} else {
    jsonResponse(false, "ID de vehículo no proporcionado");
}
?>