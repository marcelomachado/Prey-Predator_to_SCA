package ppatosca;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author gtbavi
 */
public class Util {

    static SingletonPrint printer = SingletonPrint.getInstance();

    public static <K, V extends Comparable<? super V>> Map<K, V>
            sortByValueDesc(Map<K, V> map) {
        List<Map.Entry<K, V>> list
                = new LinkedList<>(map.entrySet());
        Collections.sort(list, (Map.Entry<K, V> o1, Map.Entry<K, V> o2) -> (o2.getValue()).compareTo(o1.getValue()));

        Map<K, V> result = new LinkedHashMap<>();
        list.stream().forEach((entry) -> {
            result.put(entry.getKey(), entry.getValue());
        });
        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
            sortByValueAsc(Map<K, V> map) {
        List<Map.Entry<K, V>> list
                = new LinkedList<>(map.entrySet());
        Collections.sort(list, (Map.Entry<K, V> o1, Map.Entry<K, V> o2) -> (o1.getValue()).compareTo(o2.getValue()));

        Map<K, V> result = new LinkedHashMap<>();
        list.stream().forEach((entry) -> {
            result.put(entry.getKey(), entry.getValue());
        });
        return result;
    }

    public static Double cosineSimilarity(Individual x, Individual y) {
        int div = 0;
        for (int i = 0; i < x.getSize(); i++) {
            div += x.getPrey()[i] * y.getPrey()[i];
        }
        double norm = (norm(x) * norm(y));
        return norm == 0 ? div / Double.MAX_VALUE : div / norm;
    }

    public static Double cosineSimilarity(int[] x, int[] y) {
        int div = 0;
        for (int i = 0; i < x.length; i++) {
            div += x[i] * y[i];
        }
        double norm = (norm(x) * norm(y));
        return norm == 0 ? div / Double.MAX_VALUE : div / norm;
    }

    public static int hammingDistance(int[] x, int[] y) {
        int distance = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] != y[i]) {
                distance++;
            }
        }
        return distance;
    }

    public static int hammingDistance(Individual x, Individual y) {
        int distance = 0;
        for (int i = 0; i < x.getSize(); i++) {
            if (x.getPrey()[i] != y.getPrey()[i]) {
                distance++;
            }
        }
        return distance;
    }

    public static Double norm(Individual x) {
        Double norm = 0d;
        for (int i = 0; i < x.getSize(); i++) {
            norm += Math.pow(x.getPrey()[i], 2);
        }
        return Math.sqrt(norm);
    }

    public static Double norm(int[] x) {
        Double norm = 0d;
        for (int i = 0; i < x.length; i++) {
            norm += Math.pow(x[i], 2);
        }
        return Math.sqrt(norm);
    }

    public static void printPrey(int[] prey) {
        for (int i = 0; i < prey.length; i++) {
            printer.addString(prey[i] + " ");
        }
        printer.addString("\n");
    }

    public static String preyToString(int[] prey) {
        String returned = "";
        for (int i = 0; i < prey.length; i++) {
            returned += " " + prey[i];
        }
        return returned;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        if (value == Double.POSITIVE_INFINITY) {
            return Double.MAX_VALUE;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<Integer> initializeShuffleList(int individualSize) {
        List<Integer> individualSizeList = new ArrayList<>();
        for (int i = 0; i < individualSize; i++) {
            individualSizeList.add(i);
        }
        return individualSizeList;
    }

    public static String diff(int[] vet1, int[] vet2) {
        String diff = "";
        for (int i = 0; i < vet1.length; i++) {
            if (vet1[i] != vet2[i]) {
                diff += "x ";
            } else {
                diff += "- ";
            }
        }
        return diff;
    }

    public static int[] generateComplementaryVector(int[] vector) {
        int[] complementaryVector = new int[vector.length];
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] == 0) {
                complementaryVector[i] = 1;
            } else {
                complementaryVector[i] = 0;
            }
        }
        return complementaryVector;
    }

    public static Element readXMLFile(File file) throws ParserConfigurationException, SAXException, IOException {
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        return doc.getDocumentElement();
    }

}
