package kevindonati.M5_W2_D5.controllers;

import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.entities.Prenotazione;
import kevindonati.M5_W2_D5.exceptions.ValidationException;
import kevindonati.M5_W2_D5.payloads.PrenotazioneDTO;
import kevindonati.M5_W2_D5.payloads.PrenotazioneResponseDTO;
import kevindonati.M5_W2_D5.services.PrenotazioneService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {
    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrenotazioneResponseDTO save(@RequestBody @Validated PrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> listaErrori = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(listaErrori);
        }
        Prenotazione prenotazione = prenotazioneService.save(body);
        return new PrenotazioneResponseDTO(prenotazione.getId());
    }

    @GetMapping
    public Page<Prenotazione> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataRichiesta") String orderBy) {
        return prenotazioneService.findAll(page, size, orderBy);
    }


    @GetMapping("/{id}")
    public Prenotazione findById(@PathVariable UUID id) {
        return prenotazioneService.findById(id);
    }

    @PutMapping("/{id}")
    public Prenotazione findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated PrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> listaErrori = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(listaErrori);
        }
        return prenotazioneService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id) {
        prenotazioneService.findByIdAndDelete(id);
    }
}
