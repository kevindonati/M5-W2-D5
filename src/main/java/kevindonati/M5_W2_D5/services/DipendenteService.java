package kevindonati.M5_W2_D5.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.exceptions.BadRequestException;
import kevindonati.M5_W2_D5.exceptions.NotFoundException;
import kevindonati.M5_W2_D5.payloads.DipendenteDTO;
import kevindonati.M5_W2_D5.repositories.DipendenteRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class DipendenteService {
    private final DipendenteRepository dipendenteRepository;
    private final Cloudinary cloudinary;

    public DipendenteService(DipendenteRepository dipendenteRepository, Cloudinary cloudinary) {
        this.dipendenteRepository = dipendenteRepository;
        this.cloudinary = cloudinary;
    }

    public Dipendente save(DipendenteDTO payload) {
        if (dipendenteRepository.existsByEmail(payload.email())) {
            throw new BadRequestException("L'email " + payload.email() + " è già registrata");
        }

        if (dipendenteRepository.existsByUsername(payload.username())) {
            throw new BadRequestException("Lo username " + payload.username() + " è già in uso");
        }

        Dipendente nuovoDipendente = new Dipendente(payload.username(), payload.nome(), payload.cognome(), payload.email(), payload.password());
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

    public Dipendente findById(UUID id) {
        return dipendenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Dipendente dipendenteTrovato = this.findById(id);
        this.dipendenteRepository.delete(dipendenteTrovato);
    }

    public Dipendente findByIdAndUpdate(UUID id, DipendenteDTO payload) {
        Dipendente dipendenteTrovato = this.findById(id);

        if (!dipendenteTrovato.getEmail().equals(payload.email()))
            if (this.dipendenteRepository.existsByEmail(payload.email()))
                throw new BadRequestException("L'indirizzo email " + payload.email() + " è già utilizzato!");

        dipendenteTrovato.setUsername(payload.username());
        dipendenteTrovato.setNome(payload.nome());
        dipendenteTrovato.setCognome(payload.cognome());
        dipendenteTrovato.setEmail(payload.email());

        Dipendente dipendenteModificato = dipendenteRepository.save(dipendenteTrovato);

        return dipendenteModificato;
    }

    public Dipendente uploadImage(UUID id, MultipartFile file) {
        Dipendente dipendente = this.findById(id);
        if (file.isEmpty()) {
            throw new BadRequestException("Seleziona un'immagine.");
        }
        if (file.getSize() > 10485760) {
            throw new BadRequestException("L'immagine deve essere inferiore ai 10mb");
        }
        if (!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/webp")) {
            throw new BadRequestException("Sono consentiti solo file in formato png, jpeg o webp");
        }
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String url = (String) result.get("secure_url");

            dipendente.setImmagineProfilo(url);

            return dipendenteRepository.save(dipendente);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
