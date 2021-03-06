import java.util.ArrayList;

class Cromossomo{
    private ArrayList<Integer> colunas;
    private double custoTotal;
    private int[] qtdColunaCobreLinha;

    public Cromossomo() {
        colunas = new ArrayList<>();
        custoTotal = 0;
    }
    
    public Cromossomo(ArrayList<Integer> colunas){
        this();
        qtdColunaCobreLinha = new int[Main.colunaPorLinhas.length];
        colunas.forEach((coluna) -> {
            addColuna(coluna, Main.custos[coluna]);
        });
    }
    
    public void addColuna(int coluna, double custo){
        if (colunas.contains(coluna)){
            return;
        }
        colunas.add(coluna);
        custoTotal += custo;
        Main.linhasPorColuna[coluna].forEach((linha) -> {
            qtdColunaCobreLinha[linha]++;
        });
    }
    
    public void removeColuna(int coluna){
        colunas.remove(new Integer(coluna));
        custoTotal = custoTotal - Main.custos[coluna];
    }

    public ArrayList<Integer> getColunas() {
        return colunas;
    }

    public double getCustoTotal() {
        return custoTotal;
    }
    
    public void gerarIndividuo(){
        ArrayList<Integer> linhasDescobertas = new ArrayList<>();
        for (int i = 0; i < Main.numLinhas; i++) {
            linhasDescobertas.add(i);
        }
        
        qtdColunaCobreLinha = new int[Main.colunaPorLinhas.length];
        
        while (!linhasDescobertas.isEmpty()){
            int random_pos = Comuns.getRandomInt(linhasDescobertas.size());
            int linha = linhasDescobertas.get(random_pos);
            
            ArrayList<Integer> conjuntoColuna = Main.colunaPorLinhas[linha];
            int menorColuna = minCusto(conjuntoColuna, linhasDescobertas);
            
            this.addColuna(menorColuna, Main.custos[menorColuna]);
            linhasDescobertas.removeAll(Main.linhasPorColuna[menorColuna]);
        }
    }
    
    private static int minCusto(ArrayList<Integer> conjuntoColuna, ArrayList<Integer> linhasDescobertas){
        double menor = Double.MAX_VALUE;
        int menorColuna = -1;
        for (int i = 0; i < conjuntoColuna.size(); i++) {
            int coluna = conjuntoColuna.get(i);
            double custo = Main.custos[coluna];
            int intersecao_size = Comuns.intersecao(linhasDescobertas, Main.linhasPorColuna[coluna]).size();
            if ((custo / intersecao_size) < menor) {
                menor = custo / intersecao_size;
                menorColuna = coluna;
            }
        }
        return menorColuna;
    }
    
    public void eliminaRedundancia(){
        ArrayList<Integer> T = new ArrayList<>(this.colunas);
        while (!T.isEmpty()){
            int random_pos = Comuns.getRandomInt(T.size());
            int coluna = T.get(random_pos);
            T.remove(random_pos);
            
            if (isRedundante(Main.linhasPorColuna[coluna])){
                removeColuna(coluna);
                
                Main.linhasPorColuna[coluna].forEach((linha) -> {
                    qtdColunaCobreLinha[linha]--;
                });
            }
        }
    }
    
    private boolean isRedundante(ArrayList<Integer> conjuntoLinha){
        return conjuntoLinha.stream().noneMatch((linha) -> (qtdColunaCobreLinha[linha] < 2));
    }    
}

