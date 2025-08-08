<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (
    isset($data->nombre) &&
    isset($data->correo) &&
    isset($data->contraseña)
) {
    $nombre = $conn->real_escape_string($data->nombre);
    $correo = $conn->real_escape_string($data->correo);
    $clave = password_hash($data->contraseña, PASSWORD_DEFAULT);

    // Validar si ya existe
    $check = $conn->query("SELECT id_admin FROM administradores WHERE correo = '$correo'");
    if ($check->num_rows > 0) {
        jsonResponse(false, "El correo ya está registrado como administrador");
    }

    $sql = "INSERT INTO administradores (nombre, correo, contraseña)
            VALUES ('$nombre', '$correo', '$clave')";

    if ($conn->query($sql)) {
        jsonResponse(true, "Administrador registrado correctamente");
    } else {
        jsonResponse(false, "Error al registrar: " . $conn->error);
    }
} else {
    jsonResponse(false, "Datos incompletos");
}
