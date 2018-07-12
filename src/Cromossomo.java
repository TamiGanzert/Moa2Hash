import java.util.ArrayList;


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

