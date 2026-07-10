package kevindonati.M5_W2_D5.services;

import kevindonati.M5_W2_D5.Enum.StatoViaggio;
import kevindonati.M5_W2_D5.entities.Viaggio;
import kevindonati.M5_W2_D5.exceptions.NotFoundException;
import kevindonati.M5_W2_D5.payloads.ViaggioDTO;
import kevindonati.M5_W2_D5.repositories.ViaggioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ViaggioService {
    private final ViaggioRepository viaggioRepository;

    public ViaggioService(ViaggioRepository viaggioRepository) {
        this.viaggioRepository = viaggioRepository;
    }

    public Viaggio save(ViaggioDTO payload) {
        Viaggio nuovoViaggio = new Viaggio(payload.destinazione(), payload.data(), payload.stato());
        Viaggio viaggioSalvato = viaggioRepository.save(nuovoViaggio);

        log.info("Viaggio con id " + viaggioSalvato.getId() + " salvato correttamente");
        return viaggioSalvato;
    }

    public List<Viaggio> findAll() {
        return viaggioRepository.findAll();
    }

    public Viaggio findById(UUID id) {
        return viaggioRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Viaggio viaggioTrovato = this.findById(id);

        viaggioRepository.delete(viaggioTrovato);
    }

    public Viaggio findByIdAndUpdate(UUID id, ViaggioDTO payload) {
        Viaggio viaggioTrovato = this.findById(id);

        viaggioTrovato.setDestinazione(payload.destinazione());
        viaggioTrovato.setData(payload.data());
        viaggioTrovato.setStato(payload.stato());

        Viaggio viaggioModificato = viaggioRepository.save(viaggioTrovato);

        return viaggioModificato;
    }

    public Viaggio cambiaStato(UUID id, StatoViaggio stato) {
        Viaggio viaggioTrovato = viaggioRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        viaggioTrovato.setStato(stato);

        return viaggioRepository.save(viaggioTrovato);
    }
}
