package token;

public class OperadorDiferenca extends TokenSemSimbolo {

    public OperadorDiferenca(String lexema, int linha, int coluna) {
        super(lexema, "OPERADOR_DIFERENCA", linha, coluna);
    }
    
}
