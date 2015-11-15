package token;

public class OperadorDivisao extends TokenSimples {

    public OperadorDivisao(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_DIVISAO", linha, coluna);
    }
    
}
