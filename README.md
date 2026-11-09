# Recetas con lo que tienes

Aplicación web donde el usuario escribe los ingredientes que tiene y recibe recetas sugeridas con RAG, una explicación de cómo prepararlas y lo que le falta. Las recetas se pueden guardar en favoritos.

## Tecnologías

- Frontend: HTML, CSS y JavaScript
- Backend: Spring Boot con PostgreSQL
- Workflow: n8n, Qdrant y OpenRouter

## Cómo ejecutarlo

1. Levantar la base de datos con `docker compose up -d`.
2. Importar en n8n los workflows de la carpeta `n8n/`, ejecutar el de ingesta y activar el de búsqueda.
3. Iniciar el backend con `cd backend && ./gradlew bootRun`.
4. Abrir `frontend/index.html` con Live Server.

## Autores

- Jonatan Sarmiento
- Michael Santos
