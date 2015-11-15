package token;

public class OperadorMaiorQue extends TokenSimples {

    public OperadorMaiorQue(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_MAIOR_QUE", linha, coluna);
    }
    
}
