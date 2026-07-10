package ec.edu.solca.repositorio.controller;

import ec.edu.solca.repositorio.model.UsuarioClinico;
import ec.edu.solca.repositorio.repository.UsuarioClinicoRepository;
import ec.edu.solca.repositorio.security.JwtService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private static final Map<String, UsuarioDemo> USUARIOS_DEMO = Map.of(
            "admin@solca.local", new UsuarioDemo("ADMIN", "admin123", "SOLCA Quito"),
            "medico@solca.local", new UsuarioDemo("MEDICO", "medico123", "SOLCA Quito"),
            "laboratorio@solca.local", new UsuarioDemo("LABORATORIO", "lab123", "SOLCA Quito"));
    private final JwtService jwtService;
    private final UsuarioClinicoRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(JwtService jwtService, UsuarioClinicoRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String username = normalizarUsuario(request.username());
        String password = texto(request.password());
        if (username.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ingrese el usuario clinico."));
        }
        if (password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ingrese la contrasena."));
        }

        var usuarioRegistrado = usuarioRepository.findById(username);
        String role;
        String sede;
        if (usuarioRegistrado.isPresent()) {
            UsuarioClinico usuario = usuarioRegistrado.get();
            if (usuario.getPasswordHash() == null || usuario.getPasswordHash().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario no tiene contrasena configurada."));
            }
            if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Credenciales invalidas"));
            }
            role = usuario.getRole();
            sede = texto(usuario.getSede()).isBlank() ? "SOLCA Quito" : usuario.getSede();
        } else if (USUARIOS_DEMO.containsKey(username)) {
            UsuarioDemo usuarioDemo = USUARIOS_DEMO.get(username);
            if (!usuarioDemo.password().equals(password)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Credenciales invalidas"));
            }
            role = usuarioDemo.role();
            sede = usuarioDemo.sede();
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no registrado"));
        }

        return ResponseEntity.ok(Map.of(
                "token", jwtService.generateToken(username, role),
                "username", username,
                "role", role,
                "sede", sede));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registrar(@RequestBody RegistroRequest request) {
        String username = normalizarUsuario(request.username());
        String nombreCompleto = texto(request.nombreCompleto());
        String password = texto(request.password());
        String role = texto(request.role()).toUpperCase();
        String sede = texto(request.sede());
        if (username.isBlank() || !username.contains("@")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ingrese un usuario clinico valido."));
        }
        if (nombreCompleto.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ingrese el nombre completo."));
        }
        if (password.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "La contrasena debe tener al menos 6 caracteres."));
        }
        if (!ROLES.contains(role)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rol no permitido"));
        }
        if (usuarioRepository.existsById(username) || USUARIOS_DEMO.containsKey(username)) {
            return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe."));
        }

        UsuarioClinico usuario = new UsuarioClinico();
        String sedeAsignada = sede.isBlank() ? "SOLCA Quito" : sede;
        usuario.setUsername(username);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setRole(role);
        usuario.setSede(sedeAsignada);
        usuario.setPasswordHash(passwordEncoder.encode(password));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
                "token", jwtService.generateToken(username, role),
                "username", username,
                "role", role,
                "sede", sedeAsignada));
    }

    private String normalizarUsuario(String username) {
        return texto(username).toLowerCase();
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record RegistroRequest(@NotBlank String username, @NotBlank String nombreCompleto, @NotBlank String password, @NotBlank String role, String sede) {
    }

    private record UsuarioDemo(String role, String password, String sede) {
    }
}
