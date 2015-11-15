package token;

public class LiteralChar extends TokenComArgumento {

    public LiteralChar(int idSimbolo, int linha, int coluna) {
        super(idSimbolo, "LITERAL_CHAR", linha, coluna);
    }
    
}
