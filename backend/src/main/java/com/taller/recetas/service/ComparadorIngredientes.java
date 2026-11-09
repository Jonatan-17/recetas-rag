package com.taller.recetas.service;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

final class ComparadorIngredientes {

    private static final Set<String> BASICOS = Set.of("sal", "pimienta", "aceite", "agua");

    private static final Set<String> IGNORAR = Set.of("del", "con", "los", "las", "una", "unos", "tengo", "algo", "para");

    private ComparadorIngredientes() {
    }

    static List<String> faltantes(List<String> ingredientesReceta, String disponibles) {
        Set<String> delUsuario = palabras(disponibles);

        return ingredientesReceta.stream()
                .filter(ingrediente -> !BASICOS.contains(normalizar(ingrediente)))
                .filter(ingrediente -> palabras(ingrediente).stream()
                        .noneMatch(palabra -> delUsuario.stream().anyMatch(u -> mismaPalabra(palabra, u))))
                .toList();
    }

    private static Set<String> palabras(String texto) {
        return Arrays.stream(normalizar(texto).split("[^a-z]+"))
                .filter(p -> p.length() > 2 && !IGNORAR.contains(p))
                .collect(Collectors.toSet());
    }

    private static String normalizar(String texto) {
        return Normalizer.normalize(texto.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim();
    }

    private static boolean mismaPalabra(String a, String b) {
        return a.equals(b)
                || a.equals(b + "s") || b.equals(a + "s")
                || a.equals(b + "es") || b.equals(a + "es");
    }
}
