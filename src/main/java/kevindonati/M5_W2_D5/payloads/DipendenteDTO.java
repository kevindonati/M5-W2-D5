package kevindonati.M5_W2_D5.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(
        @NotBlank(message = "Username obbligatorio")
        @Size(min = 2, max = 25, message = "Lo username deve avere un numero di caratteri compreso tra 2 e 25")
        String username,

        @NotBlank(message = "Nome obbligatorio")
        @Size(min = 2, max = 25, message = "Il nome deve avere un numero di caratteri compreso tra 2 e 25")
        String nome,

        @NotBlank(message = "Cognome obbligatorio")
        @Size(min = 2, max = 25, message = "Il cognome deve avere un numero di caratteri compreso tra 2 e 25")
        String cognome,

        @NotBlank(message = "Email obbligatoria")
        @Email(message = "Inserisci un email valida")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        String password
) {
}
