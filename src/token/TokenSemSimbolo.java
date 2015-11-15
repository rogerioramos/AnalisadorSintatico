package token;

public abstract class TokenSemSimbolo extends Token {
    
    private String lexema;

    public TokenSemSimbolo(String lexema, String tipo, int linha, int coluna) {
        super(tipo, linha, coluna);
        setLexema(lexema);
    }
    
    public final String getLexema(){
        return lexema;
    }
    
    public final void setLexema(String lexema){
        this.lexema = lexema;
    }
}
