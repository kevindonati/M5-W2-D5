package kevindonati.M5_W2_D5.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> listaErrori;

    public ValidationException(List<String> listaErrori) {
        super("Errori di validazione");
        this.listaErrori = listaErrori;
    }

    public ValidationException(String message) {
        super(message);
    }
}
