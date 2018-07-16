import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;



class Main {
    private static List<Integer> linhasPorColuna = new ArrayList<Integer>();
    public static HashMap<Integer,List<Integer>> colunas = new HashMap<Integer,List<Integer>>();
    public static HashMap<Integer,Double> custos = new HashMap<Integer,Double>();
    public static List<Integer> vezesLinhaCoberta = new ArrayList<Integer>(); //Conta quantas vezes aquela linha foi coberta
                                                                              //Ainda não implemnetado, não sei como fazer
    private static AlgoritmoGenetico algGen;
    public static int numLinhas = 0;
    public static int numColunas = 0;
    
    int initialSize = 50;
    double loadFactor = 0.75;    
    double sizeToRehash = initialSize * loadFactor;
    
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
        
        for (int i = 0; i < numColunas; i++) {
            leLinha = buffer.readLine();
            String infoslinha[] = leLinha.split("\\s+");            
            double custo = Double.parseDouble(infoslinha[2]);
            
            linhasPorColuna = new ArrayList<Integer>();
            for (int j = 3; j < infoslinha.length; j++) {                
                leCustos(i, custo);
                linhasPorColuna.add(Integer.parseInt(infoslinha[j]));
            }
            
            colunas.put(i, linhasPorColuna);
        }
    }   
    
    public static void leCustos(int coluna, double custo){
        custos.put(coluna, custo);
    }        
    
    public static void contaVezesLinhaDescoberta(){
        Integer contador;
        colunas.keySet().stream().forEach((linha) -> {
            List<Integer> coluna = colunas.get(linha);
            System.out.println(coluna);
        });
    }
}
