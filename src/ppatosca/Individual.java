package ppatosca;

import java.util.Random;

/**
 *
 * @author gtbavi
 */
public class Individual {
    
    private int size;
    private int quantity;

    public Individual() {
    }

    public Individual(int quantity,int size) {
        this.quantity = quantity;
        this.size = size;
        
    }
    

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
   
    private int[] generateRandomIndividual(){
        int[] individual = new int[size];
        Random random = new Random();
        for(int i =0; i<size;i++){
            individual[i] = random.nextInt(2);
        }
        return individual;
    }
    
    public int[][] generatePopulation(){
        int[][] population = new int[quantity][];
        
        for(int i=0; i< quantity; i++){
            population[i] = generateRandomIndividual();
        }
        
        return population;
    }
    
    
}
