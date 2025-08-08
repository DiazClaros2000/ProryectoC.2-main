<?php
require_once __DIR__ . '/../../config/db.php';
require_once __DIR__ . '/../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->correo) && isset($data->contraseña)) {
    $correo = $conn->real_escape_string($data->correo);
    $pass = $data->contraseña;

    // Buscar al administrador por correo
    $sql = "SELECT * FROM administradores WHERE correo = '$correo'";
    $result = $conn->query($sql);

    if ($result && $result->num_rows === 1) {
        $admin = $result->fetch_assoc();

        // Verificar la contraseña encriptada
        if (password_verify($pass, $admin['contraseña'])) {
            unset($admin['contraseña']); // Ocultar contraseña en la respuesta
            jsonResponse(true, "Login de administrador exitoso", $admin);
        } else {
            jsonResponse(false, "Contraseña incorrecta");
        }
    } else {
        jsonResponse(false, "Administrador no encontrado");
    }
} else {
    jsonResponse(false, "Datos incompletos");
}
