package ppatosca;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class PPAtoSCA {

    public static void main(String[] args) throws FileNotFoundException, IOException, CloneNotSupportedException {
        Course course = new Course(args[0]);
        // Configuration: prey predator args 
        PreyPredatorAlgorithmConfig ppac = new PreyPredatorAlgorithmConfig(args[1]);
        //Configuration:  fitness function args
        FitnessFunctionConfig ffc = new FitnessFunctionConfig(args[2]);
        SingletonPrint printer = SingletonPrint.getInstance();

        /**
         * Population
         */
        try (BufferedWriter txtFile = new BufferedWriter(new FileWriter(args[3]))) {
            HashMap<String, String> learnerCurriculumSequencing = new HashMap<>();
            for (Learner learner : course.getLearners().values()) {
                printer.free();
                PPA ppa = new PPA(course.getLearningMaterials(), learner, course.getConcepts());
                ppa.setFitnessFunctions(ffc.getFitnessFunction());
                // ArrayList<Individual> bestIndividuals = new ArrayList<>();
                ppa.generatePopulation(ppac.getPopulationSize(), course.getLearningMaterials().size());
                ppa.updatePopulation(ppac.getMaxBestPreyQuantity(), ppac.getMaxPredatorQuantity());
                printer.addString("População Original\n");
                printer.addString(ppa.getPopulation().toString());
                Individual bestIndividual = null;
                //long tempoInicial = System.currentTimeMillis();
                for (int j = 1; j <= ppac.getMovementQuantity(); j++) {
                    Population populationClone = Population.clone(ppa.getPopulation());
                    printer.addString("\n\n************** MOVIMENTO " + j + " **************\n");
                    for (Individual individual : populationClone.getIndividuals()) {
                        ppa.moveIndividual(individual, ppac.getDistanceFactor(), ppac.getSurvivalValueFactor(), ppac.getMinimumStepLength(), ppac.getMaximumStepLength(), 1, ppac.getFollowedPreysQuantity(), ppac.getFollowUp(), ppac.getQuantityBestRandomPreys());
                    }
                    ppa.setPopulation(populationClone);

                    ppa.updatePopulation(ppac.getMaxBestPreyQuantity(), ppac.getMaxPredatorQuantity());

                    //System.out.println("o metodo executou em " + (System.currentTimeMillis() - tempoInicial));
                    Individual individual = ppa.getPopulation().getIndividuals().get(ppa.getPopulation().getBestPreysId().get(0)).clone();
                    if (bestIndividual == null) {
                        bestIndividual = individual;
                    } else if (individual.getSurvivalValue() < bestIndividual.getSurvivalValue()) {
                        bestIndividual = individual;
                    }

                }
                //System.out.println("Fim");
//        printer.out();
                //System.out.println(bestIndividual.toString());
              
                String sequencing = "";
                for (int i = 0; i < bestIndividual.getSize(); i++) {
                    if (bestIndividual.getPrey()[i] == 1) {                        
                        sequencing += i + " ";
                    }
                }
                System.out.println(learner.getRegistrationCode() + "\t" + sequencing.trim()+"\t"+bestIndividual.getSurvivalValue());
                txtFile.append(learner.getRegistrationCode() + "\t" + sequencing.trim()+"\t"+bestIndividual.getSurvivalValue()+"\n");
                learnerCurriculumSequencing.put(learner.getRegistrationCode(), sequencing.trim());
            }
            txtFile.close();
        }

        //System.out.println();
        //for (Individual ind : bestIndividuals) {
        // for (int i = 0; i < ind.getPrey().length; i++) {
        //System.out.print(ind.getPrey()[i] + " ");
        //   }
        //System.out.println("Survival value: " + ind.getSurvivalValue());
        // }
    }
}
