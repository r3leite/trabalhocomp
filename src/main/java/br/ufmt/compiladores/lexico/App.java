package br.ufmt.compiladores.lexico;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
    /** Analisador Léxico
      LexScanner scan = new LexScanner("input.txt");
      Token token = null;
      do {
      token = scan.nextToken();
      System.out.println(token);
    } while (token != null);
     */
    
    
    Sintatico sintatico = new Sintatico("input.txt");
    sintatico.analise();
      
  }    
}
