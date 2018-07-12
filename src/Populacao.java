/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tamin
 */

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
