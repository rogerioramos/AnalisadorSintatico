package token;

public class OperadorSoma extends TokenSimples {

    public OperadorSoma(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_SOMA", linha, coluna);
    }
    
}
