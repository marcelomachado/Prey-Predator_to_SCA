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
        Individual ind = new Individual(2,3);
        
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
        Concept conc_1= new Concept(0, "Computer Science", LMs_1);
        
        ArrayList<LearningMaterial> LMs_2 = new ArrayList<>();
        LMs_2.add(lm_3);
        
        Concept conc_2= new Concept(0, "Multimedia System", LMs_2);
        
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
        
        int[][] population = ind.generatePopulation();
        for(int i=0;i<population.length;i++){
            for(int j=0;j<population[i].length;j++){
                System.out.print(population[i][j]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
        
        System.out.println(new PPA().FitnessFunction(new PPA().ConceptsOF(LMs, population[0], learner, conc), new PPA().DifficultyOF(LMs, population[0], learner), new PPA().TimeOF(LMs, population[0], learner), new PPA().BalanceOF(LMs, population[0], learner, conc)));
        
        
        
    }
    
}
