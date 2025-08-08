<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->id_vehiculo) &&
    isset($data->marca) &&
    isset($data->modelo) &&
    isset($data->año) &&
    isset($data->tipo_aceite)
) {
    $sql = "UPDATE vehiculos SET
            marca = '$data->marca',
            modelo = '$data->modelo',
            año = '$data->año',
            tipo_aceite = '$data->tipo_aceite'
            WHERE id_vehiculo = $data->id_vehiculo";

    if ($conn->query($sql)) {
        jsonResponse(true, "Vehículo actualizado");
    } else {
        jsonResponse(false, "Error al actualizar: " . $conn->error);
    }
} else {
    jsonResponse(false, "Datos incompletos");
}
?>