<?php
/******* Ejercicio 3 ************************************************************/

$valor = $_POST['v'];

//validar
$correcto=false;
$regex = "/^[0-9]{1,}$/";
if (preg_match($regex, $valor)) {
    $correcto = true;
}

//respuesta
$respuesta;
if ($correcto) {
    $respuesta = '✓';
} else {
    $respuesta = '⛌';
}
echo $respuesta;

?>
