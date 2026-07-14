package kevindonati.M5_W2_D5.services;

import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.exceptions.UnhautorizedException;
import kevindonati.M5_W2_D5.payloads.LoginDTO;
import kevindonati.M5_W2_D5.repositories.DipendenteRepository;
import kevindonati.M5_W2_D5.security.JWTTools;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final DipendenteRepository dipendenteRepository;
    private final JWTTools jwtTools;

    public AuthService(DipendenteRepository dipendenteRepository, JWTTools jwtTools) {
        this.dipendenteRepository = dipendenteRepository;
        this.jwtTools = jwtTools;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        Dipendente found = this.dipendenteRepository.findByEmail(body.email());

        if (found.getPassword().equals(body.password())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new UnhautorizedException("Credenziali sbagliate");
        }
    }
}
