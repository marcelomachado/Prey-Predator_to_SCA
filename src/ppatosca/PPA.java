package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author gtbavi
 */
public class PPA {

    private static final Double COURSE_COMPLETED = 999999999d;
    private LearningMaterial[] LMs;
    private Learner learner;
    private Concept[] concepts;
    private Population population;

    public PPA(LearningMaterial[] LMs, Learner learner, Concept[] concepts) {
        this.LMs = LMs;
        this.learner = learner;
        this.concepts = concepts;
    }

    public PPA(LearningMaterial[] LMs, Learner learner, Concept[] concepts, Population population) {
        this.LMs = LMs;
        this.learner = learner;
        this.concepts = concepts;
        this.population = population;
    }

    public LearningMaterial[] getLMs() {
        return LMs;
    }

    public void setLMs(LearningMaterial[] LMs) {
        this.LMs = LMs;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public Concept[] getConcepts() {
        return concepts;
    }

    public void setConcepts(Concept[] concepts) {
        this.concepts = concepts;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public Individual movePrey(int prey_id, int predator_id, Double distance_factor, Double survival_value_factor) {
        Map<Integer, Double> follow_up_value = new HashMap<>();
        ArrayList<Individual> individuals = population.getIndividuals();
        Double follow_up;
        // Identificadores começam sempre de 1 porém array list começa do zero portanto é necessário diminuir 1
        Individual prey_following = individuals.get(prey_id - 1);
        for (Individual ind : individuals) {
            if (ind.getId() != prey_id && ind.getId() != predator_id) {
                follow_up = (distance_factor * Util.similarity(prey_following, ind)) + (survival_value_factor * (1 / ind.getSurvival_value()));
                follow_up_value.put(ind.getId(), follow_up);

                System.out.println("seguida: " + ind.getId() + " follow up: " + follow_up);
            }
        }
        int[] roullet = createRoullet(follow_up_value);
        System.out.println("Roleta: ");
        for (int i = 0; i < roullet.length; i++) {
            System.out.print(roullet[i] + " ");

        }
        System.out.println("\n");
        return moveDirectionByRoulletResult(prey_following, roullet);
    }

    /**
     *
     * @param individual
     * @param roullet
     * @return
     */
    private Individual moveDirectionByRoulletResult(Individual individual, int[] roullet) {
        ArrayList<Individual> individuals = population.getIndividuals();
        int followed_prey;
        for (int i = 0; i < individual.getPrey().length; i++) {
            followed_prey = roulletResult(roullet);
            if (followed_prey != 0) {
                individual.getPrey()[i] = individuals.get(followed_prey - 1).getPrey()[i];
            }
        }

        return individual;
    }

    /**
     *
     * @param prey_id
     * @param direction_length
     * @return
     */
    private Individual localSearchDirection(int prey_id, int direction_length) {
        ArrayList<Individual> individuals = population.getIndividuals();
        Individual individual = individuals.get(prey_id - 1);
        int size = individual.getSize();
        int[] random_direction;
        Double new_survival_value;

        for (int i = 0; i < direction_length; i++) {
            //Alterar caso necessário método que gera direções             
            random_direction = Individual.generateRandomPrey(size);
            //
            new_survival_value = generateSurvivalValue(random_direction);
            if (new_survival_value < individual.getSurvival_value()) {
                individual.setPrey(random_direction);
                individual.setSurvival_value(new_survival_value);
            }
        }
        return individual;
    }

    public int[] createRoullet(Map<Integer, Double> follow_up_value) {
        follow_up_value = Util.sortByValueDesc(follow_up_value);
        int[] roullet = new int[follow_up_value.size()];
        Double div = 0d;
        int roullet_start = 0;
        int roullet_quantity = 0;
        for (Double value : follow_up_value.values()) {
            div += Math.pow(value, 2);
        }

        for (Integer key : follow_up_value.keySet()) {
            roullet_quantity += Math.round((Math.pow(follow_up_value.get(key), 2) / div) * (follow_up_value.size()));
            for (int i = roullet_start; i < roullet.length && i < roullet_quantity; i++) {
                roullet[i] = key;
            }
            roullet_start = roullet_quantity;
        }

        return roullet;
    }

    public int roulletResult(int[] roullet) {
        return roullet[new Random().nextInt(roullet.length)];
    }

    public Individual moveDirectionBestPrey(int prey_id) {
        return new Individual(prey_id, prey_id);
    }

    public void movePredator(int predator_id) {

    }

    public Double generateSurvivalValue(int[] prey) {
        return executeFitnessFunction(conceptsObjetiveFunction(prey),
                difficultyObjetiveFunction(prey),
                timeObjetiveFunction(prey),
                balanceObjetiveFunction(prey));
    }

    public Double executeFitnessFunction(Double... objetiveFunctions) {
        Double fitnessValue = 0d;
        for (Double objtiveFunction : objetiveFunctions) {
            fitnessValue += objtiveFunction;
        }
        return fitnessValue;
    }

    // O1
    public Double conceptsObjetiveFunction(int[] individual) {
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

        return (qntt_1 != 0) ? (sum / qntt_1) : COURSE_COMPLETED;
    }

    // O2
    public Double difficultyObjetiveFunction(int[] individual) {
        Double sum = 0d;
        int qntt_1 = 0;
        for (int i = 0; i < individual.length; i++) {
            sum += individual[i] * Math.abs(LMs[i].getDificulty() - learner.getAbilityLevel());
        }

        for (int i = 0; i < individual.length; i++) {
            qntt_1 += individual[i];
        }

        return (qntt_1 != 0) ? (sum / qntt_1) : COURSE_COMPLETED;

    }

    // O3
    public Double timeObjetiveFunction(int[] individual) {

        int time_total = 0;

        for (int i = 0; i < individual.length; i++) {
            time_total += LMs[i].getTime() * individual[i];
        }
        return Math.max(0d, (learner.getLower_time() - time_total)) + Math.max(0d, (time_total - learner.getUpper_time()));
    }

    // O4
    public Double balanceObjetiveFunction(int[] individual) {

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
