import java.util.ArrayList;

class AlgoritmoGenetico{
    private static final int TAM_POPULACAO = 1000;
    private static final double TAXA_MIN_MUTACAO = 1;
    private static final int NUM_GERACOES = 2000;
    public static final int QTD_TORNEIO = 1;
    
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
            
            double random_double = Comuns.getRandomDouble();
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
            int random_pos = Comuns.getRandomInt(TAM_POPULACAO);
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
        
        ArrayList<Integer> uniao = Comuns.uniao(pai_x.getColunas(), pai_y.getColunas());
        Cromossomo filho = new Cromossomo(uniao);
        filho.eliminaRedundancia();
        return filho;
    }
    
    public void mutacao(Cromossomo C){
        double random_double = Comuns.getRandomDouble();
        int n = (int)(random_double * C.getColunas().size());
        for (int i = 0; i < n; i++) {
            int random_col = Comuns.getRandomInt(Main.linhasPorColuna.length);
            C.addColuna(random_col, Main.custos[random_col]);
        }
        C.eliminaRedundancia();
    }
    
    public Double taxaMutacao(Double custoMaisApto, Double custoMenosApto){
        double taxa = TAXA_MIN_MUTACAO;
        taxa = taxa / (1 - Math.exp((-custoMenosApto - custoMaisApto) / custoMenosApto));
        return taxa;
    }
}
