package br.ufmt.compiladores.lexico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LexScanner {

  private char[] conteudo;
  private int estado;
  private int pos;
  
  private String palavras [] = {"begin", "end", "program", "real", "integer", "write", "if", "read", 
  "else", "then"};

  public LexScanner(String arq) {
    try {
      byte[] bytes = Files.readAllBytes(Paths.get(arq));
      conteudo = (new String(bytes)).toCharArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Token nextToken() {
    if (isEOF()) {
      return null;
    }
    estado = 0;
    char c;
    Token token = null;
    String termo = "";
    while (true) {
      if(isEOF()){
        pos = conteudo.length + 1;
      }
        
      c = nextChar();
      switch (estado) {
        case 0:
          if (isLetra(c)) {
            termo += c;
            estado = 3;
          } else if (isDigito(c)) {
            termo += c;
            estado = 1;
          } else if (isEspaco(c)) {
            estado = 0;
          } else if (isRelacao(c)) {
            termo += c;  
            estado = 5;
          } else if (isOperador(c)) {
            termo += c;
            estado = 4;
          } else if(c == 0){
              return null;
          } else {
            throw new RuntimeException("Token não reconhecido!");
          }
          break;
        case 1:
          if (isDigito(c)) {
            termo += c;  
            estado = 1;
          } else if (c == '.') {
            estado = 2;
          } else {
              token = new Token();
              token.setTipo(Token.NUMERO_INTEIRO);
              token.setTermo(termo);
              back();
              return token;
          }
          break;
        case 2:
          if (isDigito(c)) {
            termo += c;
            estado = 2;
            
          } else {
              token = new Token();
              token.setTipo(Token.NUMERO_REAL);
              token.setTermo(termo);
              back();
              return token;
          }
          break;
        case 3:
            if (isLetra(c) || isDigito(c)) {
            termo += c;
            estado = 3;
            } else { 
                int v = 0;
                for(int i=0; i<10;i++){
                    if(termo.equals(palavras[i]))
                        v = 1;
                }
                
                token = new Token();
                
                if (v == 0)
                    token.setTipo(Token.IDENT);
                if (v == 1)
                    token.setTipo(Token.PALAVRA_RESERVADA);
                
                token.setTermo(termo);
                back();
                return token;
            }
            break;
            
              
          
        case 4:
           token = new Token();
           token.setTipo(Token.OPERADOR);
           token.setTermo(termo);
           back();
           return token;
        
        case 5:
            if (isRelacao(c)){
                termo += c;
                estado = 6;
            } else {
            token = new Token();
            token.setTipo(Token.RELACAO);
            token.setTermo(termo);
            back();
            return token;
            }
            break;
        case 6:
            token = new Token();
            token.setTipo(Token.RELACAO);
            token.setTermo(termo);
            back();
            return token;
            
      }
    }
  }

  private boolean isLetra(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  private boolean isDigito(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isEspaco(char c) {
    return c == ' ' || c == '\n' || c == '\t';
  }
  
  private boolean isRelacao(char c){
    return c == ':' || c == '<' || c == '>' || c == '=';
  }
  private boolean isOperador(char c){
    return  c == '+' || c == '-' || c == '*' || c == '/' || c == ';' || c == '(' || c == ')' || c == ',' || c == '.' || c == '$';
  }
  private boolean isEOF() {
    return pos >= conteudo.length;
  }

  private char nextChar() {
    if(isEOF()){
      return 0;
    }
    return conteudo[pos++];
  }

  private void back() {
      
      pos--;
  }
}
