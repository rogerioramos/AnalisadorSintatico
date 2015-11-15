package token;

public class OperadorSubtracao extends TokenSemSimbolo {

    public OperadorSubtracao(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_SUBTRACAO", linha, coluna);
    }
    
}
