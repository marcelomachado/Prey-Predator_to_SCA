package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class FitnessFunction {

    public static final double COURSE_COMPLETED = Double.MAX_VALUE;
    public static HashMap<Integer,LearningMaterial> learningMaterials;
    public static Learner learner;
    public static HashMap<String,Concept> concepts;

    public static HashMap<Integer, LearningMaterial> getLearningMaterials() {
        return learningMaterials;
    }

    public static void setLearningMaterials(HashMap<Integer, LearningMaterial> learningMaterials) {
        FitnessFunction.learningMaterials = learningMaterials;
    }
    
    

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public static HashMap<String, Concept> getConcepts() {
        return concepts;
    }

    public static void setConcepts(HashMap<String, Concept> concepts) {
        FitnessFunction.concepts = concepts;
    }
    
    

    public static double executeFitnessFunction(double... objetiveFunctions) {
        double fitnessValue = 0d;
        for (double objtiveFunction : objetiveFunctions) {
            fitnessValue += objtiveFunction;
        }
        return fitnessValue;
    }

    // O1
    public static double conceptsObjetiveFunction(int[] individual) {
        int qntt = 0;
        double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;

                for (Concept concept : concepts.values()){ 
                    if(concept.getLMs() == null){
                        sum += Math.abs(-((learner.getLearningGoals().contains(concept)) ? 1 : 0));
                    }
                    else{
                        sum += Math.abs((concept.getLMs().contains(learningMaterials.get(i)) ? 1 : 0) - ((learner.getLearningGoals().contains(concept)) ? 1 : 0));
                        
                    }
                }
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }

    // O2
    public static double difficultyObjetiveFunction(int[] individual) {
        double sum = 0d;
        double learnerConceptAbility;
        ArrayList<Concept> coveredConcepts;
        int qntt = 0;
        int numberOfConcepts;
        
        for (int i = 0; i < individual.length; i++) {
            learnerConceptAbility = 0d;
            numberOfConcepts = 0;
            if (individual[i] == 1) {
                coveredConcepts = learningMaterials.get(i).getCoveredConcepts();
                for (Concept concept : coveredConcepts) {
                    if (learner.getLearningGoals().contains(concept)) {
                        numberOfConcepts++;
                        learnerConceptAbility += learner.getScore().get(concept);
                    }
                }
                if(numberOfConcepts != 0){
                    learnerConceptAbility /= numberOfConcepts;
                }
                sum += Math.abs(learningMaterials.get(i).getDifficulty() - learnerConceptAbility);
                qntt++;
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }

    // O3
    public static double timeObjetiveFunction(int[] individual) {
        int totalTime = 0;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                totalTime += learningMaterials.get(i).getTypicalLearningTime();
            }
        }
        return Math.max(0d, (learner.getLowerTime() - totalTime)) + Math.max(0d, (totalTime - learner.getUpperTime()));
    }

    // O4
    public static double balanceObjetiveFunction(int[] individual) {
        double sum = 0d;
        int learningGoal;
        int i;

        // Dividend: the amount of concepts covered by learn material
        int conceptsCoveredByLM = 0;
        for (i = 0; i < individual.length; i++) {
            for (Concept concept_k : concepts.values()) {
                if (individual[i] == 1) {
                    if(concept_k.getLMs() != null){
                        conceptsCoveredByLM += (concept_k.getLMs().contains(learningMaterials.get(i)) ? 1 : 0);
                    }
                }
            }
        }
        // Divisor: the amount of concepts the learner should learn
        int conceptsLearnShouldLearn = learner.getLearningGoals().size();

        double div = (double) conceptsCoveredByLM / conceptsLearnShouldLearn;

        for (Concept concept : concepts.values()) {
            int relevantConcepts = 0;
            // Elj
            learningGoal = ((learner.getLearningGoals().contains(concept)) ? 1 : 0);
            if (learningGoal == 0) {
                continue;
            }

            for (i = 0; i < individual.length; i++) {
                if (individual[i] == 1) {
                    relevantConcepts += (concept.getLMs().contains(learningMaterials.get(i)) ? 1 : 0);
                }
            }

            sum += Math.abs(relevantConcepts - div);

        }

        return sum;
    }
    public static double learningStyleObjetiveFunction(double ... learningStyles) {
        double leaningStyleValue = 0d;
        int numberOfLearningStyleValues =0;
        for (double learningStyle : learningStyles) {
            
            leaningStyleValue += learningStyle;
            numberOfLearningStyleValues++;
        }
        return leaningStyleValue/ numberOfLearningStyleValues;
    }
    // O5
    public static double learningStyleActiveReflexiveObjetiveFunction(int[] individual) {
        int qntt = 0;
        double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                int materialActiveReflexiveValue = learningMaterials.get(i).getLearningStyleActiveValue() - learningMaterials.get(i).getLearningStyleReflexiveValue();
                
                if(materialActiveReflexiveValue >0){
                    sum+=Math.abs(3 - learner.getAtvref());                    
                }
                else if(materialActiveReflexiveValue <0){
                    sum+=Math.abs(-3 - learner.getAtvref());  
                }
                else{
                    sum+=Math.abs(0 - learner.getAtvref()); 
                }                   
                sum/=6;
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }
    // 06
    public static double learningStyleSensoryIntuitiveObjetiveFunction(int[] individual) {
        int qntt = 0;
        double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                int materialSensoryIntuitiveValue = learningMaterials.get(i).getLearningStyleSensoryValue() - learningMaterials.get(i).getLearningStyleIntuitiveValue();
                
                if(materialSensoryIntuitiveValue >0){
                    sum+=Math.abs(3 - learner.getSenint());                    
                }
                else if(materialSensoryIntuitiveValue <0){
                    sum+=Math.abs(-3 - learner.getSenint());  
                }
                else{
                    sum+=Math.abs(0 - learner.getSenint()); 
                }                   
                sum/=6;
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }
    // 07
    public static double learningStyleVisualVerbalObjetiveFunction(int[] individual) {
        int qntt = 0;
        double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                int materialVisualVerbalValue = learningMaterials.get(i).getLearningStyleVisualValue() - learningMaterials.get(i).getLearningStyleVerbalValue();
                
                if(materialVisualVerbalValue >0){
                    sum+=Math.abs(3 - learner.getVisver());                    
                }
                else if(materialVisualVerbalValue <0){
                    sum+=Math.abs(-3 - learner.getVisver());  
                }
                else{
                    sum+=Math.abs(0 - learner.getVisver()); 
                }                   
                sum/=6;
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }
    
    // 08
    public static double learningStyleSequentialGlobalObjetiveFunction(int[] individual) {
        int qntt = 0;
        double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                int materialSequentialGlobalValue = learningMaterials.get(i).getLearningStyleSequentialValue() - learningMaterials.get(i).getLearningStyleGlobalValue();
                
                if(materialSequentialGlobalValue >0){
                    sum+=Math.abs(3 - learner.getSeqglo());                    
                }
                else if(materialSequentialGlobalValue <0){
                    sum+=Math.abs(-3 - learner.getSeqglo());  
                }
                else{
                    sum+=Math.abs(0 - learner.getSeqglo()); 
                }                   
                sum/=6;
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }


}
