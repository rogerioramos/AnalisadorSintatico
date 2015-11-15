package compilador;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Compilador {
    
    private static String arquivoFonte;
    
    public static void main(String[] args) {
        
        if (args.length == 0){
            System.out.println("Informe o nome do arquivo fonte como parametro.");
            System.exit(1);
        }        
        
        arquivoFonte = args[0];
        
        try {

            FileInputStream fis = new FileInputStream(arquivoFonte);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            
            
        } catch (Exception e) {
        }
    }
    
}
