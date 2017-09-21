package ppatosca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author gtbavi Class with the main implementation of Prey-Pradator Algorithm
 * for Adaptive Course Generation
 */
public class PPA {

    private static final Double COURSE_COMPLETED = 999999999d;
    private ArrayList<LearningMaterial> LearningMaterials;
    private Learner learner;
    private ArrayList<Concept> concepts;
    private Population population;
    private List<Integer> listToShuffle;

    public PPA(ArrayList<LearningMaterial> LearningMaterials, Learner learner, ArrayList<Concept> concepts) {
        this.LearningMaterials = LearningMaterials;
        this.learner = learner;
        this.concepts = concepts;
        this.listToShuffle = Util.initializeShuffleList(this.LearningMaterials.size());
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
     * Update population values after movements, i.e. best prey, predator and
     * order ordinary preys
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

    /**
     * Method called any time a individual needs to move
     *
     * @param prey moving individual
     * @param distanceFactor importance of distance for movement
     * @param survivalValueFactor importance of survival value for movement
     * @param minimumStepLength minimum quantity of changing bits
     * @param maximumStepLength maximum quantity of changing bits
     * @param returnSelector select one of the type of movements
     * @return an individual after the movement
     * @throws CloneNotSupportedException
     */
    public Individual moveIndividual(Individual prey, Double distanceFactor, Double survivalValueFactor, int minimumStepLength, int maximumStepLength, int returnSelector) throws CloneNotSupportedException {
        if (prey.getId() == population.getBestPreyId()) {
            System.out.println("A presa que irá movimentar neste momento é a melhor presa: " + prey.getId());
            //localSearch(individual);
            moveBestPrey(prey, minimumStepLength);
            System.out.println("");
        } else if (prey.getId() == population.getPredatorId()) {
            System.out.println("Movendo o predador: " + prey.getId());
            movePredator(prey, minimumStepLength, maximumStepLength);
            System.out.println("");
        } else {
            System.out.println("Presa comum se movimentando: " + prey.getId());
            movePrey(prey, distanceFactor, survivalValueFactor, minimumStepLength, maximumStepLength, 10, 0.5d, returnSelector);
            System.out.println("");
            //localSearch(individual);
        }

        return prey;
    }

    /**
     * When the individual is the best prey it just do a local search
     *
     * @param individual
     * @param minimumStepLength
     * @throws CloneNotSupportedException
     */
    public void moveBestPrey(Individual individual, int minimumStepLength) throws CloneNotSupportedException {
        System.out.println("Este é seu vetor binário:");
        System.out.println(individual.toString());
        int[] vet1 = individual.getPrey().clone(); //Apenas para debbug
        System.out.println("Gerando vetor randômico com melhor valor de sobrevivência:");
        individual.setPrey(generateBestRandomDirection(individual.getPrey(), 10, minimumStepLength));
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
        System.out.println(individual.toString());
        System.out.println(Util.diff(vet1, individual.getPrey()));
    }

    /**
     *
     * @param individual moving individual
     * @param distanceFactor importance of survival value for movement
     * @param survivalValueFactor importance of survival value for movement
     * @param minimumStepLength minimum quantity of changing bits
     * @param maximumStepLength maximum quantity of changing bits
     * @param randomBestPreyQuantity number of random generations to be tested
     * to be better than individual
     * @param followUp a number between 0 and 1 to decide if the prey will
     * follow better preys or move in a random direction away from predator
     * @param returnSelector select one of the type of movements
     * @throws CloneNotSupportedException
     */
    public void movePrey(Individual individual, Double distanceFactor, Double survivalValueFactor, int minimumStepLength, int maximumStepLength, int randomBestPreyQuantity, Double followUp, int returnSelector) throws CloneNotSupportedException {
        Double rand = new Random().nextDouble();
        Double uniformProbabilityDistribution;
        int stepLength;
        List<Integer> steps;
        if (rand <= followUp) { // Follow best preys
            System.out.println("Esta presa irá se movimentar seguindo as melhores presas");
            System.out.println(individual.toString());
            // Generate random preys: yr
            int[] bestRandomPrey = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, minimumStepLength);            
            Double followValue;
            Map<Integer, Double> followUpValue = new HashMap<>();
            ArrayList<Individual> individuals = population.getIndividuals();
            //FollowUp value the bigger the better = T*sim(xi,xj) + N*(1/ SV(xj))
            for (Individual ind : individuals) {
                if (ind.getSurvivalValue() < individual.getSurvivalValue()) {
                    followValue = Util.round((distanceFactor * Util.similarity(individual, ind)) + (survivalValueFactor * (1 / ind.getSurvivalValue())), 2);
                    followUpValue.put(ind.getId(), followValue);
                    //System.out.println("seguida: " + ind.getId() + " follow up: " + followValue);
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
            stepLength = (int) Math.round(minimumStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength);
            System.out.println("Melhor presa gerada (yr)");
            Util.printPrey(bestRandomPrey);
            System.out.println("Seguindo melhor presa randômica (yr) em "+stepLength+" passos.");
            int[] vet1 = individual.getPrey().clone(); //Apenas para debbug
            Util.printPrey(vet1);
            for (int i = 0; i < stepLength; i++) {
                individual.getPrey()[steps.get(i)] = bestRandomPrey[steps.get(i)];
            }
            System.out.println(Util.diff(vet1, individual.getPrey()));
            Util.printPrey(individual.getPrey());
        } else { // Generate to best directions and compare witch is better, i.e. away from predator
            System.out.println("Essa presa está fugindo do predador (yr e -yr)");
            int[] randomDirection1 = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, minimumStepLength);
            int[] randomDirection2 = generateComplementaryVector(randomDirection1);
            int[] predator = population.getIndividuals().get(population.getPredatorId()).getPrey();
            // [0..1]
            //uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);
            // lambda_max * [0..1]
            //stepLength = (int) Math.round(maximumStepLength * uniformProbabilityDistribution);
            //steps = shuffleSteps(stepLength);
            int[] vet1 = individual.getPrey().clone(); //Apenas para debbug
            System.out.println(individual.toString());
            if (Util.similarity(randomDirection1, predator) <= Util.similarity(randomDirection2, predator)) {
                individual.setPrey(randomDirection1);
            } else {
                individual.setPrey(randomDirection2);

            }
            Util.printPrey(individual.getPrey());
            System.out.println(Util.diff(vet1, individual.getPrey()));
        }

        // Update SV
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
        System.out.println("Novo valor de sobrevivência " + individual.getSurvivalValue());

    }

    /**
     *
     * @param predator predator representation
     * @param minimumStepLength minimum quantity of changing bits
     * @param maximumStepLength maximum quantity of changing bits
     */
    public void movePredator(Individual predator, int minimumStepLength, int maximumStepLength) {
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
        steps = shuffleSteps(stepLength);

        System.out.println("Direção randômica seguida em " + stepLength + " passos");
        //Random Direction yr
        for (int i = 0; i < stepLength; i++) {
            predator.getPrey()[steps.get(i)] = randomDirection[steps.get(i)];
        }
        // |sim -1| x lambda_min x [0..1]
        //int stepLength = (int) Math.round(Math.abs(similarity - 1) * minimumStepLength * uniformProbabilityDistribution);
        Double similarity = Util.similarity(predator, followedPrey);
        uniformProbabilityDistribution = Util.round(new Random().nextDouble(), 1);
        stepLength = (int) Math.round(Math.abs(similarity - 1) * minimumStepLength * uniformProbabilityDistribution);
        //stepLength = (int) Math.round(minimumStepLength * uniformProbabilityDistribution);
        steps = shuffleSteps(stepLength);
        System.out.println("Seguindo segunda pior presa " + followedPrey.getId() + " em " + stepLength + " passos");
        //Following the second worst prey        
        for (int i = 0; i < stepLength; i++) {
            predator.getPrey()[steps.get(i)] = followedPrey.getPrey()[steps.get(i)];
        }

        predator.setSurvivalValue(generateSurvivalValue(predator.getPrey()));
        System.out.println(predator.toString());

    }

    /**
     *
     * @param prey
     * @param predator
     * @param maximumStepLength
     * @param survivalValueFactor
     * @return quantity of bits to be changed (Step Length)
     */
    public int stepLengthBySimilarity(Individual prey, Individual predator, int maximumStepLength, Double survivalValueFactor) {
        // (lambda_max * [0..1])/e^[N*(1-sim(xi,xpredator))]
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
        List<Integer> steps = shuffleSteps(stepLength);
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
        List<Integer> steps = shuffleSteps(stepLength);
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

    private List<Integer> shuffleSteps(int stepLength) {
        Collections.shuffle(listToShuffle);
        return listToShuffle.subList(0, stepLength);
    }

    /**
     *
     * @param prey_id
     * @param randomIndividualsQuantity
     * @return random prey with grater or equal SV than parameter prey_id
     */
    private int[] generateBestRandomDirection2(int[] prey, int randomIndividualsQuantity) throws CloneNotSupportedException {
        int[] bestPrey = prey.clone();
        Double partialSurvivalValue;
        Double bestSurvivalValue = generateSurvivalValue(prey);
        for (int i = 0; i < randomIndividualsQuantity; i++) {
            int[] newPrey = generateRandomPrey(prey.length);
            partialSurvivalValue = generateSurvivalValue(newPrey);
            if (partialSurvivalValue < bestSurvivalValue) {
                bestSurvivalValue = partialSurvivalValue;
                bestPrey = newPrey;
            }

        }
        return bestPrey;
    }

    /**
     *
     * @param prey_id
     * @param randomIndividualsQuantity
     * @return random prey with grater or equal SV than parameter prey_id
     */
    private int[] generateBestRandomDirection(int[] prey, int randomIndividualsQuantity, int minimumStepLength) throws CloneNotSupportedException {
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
            stepLength = (int) Math.round(uniformProbabilityDistribution * minimumStepLength);
            steps = shuffleSteps(stepLength);
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
     * @param individual
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

    public int[] generateComplementaryVector(int[] vector) {
        int[] complementaryVector = new int[vector.length];
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] == 0) {
                complementaryVector[i] = 1;
            } else {
                complementaryVector[i] = 0;
            }
        }
        return complementaryVector;
    }

    public Double generateSurvivalValue(int[] prey) {
        return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), difficultyObjetiveFunction(prey), timeObjetiveFunction(prey), balanceObjetiveFunction(prey)), 2);
        //return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), difficultyObjetiveFunction(prey), timeObjetiveFunction(prey)), 2);
        //return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), difficultyObjetiveFunction(prey)), 2);
        //return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), 2);
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
