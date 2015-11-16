package compilador;

import analisadorlexico.*;
import token.*;

public class Compilador {
    
    private static String arquivoFonte;
    
    public static void main(String[] args) {
        /*
        if (args.length == 0){
            System.out.println("Informe o nome do arquivo fonte como parametro.");
            System.exit(1);
        }
        
        arquivoFonte = args[0];
        */
        
        arquivoFonte = "fonteTeste.txt";

        AnalisadorLexico analisadorLexico = new AnalisadorLexico(arquivoFonte);
        Token token = analisadorLexico.getNextToken();

        while (token != null){
            System.out.println(token.getTipo()+"-"+token.getLinha()+"-"+token.getColuna());
            token = analisadorLexico.getNextToken();
        }
        
        analisadorLexico.printListaErros();
        analisadorLexico.printTabelaSimbolos();
    }
    
}
