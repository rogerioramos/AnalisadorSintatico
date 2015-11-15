package token;

public class Identificador extends TokenComArgumento {

    public Identificador(int idSimbolo, int linha, int coluna) {
        super(idSimbolo, "IDENTIFICADOR", linha, coluna);
    }

}
