<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->id_usuario) &&
    isset($data->marca) &&
    isset($data->modelo) &&
    isset($data->año) &&
    isset($data->tipo_aceite)
) {
    $sql = "INSERT INTO vehiculos (id_usuario, marca, modelo, año, tipo_aceite)
            VALUES ('$data->id_usuario', '$data->marca', '$data->modelo', '$data->año', '$data->tipo_aceite')";

    if ($conn->query($sql)) {
        jsonResponse(true, "Vehículo registrado");
    } else {
        jsonResponse(false, "Error al registrar: " . $conn->error);
    }
} else {
    jsonResponse(false, "Datos incompletos");
}
?>