package token;

public class Identificador extends TokenComSimbolo {

    public Identificador(int idSimbolo, int linha, int coluna) {
        super(idSimbolo, "IDENTIFICADOR", linha, coluna);
    }

}
