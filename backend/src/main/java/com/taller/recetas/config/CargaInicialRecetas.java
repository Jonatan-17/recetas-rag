package com.taller.recetas.config;

import com.taller.recetas.entity.Receta;
import com.taller.recetas.repository.RecetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
public class CargaInicialRecetas implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CargaInicialRecetas.class);

    private final RecetaRepository recetaRepository;
    private final ObjectMapper objectMapper;

    public CargaInicialRecetas(RecetaRepository recetaRepository, ObjectMapper objectMapper) {
        this.recetaRepository = recetaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (recetaRepository.count() > 0) {
            return;
        }

        try (InputStream archivo = new ClassPathResource("recetas.json").getInputStream()) {
            List<RecetaJson> recetas = objectMapper.readValue(archivo, new TypeReference<>() {
            });

            recetaRepository.saveAll(recetas.stream()
                    .map(r -> new Receta(r.id(), r.nombre(), r.descripcion(), r.tiempoMinutos(), r.ingredientes(), r.pasos()))
                    .toList());

            log.info("Se cargaron {} recetas", recetas.size());
        }
    }

    private record RecetaJson(Long id, String nombre, String descripcion, int tiempoMinutos,
                              List<String> ingredientes, List<String> pasos) {
    }
}
