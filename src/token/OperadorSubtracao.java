package token;

public class OperadorSubtracao extends TokenSimples {

    public OperadorSubtracao(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_SUBTRACAO", linha, coluna);
    }
    
}
