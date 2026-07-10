package kevindonati.M5_W2_D5.controllers;

import kevindonati.M5_W2_D5.Enum.StatoViaggio;
import kevindonati.M5_W2_D5.entities.Viaggio;
import kevindonati.M5_W2_D5.exceptions.ValidationException;
import kevindonati.M5_W2_D5.payloads.ViaggioDTO;
import kevindonati.M5_W2_D5.payloads.ViaggioResponseDTO;
import kevindonati.M5_W2_D5.services.ViaggioService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {
    private final ViaggioService viaggioService;

    public ViaggioController(ViaggioService viaggioService) {
        this.viaggioService = viaggioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ViaggioResponseDTO save(@RequestBody @Validated ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> listaErrori = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(listaErrori);
        }
        Viaggio viaggio = viaggioService.save(body);
        return new ViaggioResponseDTO(viaggio.getId());
    }

    @GetMapping
    public List<Viaggio> findAll() {
        return viaggioService.findAll();
    }

    @GetMapping("/{id}")
    public Viaggio findById(@PathVariable UUID id) {
        return viaggioService.findById(id);
    }

    @PutMapping("/{id}")
    public Viaggio findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> listaErrori = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(listaErrori);
        }
        return viaggioService.findByIdAndUpdate(id, body);
    }

    @PatchMapping("/{id}/stato")
    public Viaggio cambiaStato(@PathVariable UUID id, @RequestParam StatoViaggio stato) {
        return viaggioService.cambiaStato(id, stato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id) {
        viaggioService.findByIdAndDelete(id);
    }
}
