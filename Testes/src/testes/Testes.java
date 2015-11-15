/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author antoniofranco
 */
public class Testes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        InputStream fis1 = new FileInputStream("teste.txt");
        InputStream fis2 = new FileInputStream("teste.txt");
        BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
        
        System.out.println("Caractere do br1: " + (char) br1.read());
        br1 = new BufferedReader(new InputStreamReader(fis1));
        System.out.println("Caractere do br1: " + (char) br1.read());

    }
    
}
