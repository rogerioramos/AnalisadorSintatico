package token;

public class OperadorMaiorIgualQue extends TokenSemSimbolo {

    public OperadorMaiorIgualQue(String lexema, int linha, int coluna) {
        super(lexema, "OPERADOR_MAIOR_IGUAL_QUE", linha, coluna);
    }
    
}
