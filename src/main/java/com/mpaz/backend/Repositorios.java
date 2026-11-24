package com.mpaz.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}

interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByStatus(String status);
}

interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByStudentUsername(String studentUsername);
}