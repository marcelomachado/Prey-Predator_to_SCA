
import java.util.Objects;
import java.util.Vector;

/**
 *
 * @author gtbavi
 */
public class GeneralTests {

    /**
     * @param args the command line arguments
     */
    

    public static void main(String[] args) {
        if (Objects.equals(Math.round(25.211d), Math.round(25.21d))) {
            System.out.println("Igual");
        }
        for (int i = 0; i < 128; i++) {
            System.out.println(Integer.toBinaryString(i));
        }

        //printBin("", 4);
        binaryVectorPermutation(vector,vector.length);
    }

    public static void printBin(String soFar, int iterations) {
        if (iterations == 0) {
            for (int i = 0; i < soFar.length(); i++) {
                vector[i] = Character.getNumericValue(soFar.charAt(i));
            }
            for (int i = 0; i < soFar.length(); i++) {
                System.out.print(vector[i]);
            }
            System.out.println();
        } else {
            printBin(soFar + "0", iterations - 1);
            printBin(soFar + "1", iterations - 1);
        }

    }

    

}
