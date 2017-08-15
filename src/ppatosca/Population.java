package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public Individual movePrey(int prey_id, int predator_id) {
        Map<Integer, Double> follow_up_value = new HashMap<>();

        Double follow_up;
        // Identificadores começam sempre de 1 porém array list começa do zero portanto é necessário diminuir 1
        Individual prey_following = individuals.get(prey_id - 1);
        ArrayList<Individual> preys_followed = new ArrayList<>();
        for (Individual ind : individuals) {
            if (ind.getId() != prey_id && ind.getId() != predator_id) {
                preys_followed.add(ind);
                follow_up = similarity(prey_following, ind) / ind.getSurvival_value();
                follow_up_value.put(ind.getId(), follow_up);

                System.out.println("seguida: " + ind.getId() + " follow up: " + follow_up);
            }
        }

        int[] roullet = createRoullet(follow_up_value);
        System.out.println("Roleta: ");
        for (int i = 0; i < roullet.length; i++) {
            System.out.print(roullet[i] + " ");

        }
        return moveByRoulletResult(prey_following, roullet);
    }

    public void movePredator(int predator_id) {

    }

    public int[] createRoullet(Map<Integer, Double> follow_up_value) {
        follow_up_value = Util.sortByValueDesc(follow_up_value);
        int[] roullet = new int[follow_up_value.size()];
        Double div = 0d;
        int roullet_start = 0;
        int roullet_quantity = 0;
        for (Double value : follow_up_value.values()) {
            div += Math.pow(value, 2);
        }

        for (Integer key : follow_up_value.keySet()) {
            roullet_quantity += Math.round((Math.pow(follow_up_value.get(key), 2) / div) * (follow_up_value.size()));
            for (int i = roullet_start; i < roullet.length && i < roullet_quantity; i++) {
                roullet[i] = key;
            }
            roullet_start = roullet_quantity;
        }

        return roullet;
    }

    public int roulletResult(int[] roullet) {
        Random random = new Random();
        int random_number = random.nextInt(roullet.length);

        return roullet[random_number];
    }

    private Individual moveByRoulletResult(Individual individual, int[] roullet) {
        int followed_prey;
        for(int i=0;i<individual.getPrey().length;i++){
            followed_prey = roulletResult(roullet);
            if(followed_prey!=0){
                individual.getPrey()[i] = individuals.get(followed_prey -1).getPrey()[i];
            }                       
        }
        
        return individual;
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

    @Override
    public String toString() {
        String returned = "";
        for (Individual individual : individuals) {
            returned += "ID = " + individual.getId() + "\n";
            for (int position : individual.getPrey()) {
                returned += position + " ";
            }
            returned += "\n";
            returned += "Survival value = " + individual.getSurvival_value() + "\n";
        }
        returned += "\nBest prey = " + best_prey_id + "\n";
        returned += "Predator = " + predator_id;

        return returned;
    }

}
