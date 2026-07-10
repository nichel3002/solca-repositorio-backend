package ec.edu.solca.consulta.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtService {
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private final ObjectMapper objectMapper;
    private final String secret;

    public JwtService(ObjectMapper objectMapper, @Value("${security.jwt.secret}") String secret) {
        this.objectMapper = objectMapper;
        this.secret = secret;
    }

    public Optional<JwtPrincipal> validate(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }
            String unsigned = parts[0] + "." + parts[1];
            if (!sign(unsigned).equals(parts[2])) {
                return Optional.empty();
            }
            Map<String, Object> payload = objectMapper.readValue(URL_DECODER.decode(parts[1]), new TypeReference<>() {});
            long exp = ((Number) payload.getOrDefault("exp", 0)).longValue();
            if (Instant.now().getEpochSecond() > exp) {
                return Optional.empty();
            }
            return Optional.of(new JwtPrincipal(String.valueOf(payload.get("sub")), String.valueOf(payload.get("role"))));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private String sign(String unsigned) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return URL_ENCODER.encodeToString(mac.doFinal(unsigned.getBytes(StandardCharsets.UTF_8)));
    }
}
