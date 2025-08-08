<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->id_usuario) &&
    isset($data->id_vehiculo) &&
    isset($data->id_servicio) &&
    isset($data->ubicacion) &&
    isset($data->fecha_servicio) &&
    isset($data->hora_servicio)
) {
    $id_usuario = intval($data->id_usuario);
    $id_vehiculo = intval($data->id_vehiculo);
    $id_servicio = intval($data->id_servicio);
    $ubicacion = $conn->real_escape_string($data->ubicacion);
    $fecha_servicio = $conn->real_escape_string($data->fecha_servicio);
    $hora_servicio = $conn->real_escape_string($data->hora_servicio);

    $latitud = isset($data->latitud) ? $data->latitud : null;
    $longitud = isset($data->longitud) ? $data->longitud : null;

    // Verificar si el servicio existe y si solo se permite en sitio
    $query = "SELECT solo_en_sitio FROM servicios WHERE id_servicio = $id_servicio";
    $result = $conn->query($query);

    if ($result->num_rows === 0) {
        jsonResponse(false, "El servicio no existe");
    }

    $servicio = $result->fetch_assoc();

    if ($servicio['solo_en_sitio'] && $ubicacion === 'domicilio') {
        jsonResponse(false, "Este servicio solo se puede realizar en el centro de servicio");
    }

    $sql = "INSERT INTO cotizaciones (
        id_usuario, id_vehiculo, id_servicio, ubicacion, latitud, longitud, fecha_solicitud, fecha_servicio, hora_servicio, estado
    ) VALUES (
        $id_usuario, $id_vehiculo, $id_servicio, '$ubicacion',
        " . ($latitud !== null ? "'$latitud'" : "NULL") . ",
        " . ($longitud !== null ? "'$longitud'" : "NULL") . ",
        NOW(), '$fecha_servicio', '$hora_servicio', 'pendiente'
    )";

    if ($conn->query($sql)) {
        jsonResponse(true, "Cotización registrada exitosamente");
    } else {
        jsonResponse(false, "Error al registrar la cotización: " . $conn->error);
    }

} else {
    jsonResponse(false, "Faltan datos obligatorios");
}
?>