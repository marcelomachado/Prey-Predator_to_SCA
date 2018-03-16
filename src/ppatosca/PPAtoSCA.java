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

    public static Object main(String[] args, int teste, PreyPredatorAlgorithmConfig ppac ) throws FileNotFoundException, IOException, CloneNotSupportedException {
        long tempoFinal = 0;
        Individual bestIndividual = null;
        Individual individualX;
        Course course = new Course(args[0]);
        // Configuration: prey predator args 
        //Configuration:  fitness function args
        FitnessFunctionConfig ffc = new FitnessFunctionConfig(args[2]);
        //SingletonPrint printer = SingletonPrint.getInstance();

        /**
         * Population
         */
            //HashMap<String, String> learnerCurriculumSequencing = new HashMap<>();
        
        for (Learner learner : course.getLearners().values()) {
            //printer.free();
            PPA ppa = new PPA(course.getLearningMaterials(), learner, course.getConcepts());
            ppa.setFitnessFunctions(ffc.getFitnessFunction());
            // ArrayList<Individual> bestIndividuals = new ArrayList<>();
            ppa.generatePopulation(ppac.getPopulationSize(), course.getLearningMaterials().size());
            ppa.updatePopulation(ppac.getMaxBestPreyQuantity(), ppac.getMaxPredatorQuantity());
            //printer.addString("População Original\n");
            //printer.addString(ppa.getPopulation().toString());
            
            long tempoInicial = System.currentTimeMillis();
            for (int j = 1; j <= ppac.getMovementQuantity(); j++) {
                Population populationClone = Population.clone(ppa.getPopulation());
                //printer.addString("\n\n************** MOVIMENTO " + j + " **************\n");
                //printer.addString(populationClone.toString());
                for (Individual individual : populationClone.getIndividuals()) {
                    ppa.moveIndividual(individual, ppac.getDistanceFactor(), ppac.getSurvivalValueFactor(), ppac.getMinimumStepLength(), ppac.getMaximumStepLength(), 1, ppac.getFollowedPreysQuantity(), ppac.getFollowUp(), ppac.getQuantityBestRandomPreys());
                }
                ppa.setPopulation(populationClone);

                ppa.updatePopulation(ppac.getMaxBestPreyQuantity(), ppac.getMaxPredatorQuantity());


                individualX = ppa.getPopulation().getIndividuals().get(ppa.getPopulation().getBestPreysId().get(0)).clone();
                if (bestIndividual == null) {
                    bestIndividual = individualX;
                } else if (individualX.getSurvivalValue() < bestIndividual.getSurvivalValue()) {
                    bestIndividual = individualX;
                }

            }
            tempoFinal = System.currentTimeMillis() - tempoInicial;
               
        }
        final double fitnessOuter = bestIndividual.getSurvivalValue();
        final long timeOuter = tempoFinal;
        return new Object(){double fitness = fitnessOuter; long time = timeOuter;};

    }

}

