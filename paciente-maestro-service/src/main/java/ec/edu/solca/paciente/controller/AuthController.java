package ec.edu.solca.paciente.controller;

import ec.edu.solca.paciente.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;

    @Value("${auth.username}")
    private String username;

    @Value("${auth.password}")
    private String password;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        if (username.equals(request.username()) && password.equals(request.password())) {
            return ResponseEntity.ok(new AuthResponse(jwtService.createToken(request.username()), request.username(), jwtService.getExpirationSeconds()));
        }
        return ResponseEntity.status(401).build();
    }

    public record LoginRequest(String username, String password) {}
    public record AuthResponse(String token, String username, long expiresInSeconds) {}
}
