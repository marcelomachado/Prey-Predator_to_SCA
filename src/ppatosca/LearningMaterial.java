package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class LearningMaterial {
    private int id;
    // LOM attributes
    private String name;
    private String type;    
    private int typical_learning_time;
    private Double dificulty;
    private String learningResourceType;
    private String interativityLevel;
    private String interativityType;
    // noLOM
    private ArrayList<Concept> coveredConcepts;
    
    public LearningMaterial() {
    }

    public LearningMaterial(int id, String name, String type, int typical_learning_time, Double dificulty, String interativityLevel, String interativityType) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.typical_learning_time = typical_learning_time;
        this.dificulty = dificulty;
        this.interativityLevel = interativityLevel;
        this.interativityType = interativityType;
        
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDificulty() {
        return dificulty;
    }

    public void setDificulty(Double dificulty) {
        this.dificulty = dificulty;
    }

    public int getTypical_learning_time() {
        return typical_learning_time;
    }

    public void setTypical_learning_time(int typical_learning_time) {
        this.typical_learning_time = typical_learning_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Concept> getCoveredConcepts() {
        return coveredConcepts;
    }

    public void setCoveredConcepts(ArrayList<Concept> coveredConcepts) {
        this.coveredConcepts = coveredConcepts;
    }

    public String getLearningResourceType() {
        return learningResourceType;
    }

    public void setLearningResourceType(String learningResourceType) {
        this.learningResourceType = learningResourceType;
    }

    public String getInterativityLevel() {
        return interativityLevel;
    }

    public void setInterativityLevel(String interativityLevel) {
        this.interativityLevel = interativityLevel;
    }

    public String getInterativityType() {
        return interativityType;
    }

    public void setInterativityType(String interativityType) {
        this.interativityType = interativityType;
    }
    
    
    @Override
    public String toString() {
        return "LearningMaterial{" + "id=" + id + ", name=" + name + ", type=" + type + ", typical_learning_time=" + typical_learning_time + ", dificulty=" + dificulty + ", couveredConcepts=" + coveredConcepts + '}';
    }
    
    

}
