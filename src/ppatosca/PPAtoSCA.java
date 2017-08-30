package ppatosca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class PPAtoSCA {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Read Concepts

        FileInputStream stream = new FileInputStream(new File(args[0]));
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();

        String[] ccp_info;
        /**
         * Concepts
         */
        ArrayList<Concept> concepts = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            concepts.add(new Concept(Integer.parseInt(ccp_info[0]), ccp_info[1], Integer.parseInt(ccp_info[2])));
            line = br.readLine();
        }
        stream = new FileInputStream(new File(args[1]));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);

        /**
         * Prerequisites
         */
        line = br.readLine();
        while (line != null) {
            ccp_info = line.split(";");
            if (ccp_info.length > 1) {
                ArrayList<Concept> prerequisites = new ArrayList<>();
                for (int i = 1; i < ccp_info.length; i++) {
                    prerequisites.add(concepts.get(Integer.parseInt(ccp_info[i])));
                }
                concepts.get(Integer.parseInt(ccp_info[0])).setPrerequisites(prerequisites);
            }

            line = br.readLine();
        }

        /**
         * Learning Materials
         */
        
        stream = new FileInputStream(new File(args[2]));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        line = br.readLine();
        ArrayList<LearningMaterial> learningMaterials = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            LearningMaterial learningMaterial = new LearningMaterial(Integer.parseInt(ccp_info[0]), ccp_info[1], ccp_info[2], Integer.parseInt(ccp_info[3]),Double.parseDouble(ccp_info[4]));
            learningMaterials.add(learningMaterial);
            Concept conceptMaterial = concepts.get(Integer.parseInt(ccp_info[5]));
            if(conceptMaterial.getLMs() == null){
                ArrayList<LearningMaterial> lMs = new ArrayList<>();
                learningMaterials.add(learningMaterial);
                conceptMaterial.setLMs(lMs);
            }
            else{
                conceptMaterial.getLMs().add(learningMaterial);
            }

            line = br.readLine();
        }        

       /**
        * Learner
        */       
        stream = new FileInputStream(new File(args[3]));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        line = br.readLine();
        ArrayList<Learner> learners= new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            Learner learner = new Learner(Integer.parseInt(ccp_info[0]),Double.parseDouble(ccp_info[1]),Integer.parseInt(ccp_info[2]) , Integer.parseInt(ccp_info[3]));
            if(ccp_info.length >4){
                for(int i = 4;i<ccp_info.length;i++){
                    if(learner.getLearningGoals()==null){
                        ArrayList<Concept> learningGoals = new ArrayList<>();
                        learningGoals.add(concepts.get(i));                                                        
                        learner.setLearningGoals(learningGoals);
                    }
                    else{
                        learner.getLearningGoals().add(concepts.get(i));
                    }
                    
                }
            }
            learners.add(learner);
        }
        
        br.close();
        reader.close();
        stream.close();
        

        int worst_survival_value_id = 0;
        int best_survival_value_id = 0;
        Double worst_survival_value = 0d;
        Double better_survival_value = 9999999999d;

        ArrayList<Individual> individuals = new ArrayList<>();
        PPA ppa = new PPA(learningMaterials, learners.get(0), concepts);
        for (int i = 1; i <= 5; i++) {
            Individual individual = new Individual(5, i);
            //individual.generateRandomIndividual();
            individual.generateIndividualTest(i);

            individual.setSurvival_value(ppa.generateSurvivalValue(individual.getPrey()));

            individuals.add(individual);

            if (individual.getSurvival_value() > worst_survival_value) {
                worst_survival_value = individual.getSurvival_value();
                worst_survival_value_id = i;
            }
            if (individual.getSurvival_value() < better_survival_value) {
                better_survival_value = individual.getSurvival_value();
                best_survival_value_id = i;
            }
        }
        Population population = new Population(individuals);
        population.setBest_prey_id(best_survival_value_id);
        population.setPredator_id(worst_survival_value_id);
        ppa.setPopulation(population);
        System.out.println(population.toString());

        Individual new_indiIndividual = ppa.movePrey(best_survival_value_id, worst_survival_value_id, 1d, 1d);

//        individuals.get(best_survival_value_id -1).setId(new_indiIndividual.getId());
//        individuals.get(best_survival_value_id -1).setPrey(new_indiIndividual.getPrey());
//        individuals.get(best_survival_value_id -1).setSize(new_indiIndividual.getSize());
//        individuals.get(best_survival_value_id -1).setSurvival_value(ppa.generateSurvivalValue(new_indiIndividual.getPrey()));
        System.out.println(population.toString());
        // teste similaridade
//        Individual individual_0 = new Individual(3, 0);
//        Individual individual_1 = new Individual(3, 1);
//        int [] p = {1,0,1};
//        int [] k = {1,1,0};
//        individual_0.setPrey(p);
//        individual_1.setPrey(k);
//        System.out.printf("%.2f",Population.similarity(individual_0, individual_1));

    }

}
