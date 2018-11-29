package ppatosca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author gtbavi Class with the main implementation of Prey-Pradator Algorithm
 * for Adaptive Curriculum Sequencing problem
 */
public class PPA extends FitnessFunction {

    private Population population;
    private List<Integer> listToShuffle;
    private int fitnessFunctionSelector;
    SingletonPrint printer = SingletonPrint.getInstance();

    public PPA() {
    }

    public PPA(HashMap<Integer,LearningMaterial> learningMaterials, Learner learner, HashMap<String,Concept> concepts) {
        PPA.learningMaterials = learningMaterials;
        PPA.learner = learner;
        PPA.concepts = concepts;
        this.listToShuffle = Util.initializeShuffleList(PPA.learningMaterials.size());
    }

    public PPA(HashMap<Integer,LearningMaterial> LearningMaterials, Learner learner, HashMap<String,Concept> concepts, Population population) {
        PPA.learningMaterials = LearningMaterials;
        PPA.learner = learner;
        PPA.concepts = concepts;
        this.population = population;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public int getFitnessFunctions() {
        return fitnessFunctionSelector;
    }

    public void setFitnessFunctions(int fitnessFunctions) {
        this.fitnessFunctionSelector = fitnessFunctions;
    }

    /**
     * Update population values after movements, i.e. best prey, predator and
     * order ordinary preys
     *
     * @param maxBestPreyQuantity
     * @param maxPredatorQuantity
     */
    public void updatePopulation(int maxBestPreyQuantity, int maxPredatorQuantity) {
        Map<Integer, Double> populationSurvivalValues = new HashMap<>();
        for (Individual individual : population.getIndividuals()) {
            populationSurvivalValues.put(individual.getId(), individual.getSurvivalValue());
        }
        populationSurvivalValues = Util.sortByValueAsc(populationSurvivalValues);

        Object[] orderedPopulation = populationSurvivalValues.keySet().toArray();
        int bestPreyId = (int) orderedPopulation[0];

        ArrayList<Integer> bestPreysIds = new ArrayList<>();
        Double bestSurvivalValue = populationSurvivalValues.get(bestPreyId);
        // set best prey
        bestPreysIds.add(bestPreyId);
        int bestPreysSize = 1;
        for (int i = 1; i < orderedPopulation.length; i++) {

            if (!Objects.equals(bestSurvivalValue, populationSurvivalValues.get((int) orderedPopulation[i]))) {
                break;
            }
            for (int j = 0; j < bestPreysSize; j++) {

                if (Util.hammingDistance(population.getIndividuals().get((int) orderedPopulation[i]), population.getIndividuals().get(bestPreysIds.get(j))) == 0) {
                    break;
                } else { // Same SV but different structure                               
                    // if prey structure is different of all best preys then put it as a best prey too 
                    if (j == bestPreysSize - 1) {
                        if (bestPreysSize < maxBestPreyQuantity) {
                            bestPreysIds.add((int) orderedPopulation[i]);
                            bestPreysSize++;
                        }
                    }
                }
            }
        }
        population.setBestPreysId(bestPreysIds);
        population.setPredatorId((int) orderedPopulation[population.getIndividuals().size() - 1]);
        ArrayList<Integer> ordinaryPreysIds = new ArrayList<>();

        for (int i = 1; i < orderedPopulation.length - 1; i++) {
            if (!bestPreysIds.contains((int) orderedPopulation[i])) {
                ordinaryPreysIds.add((int) orderedPopulation[i]);
            }
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
     * @param selector select one of the type of movements
     * @param followedPreysQuantity
     * @param followUp
     * @param quantityBestRandomPreys
     * @return an individual after the movement
     * @throws CloneNotSupportedException
     */
    public Individual moveIndividual(Individual prey, Double distanceFactor, Double survivalValueFactor, int minimumStepLength, int maximumStepLength, int selector, int followedPreysQuantity, Double followUp, int quantityBestRandomPreys) throws CloneNotSupportedException {
        if (population.getBestPreysId().contains(prey.getId())) {
            moveBestPrey(prey, minimumStepLength);
        } else if (prey.getId() == population.getPredatorId()) {
            movePredator(prey, minimumStepLength, maximumStepLength, followedPreysQuantity);
        } else {
            movePrey(prey, distanceFactor, survivalValueFactor, minimumStepLength, maximumStepLength, quantityBestRandomPreys, followUp, selector);
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
        individual.setPrey(generateBestRandomDirection(individual.getPrey(), 10, minimumStepLength));
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
    }

    public void movePreySimilarity(Individual individual, Double distanceFactor, Double survivalValueFactor, int minimumStepLength, int maximumStepLength, int randomBestPreyQuantity, Double followUp, int returnSelector) throws CloneNotSupportedException {
        Double rand = new Random().nextDouble();
        Double uniformProbabilityDistribution;
        int stepLength;
        List<Integer> steps;
        Individual predator = population.getIndividuals().get(population.getPredatorId());

        if (rand <= followUp) { // Follow best preys
            Double followValue;
            Map<Integer, Double> followUpValue = new HashMap<>();
            ArrayList<Individual> individuals = population.getIndividuals();
            //FollowUp value the bigger the better = T*sim(xi,xj) + N*(1/ SV(xj))
            for (Individual ind : individuals) {
                if (ind.getSurvivalValue() < individual.getSurvivalValue()) {
                    followValue = Util.round((distanceFactor * Util.cosineSimilarity(individual, ind)) + (survivalValueFactor * (1 / ind.getSurvivalValue())), 2);
                    followUpValue.put(ind.getId(), followValue);
                }
            }

            stepLength = stepLengthBySimilarity(individual, predator, maximumStepLength, 1d);
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
            uniformProbabilityDistribution = new Random().nextDouble();
            stepLength = (int) Math.round(minimumStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength);
            // Generate random prey: yr
            int[] bestRandomPrey = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, minimumStepLength);
            for (int i = 0; i < stepLength; i++) {
                individual.getPrey()[steps.get(i)] = bestRandomPrey[steps.get(i)];
            }
        } else { // Generate to best directions and compare witch is better, i.e. away from predator
            int[] randomDirection = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, minimumStepLength);
            int[] randomDirectionComplement = Util.generateComplementaryVector(randomDirection);
            uniformProbabilityDistribution = new Random().nextDouble();
            stepLength = (int) Math.round(maximumStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength);
            if (Util.cosineSimilarity(randomDirection, predator.getPrey()) <= Util.cosineSimilarity(randomDirectionComplement, predator.getPrey())) {                
                for (int i = 0; i < stepLength; i++) {
                    individual.getPrey()[steps.get(i)] = randomDirection[steps.get(i)];
                }
            } else {
                for (int i = 0; i < stepLength; i++) {
                    individual.getPrey()[steps.get(i)] = randomDirectionComplement[steps.get(i)];
                }
            }
        }
        // Update SV
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
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
        Double distanceComponent;
        Double survivalValueComponent;
        Individual predator = population.getIndividuals().get(population.getPredatorId());
        List<Integer> steps;

        if (rand <= followUp) { // Follow best preys
            Double followValue;
            Map<Integer, Double> followUpValue = new HashMap<>();
            ArrayList<Individual> individuals = population.getIndividuals();
            //FollowUp value the bigger the better 
            for (Individual followedIndividual : individuals) {
                if (followedIndividual.getSurvivalValue() < individual.getSurvivalValue()) {
                    // T*(1/hammingDistance(xi,xj)) + N*(1/SV(xj))
                    distanceComponent = Util.hammingDistance(individual, followedIndividual) / (double) individual.getSize();
                    survivalValueComponent = followedIndividual.getSurvivalValue() / individual.getSurvivalValue();
                    followValue = (2 + (-distanceFactor * distanceComponent - survivalValueFactor * survivalValueComponent)) / 2;
                    followUpValue.put(followedIndividual.getId(), followValue);
                }
            }
            stepLength = stepLengthByHamming(individual, predator, maximumStepLength, 1d);
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

            uniformProbabilityDistribution = new Random().nextDouble();
            stepLength = (int) Math.round(minimumStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength);
            // Generate random prey: yr
            int[] bestRandomPrey = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, minimumStepLength);
            for (int i = 0; i < stepLength; i++) {
                individual.getPrey()[steps.get(i)] = bestRandomPrey[steps.get(i)];
            }

        } else { // Generate to best directions and compare witch is better, i.e. away from predator            
            int[] randomDirection = generateBestRandomDirection(individual.getPrey(), randomBestPreyQuantity, minimumStepLength);
            int[] randomDirectionComplement = Util.generateComplementaryVector(randomDirection);
            uniformProbabilityDistribution = new Random().nextDouble();
            stepLength = (int) Math.round(maximumStepLength * uniformProbabilityDistribution);
            steps = shuffleSteps(stepLength);            
            if (Util.hammingDistance(randomDirection, predator.getPrey()) >= Util.hammingDistance(randomDirectionComplement, predator.getPrey())) {
                for (int i = 0; i < stepLength; i++) {
                    individual.getPrey()[steps.get(i)] = randomDirection[steps.get(i)];
                }
            } else {
                for (int i = 0; i < stepLength; i++) {
                    individual.getPrey()[steps.get(i)] = randomDirectionComplement[steps.get(i)];
                }
            }
        }

        // Update SV
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
    }

    /**
     *
     * @param predator predator representation
     * @param minimumStepLength minimum quantity of changing bits
     * @param maximumStepLength maximum quantity of changing bits
     * @param followedPreysQuantity the number of worst preys predator will
     * follow
     */
    public void movePredator(Individual predator, int minimumStepLength, int maximumStepLength, int followedPreysQuantity) {        
        // Position of the second worst prey
        int followedPreyPosition = population.getOrdinaryPreysIds().size() - 1;
        // Second worst prey
        Individual followedPrey = population.getIndividuals().get(population.getOrdinaryPreysIds().get(followedPreyPosition));

        int[] randomDirection = generateRandomPrey(followedPrey.getSize());
        Double uniformProbabilityDistribution = new Random().nextDouble();

        int stepLength;
        List<Integer> steps;
        // lambda_max * [0..1]
        stepLength = (int) Math.round(maximumStepLength * uniformProbabilityDistribution);
        steps = shuffleSteps(stepLength);        
        //Random Direction yr
        for (int i = 0; i < stepLength; i++) {
            predator.getPrey()[steps.get(i)] = randomDirection[steps.get(i)];
        }        
        uniformProbabilityDistribution = new Random().nextDouble();
        stepLength = (int) Math.round(minimumStepLength * uniformProbabilityDistribution);
        steps = shuffleSteps(stepLength);
        //Following the second worst prey        
        for (int i = 0; i < stepLength; i++) {
            predator.getPrey()[steps.get(i)] = followedPrey.getPrey()[steps.get(i)];
        }

        predator.setSurvivalValue(generateSurvivalValue(predator.getPrey()));
    }

    /**
     *
     * @param prey
     * @param predator
     * @param maximumStepLength
     * @param survivalValueFactor
     * @return quantity of bits to be changed (Step Length)
     */
    public int stepLengthByHamming(Individual prey, Individual predator, int maximumStepLength, Double survivalValueFactor) {
        // (lambda_max * [0..1])/e^[N*(1-sim(xi,xpredator))]
        Double div = Math.pow(Math.E, survivalValueFactor * (Util.hammingDistance(prey, predator) / (double) prey.getSize()));
        Double uniformProbabilityDistribution = new Random().nextDouble();
        Double sup = maximumStepLength * uniformProbabilityDistribution;
        return (int) Math.round(sup / div);
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
        Double div = Math.pow(Math.E, survivalValueFactor * (1 - Util.cosineSimilarity(prey, predator)));
        Double uniformProbabilityDistribution = new Random().nextDouble();
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
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
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
        individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));
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
        boolean isBetter = false; // debug

        for (int i = 0; i < randomIndividualsQuantity; i++) {
            int[] newPrey = prey.clone();
            randomDirection = generateRandomPrey(prey.length);
            uniformProbabilityDistribution = new Random().nextDouble();
            stepLength = (int) Math.round(uniformProbabilityDistribution * minimumStepLength);
            steps = shuffleSteps(stepLength);
            for (int j = 0; j < stepLength; j++) {
                newPrey[steps.get(j)] = randomDirection[steps.get(j)];
            }
            partialSurvivalValue = generateSurvivalValue(newPrey);
            if (partialSurvivalValue < bestSurvivalValue) {
                isBetter = true; // debug
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

    public void generatePopulation(int individualsQuantity, int individualSize) {
        int worst_survival_value_id = 0;
        int best_survival_value_id = 0;
        Double worst_survival_value = 0d;
        Double better_survival_value = COURSE_COMPLETED;

        ArrayList<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < individualsQuantity; i++) {
            Individual individual = new Individual(i, individualSize);
            individual.setPrey(generateRandomPrey(individualSize)); //usefull            
            individual.setSurvivalValue(generateSurvivalValue(individual.getPrey()));            
            individuals.add(individual);
        }
        Population pop = new Population(individuals);
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

    public Double generateSurvivalValue(int[] prey) {

        switch (fitnessFunctionSelector) {
            case 1:
                //Concepts
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey)), 2);
            case 2:
                // Difficulty
                return Util.round(executeFitnessFunction(difficultyObjetiveFunction(prey)), 2);
            case 3:
                // Concepts + Difficulty
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), difficultyObjetiveFunction(prey)), 2);
            case 4:
                // Concepts + Balance
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), balanceObjetiveFunction(prey)), 2);
            case 5:
                // Concepts + time
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), timeObjetiveFunction(prey)), 2);
            case 6:
                // Concepts + Balance + Difficulty
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), balanceObjetiveFunction(prey), difficultyObjetiveFunction(prey)), 2);
            case 7:
                // Concepts + Balance + Time
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), balanceObjetiveFunction(prey), timeObjetiveFunction(prey)), 2);
            case 8:
                // Concepts + Balance + difficulty + Time
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), timeObjetiveFunction(prey), balanceObjetiveFunction(prey), difficultyObjetiveFunction(prey)), 2);
            case 9:
                 // Concepts + Balance + difficulty + Time
                
                //3ª Semana return Util.round(executeFitnessFunction(0.4*conceptsObjetiveFunction(prey), 0.001*timeObjetiveFunction(prey), 0.2*balanceObjetiveFunction(prey), 0.2*difficultyObjetiveFunction(prey),0.2*learningStyleObjetiveFunction(learningStyleActiveReflexiveObjetiveFunction(prey),learningStyleSensoryIntuitiveObjetiveFunction(prey),learningStyleVisualVerbalObjetiveFunction(prey),learningStyleSequentialGlobalObjetiveFunction(prey))), 3);
                // 4ª semana    
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), (1d/600d)*timeObjetiveFunction(prey), balanceObjetiveFunction(prey), difficultyObjetiveFunction(prey),learningStyleObjetiveFunction(learningStyleActiveReflexiveObjetiveFunction(prey),learningStyleSensoryIntuitiveObjetiveFunction(prey),learningStyleVisualVerbalObjetiveFunction(prey),learningStyleSequentialGlobalObjetiveFunction(prey))), 3);
            case 10: 
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey), (1d/600d)*timeObjetiveFunction(prey), learningStyleObjetiveFunction(learningStyleActiveReflexiveObjetiveFunction(prey),learningStyleSensoryIntuitiveObjetiveFunction(prey),learningStyleVisualVerbalObjetiveFunction(prey),learningStyleSequentialGlobalObjetiveFunction(prey))), 3);
            case 11: 
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey),difficultyObjetiveFunction(prey), (1d/600d)*timeObjetiveFunction(prey), learningStyleObjetiveFunction(learningStyleActiveReflexiveObjetiveFunction(prey),learningStyleSensoryIntuitiveObjetiveFunction(prey),learningStyleVisualVerbalObjetiveFunction(prey),learningStyleSequentialGlobalObjetiveFunction(prey))), 3);
            case 12: 
                return Util.round(executeFitnessFunction(conceptsObjetiveFunction(prey),difficultyObjetiveFunction(prey), balanceObjetiveFunction(prey), (1d/600d)*timeObjetiveFunction(prey), learningStyleObjetiveFunction(learningStyleActiveReflexiveObjetiveFunction(prey),learningStyleSensoryIntuitiveObjetiveFunction(prey),learningStyleVisualVerbalObjetiveFunction(prey),learningStyleSequentialGlobalObjetiveFunction(prey))), 3);
            default:
                // Concepts + Balance + difficulty + Time
                return Util.round(executeFitnessFunction(0.25*conceptsObjetiveFunction(prey), 0.25*timeObjetiveFunction(prey), balanceObjetiveFunction(prey), difficultyObjetiveFunction(prey)), 2);
        }

    }
}
