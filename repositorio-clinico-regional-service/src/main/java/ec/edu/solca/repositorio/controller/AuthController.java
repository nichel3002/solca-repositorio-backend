package ec.edu.solca.repositorio.controller;

import ec.edu.solca.repositorio.model.UsuarioClinico;
import ec.edu.solca.repositorio.repository.UsuarioClinicoRepository;
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
    private static final Set<String> USUARIOS_DEMO = Set.of("admin@solca.local", "medico@solca.local", "laboratorio@solca.local");
    private final JwtService jwtService;
    private final UsuarioClinicoRepository usuarioRepository;

    public AuthController(JwtService jwtService, UsuarioClinicoRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String username = normalizarUsuario(request.username());
        String role = texto(request.role()).toUpperCase();
        if (!ROLES.contains(role)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rol no permitido"));
        }

        if (!username.isBlank()) {
            var usuarioRegistrado = usuarioRepository.findById(username);
            if (usuarioRegistrado.isPresent()) {
                role = usuarioRegistrado.get().getRole();
            } else if (!USUARIOS_DEMO.contains(username)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuario no registrado"));
            }
        } else {
            username = role.toLowerCase() + "@solca.local";
        }

        return ResponseEntity.ok(Map.of(
                "token", jwtService.generateToken(username, role),
                "username", username,
                "role", role));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registrar(@RequestBody RegistroRequest request) {
        String username = normalizarUsuario(request.username());
        String nombreCompleto = texto(request.nombreCompleto());
        String role = texto(request.role()).toUpperCase();
        String sede = texto(request.sede());
        if (username.isBlank() || !username.contains("@")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ingrese un usuario clinico valido."));
        }
        if (nombreCompleto.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ingrese el nombre completo."));
        }
        if (!ROLES.contains(role)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rol no permitido"));
        }
        if (usuarioRepository.existsById(username) || USUARIOS_DEMO.contains(username)) {
            return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe."));
        }

        UsuarioClinico usuario = new UsuarioClinico();
        usuario.setUsername(username);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setRole(role);
        usuario.setSede(sede.isBlank() ? "SOLCA Quito" : sede);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
                "token", jwtService.generateToken(username, role),
                "username", username,
                "role", role));
    }

    private String normalizarUsuario(String username) {
        return texto(username).toLowerCase();
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String role) {
    }

    public record RegistroRequest(@NotBlank String username, @NotBlank String nombreCompleto, @NotBlank String role, String sede) {
    }
}
