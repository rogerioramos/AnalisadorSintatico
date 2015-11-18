package analisadorsintatico;

import analisadorlexico.AnalisadorLexico;

public class AnalisadorSintatico {
    
    AnalisadorLexico analisadorLexico;
    Gramatica gramatica;
    
    public AnalisadorSintatico(AnalisadorLexico al){
        analisadorLexico = al;
        try {
            gramatica = new Gramatica(analisadorLexico);
        } catch (AnaliseSintaticaException e){
            System.out.println("Erro ao instanciar gramatica.");
        }
    }
    
    public void parse(){
        gramatica.checkProducaoPrograma();
    }
}
