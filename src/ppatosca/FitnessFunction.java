package ppatosca;

/**
 *
 * @author gtbavi
 */
public class FitnessFunction {

    public static Double ExecuteFitnessFunction(Double... objetiveFunctions) {
        Double fitnessValue = 0d;
        for (Double objtiveFunction : objetiveFunctions) {
            fitnessValue += objtiveFunction;
        }
        return fitnessValue;
    }

    // O1
    public static Double ConceptsOF(LearningMaterial[] LMs, int[] individual, Learner learner, Concept[] concepts) {
        int qntt_1 = 0;
        Double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            qntt_1 += individual[i];
        }

        for (int i = 0; i < individual.length; i++) {
            for (Concept concept : concepts) {
                sum += individual[i] * Math.abs((concept.getLMs().contains(LMs[i]) ? 1 : 0) - ((learner.getLearningGoals().contains(concept)) ? 1 : 0));
            }
        }

        return sum / qntt_1;
    }

    // O2
    public static Double DifficultyOF(LearningMaterial[] LMs, int[] individual, Learner learner) {
        Double sum = 0d;
        int qntt_1 = 0;
        for (int i = 0; i < individual.length; i++) {
            sum += individual[i] * Math.abs(LMs[i].getDificulty() - learner.getAbilityLevel());
        }

        for (int i = 0; i < individual.length; i++) {
            qntt_1 += individual[i];
        }

        return sum / qntt_1;

    }

    // O3
    public static Double TimeOF(LearningMaterial[] LMs, int[] individual, Learner learner) {

        int time_total = 0;

        for (int i = 0; i < individual.length; i++) {
            time_total += LMs[i].getTime() * individual[i];
        }
        return Math.max(0d, (learner.getLower_time() - time_total)) + Math.max(0d, (time_total - learner.getUpper_time()));
    }

    // O4
    public static Double BalanceOF(LearningMaterial[] LMs, int[] individual, Learner learner, Concept[] concepts) {

        Double sum = 0d;
        int learningGoal;

        int relevantConcepts_k = 0;
        for (int k = 0; k < individual.length; k++) {
            for (Concept concept_k : concepts) {
                relevantConcepts_k += individual[k] * (concept_k.getLMs().contains(LMs[k]) ? 1 : 0);
            }
        }

        int learningGoal_l = 0;
        for (Concept concept_l : concepts) {
            learningGoal_l += ((learner.getLearningGoals().contains(concept_l)) ? 1 : 0);
        }

        Double div = (double) relevantConcepts_k / learningGoal_l;

        for (Concept concept : concepts) {
            int relevantConcepts = 0;
            // Elj
            learningGoal = ((learner.getLearningGoals().contains(concept)) ? 1 : 0);
            if (learningGoal == 0) {
                continue;
            }

            for (int i = 0; i < individual.length; i++) {
                relevantConcepts += individual[i] * (concept.getLMs().contains(LMs[i]) ? 1 : 0);
            }

            sum += learningGoal * Math.abs(relevantConcepts - div);

        }

        return sum;
    }
}
