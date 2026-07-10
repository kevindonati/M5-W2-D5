package kevindonati.M5_W2_D5.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kevindonati.M5_W2_D5.Enum.StatoViaggio;

import java.time.LocalDate;

public record ViaggioDTO(
        @NotBlank(message = "Destinazione obbligatoria")
        String destinazione,

        @NotNull(message = "Data obbligatoria")
        @FutureOrPresent(message = "La data del viaggio non può essere nel passato")
        LocalDate data,

        @NotNull(message = "Stato obbligatorio")
        StatoViaggio stato
) {
}
