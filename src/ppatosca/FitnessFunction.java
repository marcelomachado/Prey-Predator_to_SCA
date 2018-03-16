package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gtbavi
 */
public class FitnessFunction {

    public static final double COURSE_COMPLETED = Double.MAX_VALUE;
    public static HashMap<Integer, LearningMaterial> learningMaterials;
    public static Learner learner;
    public static HashMap<String, Concept> concepts;

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
        FitnessFunction.learner = learner;
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
        int penalty = 0;
        Map<Concept,Integer> learningGoalsCoveredByIndividual = new HashMap<>();
        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                LearningMaterial learningMaterial = learningMaterials.get(i);
                //Each covered concept
                for(Concept coveredConcept: learningMaterial.getCoveredConcepts()){
                    // Penalty: Learner don't have to learn this concept                    
                    if(learner.getLearningGoals().contains(coveredConcept)){
                        learningGoalsCoveredByIndividual.put(coveredConcept, 0);
                    }
                    else{
                        penalty++;
                    }                                        
                }
            }
        }
        //Penalty: Don't cover all learning goals
        penalty = learner.getLearningGoals().stream().filter((learningGoal) -> (!learningGoalsCoveredByIndividual.containsKey(learningGoal))).map((_item) -> 2).reduce(penalty, Integer::sum);        

        return penalty;
    }
    public static double conceptsObjetiveFunctionArtigo(int[] individual) {
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
                if (numberOfConcepts != 0) {
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
        int relevantConcepts;

        // Dividend: the amount of concepts covered by learn material
        int conceptsCoveredByLM = 0;
        for (i = 0; i < individual.length; i++) {
            for (Concept concept_k : concepts.values()) {
                if (individual[i] == 1) {
                    if (concept_k.getLMs() != null) {
                        // Count how many concepts the material i cover
                        conceptsCoveredByLM += (concept_k.getLMs().contains(learningMaterials.get(i)) ? 1 : 0);
                    }
                }
            }
        }
        // Divisor: the amount of concepts the learner should learn
        int conceptsLearnShouldLearn = learner.getLearningGoals().size();

        double div = (double) conceptsCoveredByLM / conceptsLearnShouldLearn;

        for (Concept concept : concepts.values()) {
            relevantConcepts = 0;
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

    public static double learningStyleObjetiveFunction(double... learningStyles) {
        double leaningStyleValue = 0d;
        // int numberOfLearningStyleValues =0;
        for (double learningStyle : learningStyles) {

            leaningStyleValue += learningStyle;
        }
        return leaningStyleValue / learningStyles.length;
    }
    // O5
    public static double learningStyleActiveReflexiveObjetiveFunction(int[] individual) {
        int qntt = 0;
        double sum = 0d;
        int materialActiveReflexiveValue;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                materialActiveReflexiveValue = learningMaterials.get(i).getLearningStyleActiveValue() - learningMaterials.get(i).getLearningStyleReflexiveValue();
                sum += Math.abs(3*Integer.signum(materialActiveReflexiveValue) - learner.getAtvref());
            }
        }

        return (qntt != 0) ? ((sum) / qntt) : COURSE_COMPLETED;
    }
    // 06
    public static double learningStyleSensoryIntuitiveObjetiveFunction(int[] individual) {
        int qntt = 0;
        int materialSensoryIntuitiveValue;
        double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                materialSensoryIntuitiveValue = learningMaterials.get(i).getLearningStyleSensoryValue() - learningMaterials.get(i).getLearningStyleIntuitiveValue();
                sum += Math.abs(3*Integer.signum(materialSensoryIntuitiveValue) - learner.getSenint());
            }
        }

        return (qntt != 0) ? ((sum) / qntt) : COURSE_COMPLETED;
    }
    // 07
    public static double learningStyleVisualVerbalObjetiveFunction(int[] individual) {
        int qntt = 0;
        int materialVisualVerbalValue;
        double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                materialVisualVerbalValue = learningMaterials.get(i).getLearningStyleVisualValue() - learningMaterials.get(i).getLearningStyleVerbalValue();
                sum += Math.abs(3*Integer.signum(materialVisualVerbalValue) - learner.getVisver());                
            }
        }

        return (qntt != 0) ? ((sum) / qntt) : COURSE_COMPLETED;
    }
    // 08
    public static double learningStyleSequentialGlobalObjetiveFunction(int[] individual) {
        int qntt = 0;
        double sum = 0d;
        int materialSequentialGlobalValue;
        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                materialSequentialGlobalValue = learningMaterials.get(i).getLearningStyleSequentialValue() - learningMaterials.get(i).getLearningStyleGlobalValue();
                sum += Math.abs(3*Integer.signum(materialSequentialGlobalValue) - learner.getSeqglo());  
            }
        }

        return (qntt != 0) ? ((sum) / qntt) : COURSE_COMPLETED;
    }

}
