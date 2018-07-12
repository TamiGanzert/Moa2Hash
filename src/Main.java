import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

class Main {
    public static HashMap<Integer,Integer> linhas = new HashMap<Integer,Integer>();
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
            
            for (int j = 3; j < infoslinha.length; j++) {                
                leLinhaPorColuna(i, custo, Integer.parseInt(infoslinha[j]));
                vezesLinhaCoberta.set(Integer.parseInt(infoslinha[j]), vezesLinhaCoberta.get(Integer.parseInt(infoslinha[j])) + 1);
            }
        }
    }
    
    public static void leLinhaPorColuna(int coluna, double custo, int linha){
        linhas.put(linha, coluna);
        custos.put(coluna, custo);
    }        
}

class AlgoritmoGenetico{
    private static final int TAM_POPULACAO = 1000;
    private static final double TAXA_MIN_MUTACAO = 0.1;
    private static final int NUM_GERACOES = 1000;
    
    private final Populacao populacao;
    
    public AlgoritmoGenetico(){
        populacao = new Populacao(TAM_POPULACAO);
    }
    
    public void executar(){
        populacao.gerarPopulacaoInicial();
        int i = 0;
        while (i < NUM_GERACOES){
            Cromossomo filho = crossover();
            
            Cromossomo maisApto = populacao.maisApto();
            Cromossomo menosApto = populacao.menosApto();
            
            double random_double = Util.getRandomDouble();
            if (random_double < taxaMutacao(maisApto.getCustoTotal(), menosApto.getCustoTotal())){
                mutacao(filho);
            }
            
            if (filho.getCustoTotal() < menosApto.getCustoTotal()){
                populacao.atualizar(filho);
                i = 0;
            }
            else {
                i++;
            }
        }
        System.out.println("MELHOR SOLUÇÂO:");
        System.out.println("COLUNAS: "+populacao.maisApto().getColunas());
        System.out.println("CUSTO: "+populacao.maisApto().getCustoTotal());
        
    }
    
    // Seleção de individuos por torneio
    public Cromossomo selecao(){
        Cromossomo c = null;
        for (int i = 0; i < QTD_TORNEIO; i++) {
            int random_pos = Util.getRandomInt(TAM_POPULACAO);
            Cromossomo rand = populacao.getPopulacao()[random_pos];
            if (c == null || rand.getCustoTotal() < c.getCustoTotal()){
                c = rand;
            }
        }
        return c;
    }
    
    public Cromossomo crossover(){
        Cromossomo pai_x = selecao();
        Cromossomo pai_y = selecao();
        
        while (pai_x == pai_y){
            pai_y = selecao();
        }
        
        ArrayList<Integer> uniao = Util.uniao(pai_x.getColunas(), pai_y.getColunas());
        Cromossomo filho = new Cromossomo(uniao, listaColuna, listaLinha, listaCusto);
        filho.eliminaRedundancia(listaColuna, listaCusto);
        return filho;
    }
    
    public void mutacao(Cromossomo C){
        double random_double = Util.getRandomDouble();
        int n = (int)(random_double * C.getColunas().size());
        for (int i = 0; i < n; i++) {
            int random_col = Util.getRandomInt(listaColuna.length);
            C.addColuna(random_col, listaCusto[random_col], listaColuna);
        }
        C.eliminaRedundancia(listaColuna, listaCusto);
    }
    
    public Double taxaMutacao(Double custoMaisApto, Double custoMenosApto){
        double taxa = TAXA_MIN_MUTACAO;
        taxa = taxa / (1 - Math.exp((-custoMenosApto - custoMaisApto) / custoMenosApto));
        return taxa;
    }
}

class Populacao{
    private final int tam_populacao;
    private final Cromossomo[] populacao;    
    private int posMaisApto;
    private int posMenosApto;
    
    public Populacao(int tam_populacao) {
        this.tam_populacao = tam_populacao;
        populacao = new Cromossomo[tam_populacao];
        posMaisApto = -1;
        posMenosApto = -1;
    }
    
    public Cromossomo[] getPopulacao() {
        return populacao;
    }
    
    public void gerarPopulacaoInicial(){
        for (int i = 0; i < tam_populacao; i++) {
            Cromossomo c = new Cromossomo();
            
            //c.gerarIndividuo(listaLinha, listaColuna, listaCusto);
            //c.eliminaRedundancia(listaColuna, listaCusto);
            populacao[i] = c;
            classifica(i);
        }
    }
    
     private void classifica(int index){
        Cromossomo c = populacao[index];
        if (posMaisApto == -1 || c.getCustoTotal() < populacao[posMaisApto].getCustoTotal()){
            posMaisApto = index;
        }
        if (posMenosApto == -1 || c.getCustoTotal() > populacao[posMenosApto].getCustoTotal()){
            posMenosApto = index;
        }
    }
    
    public void atualizar(Cromossomo novo){
        populacao[posMenosApto] = novo;
        for (int i = 0; i < populacao.length; i++) {
            classifica(i);
        }
    }
    
    public Cromossomo maisApto(){
        return populacao[posMaisApto];
    }
    
    public Cromossomo menosApto(){
        return populacao[posMenosApto];
    }
}

class Cromossomo{
     //Lista de indices das colunas que pertencem a solução
    private ArrayList<Integer> colunas;
    private double custoTotal;
    private int[] qtdColunaCobreLinha;

    public Cromossomo() {
        colunas = new ArrayList<>();
        custoTotal = 0;
    }
    
    public Cromossomo(ArrayList<Integer> colunas, ArrayList<Integer>[] listaColuna, ArrayList<Integer>[] listaLinha, Double[] listaCusto){
        this();
        qtdColunaCobreLinha = new int[listaLinha.length];
        for (Integer coluna : colunas) {
            addColuna(coluna, listaCusto[coluna], listaColuna);
        }
    }
    
    public void addColuna(int coluna, double custo, ArrayList<Integer>[] listaColuna){
        if (colunas.contains(coluna)){
            return;
        }
        colunas.add(coluna);
        custoTotal += custo;
        for (Integer linha : listaColuna[coluna]) {
            qtdColunaCobreLinha[linha]++;
        }
    }
    
    public void removeColuna(int coluna, Double[] listaCusto){
        colunas.remove(new Integer(coluna));
        custoTotal = custoTotal - listaCusto[coluna];
    }

    public ArrayList<Integer> getColunas() {
        return colunas;
    }

    public double getCustoTotal() {
        return custoTotal;
    }
    
    public void gerarIndividuo(){
        ArrayList<Integer> linhasDescobertas = new ArrayList<>();
        for (int i = 0; i < listaLinha.length; i++) {
            linhasDescobertas.add(i);
        }
        
        qtdColunaCobreLinha = new int[listaLinha.length];
        
        while (!linhasDescobertas.isEmpty()){
            int random_pos = Util.getRandomInt(linhasDescobertas.size());
            int linha = linhasDescobertas.get(random_pos);
            
            ArrayList<Integer> conjuntoColuna = listaLinha[linha];
            int menorColuna = colunaMinimizaCusto(conjuntoColuna, linhasDescobertas, listaColuna, listaCusto);
            
            this.addColuna(menorColuna, listaCusto[menorColuna], listaColuna);
            linhasDescobertas.removeAll(listaColuna[menorColuna]);
        }
    }
    
    private static int colunaMinimizaCusto(ArrayList<Integer> conjuntoColuna, ArrayList<Integer> linhasDescobertas, ArrayList<Integer>[] listaColuna, Double[] listaCusto){
        double menor = Double.MAX_VALUE;
        int menorColuna = -1;
        for (int i = 0; i < conjuntoColuna.size(); i++) {
            int coluna = conjuntoColuna.get(i);
            double custo = listaCusto[coluna];
            int intersecao_size = Util.intersecao(linhasDescobertas, listaColuna[coluna]).size();
            if ((custo / intersecao_size) < menor) {
                menor = custo / intersecao_size;
                menorColuna = coluna;
            }
        }
        return menorColuna;
    }
    
    public void eliminaRedundancia(ArrayList<Integer>[] listaColuna, Double[] listaCusto){
        ArrayList<Integer> T = new ArrayList<>(this.colunas);
        while (!T.isEmpty()){
            int random_pos = Util.getRandomInt(T.size());
            int coluna = T.get(random_pos);
            T.remove(random_pos);
            
            if (isRedundante(listaColuna[coluna])){
                removeColuna(coluna, listaCusto);
                
                for (Integer linha : listaColuna[coluna]) {
                    qtdColunaCobreLinha[linha]--;
                }
            }
        }
    }
    
    private boolean isRedundante(ArrayList<Integer> conjuntoLinha){
        for (Integer linha : conjuntoLinha) {
            if (qtdColunaCobreLinha[linha] < 2){
                return false;
            }
        }
        return true;
    }    
}

class Utils{
    private static final Random random = new Random();
    
    public static int getRandomInt(int n){
        return random.nextInt(n);
    }
    
    public static Double getRandomDouble(){
        return random.nextDouble();
    }
    
    public static ArrayList<Integer> intersecao(ArrayList<Integer> lista1, ArrayList<Integer> lista2){
        ArrayList<Integer> intersec = new ArrayList<>();
        for (Integer l1 : lista1) {
            if(lista2.contains(l1)){
                intersec.add(l1);
            }
        }
        return intersec;
    }
    
    public static ArrayList<Integer> uniao(ArrayList<Integer> lista1, ArrayList<Integer> lista2){
        Set<Integer> set = new HashSet<>();

        set.addAll(lista1);
        set.addAll(lista2);

        return new ArrayList<Integer>(set);
    }
    
}
