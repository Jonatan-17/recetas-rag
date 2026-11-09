# Recetas con lo que tienes

Aplicación web donde el usuario escribe los ingredientes que tiene y recibe recetas sugeridas con RAG, una explicación de cómo prepararlas y lo que le falta. Las recetas se pueden guardar en favoritos.

## Tecnologías

- Frontend: HTML, CSS y JavaScript
- Backend: Spring Boot 4 (Java 21) con PostgreSQL
- Workflow: n8n, Qdrant como base vectorial y OpenRouter para los embeddings (`openai/text-embedding-3-small`) y el modelo (`openai/gpt-4.1-mini`)

## Cómo funciona

1. El usuario escribe sus ingredientes y el frontend los envía al backend.
2. El backend llama al webhook de n8n.
3. El workflow genera el embedding de los ingredientes y busca en Qdrant las 5 recetas más parecidas (se descartan las que tienen una similitud menor a 0.45).
4. Si hay recetas relacionadas, el modelo explica cómo prepararlas usando solo esas recetas. Si no hay, se informa al usuario.
5. El backend completa los datos de cada receta desde PostgreSQL, calcula los ingredientes que faltan y devuelve el resultado.

Se asume que el usuario siempre tiene sal, pimienta, aceite y agua.

## Estructura del proyecto

```
backend/    API en Spring Boot (controladores, servicios, entidades y recetas.json)
frontend/   Página web (index.html, app.js, styles.css)
n8n/        Workflows de ingesta y de búsqueda
docker-compose.yml   Base de datos PostgreSQL
```

## Requisitos

- Java 21
- Docker
- n8n y Qdrant en ejecución (los workflows usan la dirección `http://qdrant:6333`, así que ambos deben estar en la misma red de Docker)
- Una API key de OpenRouter
- Live Server (o cualquier servidor estático en el puerto 5500)

## Cómo ejecutarlo

1. Levantar la base de datos con `docker compose up -d`. PostgreSQL queda en `localhost:5433`.
2. En n8n, crear una credencial **Bearer Auth** con la API key de OpenRouter.
3. Importar los workflows de la carpeta `n8n/` y asignarles esa credencial en los nodos que llaman a OpenRouter.
4. Ejecutar una vez el workflow **Recetas - Ingesta en Qdrant** para cargar las 30 recetas en Qdrant.
5. Activar el workflow **Recetas - Buscar con RAG**. Su webhook queda en `http://localhost:5678/webhook/recetas-buscar`.
6. Iniciar el backend con `cd backend && ./gradlew bootRun`. Al arrancar por primera vez carga las recetas en PostgreSQL.
7. Abrir `frontend/index.html` con Live Server.

### Variables de entorno opcionales

| Variable | Valor por defecto |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5433/recetas` |
| `DB_USER` | `recetas` |
| `DB_PASSWORD` | `recetas` |
| `N8N_URL` | `http://localhost:5678/webhook/recetas-buscar` |

## Endpoints del backend

| Método y ruta | Función |
|---|---|
| `POST /api/busquedas` | Recibe los ingredientes y devuelve las recetas sugeridas |
| `GET /api/recetas/{id}` | Muestra el detalle de una receta |
| `GET /api/favoritos` | Lista las recetas favoritas |
| `POST /api/favoritos` | Guarda una receta como favorita |
| `DELETE /api/favoritos/{id}` | Quita una receta de favoritos |

Ejemplo de búsqueda:

```json
POST /api/busquedas
{ "ingredientes": "arroz, pollo y cebolla" }
```

## Pruebas

- **"Arroz, pollo y cebolla"**: el sistema muestra recetas con esos ingredientes e indica lo que falta.
- **"Tornillos y pegante"**: no hay recetas relacionadas y el sistema se lo informa al usuario.

## Alcance

Se cargan 30 recetas en PostgreSQL y en Qdrant. Esta versión no incluye inicio de sesión (hay una sola lista de favoritos) ni administración de recetas desde la aplicación.

## Autores

- Jonatan Sarmiento
- Michael Santos
