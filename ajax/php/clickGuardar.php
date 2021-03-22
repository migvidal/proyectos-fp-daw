<?php
/******* Ejercicio 2 ************************************************************/
session_start();
$v1 = $_POST['v1'];
$v2 = $_POST['v2'];
$v3 = $_POST['v3'];
$v4 = $_POST['v4'];
$valores = array($v1, $v2, $v3, $v4);

//validar
$valorCorrecto = true;
$regex = "/^[0-9]{1,}$/";
for ($i = 0; $i < count($valores); $i++) {
    $valor = $valores[$i];
    if ($valor == '' || preg_match($regex, $valor) == false) {
        $valorCorrecto = false;
    }
}

//guardar y respuesta
if ($valorCorrecto) {
    $_SESSION["ses1"] = $v1;
    $_SESSION["ses2"] = $v2;
    $_SESSION["ses3"] = $v3;
    $_SESSION["ses4"] = $v4;
    echo "Guardado!";
} else {
    echo "Combinación no válida"; 
}

?>
