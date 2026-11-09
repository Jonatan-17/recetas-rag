const API = "http://localhost:8080/api";

const formulario = document.getElementById("formulario");
const campo = document.getElementById("ingredientes");
const btnBuscar = document.getElementById("btnBuscar");
const estado = document.getElementById("estado");
const resultado = document.getElementById("resultado");
const explicacion = document.getElementById("explicacion");
const tarjetas = document.getElementById("tarjetas");
const listaFavoritos = document.getElementById("listaFavoritos");
const sinFavoritos = document.getElementById("sinFavoritos");

async function pedir(ruta, opciones = {}) {
    const config = { ...opciones };
    if (config.body) {
        config.headers = { "Content-Type": "application/json" };
    }

    let respuesta;
    try {
        respuesta = await fetch(API + ruta, config);
    } catch {
        throw new Error("No se pudo conectar con el servidor. Revisa que el backend esté encendido.");
    }

    if (!respuesta.ok) {
        const error = await respuesta.json().catch(() => null);
        throw new Error(error?.detail ?? "Ocurrió un error. Intenta de nuevo.");
    }

    return respuesta.status === 204 ? null : respuesta.json();
}

function elemento(etiqueta, clase = "", texto = "") {
    const el = document.createElement(etiqueta);
    if (clase) el.className = clase;
    if (texto) el.textContent = texto;
    return el;
}

function mostrarEstado(texto, esError = false) {
    estado.textContent = texto;
    estado.classList.toggle("error", esError);
    estado.hidden = false;
}

function ocultarEstado() {
    estado.hidden = true;
}

async function buscar(ingredientes) {
    mostrarEstado("Buscando recetas...");
    btnBuscar.disabled = true;
    resultado.hidden = true;

    try {
        const datos = await pedir("/busquedas", {
            method: "POST",
            body: JSON.stringify({ ingredientes })
        });
        pintarResultado(datos);
        ocultarEstado();
    } catch (error) {
        mostrarEstado(error.message, true);
    } finally {
        btnBuscar.disabled = false;
    }
}

function pintarResultado(datos) {
    explicacion.textContent = datos.explicacion;
    tarjetas.replaceChildren(...datos.recetas.map(crearTarjeta));
    resultado.hidden = false;
}

function crearTarjeta(receta) {
    const tarjeta = elemento("article", "tarjeta");

    const cabecera = elemento("div", "tarjeta-cabecera");
    cabecera.append(
        elemento("h3", "", receta.nombre),
        elemento("span", "tiempo", `${receta.tiempoMinutos} min`)
    );

    const ingredientes = elemento("ul", "ingredientes");
    receta.ingredientes.forEach(ingrediente => {
        const falta = receta.faltantes.includes(ingrediente);
        ingredientes.append(elemento("li", falta ? "falta" : "", ingrediente));
    });

    const faltan = elemento(
        "p",
        "faltan",
        receta.faltantes.length
            ? `Te falta: ${receta.faltantes.join(", ")}`
            : "Tienes todo lo necesario"
    );

    const pasos = elemento("ol", "pasos");
    pasos.hidden = true;

    const btnPasos = elemento("button", "secundario", "Ver preparación");
    btnPasos.type = "button";
    btnPasos.addEventListener("click", () => alternarPasos(receta.id, pasos, btnPasos));

    const btnFavorito = elemento("button");
    btnFavorito.type = "button";
    btnFavorito.dataset.receta = receta.id;
    marcarFavorito(btnFavorito, receta.favorita);
    btnFavorito.addEventListener("click", () => alternarFavorito(receta.id, btnFavorito));

    const acciones = elemento("div", "acciones");
    acciones.append(btnPasos, btnFavorito);

    tarjeta.append(
        cabecera,
        elemento("p", "descripcion", receta.descripcion),
        ingredientes,
        faltan,
        acciones,
        pasos
    );
    return tarjeta;
}

async function alternarPasos(recetaId, lista, boton) {
    if (lista.childElementCount === 0) {
        boton.disabled = true;
        try {
            const receta = await pedir(`/recetas/${recetaId}`);
            lista.replaceChildren(...receta.pasos.map(paso => elemento("li", "", paso)));
        } catch (error) {
            mostrarEstado(error.message, true);
            return;
        } finally {
            boton.disabled = false;
        }
    }

    lista.hidden = !lista.hidden;
    boton.textContent = lista.hidden ? "Ver preparación" : "Ocultar preparación";
}

function marcarFavorito(boton, esFavorita) {
    boton.dataset.favorita = esFavorita;
    boton.textContent = esFavorita ? "Quitar de favoritos" : "Guardar en favoritos";
    boton.className = esFavorita ? "favorito activo" : "favorito";
}

function actualizarBotonesFavorito(recetaId, esFavorita) {
    document
        .querySelectorAll(`button[data-receta="${recetaId}"]`)
        .forEach(boton => marcarFavorito(boton, esFavorita));
}

async function alternarFavorito(recetaId, boton) {
    const esFavorita = boton.dataset.favorita === "true";
    boton.disabled = true;

    try {
        if (esFavorita) {
            await pedir(`/favoritos/${recetaId}`, { method: "DELETE" });
        } else {
            await pedir("/favoritos", {
                method: "POST",
                body: JSON.stringify({ recetaId })
            });
        }
        actualizarBotonesFavorito(recetaId, !esFavorita);
    } catch (error) {
        mostrarEstado(error.message, true);
    } finally {
        boton.disabled = false;
        cargarFavoritos();
    }
}

async function cargarFavoritos() {
    let favoritos;
    try {
        favoritos = await pedir("/favoritos");
    } catch {
        listaFavoritos.replaceChildren();
        sinFavoritos.textContent = "No se pudieron cargar los favoritos.";
        sinFavoritos.hidden = false;
        return;
    }

    listaFavoritos.replaceChildren(...favoritos.map(favorito => {
        const item = elemento("li");
        const quitar = elemento("button", "secundario", "Quitar");
        quitar.type = "button";
        quitar.addEventListener("click", async () => {
            quitar.disabled = true;
            try {
                await pedir(`/favoritos/${favorito.recetaId}`, { method: "DELETE" });
                actualizarBotonesFavorito(favorito.recetaId, false);
            } catch (error) {
                mostrarEstado(error.message, true);
            }
            cargarFavoritos();
        });

        item.append(
            elemento("span", "", favorito.nombre),
            elemento("span", "tiempo", `${favorito.tiempoMinutos} min`),
            quitar
        );
        return item;
    }));

    sinFavoritos.textContent = "Todavía no has guardado recetas.";
    sinFavoritos.hidden = favoritos.length > 0;
}

formulario.addEventListener("submit", event => {
    event.preventDefault();
    const ingredientes = campo.value.trim();

    if (!ingredientes) {
        mostrarEstado("Escribe al menos un ingrediente.", true);
        campo.focus();
        return;
    }
    buscar(ingredientes);
});

document.querySelectorAll(".ejemplo").forEach(boton => {
    boton.addEventListener("click", () => {
        campo.value = boton.textContent;
        buscar(campo.value);
    });
});

cargarFavoritos();
