package analyzes;

import java.util.ArrayList;
/**
 *
 * @author gtbavi
 */
public class BinaryPermutation{
    private int[] vector;
    private ArrayList<int[]> binaryNumber = new ArrayList<>();

    public BinaryPermutation(int size) {
        vector = new int[size];

    }    
    public int[] getVector() {
        return vector;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }    
    public ArrayList<int[]> binaryVectorPermutation(int iterations) {
        if (iterations == 0) {
            binaryNumber.add(vector.clone());
        } else {
            vector[iterations - 1] = 0;
            binaryVectorPermutation(iterations - 1);
            vector[iterations - 1] = 1;
            binaryVectorPermutation(iterations - 1);
            
        }
        return binaryNumber;
    }
}
