package analisadorsintatico;

import analisadorlexico.*;
import token.*;

public class Gramatica {
    
    /*
    Especificação da gramática:
    
    PROGRAMA -> identificador BLOCO
    BLOCO -> begin COMANDO end
    COMANDO -> COMANDO | ε
    COMANDO -> DECLARACAOVARIAVEL
    COMANDO -> ATRIBUICAOVARIAVEL
    COMANDO -> for ( INICIALIZACAO ; CONDICAO ; INCREMENTO ) BLOCO
    COMANDO -> while ( CONDICAO ) BLOCO
    COMANDO -> if CONDICAO then BLOCO
    COMANDO -> if CONDICAO then BLOCO else BLOCO
    COMANDO -> identificador ( PARAMETROS )
    PARAMETROS -> PARAMETROS | , PARAMETROS | EXPRESSAO | ε 
    INICIALIZACAO -> DECLARACAOVARIAVEL
    INICIALIZACAO -> ATRIBUICAOVARIAVEL
    DECLARACAOVARIAVEL -> tipodado identificador = EXPRESSAO
    DECLARACAOVARIAVEL -> tipodado identificador = EXPRESSAORELACIONAL
    ATRIBUICAOVARIAVEL -> identificador = EXPRESSAO
    CONDICAO -> EXPRESSAORELACIONAL
    OPERACAO -> SOMA
    OPERACAO -> MULTIPLICACAO
    OPERACAO -> DIVISAO
    INCREMENTO -> OPERACAO | ε
    EXPRESSAO -> identificador
    EXPRESSAO -> ( identificador )
    EXPRESSAO -> literal
    EXPRESSAO -> ( literal )
    EXPRESSAO -> OPERACAO
    EXPRESSAO -> EXPRESSAORELACIONAL
    EXPRESSAO -> ( EXPRESSAO )
    EXPRESSAORELACIONAL -> EXPRESSAO > EXPRESSAO
    EXPRESSAORELACIONAL -> EXPRESSAO < EXPRESSAO
    EXPRESSAORELACIONAL -> EXPRESSAO >= EXPRESSAO
    EXPRESSAORELACIONAL -> EXPRESSAO <= EXPRESSAO
    EXPRESSAORELACIONAL -> EXPRESSAO == EXPRESSAO
    EXPRESSAORELACIONAL -> EXPRESSAO != EXPRESSAO
    
    */
    
    AnalisadorLexico analisadorLexico;
    
    public Gramatica(AnalisadorLexico al) throws AnaliseSintaticaException{
        if (al != null){
            analisadorLexico = al;
        } else {
            throw new AnaliseSintaticaException();
        }
    }
    
    boolean checkProducaoPrograma(){
        Token token;
        token = analisadorLexico.getNextToken();
        if (token instanceof Identificador){
            System.out.println("Derivando: PROGRAMA -> identificador BLOCO");
            checkProducaoBloco();
        } else {
            // um erro aqui
        }
        return true;
    }
    
    boolean checkProducaoBloco(){
        Token token;
        token = analisadorLexico.getNextToken();
        if (token instanceof PalavraReservada){
            if (((PalavraReservada)token).getLexema().toLowerCase().equals("begin")){
                System.out.println("Derivando: BLOCO -> begin COMANDO end");
                checkProducaoComando();
            } else {
                // um erro aqui
            }
        }
        token = analisadorLexico.getNextToken();
        if (token instanceof PalavraReservada){
            if (((PalavraReservada)token).getLexema().toLowerCase().equals("end")){
                
            } else {
                // um erro aqui
            }
        } else {
            // um erro aqui
        }
        return true;
    }
    
    boolean checkProducaoComando(){
        for (int i = 0, y=0, f=9; i < 10; i++) {
            i++;
            y++;
            f++;
        }
        return true;
    }
    
    boolean checkProducaoExpressao(){
        return true;
    }
    
    boolean checkProducaoTipoDado(){
        return true;
    }
    
    boolean checkProducaoExpressaoRelacional(){
        return true;
    }
    
    boolean checkProducaoFor(){
        return true;
    }
    
    boolean checkProducaoWhile(){
        return true;
    }
    
    boolean checkProducaoIf(){
        return true;
    }
    
    boolean checkProducaoFuncao(){
        return true;
    }
}
