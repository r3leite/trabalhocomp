
package br.ufmt.compiladores.lexico;

import java.util.HashMap;
import java.util.Map;


public class Sintatico {
    
    private LexScanner scan;
    private String simbolo;
    private int tipo;
    private Map<String, Simbolo> tabelaSimbolos = new HashMap<>();
   
    
    public Sintatico(String arq){
        scan = new LexScanner(arq);
    }
    
    public void analise(){
        obtemSimbolo();
        programa();
        
        if (simbolo.equals("")){
            System.out.print("Cadeia verificada com sucesso!!");
        } else {
             throw new RuntimeException ("Erro Sintático, esperado fim de cadeia");
        }
    
    }
    
    private void obtemSimbolo() {
        Token token = scan.nextToken();
        simbolo = "";
        if (token != null ) {
         simbolo = token.getTermo();
         tipo = token.getTipo();
         System.out.println(simbolo);
        }
       
    }
    
    private void programa(){
        if (simbolo.equals("program")){
            obtemSimbolo();
            if (tipo == Token.IDENT){
                obtemSimbolo();
                corpo();
                if (simbolo.equals(".")){
                    obtemSimbolo();
                } else {
                    throw new RuntimeException("Erro sintático, esperado '.'");
                }
            } else {
               throw new RuntimeException ("Erro Sintático, esperado identificador");
            }
           
        
        
        
        
        } else {
               throw new RuntimeException ("Erro Sintático, esperado program");
           } 
    }
    
    private void corpo(){
        dc();
        if (simbolo.equals("begin")){
            obtemSimbolo();
            comandos();
            if (simbolo.equals("end")){
                obtemSimbolo();
            } else {
               throw new RuntimeException ("Erro Sintático, esperado end");
           }
        } else {
               throw new RuntimeException ("Erro Sintático, esperado begin");
           }
            
    
    }
    
    private void dc(){
        if(simbolo.equals("real") || simbolo.equals("integer")){
             dc_v();
             mais_dc();
             
          }
    }    
    
    private void mais_dc(){
        if(simbolo.equals(";")){
            obtemSimbolo();
            dc();
        }  
    }
            
    
    
    private void dc_v(){
        tipo_var();
        if (simbolo.equals(":")){
            obtemSimbolo();
            variaveis();
        
        } else {
            throw new RuntimeException ("Erro Sintático, esperado ':' ");
          }
    }
    
    private void tipo_var(){
        
        if (simbolo.equals("real")){
            obtemSimbolo();
            
        } else if (simbolo.equals("integer")){
            obtemSimbolo();
        } 
       

    }
    
    private void variaveis(){
        if (tipo != Token.IDENT){
           throw new RuntimeException ("Erro Sintático, esperado identificador"); 
        } 
        
        if(tabelaSimbolos.containsKey(simbolo)){
            throw new RuntimeException("Erro semantico, esperado "+simbolo );
        } else {
        
            tabelaSimbolos.put(simbolo, new Simbolo(this.tipo, simbolo));
        }
            
        obtemSimbolo();
        mais_var();
    }
    
    private void mais_var(){
        if (simbolo != null){
            if (simbolo.equals(",")){
                obtemSimbolo();
                variaveis();
            } 
        
        } 
    }
    
    private void comandos(){
         comando();
         mais_comandos();
        
     }
    
    private void mais_comandos(){
        if (simbolo.equals(";")){
            obtemSimbolo();
            comandos();
        } 
        
     }
        
    
    
    private void comando(){
        if (simbolo.equals("read")){
          obtemSimbolo();
          if (simbolo.equals("(")){
            obtemSimbolo();
            if (tipo == Token.IDENT){
              if(tabelaSimbolos.containsKey(simbolo)){
                
                obtemSimbolo();
                if (simbolo.equals(")")){
                  obtemSimbolo();
                } 
              } else {
                  throw new RuntimeException ("Erro semantico, o identificador nao foi declarado");
              }
            
           }
         }
        } else if (simbolo.equals("write")){
              obtemSimbolo();
              if (simbolo.equals("(")){
                obtemSimbolo();
                if (tipo == Token.IDENT){
                  if(tabelaSimbolos.containsKey(simbolo)){
                    
                   obtemSimbolo();
                   if (simbolo.equals(")")){
                    obtemSimbolo();
                   }
                 } else {
                      throw new RuntimeException("Erro semantico, o identificador nao foi declarado");
                   }
                }
              }
         } else if (tipo == Token.IDENT) {
             if(tabelaSimbolos.containsKey(simbolo)){
              obtemSimbolo();
              if (simbolo.equals(":=")){
                 obtemSimbolo();
                
                 expressao();
                 
              }
             } else {
                 throw new RuntimeException("Erro semantico, o identificador nao foi declarado");
               }
             
         
            } else if (simbolo.equals("if")){
               obtemSimbolo();
               condicao();
               if (simbolo.equals("then")){
                 obtemSimbolo();
                 comandos();
                 pfalsa();
                 if (simbolo.equals("$")){
                     obtemSimbolo();
                 }
               }
              } else {
                  throw new RuntimeException("Erro sintático"); 
                }
        }    
    
    private void condicao(){
        expressao();
        relacao();
        expressao();
    }
    
    private void relacao(){
        if (tipo == Token.RELACAO){
            obtemSimbolo();
        } 
    }
    
    private void expressao(){
        termo();
        outros_termos();
    }
    
    private void termo(){
        op_un();
        fator();
        mais_fatores();
        
    }
    
    private void op_un(){
        if (simbolo.equals("-")){
            obtemSimbolo();
          } 
    }
    
    
    private void fator(){
        if ( tipo == Token.IDENT){
            if(tabelaSimbolos.containsKey(simbolo)){
            obtemSimbolo();
            } else {
                throw new RuntimeException("Erro semantico, o identificador nao foi declarado");
              }
        } else if (tipo == Token.NUMERO_INTEIRO){
            obtemSimbolo();
        } else if (tipo == Token.NUMERO_REAL){
            obtemSimbolo();
        } else if (simbolo.equals("(")){
            obtemSimbolo();
            expressao();
            if (simbolo.equals(")")){
                obtemSimbolo();
            }
            
            
        } 
        
        
    }
        
        
       
    private void outros_termos(){
       if (simbolo.equals("+") || simbolo.equals("-")){
            op_ad();
            termo();
            outros_termos();
        
        }   
    }
    
    private void op_ad(){
        if (simbolo.equals("+") || simbolo.equals("-")){
            obtemSimbolo();
        }
    }
    
    private void mais_fatores(){
      if (simbolo.equals("*") || simbolo.equals("/")){
          op_mul();
          fator();
          mais_fatores();
        }
        
     }   
      
    
    private void op_mul(){
        if (simbolo.equals("*") || simbolo.equals("/")){
            obtemSimbolo();
        }
    }
    
    private void pfalsa(){
     
         if (simbolo.equals("else")){
             obtemSimbolo();
             comandos();
         
         } 
    } 
    
        
}
    

