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
    private ArrayList<LearningMaterial> LearningMaterials;
    private Learner learner;
    private ArrayList<Concept> concepts;
    private Population population;

    public PPA(ArrayList<LearningMaterial> LearningMaterials, Learner learner, ArrayList<Concept> concepts) {
        this.LearningMaterials = LearningMaterials;
        this.learner = learner;
        this.concepts = concepts;
    }

    public PPA(ArrayList<LearningMaterial> LearningMaterials, Learner learner, ArrayList<Concept> concepts, Population population) {
        this.LearningMaterials = LearningMaterials;
        this.learner = learner;
        this.concepts = concepts;
        this.population = population;
    }

    public ArrayList<LearningMaterial> getLearningMaterials() {
        return LearningMaterials;
    }

    public void setLearningMaterials(ArrayList<LearningMaterial> LearningMaterials) {
        this.LearningMaterials = LearningMaterials;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public ArrayList<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(ArrayList<Concept> concepts) {
        this.concepts = concepts;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    /**
     * Update population values after movements
     */
    public void updatePopulation() {

        Map<Integer, Double> populationSurvivalValues = new HashMap<>();
        for (Individual individual : population.getIndividuals()) {
            populationSurvivalValues.put(individual.getId(), individual.getSurvival_value());
        }
        populationSurvivalValues = Util.sortByValueAsc(populationSurvivalValues);

        population.setBestPreyId((int) populationSurvivalValues.keySet().toArray()[0]);
        population.setBestPreyId((int) populationSurvivalValues.keySet().toArray()[population.getSizePopulation()]);

    }

    public Individual movePrey(int prey_id, int predator_id, Double distance_factor, Double survival_value_factor) {
        Map<Integer, Double> followUpValue = new HashMap<>();
        ArrayList<Individual> individuals = population.getIndividuals();
        Double followUp;

        if (prey_id == population.getBestPreyId()) {
            return localSearchAllDirections(prey_id);
        }
        // IDs always begin with 1, but arraylist begin from 0 then is necessary decrement 1
        Individual prey_following = individuals.get(prey_id);
        for (Individual ind : individuals) {
            if (ind.getSurvival_value() < prey_following.getSurvival_value()) {
                followUp = (distance_factor * Util.similarity(prey_following, ind)) + (survival_value_factor * (1 / ind.getSurvival_value()));
                followUpValue.put(ind.getId(), followUp);

                System.out.println("seguida: " + ind.getId() + " follow up: " + followUp);
            }
        }
        int[] roullet = createRoullet(followUpValue);
        System.out.println("Roleta: ");
        for (int i = 0; i < roullet.length; i++) {
            System.out.print(roullet[i] + " ");

        }
        System.out.println();
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
        int followedPrey;
        for (int i = 0; i < individual.getPrey().length; i++) {
            followedPrey = roulletResult(roullet);
            if (followedPrey != 0) {
                individual.getPrey()[i] = individuals.get(followedPrey).getPrey()[i];
            }
        }
        individual.setSurvival_value(generateSurvivalValue(individual.getPrey()));
        updatePopulation();
        return individual;
    }

    /**
     *
     * @param prey_id
     * @param direction_length
     * @return
     */
    private Individual localSearchRandomDirection(int prey_id, int direction_length) {
        ArrayList<Individual> individuals = population.getIndividuals();
        Individual individual = individuals.get(prey_id);
        int size = individual.getSize();
        int[] randomDirection;
        Double new_survival_value;

        for (int i = 0; i < direction_length; i++) {
            //Alterar caso necessário método que gera direções             
            randomDirection = generateRandomPrey(size);
            //
            new_survival_value = generateSurvivalValue(randomDirection);
            if (new_survival_value < individual.getSurvival_value()) {
                individual.setPrey(randomDirection);
                individual.setSurvival_value(new_survival_value);
            }
        }
        if (individual.getSurvival_value() < population.getBestPreyId()) {
            population.setBestPreyId(individual.getId());
        }
        return individual;
    }

    /**
     * Change
     *
     * @param prey_id
     * @param direction_length
     * @return
     */
    private Individual localSearchAllDirections(int prey_id) {
        ArrayList<Individual> individuals = population.getIndividuals();
        Individual individual = individuals.get(prey_id);
        int size = individual.getSize();
        int[] newPrey = individual.getPrey();
        int[] auxPrey;
        Double newSurvivalValue = individual.getSurvival_value();
        Double auxSurvivalValue;

        System.out.print("Original: ");
        Util.printPrey(newPrey);

        for (int i = 0; i < size; i++) {
            auxPrey = individual.getPrey().clone();
            if (auxPrey[i] == 0) {
                auxPrey[i] = 1;
            } else {
                auxPrey[i] = 0;
            }
            System.out.print("Nova presa: ");
            Util.printPrey(auxPrey);

            auxSurvivalValue = generateSurvivalValue(auxPrey);
            System.out.print(auxSurvivalValue);
            System.out.println("");
            if (auxSurvivalValue < newSurvivalValue) {
                newSurvivalValue = auxSurvivalValue;
                newPrey = auxPrey;
            }
        }
        individual.setPrey(newPrey);
        individual.setSurvival_value(newSurvivalValue);
        //Update Population
        if (individual.getSurvival_value() < population.getBestPreyId()) {
            population.setBestPreyId(individual.getId());
        }
        return individual;
    }

    public int[] createRoullet(Map<Integer, Double> followUpValue) {
        followUpValue = Util.sortByValueDesc(followUpValue);
        int[] roullet = new int[followUpValue.size()];
        Double div = 0d;
        int roulletStart = 0;
        int roulletQuantity = 0;
        for (Double value : followUpValue.values()) {
            div += Math.pow(value, 2);
        }

        for (Integer key : followUpValue.keySet()) {
            roulletQuantity += Math.round((Math.pow(followUpValue.get(key), 2) / div) * (followUpValue.size()));
            for (int i = roulletStart; i < roullet.length && i < roulletQuantity; i++) {
                roullet[i] = key;
            }
            roulletStart = roulletQuantity;
        }

        return roullet;
    }

    public int roulletResult(int[] roullet) {
        return roullet[new Random().nextInt(roullet.length)];
    }

    public Individual moveDirectionBestPrey(int preyId) {
        return new Individual(preyId, preyId);
    }

    public void movePredator(int predatorId) {

    }

    public Double generateSurvivalValue(int[] prey) {
        return executeFitnessFunction(conceptsObjetiveFunction(prey),
                difficultyObjetiveFunction(prey),
                timeObjetiveFunction(prey),
                balanceObjetiveFunction(prey));
    }

    public void generatePopulation(int individualsQuantity, int individualSize) {
        int worst_survival_value_id = 0;
        int best_survival_value_id = 0;
        Double worst_survival_value = 0d;
        Double better_survival_value = COURSE_COMPLETED;

        ArrayList<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < individualsQuantity; i++) {
            Individual individual = new Individual(i, individualSize);
            individual.setPrey(generateRandomPrey(individualSize));
            individual.setSurvival_value(generateSurvivalValue(individual.getPrey()));
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
        population.setBestPreyId(best_survival_value_id);
        population.setPredatorId(worst_survival_value_id);
        this.population = population;
    }

    public static int[] generateRandomPrey(int size) {
        int[] prey = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            prey[i] = random.nextInt(2);
        }
        return prey;
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
        int qntt = 0;
        Double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            qntt += individual[i];
        }

        for (int i = 0; i < individual.length; i++) {
            for (Concept concept : concepts) {
                sum += individual[i] * Math.abs((concept.getLMs().contains(LearningMaterials.get(i)) ? 1 : 0) - ((learner.getLearningGoals().contains(concept)) ? 1 : 0));
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }

    // O2
    public Double difficultyObjetiveFunction(int[] individual) {
        Double sum = 0d;
        int qntt = 0;
        for (int i = 0; i < individual.length; i++) {
            sum += individual[i] * Math.abs(LearningMaterials.get(i).getDificulty() - learner.getAbilityLevel());
        }

        for (int i = 0; i < individual.length; i++) {
            qntt += individual[i];
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;

    }

    // O3
    public Double timeObjetiveFunction(int[] individual) {

        int totalTime = 0;

        for (int i = 0; i < individual.length; i++) {
            totalTime += LearningMaterials.get(i).getTypical_learning_time() * individual[i];
        }
        return Math.max(0d, (learner.getLower_time() - totalTime)) + Math.max(0d, (totalTime - learner.getUpper_time()));
    }

    // O4
    public Double balanceObjetiveFunction(int[] individual) {

        Double sum = 0d;
        int learningGoal;

        int relevantConceptsK = 0;
        for (int k = 0; k < individual.length; k++) {
            for (Concept concept_k : concepts) {
                relevantConceptsK += individual[k] * (concept_k.getLMs().contains(LearningMaterials.get(k)) ? 1 : 0);
            }
        }

        int learningGoalL = 0;
        for (Concept concept_l : concepts) {
            learningGoalL += ((learner.getLearningGoals().contains(concept_l)) ? 1 : 0);
        }

        Double div = (double) relevantConceptsK / learningGoalL;

        for (Concept concept : concepts) {
            int relevantConcepts = 0;
            // Elj
            learningGoal = ((learner.getLearningGoals().contains(concept)) ? 1 : 0);
            if (learningGoal == 0) {
                continue;
            }

            for (int i = 0; i < individual.length; i++) {
                relevantConcepts += individual[i] * (concept.getLMs().contains(LearningMaterials.get(i)) ? 1 : 0);
            }

            sum += learningGoal * Math.abs(relevantConcepts - div);

        }

        return sum;
    }
}
