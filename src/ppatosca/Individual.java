package ppatosca;

import java.util.Random;

/**
 *
 * @author gtbavi
 */
public class Individual {
    private int size;
    private int id;
    private int[] prey;
    private Double survival_value;

    public Individual(int size, int id) {
        this.size = size;
        this.id = id;
        this.prey = new int[size];
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }   

    public int[] getPrey() {
        return prey;
    }

    public void setPrey(int[] prey) {
        this.prey = prey;
    }

    public Double getSurvival_value() {
        return survival_value;
    }

    public void setSurvival_value(Double survival_value) {
        this.survival_value = survival_value;
    }    
    
    public void generateRandomIndividual(){
        //int[] individual = new int[size];
        Random random = new Random();
        for(int i =0; i<size;i++){
            this.prey[i] = random.nextInt(2);
        }

    }
    
    
}
