package analisadorlexico;

public class ErroLexico {
    
    private int linha;
    private int coluna;
    private String mensagem;

    public ErroLexico(int linha, int coluna, String mensagem) {
        this.setLinha(linha);
        this.setColuna(coluna);
        this.setMensagem(mensagem);
    }
    

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
    
}
