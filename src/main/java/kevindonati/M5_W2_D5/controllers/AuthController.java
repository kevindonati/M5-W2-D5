package kevindonati.M5_W2_D5.controllers;

import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.exceptions.ValidationException;
import kevindonati.M5_W2_D5.payloads.DipendenteDTO;
import kevindonati.M5_W2_D5.payloads.DipendenteResponseDTO;
import kevindonati.M5_W2_D5.payloads.LoginDTO;
import kevindonati.M5_W2_D5.payloads.LoginResponseDTO;
import kevindonati.M5_W2_D5.services.AuthService;
import kevindonati.M5_W2_D5.services.DipendenteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final DipendenteService dipendenteService;

    public AuthController(AuthService authService, DipendenteService dipendenteService) {
        this.authService = authService;
        this.dipendenteService = dipendenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteResponseDTO saveDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        Dipendente salvato = this.dipendenteService.save(body);
        return new DipendenteResponseDTO(salvato.getId());
    }
}
