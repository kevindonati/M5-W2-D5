package kevindonati.M5_W2_D5.repositories;

import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    boolean existsByDipendenteAndViaggio_Data(Dipendente dipendente, LocalDate data);
}
