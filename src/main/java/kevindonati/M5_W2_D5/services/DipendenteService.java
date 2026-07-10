package kevindonati.M5_W2_D5.services;

import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.exceptions.BadRequestException;
import kevindonati.M5_W2_D5.payloads.DipendenteDTO;
import kevindonati.M5_W2_D5.repositories.DipendenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DipendenteService {
    private final DipendenteRepository dipendenteRepository;

    public DipendenteService(DipendenteRepository dipendenteRepository) {
        this.dipendenteRepository = dipendenteRepository;
    }

    public Dipendente save(DipendenteDTO payload) {
        if (dipendenteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("L'email " + payload.email() + " è già registrata");
        }

        if (dipendenteRepository.existsByUsername(payload.username())) {
            throw new BadRequestException("Lo username " + payload.username() + " è già in uso");
        }

        Dipendente nuovoDipendente = new Dipendente(payload.username(), payload.nome(), payload.cognome(), payload.email());
        Dipendente dipendenteSalvato = dipendenteRepository.save(nuovoDipendente);

        log.info("Dipendente con id " + dipendenteSalvato.getId() + " salvato corretamente");
        return dipendenteSalvato;
    }

    public Page<Dipendente> findAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return dipendenteRepository.findAll(pageable);
    }
}
