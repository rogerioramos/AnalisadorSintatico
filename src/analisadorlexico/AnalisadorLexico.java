package analisadorlexico;

import java.io.*;
import java.util.*;
import token.*;

public class AnalisadorLexico {
    
    private static char [] alfabeto = {
        'a','b','c','d','e','f','g','h','i','j','k','l','m',
        'n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M',
        'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        '0','1','2','3','4','5','6','7','8','9',
        '=','>','<','!','+','-','/','*','(',')','[',']',';','"','\'','.',
        ' ','\t','\n', '\r'
    };
    private static char [] caracteresDelimitadores = {'=','>','<','!','+','-','/','*','(',')','[',']',';','"','\'',' ','\t','\n','\r', (char) -1};
    static Map<Integer, Lexema> tabelaSimbolos = new HashMap<>();
    static List<Token> listaTokens = new ArrayList<>();
    static List<ErroLexico> errosLexicos = new ArrayList<>();
    
    
    private int linhaAtual = 1;
    private int colunaAtual = 0;
    private int linhaLiteral = 1;
    private int colunaLiteral = 0;
    
    private boolean isLiteralTexto = false;
    private boolean isLiteralChar = false;
    private boolean isLiteralNumeroNegativo = false;
    
    private Token lastToken;
    
    private String arquivoFonte;
    private BufferedReader buffer1;
    private BufferedReader buffer2;
    
    private int caracteresLidos = 0;
    private int caracteresLidosLiteral = 0;
    

    public AnalisadorLexico(String arquivoFonte) {
        this.arquivoFonte = arquivoFonte;
        try {
            FileInputStream fis1 = new FileInputStream(this.arquivoFonte);
            this.buffer1 = new BufferedReader(new InputStreamReader(fis1));
            FileInputStream fis2 = new FileInputStream(this.arquivoFonte);
            this.buffer2 = new BufferedReader(new InputStreamReader(fis2));
        } catch (FileNotFoundException e){
            System.out.println("Arquivo fonte não encontrado. " + e.getMessage());
        }
    }
    
    
    public Token getNextToken(){
        Token token;
        String lexemaTemporario = "";
        char caractereAtual;
        char proximoCaractere;
        try {
            while (buffer1.ready()){
                caractereAtual = (char) buffer1.read();
                buffer2.skip(1);
                if (isLiteralTexto || isLiteralChar){
                    caracteresLidosLiteral++;
                } else {
                    caracteresLidos++;
                }
                
                buffer2.mark(1);
                proximoCaractere = (char) buffer2.read();
                buffer2.reset();
                                            
                // calcula linha e coluna
                if (caractereAtual == '\n'){
                    linhaAtual++;
                    colunaAtual = 0;
                } else {
                    colunaAtual++;
                }
                
                // se estiver formando um literal texto, adiciona o caractere e continua
                if (isLiteralTexto && caractereAtual != '"'){
                    lexemaTemporario = lexemaTemporario + caractereAtual;
                    // se o proximo caractere for o fim do arquivo, volta caractere que inicio o literal e continua a analise
                    if (proximoCaractere == (char) -1){
                        isLiteralTexto = false;
                        errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Literal não terminado \""));                        
                        lexemaTemporario = "";
                        buffer1.reset();
                        resetBuffer2();
                        buffer2.skip(caracteresLidos+2);
                        linhaAtual = linhaLiteral;
                        colunaAtual = colunaLiteral;                        
                    } else if (proximoCaractere == '"'){
                        Lexema lexema = new Lexema(lexemaTemporario, linhaLiteral, colunaLiteral+1);
                        lexemaTemporario = "";
                        int idSimbolo = addTabelaSimbolos(lexema);
                        token = new LiteralTexto(idSimbolo, linhaLiteral, colunaLiteral+1);
                        lastToken = token;
                        isLiteralTexto = false;
                        caracteresLidos += caracteresLidosLiteral;
                        caracteresLidos = 0;
                        return token;
                    }
                    continue;
                }
                
                // se estiver formando um literal char, adiciona o caractere e continua
                if (isLiteralChar && caractereAtual != '\''){
                    lexemaTemporario = lexemaTemporario + caractereAtual;
                    // se o proximo caractere for o fim do arquivo, volta caractere que inicio o literal e continua a analise
                    if (proximoCaractere == (char) -1){
                        isLiteralChar = false;
                        errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Literal não terminado '"));
                        lexemaTemporario = "";
                        buffer1.reset();
                        resetBuffer2();
                        buffer2.skip(caracteresLidos+2);
                        linhaAtual = linhaLiteral;
                        colunaAtual = colunaLiteral;
                    } else if (proximoCaractere == '\''){
                        Lexema lexema = new Lexema(lexemaTemporario, linhaLiteral, colunaLiteral+1);
                        lexemaTemporario = "";
                        int idSimbolo = addTabelaSimbolos(lexema);
                        token = new LiteralChar(idSimbolo, linhaLiteral, colunaLiteral+1);
                        lastToken = token;
                        isLiteralChar = false;
                        caracteresLidos += caracteresLidosLiteral;
                        caracteresLidos = 0;                        
                        return token;                  
                    }                    
                    continue;
                }                
                
                // se houver tentativa de inserir um caractere invalido...
                if (!isSimboloValido(caractereAtual)){
                    errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual,
                            "Caractere " + caractereAtual + " invalido"));
                    continue;
                }
                
                char auxCaractereAtual = caractereAtual;
                char auxProximoCaractere;
                int auxLinhaAtual = linhaAtual;
                int auxColunaAtual = colunaAtual;
                // armazena os caracteres em uma string temporaria até encontrar um delimitador...
                while (!isCaractereDelimitador(auxCaractereAtual)){
                    lexemaTemporario = lexemaTemporario + auxCaractereAtual;
                    buffer2.mark(2);
                    auxProximoCaractere = (char) buffer2.read();
                    buffer2.reset();
                    if (isCaractereDelimitador(auxProximoCaractere))
                        break;
                    auxCaractereAtual = (char) buffer1.read();
                    buffer2.skip(1);
                    caracteresLidos++;
                    // calcula linha e coluna
                    if (auxCaractereAtual == '\n'){
                        auxLinhaAtual++;
                        auxColunaAtual = 0;
                    } else {
                        auxColunaAtual++;
                    }
                }
                if (!lexemaTemporario.equals("") && !isLiteralTexto && !isLiteralChar){
                    if (isLiteralNumeroNegativo){
                        lexemaTemporario = "-" + lexemaTemporario;
                        isLiteralNumeroNegativo = false;
                    }
                    
                    if (lexemaTemporario.toLowerCase().equals("begin")
                            || lexemaTemporario.toLowerCase().equals("end")
                            || lexemaTemporario.toLowerCase().equals("for")
                            || lexemaTemporario.toLowerCase().equals("do")
                            || lexemaTemporario.toLowerCase().equals("while")
                            || lexemaTemporario.toLowerCase().equals("each")
                            || lexemaTemporario.toLowerCase().equals("if")
                            || lexemaTemporario.toLowerCase().equals("else")
                            || lexemaTemporario.toLowerCase().equals("elseif")
                            || lexemaTemporario.toLowerCase().equals("switch")
                            || lexemaTemporario.toLowerCase().equals("case")
                            || lexemaTemporario.toLowerCase().equals("default")){
                        token = new PalavraReservada(lexemaTemporario, linhaAtual, colunaAtual);
                    } else if (lexemaTemporario.toLowerCase().equals("int")
                            || lexemaTemporario.toLowerCase().equals("float")
                            || lexemaTemporario.toLowerCase().equals("char")
                            || lexemaTemporario.toLowerCase().equals("string")){
                        token = new TipoDado(lexemaTemporario, linhaAtual, colunaAtual);
                    } else if (isIdentificador(lexemaTemporario)){
                        Lexema lexema = new Lexema(lexemaTemporario, linhaAtual, colunaAtual);
                        int idSimbolo = addTabelaSimbolos(lexema);
                        token = new Identificador(idSimbolo, linhaAtual, colunaAtual);
                    } else if (isNumeroInteiro(lexemaTemporario)){
                        Lexema lexema = new Lexema(lexemaTemporario, linhaAtual, colunaAtual);
                        int idSimbolo = addTabelaSimbolos(lexema);
                        token = new LiteralNumeroInteiro(idSimbolo, linhaAtual, colunaAtual);
                    } else if (isNumeroReal(lexemaTemporario)){
                        Lexema lexema = new Lexema(lexemaTemporario, linhaAtual, colunaAtual);
                        int idSimbolo = addTabelaSimbolos(lexema);
                        token = new LiteralNumeroReal(idSimbolo, linhaAtual, colunaAtual);
                    } else {
                        errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual,
                            "Sequência de caracteres " + lexemaTemporario + " inválida"));
                        token = new Invalido(lexemaTemporario, linhaAtual, colunaAtual);
                    }
                    //listaTokens.add(token);
                    lexemaTemporario = "";
                    linhaAtual = auxLinhaAtual;
                    colunaAtual = auxColunaAtual;
                    return token;
                }
                
                switch (caractereAtual){
                    case '=':{
                        if (proximoCaractere == '='){
                            token = new OperadorIgualdade(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            buffer1.skip(1);
                            buffer2.skip(1);
                            colunaAtual++;
                        } else
                            token = new OperadorAtribuicao(caractereAtual, linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }
                    case '>':{
                        if (proximoCaractere == '='){
                            token = new OperadorMaiorIgualQue(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            buffer1.skip(1);
                            buffer2.skip(1);
                            colunaAtual++;
                        } else
                            token = new OperadorMaiorQue(caractereAtual, linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }
                    case '<':{
                        if (proximoCaractere == '='){
                            token = new OperadorMenorIgualQue(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            buffer1.skip(1);
                            buffer2.skip(1);
                            colunaAtual++;
                        } else
                            token = new OperadorMenorQue(caractereAtual, linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }
                    case '!':{
                        if (proximoCaractere == '='){
                            token = new OperadorDiferenca(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            buffer1.skip(1);
                            buffer2.skip(1);
                            colunaAtual++;
                        } else
                            token = new Invalido(Character.toString(caractereAtual),linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }
                    case '+':{
                        token = new OperadorSoma(caractereAtual, linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }                
                    case '-':{
                        if (Character.isDigit(proximoCaractere)){
                            isLiteralNumeroNegativo = true;
                        } else {
                            token = new OperadorSubtracao(caractereAtual, linhaAtual, colunaAtual);
                            return token;
                        }
                        break;
                    }
                    case '/':{
                        token = new OperadorDivisao(caractereAtual, linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }
                    case '*':{
                        token = new OperadorMultiplicacao(caractereAtual, linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }
                    case '(':
                    case ')':
                    case '[':
                    case ']':
                    case ';':{
                        token = new Delimitador(caractereAtual, linhaAtual, colunaAtual);
                        return token;
                        //break;
                    }
                    case '\'':{
                        token = new Delimitador(caractereAtual, linhaAtual, colunaAtual);
                        if (!(lastToken instanceof LiteralChar)){
                            isLiteralChar = true;
                            buffer1.mark(0);
                            linhaLiteral = linhaAtual;
                            colunaLiteral = colunaAtual;
                        }
                        return token;
                        //break;
                    }
                    case '"':{
                        token = new Delimitador(caractereAtual, linhaAtual, colunaAtual);
                        if (!(lastToken instanceof LiteralTexto)){
                            isLiteralTexto = true;
                            buffer1.mark(0);
                            linhaLiteral = linhaAtual;
                            colunaLiteral = colunaAtual;
                        }
                        return token;
                        //break;
                    }
                    case ' ':
                    case '\t':
                    case '\n':
                    case '\r':{
                        break;
                    }
                    default: {
                        token = new Invalido(Character.toString(caractereAtual), linhaAtual, colunaAtual);
                        errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Caractere " + caractereAtual + " invalido"));
                        return token;
                    }
                }                
                
            }
            
            // se ficou algum literal sem fechar...
            if (isLiteralTexto){
                errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Esperando \" após " + lexemaTemporario));
                token = new Invalido(lexemaTemporario, linhaAtual, colunaAtual);
                return token;
            } else if (isLiteralChar){
                errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Esperando \' após " + lexemaTemporario));
                token = new Invalido(lexemaTemporario, linhaAtual, colunaAtual);
                return token;
            }            
     
        } catch (IOException e) {
            System.out.println("Falha ao ler arquivo fonte. " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    public static boolean isSimboloValido(char simbolo){
        for (char c : alfabeto)
            if (simbolo == c) return true;
        return false;
    }
    
    public static boolean isCaractereDelimitador(char simbolo){
        for (char c : caracteresDelimitadores)
            if (simbolo == c) return true;
        return false;
    }
    
    private static boolean isIdentificador(String texto){
        
        // verifica se o primeiro caractere é letra
        if (!Character.isLetter(texto.charAt(0)))
            return false;
        
        // verifica se os caracteres sao letras (a-z) ou digitos (0-9)
        for (char c : texto.toCharArray()){
            if (!Character.isLetter(c) && !Character.isDigit(c))
                return false;
        }
        
        return true;
    }
    
    private static boolean isNumeroInteiro(String texto){
        if (texto.substring(0, 1).equals("-"))
            texto = texto.substring(1);
        
        for (char c : texto.toCharArray()){
            if (!Character.isDigit(c))
                return false;
        }        
        return true;
    }
    
    private static boolean isNumeroReal(String texto){
        // se nao possui ponto retorna falso
        if (!texto.contains("."))
            return false;
        
        if (texto.substring(0, 1).equals("-"))
            texto = texto.substring(1);

        String inteiro = texto.substring(0, texto.indexOf("."));
        String decimal = texto.substring(texto.indexOf(".")+1);
        
        // se o ponto nao estiver entre numeros, retorna falso
        if (inteiro.isEmpty() || decimal.isEmpty())
            return false;
        
        // se possui mais de um ponto retorna falso
        if (decimal.contains("."))
            return false;
        
        // verifica se os demais caracteres sao digitos
        for (char c : inteiro.toCharArray()){
            if (!Character.isDigit(c))
                return false;
        }        
        for (char c : decimal.toCharArray()){
            if (!Character.isDigit(c))
                return false;
        }
        
        return true;
    }
    
    public static void addToken(Token token){
        listaTokens.add(token);
    }
    
    public static int addTabelaSimbolos(Lexema lexema){
        int chave = 0;
        for (Map.Entry<Integer, Lexema> entry : tabelaSimbolos.entrySet()){
            chave = entry.getKey();
            Lexema valor = entry.getValue();
            
            if (lexema.getLexema().equals(valor.getLexema()))
                return chave;
        }
        chave++;

        tabelaSimbolos.put(chave, lexema);
        return chave;
    }
    
    public static void printListaTokens(){
        System.out.println("\nLista de Tokens:");
        for (Token i : listaTokens){
            if (i instanceof TokenSimples)
                System.out.println("<" + i.getTipo() + "," + ((TokenSimples)i).getLexema() 
                        + "> " + i.getLinha() + " - " + i.getColuna());
            if (i instanceof TokenComArgumento)
                System.out.println("<" + i.getTipo() + "," + ((TokenComArgumento)i).getIdSimbolo()
                        + "> " + i.getLinha() + " - " + i.getColuna());
        }
    }
    
    public static void printListaErros(){
        System.out.println("\nLista de Erros:");
        for (ErroLexico i : errosLexicos){
            System.out.println(i.getMensagem() + " na linha " 
                    + i.getLinha() + ", coluna " + i.getColuna() + ".");
        }
    }
    
    public static void printTabelaSimbolos(){
        System.out.println("\nTabela de Simbolos:");
        for (Map.Entry<Integer, Lexema> entry : tabelaSimbolos.entrySet()){
            Lexema lexema = entry.getValue();
            System.out.println(entry.getKey() 
                    + " - " + lexema.getLexema() 
                    + " - " + lexema.getLinha() 
                    + " - " + lexema.getColuna());
        }        
    }
    
    private void resetBuffer2(){
        try {
            this.buffer2.close();
            InputStream fis2 = new FileInputStream(this.arquivoFonte);
            buffer2 = new BufferedReader(new InputStreamReader(fis2));
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo fonte não encontrado. " + e.getMessage());            
        } catch (IOException e) {
            System.out.println("Erro ao manipular fonte: " + e.getMessage());
        }
        
        
    }
}
