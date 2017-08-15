package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class PPAtoSCA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        LearningMaterial lm_1 = new LearningMaterial();
        lm_1.setId(1);
        lm_1.setDificulty(10d);
        lm_1.setTime(50);

        LearningMaterial lm_2 = new LearningMaterial();
        lm_2.setId(2);
        lm_2.setDificulty(10d);
        lm_2.setTime(50);

        LearningMaterial lm_3 = new LearningMaterial();
        lm_3.setId(3);
        lm_3.setDificulty(10d);
        lm_3.setTime(50);
        
        LearningMaterial lm_4 = new LearningMaterial();
        lm_4.setId(4);
        lm_4.setDificulty(10d);
        lm_4.setTime(25);
        
        LearningMaterial lm_5 = new LearningMaterial();
        lm_5.setId(5);
        lm_5.setDificulty(15d);
        lm_5.setTime(90);

        LearningMaterial[] LMs = new LearningMaterial[5];
        LMs[0] = lm_1;
        LMs[1] = lm_2;
        LMs[2] = lm_3;
        LMs[3] = lm_4;
        LMs[4] = lm_5;

        ArrayList<LearningMaterial> LMs_1 = new ArrayList<>();
        LMs_1.add(lm_1);
        LMs_1.add(lm_2);
        
        Concept conc_1 = new Concept(1, "Computer Science", LMs_1);

        ArrayList<LearningMaterial> LMs_2 = new ArrayList<>();
        LMs_2.add(lm_3);

        Concept conc_2 = new Concept(2, "Multimedia System", LMs_2);
        
        ArrayList<LearningMaterial> LMs_3 = new ArrayList<>();
        LMs_3.add(lm_4);
        LMs_3.add(lm_5);
        
        Concept conc_3 = new Concept(3, "Computer Organization", LMs_3);

        Learner learner = new Learner();
        learner.setAbilityLevel(8d);
        learner.setId(1);
        learner.setLower_time(10);
        learner.setUpper_time(100);

        ArrayList<Concept> lg = new ArrayList<>();
        lg.add(conc_2);

        learner.setLearningGoals(lg);

        Concept[] conc = new Concept[3];
        conc[0] = conc_1;
        conc[1] = conc_2;
        conc[2] = conc_3;
        
        
        int worst_survival_value_id=0;
        int better_survival_value_id=0;
        Double worst_survival_value = 0d;
        Double better_survival_value = 9999999999d;
        
        ArrayList<Individual> individuals = new ArrayList<>();
        for (int i = 1; i <=5; i++) {
            Individual individual = new Individual(5, i);
            //individual.generateRandomIndividual();
            individual.generateIndividualTest(i);
            individual.setSurvival_value(FitnessFunction.ExecuteFitnessFunction(FitnessFunction.ConceptsOF(LMs, individual.getPrey(), learner, conc), 
                    FitnessFunction.DifficultyOF(LMs, individual.getPrey(), learner), 
                    FitnessFunction.TimeOF(LMs, individual.getPrey(), learner), 
                    FitnessFunction.BalanceOF(LMs, individual.getPrey(), learner, conc)));
            
            individuals.add(individual);
            
            if(individual.getSurvival_value() > worst_survival_value){
                worst_survival_value = individual.getSurvival_value();
                worst_survival_value_id = i;                
            }
            if(individual.getSurvival_value() < better_survival_value){
                better_survival_value = individual.getSurvival_value();
                better_survival_value_id = i;                
            }
        }
        Population population = new Population(individuals);
        population.setBest_prey_id(better_survival_value_id);
        population.setPredator_id(worst_survival_value_id);
        System.out.println(population.toString());
        
        Individual new_indiIndividual = population.movePrey(better_survival_value_id, worst_survival_value_id);
        
        individuals.get(better_survival_value_id -1).setId(new_indiIndividual.getId());
        individuals.get(better_survival_value_id -1).setPrey(new_indiIndividual.getPrey());
        individuals.get(better_survival_value_id -1).setSize(new_indiIndividual.getSize());
        individuals.get(better_survival_value_id -1).setSurvival_value(FitnessFunction.ExecuteFitnessFunction(FitnessFunction.ConceptsOF(LMs, new_indiIndividual.getPrey(), learner, conc), 
                    FitnessFunction.DifficultyOF(LMs, new_indiIndividual.getPrey(), learner), 
                    FitnessFunction.TimeOF(LMs, new_indiIndividual.getPrey(), learner), 
                    FitnessFunction.BalanceOF(LMs, new_indiIndividual.getPrey(), learner, conc)));
        
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
