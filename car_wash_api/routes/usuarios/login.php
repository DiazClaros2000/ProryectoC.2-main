<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->correo) && isset($data->contraseña)) {
    $correo = $conn->real_escape_string($data->correo);
    $pass = $data->contraseña;

    $sql = "SELECT * FROM usuarios WHERE correo = '$correo'";
    $result = $conn->query($sql);

    if ($result->num_rows === 1) {
        $user = $result->fetch_assoc();
        if (password_verify($pass, $user['contraseña'])) {
            unset($user['contraseña']); // No devolver contraseña
            jsonResponse(true, "Login exitoso", $user);
        } else {
            jsonResponse(false, "Contraseña incorrecta");
        }
    } else {
        jsonResponse(false, "Usuario no encontrado");
    }
} else {
    jsonResponse(false, "Datos incompletos");
}
?>