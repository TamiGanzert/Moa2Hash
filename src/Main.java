import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

class Main {

    private static HashMap<Integer,Integer> linhas = new HashMap<Integer,Integer>();
    private static HashMap<Integer,Double> custos = new HashMap<Integer,Double>();
    
    private static int numLinhas = 0;
    private static int numColunas = 0;
    
    int initialSize = 50;
    double loadFactor = 0.75;    
    double sizeToRehash = initialSize * loadFactor;
    
    public static void main(String[] args) throws IOException {
        Scanner ler = new Scanner(System.in);
        String nomeArquivo;
        nomeArquivo = ler.next();
        lerArquivo(nomeArquivo);   
    }

    private static void lerArquivo(String nomeArquivo) throws IOException {
       FileInputStream stream = new FileInputStream(nomeArquivo);
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        String leLinha = buffer.readLine();
        
        //Lê numero de linhas
        String leNumLinha = leLinha.substring(leLinha.indexOf(" ")).trim();
        numLinhas = Integer.parseInt(leNumLinha);
        leLinha = buffer.readLine();
        
        //Lê numero de colunas
        String leNumColum = leLinha.substring(leLinha.indexOf(" ")).trim();
        numColunas = Integer.parseInt(leNumColum);                
        buffer.readLine();
        
        for (int i = 0; i < numColunas; i++) {
            leLinha = buffer.readLine();
            String infoslinha[] = leLinha.split("\\s+");            
            double custo = Double.parseDouble(infoslinha[2]);
            
            for (int j = 3; j < infoslinha.length; j++) {                
                leLinhaPorColuna(i, custo, Integer.parseInt(infoslinha[j]));
            }
        }
    }
    
    public static void leLinhaPorColuna(int coluna, double custo, int linha){
        linhas.put(linha, coluna);
        custos.put(coluna, custo);
    }        
}
