let formulario = document.forms[0];


let inputNick = formulario[0];
let inputPass1 = formulario[1];
let inputPass2 = formulario[2];
let inputEmail = formulario[3];
let selectAficiones = formulario["aficiones"];
let inputDNI = formulario[5];
let checkCondiciones = formulario[6];
let inputAficion = formulario[7];
let btnSubmit = document.getElementById("btnSubmit");
let btnRecupera = document.getElementById("btnRecupera");
let btnAnadirAficion = document.getElementById("btnAnadirAficion");

let opciones = selectAficiones.options;


function mostrarMensaje(mensaje, nodo) {

    let label = document.createElement("label");
    let estiloLabel = label.style;

    label.id = "mensajeBienMal";
    estiloLabel.color = "white";
    estiloLabel.padding = "5px";

    if (mensaje == null) {
        estiloLabel.backgroundColor="green";
        label.innerHTML="Correcto";
    } else {
        estiloLabel.backgroundColor="red";
        label.innerHTML="Incorrecto. " + mensaje;
    } // crea mensaje correcto/incorrecto

    if (nodo.nextSibling.id != label.id) {
        nodo.parentNode.insertBefore(label, nodo.nextSibling);
    } else {
        nodo.parentNode.replaceChild(label, nodo.nextSibling);
    } // inserta mensaje si no lo hay
}


function compruebaInputs(nodo) {

    let esCorrecto = false;
    let error;

    // campos que no son de texto
    if (nodo == inputPass2) {
        if (nodo.value == inputPass1.value)  {
            esCorrecto = true;
        } else {
            error = "Contraseñas no coinciden";
        }

    } else if (nodo == selectAficiones) {
        let totalSeleccionados = 0;
        for (let i=0; i<opciones.length; i++) {
            if (opciones[i].selected) {
                totalSeleccionados++;
            }
        }
        if (totalSeleccionados >= 2) {
            esCorrecto = true;
        } else {
            error = "Debes seleccionar al menos 2";
        }
    } else if (nodo == checkCondiciones) {
        if (checkCondiciones.checked) {
            esCorrecto = true;
            error = "Debes aceptar condiciones";
        }

    //campos de texto
    let regex;
    
    } else {
        switch(nodo) {
            case inputNick:
                error = "Solo letras, entre 2 y 25";
                regex = /^[A-z]{2,25}$/;
                break;
            case inputPass1:
                error = "Solo dígitos, entre 4 y 9";
                regex = /^([A-z]|[0-9]){4,9}$/;
                break;
            case inputEmail:
                error = "No es un email";
                regex = /[A-z]+@[A-z]+\.[A-z]+/;
                break;
            case inputDNI:
                error = "8 números y una letra";
                regex = /^[0-9]{8}[A-z]{1}$/;
                break;
            case inputAficion:
                error = "Solo letras, entre 2 y 25";
                regex = /^[A-z]{2,50}$/;
                break;
                
        }
        if (nodo.value.match(regex)) {
            esCorrecto = true;
        }
    }

    if (esCorrecto) {
        error = null;
    }

    mostrarMensaje(error, nodo);
    return esCorrecto;

}



function capturarEvento(evt) {
    let nodoOrigen = evt.target;
    compruebaInputs(nodoOrigen);
}

/*************** EJERCICIO 4 ***************/

// funciones set y get cookie
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    } return "";
}


/*************** EJERCICIO 2 ***************/
function submit() {
    // comprueba que todo es correcto
    if (
        esCorrecto(inputNick) &&
        esCorrecto(inputPass1) &&
        esCorrecto(inputPass2) &&
        esCorrecto(inputEmail) &&
        esCorrecto(selectAficiones) &&
        esCorrecto(inputDNI) &&
        esCorrecto(checkCondiciones)
    ) {
        /*************** EJERCICIO 4 ***************/
        setCookie("inputNick", inputNick.value, 1);
        setCookie("inputPass1", inputPass1.value, 1);
        setCookie("inputPass2", inputPass2.value, 1);
        setCookie("inputEmail", inputEmail.value, 1);
        setCookie("inputDNI", inputDNI.value, 1);
        setCookie("checkCondiciones", checkCondiciones.checked, 1);

        formulario.submit();

    } else {
        mostrarMensaje(inputNick);
        mostrarMensaje(inputPass1);
        mostrarMensaje(inputPass2);
        mostrarMensaje(inputEmail);
        mostrarMensaje(selectAficiones);
        mostrarMensaje(inputDNI);
        mostrarMensaje(checkCondiciones);
    }
}

/*************** EJERCICIO 5 ***************/
function recuperarDatos() {
    inputNick.value = getCookie("inputNick");
    inputPass1.value = getCookie("inputPass1");
    inputPass2.value = getCookie("inputPass2");
    inputEmail.value = getCookie("inputEmail");
    inputDNI.value = getCookie("inputDNI");
    checkCondiciones.checked = getCookie("checkCondiciones");
}

/*************** EJERCICIO 3 ***************/

function anadirAficion(evt) {
    valor = inputAficion.value;
    let opcion = new Option(valor, valor);
    opciones.add(opcion);
}

inputNick.addEventListener("keyup", capturarEvento, false);
inputPass1.addEventListener("keyup", capturarEvento, false);
inputPass2.addEventListener("keyup", capturarEvento, false);
inputEmail.addEventListener("keyup", capturarEvento, false);
inputDNI.addEventListener("keyup", capturarEvento, false);
selectAficiones.addEventListener("change", capturarEvento, false);
checkCondiciones.addEventListener("change", capturarEvento, false);

/*************** EJERCICIO 2 ***************/
btnSubmit.addEventListener("click", submit, false);

/*************** EJERCICIO 3 ***************/
btnAnadirAficion.addEventListener("click", anadirAficion, false);

/*************** EJERCICIO 5 *******    ********/
btnRecupera.addEventListener("click", recuperarDatos, false);