package com.pruebareservas.config;

import com.pruebareservas.entity.CategoriaEntity;
import com.pruebareservas.entity.Usuario;
import com.pruebareservas.repository.CategoriaRepository;
import com.pruebareservas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(Usuario.builder()
                    .email("juan.perez@example.com")
                    .nombre("Juan").apellido("Pérez")
                    .password("password123").autenticado(false).build());
            usuarioRepository.save(Usuario.builder()
                    .email("maria.garcia@example.com")
                    .nombre("María").apellido("García")
                    .password("password456").autenticado(false).build());
            usuarioRepository.save(Usuario.builder()
                    .email("carlos.lopez@example.com")
                    .nombre("Carlos").apellido("López")
                    .password("password789").autenticado(false).build());
            System.out.println("✓ Usuarios de muestra creados");
        }

        if (categoriaRepository.count() == 0) {
            List<CategoriaEntity> seeds = List.of(
                    cat("Salario", "INGRESO"),
                    cat("Freelance", "INGRESO"),
                    cat("Inversiones", "INGRESO"),
                    cat("Comida", "GASTO"),
                    cat("Transporte", "GASTO"),
                    cat("Entretenimiento", "GASTO"),
                    cat("Servicios", "GASTO")
            );
            categoriaRepository.saveAll(seeds);
            System.out.println("✓ Categorías de muestra creadas");
        }

        System.out.println("✓ Base de datos inicializada");
    }

    private CategoriaEntity cat(String nombre, String tipo) {
        CategoriaEntity c = new CategoriaEntity();
        c.setNombre(nombre);
        c.setTipo(tipo);
        return c;
    }
}
