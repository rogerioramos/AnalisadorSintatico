package analisadorlexico;

import java.io.*;
import java.util.*;
import token.*;

public class AnalisadorLexico {
    
    static char [] alfabeto = {
        'a','b','c','d','e','f','g','h','i','j','k','l','m',
        'n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M',
        'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        '0','1','2','3','4','5','6','7','8','9',
        '=','>','<','!','+','-','/','*','(',')','[',']',';','"','\'','.',
        ' ','\t','\n', '\r'
    };
    static char [] caracteresDelimitadores = {'=','>','<','!','+','-','/','*','(',')','[',']',';','"','\'',' ','\t','\n','\r', (char) -1};
    static Map<Integer, Lexema> tabelaSimbolos = new HashMap<>();
    static List<Token> listaTokens = new ArrayList<>();
    static List<ErroLexico> errosLexicos = new ArrayList<>();
    
    
    public static void main(String[] args) {
        
        if (args.length == 0){
            System.out.println("Informe o nome do arquivo fonte como parametro.");
            System.exit(1);
        }
        
        String nomeArquivo = args[0];
        
        try {
            
            // Abertura do arquivo e inicializacao do buffer
            FileInputStream fis = new FileInputStream(nomeArquivo);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            
            // variaveis para controle de linha e coluna
            int linhaAtual = 1;
            int colunaAtual = 0;
            
            
            // string auxiliar para armazenamento do lexema de um token
            String lexemaTemporario = "";
            
            // variavel boleana para controle de tokens literais
            boolean isLiteralTexto = false;
            boolean isLiteralChar = false;
            boolean isLiteralNumeroNegativo = false;
            
            char caractereAtual;
            char proximoCaractere;
            
            // enquanto houver caracteres no arquivo...
            while (br.ready()){
                Token token;
                
                caractereAtual = (char) br.read();
                
                // pega proximo caractere
                br.mark(2);
                proximoCaractere = (char) br.read();
                br.reset();
                
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
                    continue;
                }
                
                // se estiver formando um literal char, adiciona o caractere e continua
                if (isLiteralChar && caractereAtual != '\''){
                    lexemaTemporario = lexemaTemporario + caractereAtual;
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
                    br.mark(2);
                    auxProximoCaractere = (char) br.read();
                    br.reset();
                    if (isCaractereDelimitador(auxProximoCaractere))
                        break;
                    auxCaractereAtual = (char) br.read();
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
                        token = new Invalido(lexemaTemporario, linhaAtual, colunaAtual);
                        errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual,
                            "Sequência de caracteres " + lexemaTemporario + " inválida"));
                    }
                    listaTokens.add(token);
                    lexemaTemporario = "";
                    linhaAtual = auxLinhaAtual;
                    colunaAtual = auxColunaAtual;
                    continue;
                }
                
                switch (caractereAtual){
                    case '=':{
                        if (proximoCaractere == '='){
                            token = new OperadorIgualdade(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            br.skip(1);
                        } else
                            token = new OperadorAtribuicao(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }
                    case '>':{
                        if (proximoCaractere == '='){
                            token = new OperadorMaiorIgualQue(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            br.skip(1);
                        } else
                            token = new OperadorMaiorQue(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }
                    case '<':{
                        if (proximoCaractere == '='){
                            token = new OperadorMenorIgualQue(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            br.skip(1);
                        } else
                            token = new OperadorMenorQue(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }
                    case '!':{
                        if (proximoCaractere == '='){
                            token = new OperadorDiferenca(Character.toString(caractereAtual)+proximoCaractere,
                                    linhaAtual, colunaAtual);
                            br.skip(1);
                        } else
                            token = new Invalido(Character.toString(caractereAtual),linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }
                    case '+':{
                        token = new OperadorSoma(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }                
                    case '-':{
                        if (Character.isDigit(proximoCaractere)){
                            isLiteralNumeroNegativo = true;
                        } else {
                            token = new OperadorSubtracao(caractereAtual, linhaAtual, colunaAtual);
                            addToken(token);
                        }
                        break;
                    }
                    case '/':{
                        token = new OperadorDivisao(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }
                    case '*':{
                        token = new OperadorMultiplicacao(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }
                    case '(':
                    case ')':
                    case '[':
                    case ']':
                    case ';':{
                        token = new Delimitador(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        break;
                    }
                    case '\'':{
                        token = new Delimitador(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        if (isLiteralChar){
                            Lexema lexema = new Lexema(lexemaTemporario, linhaAtual, colunaAtual);
                            int idSimbolo = addTabelaSimbolos(lexema);
                            token = new LiteralChar(idSimbolo, linhaAtual, colunaAtual);
                            addToken(token);
                            lexemaTemporario = "";
                        }                        
                        isLiteralChar = !isLiteralChar;
                        break;
                    }
                    case '"':{
                        token = new Delimitador(caractereAtual, linhaAtual, colunaAtual);
                        addToken(token);
                        if (isLiteralTexto){
                            Lexema lexema = new Lexema(lexemaTemporario, linhaAtual, colunaAtual);
                            int idSimbolo = addTabelaSimbolos(lexema);
                            token = new LiteralTexto(idSimbolo, linhaAtual, colunaAtual);
                            addToken(token);
                            lexemaTemporario = "";
                        }
                        isLiteralTexto = !isLiteralTexto;
                        break;
                    }
                    case ' ':
                    case '\t':
                    case '\n':
                    case '\r':{
                        break;
                    }
                    default: {
                        token = new Invalido(Character.toString(caractereAtual), linhaAtual, colunaAtual);
                        addToken(token);
                        errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Caractere " + caractereAtual + " invalido"));
                    }
                }
            }
            
            
            
            // se ficou algum literal sem fechar...
            if (isLiteralTexto){
                Token token = new Invalido(lexemaTemporario, linhaAtual, colunaAtual);
                errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Esperando \" após " + lexemaTemporario));
                addToken(token);
            } else if (isLiteralChar){
                Token token = new Invalido(lexemaTemporario, linhaAtual, colunaAtual);
                errosLexicos.add(new ErroLexico(linhaAtual, colunaAtual, "Esperando \' após " + lexemaTemporario));
                addToken(token);                
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo fonte não encontrado. " + e.getMessage());
        } catch (IOException e){
            System.out.println("Falha ao ler arquivo fonte. " + e.getMessage());
        }
        
        printTabelaSimbolos();
        printListaTokens();
        printListaErros();
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
    
    public static boolean isIdentificador(String texto){
        
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
    
    public static boolean isNumeroInteiro(String texto){
        if (texto.substring(0, 1).equals("-"))
            texto = texto.substring(1);
        
        for (char c : texto.toCharArray()){
            if (!Character.isDigit(c))
                return false;
        }        
        return true;
    }
    
    public static boolean isNumeroReal(String texto){
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
            if (i instanceof TokenSemSimbolo)
                System.out.println("<" + i.getTipo() + "," + ((TokenSemSimbolo)i).getLexema() 
                        + "> " + i.getLinha() + " - " + i.getColuna());
            if (i instanceof TokenComSimbolo)
                System.out.println("<" + i.getTipo() + "," + ((TokenComSimbolo)i).getIdSimbolo()
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
}
