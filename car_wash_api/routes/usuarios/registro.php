<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->nombre) && 
    isset($data->apellido) && 
    isset($data->correo) && 
    isset($data->contraseña) && 
    isset($data->pais)
) {
    $nombre = $conn->real_escape_string($data->nombre);
    $apellido = $conn->real_escape_string($data->apellido);
    $correo = $conn->real_escape_string($data->correo);
    $contraseña = password_hash($data->contraseña, PASSWORD_DEFAULT);
    $pais = $conn->real_escape_string($data->pais);

    $sql = "INSERT INTO usuarios (nombre, apellido, correo, contraseña, pais) 
            VALUES ('$nombre', '$apellido', '$correo', '$contraseña', '$pais')";

    if ($conn->query($sql)) {
        echo json_encode(["success" => true, "message" => "Usuario registrado"]);
    } else {
        echo json_encode(["success" => false, "message" => "Error al registrar"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Datos incompletos"]);
}
?>
