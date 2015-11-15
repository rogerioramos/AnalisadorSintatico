package token;

public abstract class Token {
    
    private String tipo;
    private int linha;
    private int coluna;

    public Token(String tipo, int linha, int coluna) {
        setTipo(tipo);
        setLinha(linha);
        setColuna(coluna);
    }

    public final String getTipo() {
        return tipo;
    }

    public final void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public final int getLinha() {
        return linha;
    }

    public final void setLinha(int linha) {
        this.linha = linha;
    }

    public final int getColuna() {
        return coluna;
    }

    public final void setColuna(int coluna) {
        this.coluna = coluna;
    }
    
}
