package ec.edu.solca.paciente.security;

public record JwtPrincipal(String username, String role) {
}
