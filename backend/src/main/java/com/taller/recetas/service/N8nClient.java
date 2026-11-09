package com.taller.recetas.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.taller.recetas.exception.ServicioExternoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class N8nClient {

    private final RestClient restClient;
    private final String url;

    public N8nClient(@Value("${app.n8n-url}") String url) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(60));

        this.restClient = RestClient.builder().requestFactory(factory).build();
        this.url = url;
    }

    public RespuestaN8n buscar(String ingredientes) {
        RespuestaN8n respuesta;
        try {
            respuesta = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("ingredientes", ingredientes))
                    .retrieve()
                    .body(RespuestaN8n.class);
        } catch (RestClientException e) {
            throw new ServicioExternoException("Error llamando al webhook de n8n", e);
        }

        if (respuesta == null || respuesta.explicacion() == null) {
            throw new ServicioExternoException("n8n devolvió una respuesta vacía");
        }
        return respuesta;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RespuestaN8n(boolean hayResultados, String explicacion, List<RecetaEncontrada> recetas) {

        public List<RecetaEncontrada> recetas() {
            return recetas == null ? List.of() : recetas;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RecetaEncontrada(Long id, double score) {
    }
}
