package token;

public class OperadorMenorIgualQue extends TokenSemSimbolo {

    public OperadorMenorIgualQue(String lexema, int linha, int coluna) {
        super(lexema, "OPERADOR_MENOR_IGUAL_QUE", linha, coluna);
    }
    
}
