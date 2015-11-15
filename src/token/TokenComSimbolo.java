package token;

public abstract class TokenComSimbolo extends Token {
    
    private int idSimbolo;

    public TokenComSimbolo(int idSimbolo, String tipo, int linha, int coluna) {
        super(tipo, linha, coluna);
        setIdSimbolo(idSimbolo);
    }
    
    public final int getIdSimbolo(){
        return idSimbolo;
    }
    
    public final void setIdSimbolo(int idSimbolo){
        this.idSimbolo = idSimbolo;
    }
    
}
