package kevindonati.M5_W2_D5.controllers;

import jakarta.validation.ValidationException;
import kevindonati.M5_W2_D5.entities.Dipendente;
import kevindonati.M5_W2_D5.payloads.DipendenteDTO;
import kevindonati.M5_W2_D5.payloads.DipendenteResponseDTO;
import kevindonati.M5_W2_D5.services.DipendenteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {
    private final DipendenteService dipendenteService;

    public DipendenteController(DipendenteService dipendenteService) {
        this.dipendenteService = dipendenteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteResponseDTO save(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> listaErrori = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException((Throwable) listaErrori);
        }
        Dipendente dipendente = dipendenteService.save(body);
        return new DipendenteResponseDTO(dipendente.getId());
    }

    @GetMapping
    public Page<Dipendente> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String orderBy) {
        return dipendenteService.findAll(page, size, orderBy);
    }

    @GetMapping("/{id}")
    public Dipendente findByID(@PathVariable UUID id) {
        return dipendenteService.findById(id);
    }

    @PutMapping("/{id}")
    public Dipendente findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> listaErrori = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException((Throwable) listaErrori);
        }
        return dipendenteService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id) {
        dipendenteService.findByIdAndDelete(id);
    }

    @PatchMapping("/{id}/upload")
    public Dipendente uploadImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        return dipendenteService.uploadImage(id, file);
    }
}
