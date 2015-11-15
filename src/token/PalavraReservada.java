package token;

public class PalavraReservada extends TokenSimples {

    public PalavraReservada(String lexema, int linha, int coluna) {
        super(lexema, "PALAVRA_RESERVADA", linha, coluna);
    }
    
}
