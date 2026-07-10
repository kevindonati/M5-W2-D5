package kevindonati.M5_W2_D5.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "Data della richiesta obbligatoria")
        LocalDate dataRichiesta,

        String note,

        @NotNull(message = "Id del dipendente obbligatorio")
        UUID dipendenteId,

        @NotNull(message = "Id del viaggio obbligatorio")
        UUID viaggioId
) {
}
