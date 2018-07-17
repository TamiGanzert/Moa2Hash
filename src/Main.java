import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static ArrayList<Integer>[] linhasPorColuna;
    public static ArrayList<Integer>[] colunaPorLinhas;
    public static Double[] custos;
        
    private static AlgoritmoGenetico algGen;
    public static int numLinhas = 0;
    public static int numColunas = 0;
    
    public static void main(String[] args) throws IOException {
        Scanner ler = new Scanner(System.in);
        String nomeArquivo;
        nomeArquivo = ler.next();
        lerArquivo(nomeArquivo); 
        algGen.executar();
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
        
        algGen = new AlgoritmoGenetico();
        linhasPorColuna = new ArrayList[numColunas];
        colunaPorLinhas = new ArrayList[numLinhas];
        custos = new Double[numColunas];
        
        for (int i = 0; i < numColunas; i++) {
            leLinha = buffer.readLine();
            String infoslinha[] = leLinha.split("\\s+");            
            double custo = Double.parseDouble(infoslinha[2]);
            custos[i] = custo;                   
            
            for (int j = 3; j < infoslinha.length; j++) {
                preencheListas(i, Integer.parseInt(infoslinha[j]));
            }
        }
    }   
    
    public static void preencheListas(int coluna, int linha){
        linha--;
        if (linhasPorColuna[coluna] == null){
            linhasPorColuna[coluna] = new ArrayList<>();
        }
        
        if (colunaPorLinhas[linha] == null){
            colunaPorLinhas[linha] = new ArrayList<>();
        }
        
        linhasPorColuna[coluna].add(linha);
        colunaPorLinhas[linha].add(coluna);
    }
    
}
