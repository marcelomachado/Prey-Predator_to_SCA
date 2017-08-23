package ppatosca;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gtbavi
 */
public class Util {

    public static <K, V extends Comparable<? super V>> Map<K, V>
            sortByValueDesc(Map<K, V> map) {
        List<Map.Entry<K, V>> list
                = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
            
    public static <K, V extends Comparable<? super V>> Map<K, V>
            sortByValueAsc(Map<K, V> map) {
        List<Map.Entry<K, V>> list
                = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
            
    public static Double similarity(Individual x, Individual y) {
        int div = 0;
        for (int i = 0; i < x.getSize(); i++) {
            div += x.getPrey()[i] * y.getPrey()[i];
        }
        return div / (norm(x) * norm(y));
    }

    public static Double norm(Individual x) {
        Double norm = 0d;
        for (int i = 0; i < x.getSize(); i++) {
            norm += Math.pow(x.getPrey()[i], 2);
        }
        return Math.sqrt(norm);
    }
    
    public static void printPrey(int[] prey){
        for(int i=0;i<prey.length;i++){
            System.out.print(prey[i]+" ");
        }
        System.out.println("");
    }

}
