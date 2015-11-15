/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package token;

/**
 *
 * @author antoniofranco
 */
public class Delimitador extends TokenSemSimbolo {

    public Delimitador(char lexema, int linha, int coluna) {
        super(Character.toString(lexema), "DELIMITADOR", linha, coluna);
    }
    
}
