package kevindonati.M5_W2_D5.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private LocalDate dataRichiesta;

    private String note;

    @ManyToOne
    @JoinColumn(name = "id_dipendente", nullable = false)
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "id_viaggio", nullable = false)
    private Viaggio viaggio;

    public Prenotazione(LocalDate dataRichiesta, String note, Dipendente dipendente, Viaggio viaggio) {
        this.dataRichiesta = dataRichiesta;
        this.note = note;
        this.dipendente = dipendente;
        this.viaggio = viaggio;
    }
}
