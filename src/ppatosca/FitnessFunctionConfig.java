package ppatosca;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author gtbavi
 */
public class FitnessFunctionConfig {
    
    private int fitnessFunction;
    
    public FitnessFunctionConfig(String configFile) throws IOException {
        Properties config = new Properties();
        config.load(new FileInputStream(new File(configFile)));
        fitnessFunction = Integer.parseInt(config.getProperty("ppatosca.fitness.number"));  
    }

    public int getFitnessFunction() {
        return fitnessFunction;
    }

    public void setFitnessFunction(int fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }
    

    
}
