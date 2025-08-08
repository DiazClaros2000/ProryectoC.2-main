<?php
require_once __DIR__ . '/../../../config/db.php';
require_once __DIR__ . '/../../../helpers/jsonResponse.php';

$sql = "SELECT * FROM personal_tecnico";
$result = $conn->query($sql);
$tecnicos = [];
while ($row = $result->fetch_assoc()) {
    $tecnicos[] = $row;
}
jsonResponse(true, "Lista de técnicos", $tecnicos);
