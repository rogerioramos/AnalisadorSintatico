package token;

public class OperadorMultiplicacao extends TokenSimples {

    public OperadorMultiplicacao(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_MULTIPLICACAO", linha, coluna);
    }
    
}
