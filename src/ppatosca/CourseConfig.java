package ppatosca;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author gtbavi
 */
public class CourseConfig {
    private final String path;
    private String conceptsFile;
    private String prerequisitesFile;
    private String learningMaterialsFile;
    private String learningMaterialsLOM;
    private String learnersFile;
    private String learnersScoreFile;

    public CourseConfig(String configFile) throws FileNotFoundException, IOException {
        Properties config = new Properties();
        config.load(new FileInputStream(new File(configFile)));
        path = config.getProperty("ppatosca.path");
        learningMaterialsLOM = config.getProperty("ppatosca.path.learningMaterialsLOM");
        
        conceptsFile = path + config.getProperty("ppatosca.file.concepts");
        prerequisitesFile = path + config.getProperty("ppatosca.file.prerequisites");
        learningMaterialsFile = path + config.getProperty("ppatosca.file.learningMaterials");
        learnersFile = path + config.getProperty("ppatosca.file.learners");
        learnersScoreFile = path + config.getProperty("ppatosca.file.learnersScore");
        
    }

    public String getConceptsFile() {
        return conceptsFile;
    }

    public void setConceptsFile(String conceptsFile) {
        this.conceptsFile = conceptsFile;
    }

    public String getPrerequisitesFile() {
        return prerequisitesFile;
    }

    public void setPrerequisitesFile(String prerequisitesFile) {
        this.prerequisitesFile = prerequisitesFile;
    }

    public String getLearningMaterialsFile() {
        return learningMaterialsFile;
    }

    public void setLearningMaterialsFile(String learningMaterialsFile) {
        this.learningMaterialsFile = learningMaterialsFile;
    }

    public String getLearnersFile() {
        return learnersFile;
    }

    public void setLearnersFile(String learnersFile) {
        this.learnersFile = learnersFile;
    }

    public String getLearnersScoreFile() {
        return learnersScoreFile;
    }

    public void setLearnersScoreFile(String learnersScoreFile) {
        this.learnersScoreFile = learnersScoreFile;
    }   

    public String getLearningMaterialsLOM() {
        return learningMaterialsLOM;
    }

    public void setLearningMaterialsLOM(String learningMaterialsLOM) {
        this.learningMaterialsLOM = learningMaterialsLOM;
    }
    
    
}
