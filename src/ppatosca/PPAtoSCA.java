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
        lm_1.setId(0);
        lm_1.setDificulty(10d);
        lm_1.setTime(50);

        LearningMaterial lm_2 = new LearningMaterial();
        lm_2.setId(1);
        lm_2.setDificulty(10d);
        lm_2.setTime(50);

        LearningMaterial lm_3 = new LearningMaterial();
        lm_3.setId(2);
        lm_3.setDificulty(10d);
        lm_3.setTime(50);

        LearningMaterial[] LMs = new LearningMaterial[3];
        LMs[0] = lm_1;
        LMs[1] = lm_2;
        LMs[2] = lm_3;

        ArrayList<LearningMaterial> LMs_1 = new ArrayList<>();
        LMs_1.add(lm_1);
        LMs_1.add(lm_2);
        Concept conc_1 = new Concept(0, "Computer Science", LMs_1);

        ArrayList<LearningMaterial> LMs_2 = new ArrayList<>();
        LMs_2.add(lm_3);

        Concept conc_2 = new Concept(0, "Multimedia System", LMs_2);

        Learner learner = new Learner();
        learner.setAbilityLevel(8d);
        learner.setId(1);
        learner.setLower_time(10);
        learner.setUpper_time(100);

        ArrayList<Concept> lg = new ArrayList<>();
        lg.add(conc_2);

        learner.setLearningGoals(lg);

        Concept[] conc = new Concept[2];
        conc[0] = conc_1;
        conc[1] = conc_2;
        
        int worst_survival_value_id=0;
        int better_survival_value_id=0;
        Double worst_survival_value = 0d;
        Double better_survival_value = 9999999999d;
        
        ArrayList<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Individual individual = new Individual(3, i);
            individual.generateRandomIndividual();
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
       
//        for(Individual individual : population.getIndividuals()){
//            System.out.println(FitnessFunction.ExecuteFitnessFunction(FitnessFunction.ConceptsOF(LMs, individual.getPrey(), learner, conc), FitnessFunction.DifficultyOF(LMs, individual.getPrey(), learner), FitnessFunction.TimeOF(LMs, individual.getPrey(), learner), FitnessFunction.BalanceOF(LMs, individual.getPrey(), learner, conc)));
//        }
    }

}
