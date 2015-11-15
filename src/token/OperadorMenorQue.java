package token;

public class OperadorMenorQue extends TokenSimples {

    public OperadorMenorQue(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_MENOR_QUE", linha, coluna);
    }
    
}
