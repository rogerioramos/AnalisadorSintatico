package token;

public class Invalido extends TokenSimples {

    public Invalido(String lexema, int linha, int coluna) {
        super(lexema, "INVALIDO", linha, coluna);
    }
    
}
