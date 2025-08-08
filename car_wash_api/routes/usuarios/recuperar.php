<?php
require_once '../../config/db.php';
require_once '../../helpers/jsonResponse.php';

$data = json_decode(file_get_contents("php://input"));

if (isset($data->correo)) {
    $correo = $conn->real_escape_string($data->correo);
    
    $check = $conn->query("SELECT * FROM usuarios WHERE correo = '$correo'");
    if ($check->num_rows > 0) {
        $nueva = substr(str_shuffle("0123456789abcdefghijklmnopqrstuvwxyz"), 0, 8);
        $hash = password_hash($nueva, PASSWORD_DEFAULT);
        
        $conn->query("UPDATE usuarios SET contraseña = '$hash' WHERE correo = '$correo'");
        // Aquí deberías enviar el correo con $nueva usando PHPMailer o similar
        jsonResponse(true, "Nueva contraseña enviada al correo: $nueva (solo para pruebas)");
    } else {
        jsonResponse(false, "Correo no registrado");
    }
} else {
    jsonResponse(false, "Correo no proporcionado");
}
?>