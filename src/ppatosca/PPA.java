package ppatosca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
            populationSurvivalValues.put(individual.getId(), individual.getSurvivalValue());
        }
        populationSurvivalValues = Util.sortByValueAsc(populationSurvivalValues);

        Object[] orderedPopulation = populationSurvivalValues.keySet().toArray();
        population.setBestPreyId((int) orderedPopulation[0]);
        population.setPredatorId((int) orderedPopulation[population.getIndividuals().size() - 1]);

        int[] ordinaryPreysIds = new int[orderedPopulation.length - 2];
        for (int i = 1; i <= ordinaryPreysIds.length; i++) {
            ordinaryPreysIds[i - 1] = (int) orderedPopulation[i];
        }
        population.setOrdinaryPreysIds(ordinaryPreysIds);

    }

    public Individual moveIndividual(int prey_id, Double distanceFactor, Double survivalValueFactor, int minimalStepLength, int maximumStepLength, int returnSelector) throws CloneNotSupportedException {
        Individual individual = population.getIndividuals().get(prey_id);
        if (prey_id == population.getBestPreyId()) {
            //localSearch(individual);
            moveBestPrey(individual, minimalStepLength);
        } else if (prey_id == population.getPredatorId()) {
            movePredator(individual, minimalStepLength, maximumStepLength);
            //movePrey(individual, distanceFactor, survivalValueFactor, minimalStepLength, maximumStepLength, 10, returnSelector);
        } else {
            movePrey(individual, distanceFactor, survivalValueFactor, minimalStepLength, maximumStepLength, 10, 0.5d, returnSelector);
            //localSearch(individual);
        }

        return individual;
    }

    public void moveBestPrey(Individual individual, int minimalStepLength) throws CloneNotSupportedException {
        System.out.println(individual.toString());
        individual.setPrey(generateBestRandomDirection(individual.getPrey(), 10, minimalStepLength));
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
        System.out.println(individual.toString());
    }

    public void movePrey(Individual individual, Double distance_factor, Double survival_value_factor, int minimalStepLength, int maximumStepLength, int randomBestPreyQuantity, Double followUp, int returnSelector) throws CloneNotSupportedException {
        Double rand = new Random().nextDouble();
        Double uniformProbabilityDistribution;
        int stepLength;
        List<Integer> steps;
        if (rand <= followUp) {
            System.out.println("Following preys");
            int[] bestRandomPrey = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, minimalStepLength);

            Map<Integer, Double> followUpValue = new HashMap<>();
            ArrayList<Individual> individuals = population.getIndividuals();
            Double followValue;
            for (Individual ind : individuals) {
                if (ind.getSurvivalValue() < individual.getSurvivalValue()) {
                    followValue = Util.round((distance_factor * Util.similarity(individual, ind)) + (survival_value_factor * (1 / ind.getSurvivalValue())), 2);
                    followUpValue.put(ind.getId(), followValue);
                    System.out.println("seguida: " + ind.getId() + " follow up: " + followValue);
                }
            }

            stepLength = stepLengthBySimilarity(individual, population.getIndividuals().get(population.getPredatorId()), maximumStepLength, 1d);
            switch (returnSelector) {
                case 1:
                    moveDirectionByRoulletResult(individual, followUpValue, stepLength);
                    break;
                case 2:
                    moveDirectionByMajority(individual, followUpValue, stepLength);
                    break;
                default:
                    moveDirectionByMajority(individual, followUpValue, stepLength);
            }
            uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);
            stepLength = (int) Math.round(minimalStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength, individual.getSize());
            for (int i = 0; i < stepLength; i++) {
                individual.getPrey()[steps.get(i)] = bestRandomPrey[steps.get(i)];
            }
        } else {
            System.out.println("Moving away from predator");
            int[] randomDirection1 = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, maximumStepLength);
            int[] randomDirection2 = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, maximumStepLength);
            int[] predator = population.getIndividuals().get(population.getPredatorId()).getPrey();
            uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);
            stepLength = (int) Math.round(maximumStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength, individual.getSize());
            if (Util.similarity(randomDirection1, predator) <= Util.similarity(randomDirection2, predator)) {
                for (int i = 0; i < stepLength; i++) {
                    individual.getPrey()[steps.get(i)] = randomDirection1[steps.get(i)];
                }
            }
            else{
                 for (int i = 0; i < stepLength; i++) {
                    individual.getPrey()[steps.get(i)] = randomDirection2[steps.get(i)];
                }
            }
        }

        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));

    }

    public void movePredator(Individual predator, int minimalStepLength, int maximumStepLength) throws CloneNotSupportedException {
        // Position of the second worst prey
        int followedPreyPosition = population.getOrdinaryPreysIds().length - 1;
        // Second worst prey
        Individual followedPrey = population.getIndividuals().get(population.getOrdinaryPreysIds()[followedPreyPosition]);

        int[] randomDirection = generateRandomPrey(followedPrey.getSize());
        Double uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);

        int stepLength;
        List<Integer> steps;
        // lambda_max * [0..1]
        stepLength = (int) Math.round(maximumStepLength * uniformProbabilityDistribution);
        steps = shuffleSteps(stepLength, followedPrey.getSize());
        //Random Direction
        for (int i = 0; i < stepLength; i++) {
            predator.getPrey()[steps.get(i)] = randomDirection[steps.get(i)];
        }

        // |sim -1| x lambda_min x [0..1]
        //int stepLength = (int) Math.round(Math.abs(similarity - 1) * minimalStepLength * uniformProbabilityDistribution);
        Double similarity = Util.similarity(predator, followedPrey);
        if (similarity != 1d) {
            uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);
            stepLength = (int) Math.round(minimalStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength, followedPrey.getSize());

            //Following the second worst prey
            for (int i = 0; i < stepLength; i++) {
                predator.getPrey()[steps.get(i)] = followedPrey.getPrey()[steps.get(i)];
            }
        }

        predator.setSurvivalValue(generateSurvivalValue(predator.getPrey()));

    }

    /**
     *
     * @param prey
     * @param predator
     * @param maximumStepLength
     * @param survivalValueFactor
     * @return
     */
    public int stepLengthBySimilarity(Individual prey, Individual predator, int maximumStepLength, Double survivalValueFactor) {
        Double div = Math.pow(Math.E, survivalValueFactor * (1 - Util.similarity(prey, predator)));
        Double uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);
        Double sup = maximumStepLength * uniformProbabilityDistribution;
        return (int) Math.round(sup / div);
    }

    /**
     *
     * @param individual
     * @param followUpValue
     * @return
     */
    private void moveDirectionByRoulletResult(Individual individual, Map<Integer, Double> followUpValue, int stepLength) {
        int[] roullet = createRoullet(followUpValue);
        List<Integer> steps = shuffleSteps(stepLength, individual.getPrey().length);
        ArrayList<Individual> individuals = population.getIndividuals();
        int followedPrey;
        for (int i = 0; i < stepLength; i++) {
            followedPrey = roulletResult(roullet);
            if (followedPrey != -1) {
                individual.getPrey()[steps.get(i)] = individuals.get(followedPrey).getPrey()[steps.get(i)];
            }
        }
    }

    /**
     *
     * @param individual
     * @param followUpValue
     * @return
     */
    private void moveDirectionByMajority(Individual individual, Map<Integer, Double> followUpValue, int stepLength) {
        int counter_0;
        List<Integer> steps = shuffleSteps(stepLength, individual.getPrey().length);
        for (int i = 0; i < stepLength; i++) {
            counter_0 = 0;
            for (Integer individuals_id : followUpValue.keySet()) {
                if (population.getIndividuals().get(individuals_id).getPrey()[steps.get(i)] == 0) {
                    counter_0++;
                } else {
                    counter_0--;
                }
            }
            if (counter_0 > 0) {
                individual.getPrey()[steps.get(i)] = 0;
            } else if (counter_0 < 0) {
                individual.getPrey()[steps.get(i)] = 1;
            }
            //else keep the same value
        }
    }

    //TODO do not initialize list in every prey movement (parameter)
    private List<Integer> shuffleSteps(int stepLength, int individualSize) {
        List<Integer> individualSizeList = new ArrayList<>();

        for (int i = 0; i < individualSize; i++) {
            individualSizeList.add(i);
        }
        Collections.shuffle(individualSizeList);
        return individualSizeList.subList(0, stepLength);
    }

    /**
     *
     * @param prey_id
     * @param randomIndividualsQuantity
     * @return
     */
    private int[] generateBestRandomDirection(int[] prey, int randomIndividualsQuantity, int minimalStepLength) throws CloneNotSupportedException {
        int[] randomDirection;
        Double uniformProbabilityDistribution;
        int stepLength;
        List<Integer> steps;
        int[] bestPrey = prey.clone();
        Double partialSurvivalValue;
        Double bestSurvivalValue = generateSurvivalValue(prey);
        for (int i = 0; i < randomIndividualsQuantity; i++) {
            int[] newPrey = prey.clone();
            randomDirection = generateRandomPrey(prey.length);
            uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);
            stepLength = (int) Math.round(uniformProbabilityDistribution * minimalStepLength);
            steps = shuffleSteps(stepLength, prey.length);
            for (int j = 0; j < stepLength; j++) {
                newPrey[steps.get(j)] = randomDirection[steps.get(j)];
            }
            partialSurvivalValue = generateSurvivalValue(newPrey);
            if (partialSurvivalValue < bestSurvivalValue) {
                bestSurvivalValue = partialSurvivalValue;
                bestPrey = newPrey;
            }

        }
        return bestPrey;
    }

    /**
     * Change each bit and calculate the fitness value
     *
     * @param prey_id
     * @param direction_length
     * @return
     */
    private void localSearch(Individual individual) {
        int size = individual.getSize();
        int[] newPrey = individual.getPrey();
        int[] auxPrey;
        Double newSurvivalValue = individual.getSurvivalValue();
        Double auxSurvivalValue;

        for (int i = 0; i < size; i++) {

            auxPrey = individual.getPrey().clone();
            if (auxPrey[i] == 0) {
                auxPrey[i] = 1;
            } else {
                auxPrey[i] = 0;
            }

            auxSurvivalValue = generateSurvivalValue(auxPrey);
            if (auxSurvivalValue < newSurvivalValue) {
                newSurvivalValue = auxSurvivalValue;
                newPrey = auxPrey;
            }
        }
        individual.setPrey(newPrey);
        individual.setSurvivalValue(newSurvivalValue);
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
        if (roullet.length == 0) {
            return -1;
        }
        return roullet[new Random().nextInt(roullet.length)];
    }

    public Individual moveDirectionBestPrey(int preyId) {
        return new Individual(preyId, preyId);
    }

    public Double generateSurvivalValue(int[] prey) {
        return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey),
                difficultyObjetiveFunction(prey),
                timeObjetiveFunction(prey),
                balanceObjetiveFunction(prey)), 2);
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
            individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
            individuals.add(individual);

            if (individual.getSurvivalValue() > worst_survival_value) {
                worst_survival_value = individual.getSurvivalValue();
                worst_survival_value_id = i;
            }
            if (individual.getSurvivalValue() < better_survival_value) {
                better_survival_value = individual.getSurvivalValue();
                best_survival_value_id = i;
            }

        }
        Population pop = new Population(individuals);
        pop.setBestPreyId(best_survival_value_id);
        pop.setPredatorId(worst_survival_value_id);
        this.population = pop;
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
