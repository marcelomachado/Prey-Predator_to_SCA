package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author gtbavi
 */
public class Population {
    
    private int quantity;
    private int best_prey_id;
    private int predator_id; // Worst prey
    private int[] ordinary_preys_ids;
    private ArrayList<Individual> individuals;

    public Population(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }        
    

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }           
    

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBest_prey_id() {
        return best_prey_id;
    }

    public void setBest_prey_id(int best_prey_id) {
        this.best_prey_id = best_prey_id;
    }

    public int getPredator_id() {
        return predator_id;
    }

    public void setPredator_id(int predator_id) {
        this.predator_id = predator_id;
    }

    public int[] getOrdinary_preys_ids() {
        return ordinary_preys_ids;
    }

    public void setOrdinary_preys_ids(int[] ordinary_preys_ids) {
        this.ordinary_preys_ids = ordinary_preys_ids;
    }
    
    public void MovePrey(int prey_id, int predator_id ){
        HashMap<Integer,Double> follow_up_value = new HashMap<>();
        Double follow_up;
        Individual prey_following = individuals.get(prey_id);
        ArrayList<Individual> preys_followed = new ArrayList<>();
        for(Individual ind: individuals){
            if(ind.getId()!= prey_id && ind.getId() != predator_id){
                preys_followed.add(ind);
                follow_up = similarity(prey_following, ind)/ind.getSurvival_value();
                follow_up_value.put(ind.getId(),follow_up);
                System.out.println("seguida: "+ind.getId()+" follow up: "+follow_up);
            }
        }
        
                
    }
    
    public void MovePredator(int predator_id){
        
    }
    
    public static Double similarity(Individual x, Individual y)
    {
        int div = 0;
        for(int i=0;i<x.getSize();i++){
            div+=x.getPrey()[i]*y.getPrey()[i];
        }
        return div/(norm(x)*norm(y));
    }
    
    public static Double norm(Individual x){
        Double norm =0d;
        for (int i=0; i<x.getSize();i++){
            norm +=Math.pow(x.getPrey()[i], 2);
        }
        return Math.sqrt(norm);
    }
    @Override
    public String toString(){
        String returned = "";
        for(Individual individual:individuals){
            returned+="ID = "+ individual.getId()+"\n";
            for(int position: individual.getPrey()){
                returned+=position+" ";
            }
            returned+="\n";
            returned+="Survival value = "+individual.getSurvival_value()+"\n";
        }
        returned+="\nBest prey = "+best_prey_id+"\n";
        returned+="Predator = "+predator_id;        
        
        return returned;
    }
    
        
}
