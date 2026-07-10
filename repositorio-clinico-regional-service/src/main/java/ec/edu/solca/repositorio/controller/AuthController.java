package ec.edu.solca.repositorio.controller;

import ec.edu.solca.repositorio.security.JwtService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Set<String> ROLES = Set.of("ADMIN", "MEDICO", "LABORATORIO");
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String role = request.role().toUpperCase();
        if (!ROLES.contains(role)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rol no permitido"));
        }
        String username = request.username().isBlank() ? role.toLowerCase() + "@solca.local" : request.username();
        return ResponseEntity.ok(Map.of(
                "token", jwtService.generateToken(username, role),
                "username", username,
                "role", role));
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String role) {
    }
}
