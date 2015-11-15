package token;

public class OperadorAtribuicao extends TokenSemSimbolo {

    public OperadorAtribuicao(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "OPERADOR_ATRIBUICAO", linha, coluna);
    }
    
}
