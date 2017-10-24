package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class Population {

    private int sizePopulation;
    private ArrayList<Integer> bestPreysId;
    private int predatorId; // Worst prey
    private ArrayList<Integer> ordinaryPreysIds;
    private ArrayList<Individual> individuals;

    public Population() {
    }

    public Population(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    public int getSizePopulation() {
        return sizePopulation;
    }

    public void setSizePopulation(int sizePopulation) {
        this.sizePopulation = sizePopulation;
    }

    public int getPredatorId() {
        return predatorId;
    }

    public void setPredatorId(int predatorId) {
        this.predatorId = predatorId;
    }        
    
    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public ArrayList<Integer> getBestPreysId() {
        return bestPreysId;
    }

    public void setBestPreysId(ArrayList<Integer> bestPreysId) {
        this.bestPreysId = bestPreysId;
    }

    public ArrayList<Integer> getOrdinaryPreysIds() {
        return ordinaryPreysIds;
    }

    public void setOrdinaryPreysIds(ArrayList<Integer> ordinaryPreysIds) {
        this.ordinaryPreysIds = ordinaryPreysIds;
    }
    
    
    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
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
            returned += "Survival value = " + individual.getSurvivalValue() + "\n";
        }
        returned += "\nBest prey = " + bestPreysId + "\n";
        returned += "Predator = " + predatorId + "\n";
        returned += "Ordinary Preys = "+ ordinaryPreysIds +"\n";
        for (int ordinaryPrey: ordinaryPreysIds) {
            returned += " " + ordinaryPrey;
        }
       // returned += "\n";

        return returned;
    }
    // TODO: verificar validade deste método após alteração da popuulação
    // TODO: criar um método de teste
    protected static Population clone(Population original) throws CloneNotSupportedException {
        Population clone = new Population();
        clone.individuals = new ArrayList<>();
        clone.bestPreysId = original.bestPreysId;
        for (Individual ind : original.individuals) {
            clone.getIndividuals().add(ind.clone());
        }       

        clone.predatorId = original.predatorId;
        clone.sizePopulation = original.sizePopulation;
        clone.ordinaryPreysIds = (ArrayList<Integer>) original.ordinaryPreysIds.clone();
        return clone;
    }

}
