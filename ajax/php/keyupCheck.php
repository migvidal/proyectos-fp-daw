<?php
/******* Ejercicio 5 ************************************************************/

session_start();


$valor = $_POST['v'];
$numeroInput = $_POST['n'];

if (isset($_SESSION["ses1"], $_SESSION["ses2"], $_SESSION["ses3"], $_SESSION["ses4"])) {
    $sesionArray = array($_SESSION["ses1"], $_SESSION["ses2"], $_SESSION["ses3"], $_SESSION["ses4"]);

    //validar
    $valorCorrecto = true;
    if ($valor != $sesionArray[$numeroInput]) {
        $valorCorrecto = false;
    }

    if ($valorCorrecto) {
        $respuesta = "✓";
    } else {
        $respuesta = "⛌";
    }

} else {
    $respuesta = "No hay clave guardada";
}

//respuesta
echo $respuesta;

?>
