package kevindonati.M5_W2_D5.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrenotazioneDTO(
        @NotNull(message = "Data della richiesta obbligatoria")
        LocalDate dataRichiesta,

        String note,

        @NotNull(message = "Id del dipendente obbligatorio")
        String dipendenteId,

        @NotNull(message = "Id del viaggio obbligatorio")
        String viaggioId
) {
}
