package token;

public class OperadorMaiorQue extends TokenSemSimbolo {

    public OperadorMaiorQue(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_MAIOR_QUE", linha, coluna);
    }
    
}
