package ppatosca;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author gtbavi
 */
public class PreyPredatorAlgorithmConfig {

    private int movementQuantity;
    private int populationSize;
    private Double distanceFactor;
    private Double survivalValueFactor;
    private int minimumStepLength;
    private int maximumStepLength;
    private int followedPreysQuantity;
    private Double followUp;
    private int quantityBestRandomPreys;
    private int maxBestPreyQuantity;
    private int maxPredatorQuantity;

    public PreyPredatorAlgorithmConfig(String configFile) throws IOException {
        Properties config = new Properties();
        config.load(new FileInputStream(new File(configFile)));
        this.movementQuantity = Integer.parseInt(config.getProperty("ppatosca.arg.movementQuantity", "100"));
        this.populationSize = Integer.parseInt(config.getProperty("ppatosca.arg.populationSize", "10"));
        this.distanceFactor = Double.parseDouble(config.getProperty("ppatosca.arg.distanceFactor","1"));
        this.survivalValueFactor = Double.parseDouble(config.getProperty("ppatosca.arg.survivalValueFactor","1"));
        this.maximumStepLength = Integer.parseInt(config.getProperty("ppatosca.arg.minimumStepLength"));
        this.maximumStepLength = Integer.parseInt(config.getProperty("ppatosca.arg.maximumStepLength"));
        this.followedPreysQuantity = Integer.parseInt(config.getProperty("ppatosca.arg.followedPreysQuantity"));
        this.followUp = Double.parseDouble(config.getProperty("ppatosca.arg.followUp"));
        this.quantityBestRandomPreys = Integer.parseInt(config.getProperty("ppatosca.arg.quantityBestRandomPreys"));
        this.maxBestPreyQuantity = Integer.parseInt(config.getProperty("ppatosca.arg.maxBestPreyQuantity","1"));
        this.maxPredatorQuantity = Integer.parseInt(config.getProperty("ppatosca.arg.maxPredatorQuantity","1"));

    }

    public PreyPredatorAlgorithmConfig() {
    }
    
    

    public int getMovementQuantity() {
        return movementQuantity;
    }

    public void setMovementQuantity(int movementQuantity) {
        this.movementQuantity = movementQuantity;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public Double getDistanceFactor() {
        return distanceFactor;
    }

    public void setDistanceFactor(Double distanceFactor) {
        this.distanceFactor = distanceFactor;
    }

    public Double getSurvivalValueFactor() {
        return survivalValueFactor;
    }

    public void setSurvivalValueFactor(Double survivalValueFactor) {
        this.survivalValueFactor = survivalValueFactor;
    }

    public int getMinimumStepLength() {
        return minimumStepLength;
    }

    public void setMinimumStepLength(int minimumStepLength) {
        this.minimumStepLength = minimumStepLength;
    }

    public int getMaximumStepLength() {
        return maximumStepLength;
    }

    public void setMaximumStepLength(int maximumStepLength) {
        this.maximumStepLength = maximumStepLength;
    }

    public int getFollowedPreysQuantity() {
        return followedPreysQuantity;
    }

    public void setFollowedPreysQuantity(int followedPreysQuantity) {
        this.followedPreysQuantity = followedPreysQuantity;
    }

    public Double getFollowUp() {
        return followUp;
    }

    public void setFollowUp(Double followUp) {
        this.followUp = followUp;
    }

    public int getQuantityBestRandomPreys() {
        return quantityBestRandomPreys;
    }

    public void setQuantityBestRandomPreys(int quantityBestRandomPreys) {
        this.quantityBestRandomPreys = quantityBestRandomPreys;
    }

    public int getMaxBestPreyQuantity() {
        return maxBestPreyQuantity;
    }

    public void setMaxBestPreyQuantity(int maxBestPreyQuantity) {
        this.maxBestPreyQuantity = maxBestPreyQuantity;
    }

    public int getMaxPredatorQuantity() {
        return maxPredatorQuantity;
    }

    public void setMaxPredatorQuantity(int maxPredatorQuantity) {
        this.maxPredatorQuantity = maxPredatorQuantity;
    }
    
    

}
