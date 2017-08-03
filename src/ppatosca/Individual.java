package ppatosca;

import java.util.Random;

/**
 *
 * @author gtbavi
 */
public class Individual {
    
    private short[] individual;

    public Individual() {
    }

    public short[] getIndividual() {
        return individual;
    }

    public void setIndividual(short[] individual) {
        this.individual = individual;
    }
    
    public void generateRandomIndividual(int size){
        individual = new short[size];
        Random random = new Random();
        for(int i =0; i<size;i++){
            individual[i] =  (short)random.nextInt(2);
        }                
    }
    
}
