package com.mpaz.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MpazController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private MaterialRepository materialRepo;

    @Autowired
    private EvaluacionRepository evalRepo;

    // Crea 3 usuarios de prueba y 2 guías al arrancar si la base está vacía
    @Bean
    CommandLineRunner cargarDatosIniciales() {
        return args -> {
            if (!usuarioRepo.existsByUsername("utp")) {
                Usuario u1 = new Usuario();
                u1.username = "utp";
                u1.password = "1234";
                u1.role = "UTP";
                usuarioRepo.save(u1);
            }
            if (!usuarioRepo.existsByUsername("docente")) {
                Usuario u2 = new Usuario();
                u2.username = "docente";
                u2.password = "1234";
                u2.role = "DOCENTE";
                usuarioRepo.save(u2);
            }
            if (!usuarioRepo.existsByUsername("estudiante")) {
                Usuario u3 = new Usuario();
                u3.username = "estudiante";
                u3.password = "1234";
                u3.role = "ESTUDIANTE";
                usuarioRepo.save(u3);
            }
            if (materialRepo.count() == 0) {
                Material m1 = new Material();
                m1.subject = "Lenguaje";
                m1.unitName = "Unidad 1";
                m1.oaCode = "OA 01";
                m1.description = "Las Vocales Iniciales";
                m1.contentBody = "Guía de Estudio: Las vocales son A, E, I, O, U. Ejemplo: A de Árbol, E de Elefante, I de Isla, O de Oso, U de Uva.";
                m1.status = "PUBLICADO";
                m1.authorRole = "UTP";
                m1.dateCreated = "02/12/2026";
                materialRepo.save(m1);

                Material m2 = new Material();
                m2.subject = "Matemática";
                m2.unitName = "Unidad 1";
                m2.oaCode = "OA 01";
                m2.description = "Conteo del 1 al 10";
                m2.contentBody = "Guía de Estudio: Contar elementos del 1 al 10. Ejemplo: 1 sol, 2 ojos, 5 dedos en una mano.";
                m2.status = "PUBLICADO";
                m2.authorRole = "UTP";
                m2.dateCreated = "02/12/2026";
                materialRepo.save(m2);
            }
        };
    }

    // 1. REGISTRO DE USUARIO
    @PostMapping("/auth/register")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario nuevo) {
        if (usuarioRepo.existsByUsername(nuevo.username)) {
            return ResponseEntity.badRequest().build();
        }
        Usuario guardado = usuarioRepo.save(nuevo);
        return ResponseEntity.ok(guardado);
    }

    // 2. INICIO DE SESIÓN
    @PostMapping("/auth/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario datos) {
        Usuario encontrado = usuarioRepo.findByUsernameAndPassword(datos.username, datos.password);
        if (encontrado == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(encontrado);
    }

    // 3. CREAR MATERIAL (Docente -> EN_REVISION | UTP -> PUBLICADO)
    @PostMapping("/materials")
    public Material crearMaterial(@RequestBody Material mat) {
        if (mat.authorRole != null && mat.authorRole.equalsIgnoreCase("UTP")) {
            mat.status = "PUBLICADO";
        } else {
            mat.status = "EN_REVISION";
        }
        return materialRepo.save(mat);
    }

    // 4. LISTAR TODOS LOS MATERIALES (Para UTP y Docente)
    @GetMapping("/materials")
    public List<Material> listarTodos() {
        return materialRepo.findAll();
    }

    // 5. LISTAR SOLO MATERIALES PUBLICADOS (Para Estudiante)
    @GetMapping("/materials/published")
    public List<Material> listarPublicados() {
        return materialRepo.findByStatus("PUBLICADO");
    }

    // 6. AUTORIZAR O RECHAZAR MATERIAL (Jefe UTP)
    @PutMapping("/materials/{id}/status")
    public ResponseEntity<Material> cambiarEstado(@PathVariable Long id, @RequestParam String status) {
        Optional<Material> opcion = materialRepo.findById(id);
        if (opcion.isPresent()) {
            Material mat = opcion.get();
            mat.status = status.toUpperCase();
            Material actualizado = materialRepo.save(mat);
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // 7. ELIMINAR MATERIAL
    @DeleteMapping("/materials/{id}")
    public ResponseEntity<Void> eliminarMaterial(@PathVariable Long id) {
        if (materialRepo.existsById(id)) {
            materialRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 8. GUARDAR EVALUACIÓN DEL ESTUDIANTE
    @PostMapping("/evaluations")
    public Evaluacion guardarEvaluacion(@RequestBody Evaluacion ev) {
        return evalRepo.save(ev);
    }

    // 9. OBTENER PROGRESO DEL ESTUDIANTE
    @GetMapping("/evaluations/{username}")
    public List<Evaluacion> obtenerProgreso(@PathVariable String username) {
        return evalRepo.findByStudentUsername(username);
    }
}