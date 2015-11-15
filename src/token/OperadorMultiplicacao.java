package token;

public class OperadorMultiplicacao extends TokenSemSimbolo {

    public OperadorMultiplicacao(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_MULTIPLICACAO", linha, coluna);
    }
    
}
