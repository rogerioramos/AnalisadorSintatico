package token;

public class OperadorIgualdade extends TokenSimples {

    public OperadorIgualdade(String lexema, int linha, int coluna) {
        super(lexema, "OPERADOR_IGUALDADE", linha, coluna);
    }
    
}
