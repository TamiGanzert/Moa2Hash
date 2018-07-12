import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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