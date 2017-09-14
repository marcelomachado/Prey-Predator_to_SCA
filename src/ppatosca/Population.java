package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class Population {

    private int sizePopulation;
    private int bestPreyId;
    private int predatorId; // Worst prey
    private int[] ordinaryPreysIds;
    private ArrayList<Individual> individuals;

    public Population(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    public int getSizePopulation() {
        return sizePopulation;
    }

    public void setSizePopulation(int sizePopulation) {
        this.sizePopulation = sizePopulation;
    }

    public int getBestPreyId() {
        return bestPreyId;
    }

    public void setBestPreyId(int bestPreyId) {
        this.bestPreyId = bestPreyId;
    }

    public int getPredatorId() {
        return predatorId;
    }

    public void setPredatorId(int predatorId) {
        this.predatorId = predatorId;
    }

    public int[] getOrdinaryPreysIds() {
        return ordinaryPreysIds;
    }

    public void setOrdinaryPreysIds(int[] ordinaryPreysIds) {
        this.ordinaryPreysIds = ordinaryPreysIds;
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
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
            returned += "Survival value = " + individual.getSurvival_value() + "\n";
        }
        returned += "\nBest prey = " + bestPreyId + "\n";
        returned += "Predator = " + predatorId+"\n";
        returned+="Ordinary Preys:";
        for(int i =0;i<ordinaryPreysIds.length;i++){
            returned+=" "+ordinaryPreysIds[i];
        }
        returned+="\n";

        return returned;
    }

}
