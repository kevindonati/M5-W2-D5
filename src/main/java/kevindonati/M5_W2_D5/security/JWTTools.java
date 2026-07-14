package kevindonati.M5_W2_D5.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.exceptions.UnhautorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {
    private final String secret;

    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(Dipendente dipendente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .subject(String.valueOf(dipendente.getId()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception e) {
            throw new UnhautorizedException("Si sono riscontrati problemi con il token, Rieffetua il login");
        }
    }
}
