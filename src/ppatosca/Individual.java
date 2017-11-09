package ppatosca;

import java.util.Random;

/**
 *
 * @author gtbavi
 */
public class Individual implements Cloneable {

    private int size;
    private int id;
    private int[] prey;
    private Double survivalValue;

    public Individual(int id, int size) {
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

    public Double getSurvivalValue() {
        return survivalValue;
    }

    public void setSurvivalValue(Double survival_value) {
        this.survivalValue = survival_value;
    }

    public void generateRandomPrey() {
        prey = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            prey[i] = random.nextInt(2);
        }
    }

    @Override
    protected Individual clone() throws CloneNotSupportedException {
        return (Individual) super.clone();
    }
    
    @Override
    public String toString(){
        String returned="";
        for(int i =0; i< size;i++){
            returned+=prey[i]+" ";
        }
        return returned+="\nSurvival value: "+survivalValue+"\n";
    }

}
