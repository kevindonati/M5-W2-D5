package kevindonati.M5_W2_D5.services;

import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.entities.Prenotazione;
import kevindonati.M5_W2_D5.entities.Viaggio;
import kevindonati.M5_W2_D5.exceptions.BadRequestException;
import kevindonati.M5_W2_D5.exceptions.NotFoundException;
import kevindonati.M5_W2_D5.payloads.PrenotazioneDTO;
import kevindonati.M5_W2_D5.repositories.DipendenteRepository;
import kevindonati.M5_W2_D5.repositories.PrenotazioneRepository;
import kevindonati.M5_W2_D5.repositories.ViaggioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PrenotazioneService {
    private final PrenotazioneRepository prenotazioneRepository;
    private final DipendenteRepository dipendenteRepository;
    private final ViaggioRepository viaggioRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, DipendenteRepository dipendenteRepository, ViaggioRepository viaggioRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.dipendenteRepository = dipendenteRepository;
        this.viaggioRepository = viaggioRepository;
    }

    public Prenotazione save(PrenotazioneDTO payload) {
        Dipendente dipendente = dipendenteRepository.findById(payload.dipendenteId()).orElseThrow(() -> new NotFoundException(payload.dipendenteId()));
        Viaggio viaggio = viaggioRepository.findById(payload.viaggioId()).orElseThrow(() -> new NotFoundException(payload.viaggioId()));

        if (prenotazioneRepository.existsByDipendenteAndViaggio_Data(dipendente, viaggio.getData())) {
            throw new BadRequestException("Il dipendente ha già una prenotazione in questa data");
        }

        Prenotazione nuovaPrenotazione = new Prenotazione(payload.dataRichiesta(), payload.note(), dipendente, viaggio);
        Prenotazione prenotazioneSalvata = prenotazioneRepository.save(nuovaPrenotazione);

        log.info("Prenoazione con id " + prenotazioneSalvata.getId() + " salvata correttamente");

        return prenotazioneSalvata;
    }

    public Page<Prenotazione> findAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID id) {
        return prenotazioneRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Prenotazione prenotazioneTrovata = this.findById(id);

        prenotazioneRepository.delete(prenotazioneTrovata);
    }

    public Prenotazione findByIdAndUpdate(UUID id, PrenotazioneDTO payload) {
        Prenotazione prenotazioneTrovata = this.findById(id);

        Dipendente dipendente = dipendenteRepository.findById(payload.dipendenteId()).orElseThrow(() -> new NotFoundException(payload.dipendenteId()));
        Viaggio viaggio = viaggioRepository.findById(payload.viaggioId()).orElseThrow(() -> new NotFoundException(payload.viaggioId()));

        prenotazioneTrovata.setDataRichiesta(payload.dataRichiesta());
        prenotazioneTrovata.setNote(payload.note());
        prenotazioneTrovata.setDipendente(dipendente);
        prenotazioneTrovata.setViaggio(viaggio);

        Prenotazione prenotazioneModificata = prenotazioneRepository.save(prenotazioneTrovata);
        return prenotazioneModificata;
    }
}
