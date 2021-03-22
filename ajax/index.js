/******* MIGUEL VIDAL FUNCIA *** DAW_M06_ACT06 ****************************/


// VARIABLES GLOBALES
var respuesta;
var nodo; //nodo en el que se mostrará el mensaje
var inputsCheck = Array.from(document.getElementsByClassName("inputCheck"));

// MANIPULACION HTML
function agregarHtml(mensaje) {
    let div = document.createElement("div");
    div.class = "mensaje";
    div.style.display = "inline";
    div.style.paddingRight = "10px";
    div.innerHTML = mensaje;
    let siguienteElemento = nodo.nextElementSibling;
    if (siguienteElemento != null && siguienteElemento.class == "mensaje") {
        nodo.parentNode.removeChild(siguienteElemento);
    }
    nodo.parentNode.insertBefore(div, nodo.nextElementSibling);
}

// FETCH
function hacerFetch(url, parametros) {
    let configFetch = {
        method: "POST",
        body: parametros,
        headers: { "Content-Type":"application/x-www-form-urlencoded" }
    };
    let promise = fetch(url, configFetch);
    promise.then(function(respuesta) {
        console.log(`Estado:" + ${respuesta.status} + ${respuesta}`);
        respuesta.text().then(
            function (textoRespuesta) {
                agregarHtml(textoRespuesta);
            },
            function(error) {
                console.log('Error en respuesta: ' + error.message);
            }
        );
    },
        function(error) {
            console.log('Error con la petición:' + error.message);
        });
        ;
}

// XMLHTTP
function gestionarRespuesta(xhr) {
    if (xhr.status == 404) {
        console.log("Url incorrecta (404)");
    }
    if (xhr.status == 200) {
        console.log("Respuesta correcta (200)");
        respuesta = xhr.responseText;
        agregarHtml(respuesta); 
    }
}

function enviarPeticion(url, parametros) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST",url,true);
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if (xhr.readyState==4) {
            gestionarRespuesta(xhr);
        }
    }
    xhr.send(parametros);
}

// EVENTOS
function procesarEvento(evt) {
    let url;
    let parametros;
    let formularioOrigen = evt.target.parentNode;
    let tipoEvento = evt.type;
    let inputs;
    let valores = [];
    let indiceInput;
    // Crear url y parámetros
    if (tipoEvento == "click") {
        nodo = document.getElementById("divRespuesta");
        if (formularioOrigen == document.forms[0]) {
            inputs = document.forms[0];
            for (let i=0; i < inputs.length-1; i++) {
                valores.push(inputs[i].value);
            }
            url = "php/clickGuardar.php";
        } else if (formularioOrigen == document.forms[1]) {
            inputs = document.forms[1];
            for (let i=0; i < inputs.length-1; i++) {
                valores.push(inputs[i].value);
            }
            url = "php/clickCheck.php";
        }
        parametros = "v1=" + valores[0] + "&v2=" + valores[1] + "&v3=" + valores[2] + "&v4=" + valores[3];

    } else if (tipoEvento == "keyup") {
        nodo = evt.target;
        let valor = nodo.value;
        parametros = "v=" + valor;
        if (formularioOrigen == document.forms[0]) {
            url = "php/keyupGuardar.php";
        } else if (formularioOrigen == document.forms[1]){
            url = "php/keyupCheck.php";

            //obtener el índice del input seleccionado
            for (let i=0; i<inputsCheck.length; i++) {
                if (inputsCheck[i] === nodo) {
                    indiceInput = i;
                }
            }
            parametros += "&n=" + indiceInput;
        }
        parametros = "v=" + valor + "&n=" + indiceInput;
    }
    // Enviar
    if (tipoEvento == "click") {
        enviarPeticion(url, parametros);
    } else if (tipoEvento == "keyup") {
        hacerFetch(url, parametros);
    }
}

// INICIAL
function inicial() {
    
    /******* Ejercicio 2 ************************************************************/
    let btnGuardar = document.getElementById("btnGuardar");
    btnGuardar.addEventListener("click", procesarEvento, true);

    /******* Ejercicio 3 ************************************************************/
    let inputsGuardar = document.forms[0];
    for (let i=0; i < inputsGuardar.length-1; i++) {
        inputsGuardar[i].addEventListener("keyup", procesarEvento);
    }
    /******* Ejercicio 4 ************************************************************/
    let btnCheck = document.getElementById("btnCheck");
    btnCheck.addEventListener("click", procesarEvento, true);

    /******* Ejercicio 5 ************************************************************/
    let inputsCheck = document.forms[1];
    for (let i=0; i < inputsCheck.length-1; i++) {
        inputsCheck[i].addEventListener("keyup", procesarEvento);
    }
}

document.onload = inicial();