package ppatosca;

import java.math.BigInteger;
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
    private int typicalLearningTime;
    private Double difficulty;
    private String learningResourceType;
    private String interativityLevel;
    private String interativityType;
    // noLOM
    private ArrayList<Concept> coveredConcepts;
    private int learningStyleActiveValue;
    private int learningStyleReflexiveValue;

    public LearningMaterial() {
    }

    public LearningMaterial(int id, String name, String type, String typicalLearningTime, String difficulty, String interativityLevel, String interativityType) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        this.id = id;
        this.name = name;
        this.type = type;

        if (difficulty.equals("very difficult")) {
            this.difficulty = 1d;
        } else if (difficulty.equals("difficult")) {
            this.difficulty = 0.75d;
        } else if (difficulty.equals("easy")) {
            this.difficulty = 0.25d;
        } else if (difficulty.equals("very easy")) {
            this.difficulty = 0d;
        } else {
            this.difficulty = 0.5d;
        }

        typicalLearningTime = typicalLearningTime.replace("PT", "");
        if (typicalLearningTime.contains("H")) {
            hour = Integer.parseInt(typicalLearningTime.split("H")[0]);
            if (typicalLearningTime.split("H").length == 2) {                
                typicalLearningTime = typicalLearningTime.split("H")[1];
            }
        }
        if (typicalLearningTime.contains("M")) {
            minute = Integer.parseInt(typicalLearningTime.split("M")[0]);
            if (typicalLearningTime.split("M").length == 2) {                
                typicalLearningTime = typicalLearningTime.split("M")[1];
            }            
        }
        if (typicalLearningTime.contains("S")) {
            second = Integer.parseInt(typicalLearningTime.replace("S", ""));
        }
        this.typicalLearningTime = (hour * 360) + (minute * 60) + second;

        this.interativityLevel = interativityLevel;

        this.interativityType = interativityType;
        
        //TODO associate learning style values based on table from the paper
        this.learningStyleActiveValue = 0;
        this.learningStyleReflexiveValue = 0;
        
        

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    public int getTypicalLearningTime() {
        return typicalLearningTime;
    }

    public void setTypicalLearningTime(int typicalLearningTime) {
        this.typicalLearningTime = typicalLearningTime;
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

    public int getLearningStyleActiveValue() {
        return learningStyleActiveValue;
    }

    public void setLearningStyleActiveValue(int learningStyleActiveValue) {
        this.learningStyleActiveValue = learningStyleActiveValue;
    }

    public int getLearningStyleReflexiveValue() {
        return learningStyleReflexiveValue;
    }

    public void setLearningStyleReflexiveValue(int learningStyleReflexiveValue) {
        this.learningStyleReflexiveValue = learningStyleReflexiveValue;
    }
    
    

    @Override
    public String toString() {
        return "LearningMaterial{" + "id=" + id + ", name=" + name + ", type=" + type + ", typical_learning_time=" + typicalLearningTime + ", dificulty=" + difficulty + ", couveredConcepts=" + coveredConcepts + '}';
    }

}
