package ppatosca;

/**
 *
 * @author gtbavi
 */
public class PPA {

    public Double FitnessFunction(Double... objetiveFunctions) {
        Double fitnessValue = 0d;
        for (Double objtiveFunction : objetiveFunctions) {
            fitnessValue += objtiveFunction;
        }
        return fitnessValue;
    }

    // O1
    public Double ConceptsOF(LearningMaterial[] LMs, Individual individual, Learner learner, Concept[] concepts) {
        short[] ind = individual.getIndividual();
        int qntt_1 = 0;
        Double sum = 0d;

        for (int i = 0; i < ind.length; i++) {
            qntt_1 += ind[i];
        }

        for (int i = 0; i < ind.length; i++) {
            for (Concept concept : concepts) {
                sum += ind[i] * Math.abs((concept.getLMs().contains(LMs[i]) ? 1 : 0) - ((learner.getLearningGoals().contains(concept)) ? 1 : 0));
            }
        }

        return sum / qntt_1;
    }

    // O2
    public Double DifficultyOF(LearningMaterial[] LMs, Individual individual, Learner learner) {
        short[] ind = individual.getIndividual();
        Double sum = 0d;
        int qntt_1 = 0;
        for (int i = 0; i < ind.length; i++) {
            sum += ind[i] * Math.abs(LMs[i].getDificulty() - learner.getAbilityLevel());
        }

        for (int i = 0; i < ind.length; i++) {
            qntt_1 += ind[i];
        }

        return sum / qntt_1;

    }

    // O3
    public Double TimeOF(LearningMaterial[] LMs, Individual individual, Learner learner) {
        short[] ind = individual.getIndividual();
        int time_total = 0;

        for (int i = 0; i < ind.length; i++) {
            time_total += LMs[i].getTime() * ind[i];
        }
        return Math.max(0d, (learner.getLower_time() - time_total)) + Math.max(0d, (time_total - learner.getUpper_time()));
    }

    // O4
    public Double BalanceOF(LearningMaterial[] LMs, Individual individual, Learner learner, Concept[] concepts) {
        short[] ind = individual.getIndividual();

        Double sum = 0d;
        int learningGoal;

        int relevantConcepts_k = 0;
        for (int k = 0; k < ind.length; k++) {
            for (Concept concept_k : concepts) {
                relevantConcepts_k += ind[k] * (concept_k.getLMs().contains(LMs[k]) ? 1 : 0);
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

            for (int i = 0; i < ind.length; i++) {
                relevantConcepts += ind[i] * (concept.getLMs().contains(LMs[i]) ? 1 : 0);
            }

            sum += learningGoal * Math.abs(relevantConcepts - div);

        }

        return sum;
    }
}
