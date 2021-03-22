<?php
/******* Ejercicio 4 ************************************************************/
session_start();
$v1 = $_POST['v1'];
$v2 = $_POST['v2'];
$v3 = $_POST['v3'];
$v4 = $_POST['v4'];

$valores = array($v1, $v2, $v3, $v4);

if (isset($_SESSION["ses1"], $_SESSION["ses2"], $_SESSION["ses3"], $_SESSION["ses4"])) {
    $sesionArray = array($_SESSION["ses1"], $_SESSION["ses2"], $_SESSION["ses3"], $_SESSION["ses4"]);
    //validar
    $valorCorrecto = true;
    for ($i = 0; $i < count($valores); $i++) {
        if ($valores[$i] != $sesionArray[$i]) {
            $valorCorrecto = false;
        }
    }

    if ($valorCorrecto) {
        $respuesta = "Valor correcto!";
    } else {
        $respuesta = "Valor incorrecto";
    }
} else {
    $respuesta = "No hay clave guardada";
}



echo $respuesta;

?>
