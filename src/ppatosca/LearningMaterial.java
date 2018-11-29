package ppatosca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private String[] learningResourceTypes;
    private String interativityLevel;
    private String interativityType;
    // noLOM
    private ArrayList<Concept> coveredConcepts;

    private int learningStyleActiveValue = 0;
    private int learningStyleReflexiveValue = 0;
    private int learningStyleSensoryValue = 0;
    private int learningStyleIntuitiveValue = 0;
    private int learningStyleVisualValue = 0;
    private int learningStyleVerbalValue = 0;
    private int learningStyleSequentialValue = 0;
    private int learningStyleGlobalValue = 0;

    public LearningMaterial() {
    }

    public LearningMaterial(int id, String name, String type, String typicalLearningTime, String difficulty, String[] learningResourceTypes, String interativityLevel, String interativityType) {
        List<String> learningResourceTypePossibleActiveValues = Arrays.asList("exercise", "simulation", "questionnaire", "test", "experiment");
        List<String> learningResourceTypePossibleReflexiveValues = Arrays.asList("simulation", "diagram", "figure", "graphic", "slide", "table", "narrative text", "test", "problems declaration");

        List<String> learningResourceTypePossibleSensoryValues = Arrays.asList("exercise", "simulation", "graphic", "slide", "test", "table", "narrative text", "experiment", "problems declaration");
        List<String> learningResourceTypePossibleIntuitiveValues = Arrays.asList("simulation", "questionnaire", "diagram", "test", "figure", "slide", "narrative text");

        List<String> learningResourceTypePossibleVisualValues = Arrays.asList("simulation", "diagram", "figure", "graphic", "slide", "table");
        List<String> learningResourceTypePossibleVerbalValues = Arrays.asList("slide", "narrative text", "reading");

        List<String> learningResourceTypePossibleSequentialValues = Arrays.asList("exercise", "simulation", "diagram", "slide", "narrative text", "experiment");
        List<String> learningResourceTypePossibleGlobalValues = Arrays.asList("diagram", "figure", "graphic", "slide", "table", "narrative text");

        int hour = 0;
        int minute = 0;
        int second = 0;
        this.id = id;
        this.name = name;
        this.type = type;

        switch (difficulty) {
            case "very difficult":
                this.difficulty = 5d;
                break;        
            case "difficult":
                this.difficulty = 4d;
                break;
            case "easy":
                this.difficulty = 2d;
                break;
            case "very easy":
                this.difficulty = 1d;
                break;
            case "medium":
                this.difficulty = 3d;
                break;
            default:
                System.out.println("Dificuldade não mapeada: "+ difficulty);
                break;
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
        this.typicalLearningTime = (hour * 3600) + (minute * 60) + second;       
        for (String learningResourceType : learningResourceTypes) {
            if (learningResourceTypePossibleActiveValues.contains(learningResourceType)) {
                this.learningStyleActiveValue++;
            }
            if (learningResourceTypePossibleReflexiveValues.contains(learningResourceType)) {
                this.learningStyleReflexiveValue++;
            }
            if (learningResourceTypePossibleSensoryValues.contains(learningResourceType)) {
                this.learningStyleSensoryValue++;
            }
            if (learningResourceTypePossibleIntuitiveValues.contains(learningResourceType)) {
                this.learningStyleIntuitiveValue++;
            }
            if (learningResourceTypePossibleVisualValues.contains(learningResourceType)) {
                this.learningStyleVisualValue++;
            }
            if (learningResourceTypePossibleVerbalValues.contains(learningResourceType)) {
                this.learningStyleVerbalValue++;
            }
            if (learningResourceTypePossibleSequentialValues.contains(learningResourceType)) {
                this.learningStyleSequentialValue++;
            }
            if (learningResourceTypePossibleGlobalValues.contains(learningResourceType)) {
                this.learningStyleGlobalValue++;
            }

        }
        switch (interativityLevel) {
            case "low":
                this.learningStyleActiveValue += 1;
                break;
            case "medium":
                this.learningStyleActiveValue += 2;
                break;
            case "high":
                this.learningStyleActiveValue += 3;
                break;
            case "very high":
                this.learningStyleActiveValue += 4;
                break;
        // Apenas para debug
            case "very low":
                break;
            default:
                System.out.println("nivel de interatividade não mapeado: "+interativityLevel);
                break;
        }
        
        switch (interativityType) {
            case "active":
                this.learningStyleActiveValue++;
                break;
            case "expositive":
                this.learningStyleReflexiveValue++;
                break;
            case "mixed":
                this.learningStyleActiveValue++;
                this.learningStyleReflexiveValue++;
                break;
            default:
                System.out.println("Tipo de interatividade não mapeado: "+interativityType);
                break;
        }
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

    public String[] getLearningResourceTypes() {
        return learningResourceTypes;
    }

    public void setLearningResourceTypes(String[] learningResourceTypes) {
        this.learningResourceTypes = learningResourceTypes;
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

    public int getLearningStyleSensoryValue() {
        return learningStyleSensoryValue;
    }

    public void setLearningStyleSensoryValue(int learningStyleSensoryValue) {
        this.learningStyleSensoryValue = learningStyleSensoryValue;
    }

    public int getLearningStyleIntuitiveValue() {
        return learningStyleIntuitiveValue;
    }

    public void setLearningStyleIntuitiveValue(int learningStyleIntuitiveValue) {
        this.learningStyleIntuitiveValue = learningStyleIntuitiveValue;
    }

    public int getLearningStyleVisualValue() {
        return learningStyleVisualValue;
    }

    public void setLearningStyleVisualValue(int learningStyleVisualValue) {
        this.learningStyleVisualValue = learningStyleVisualValue;
    }

    public int getLearningStyleVerbalValue() {
        return learningStyleVerbalValue;
    }

    public void setLearningStyleVerbalValue(int learningStyleVerbalValue) {
        this.learningStyleVerbalValue = learningStyleVerbalValue;
    }

    public int getLearningStyleSequentialValue() {
        return learningStyleSequentialValue;
    }

    public void setLearningStyleSequentialValue(int learningStyleSequentialValue) {
        this.learningStyleSequentialValue = learningStyleSequentialValue;
    }

    public int getLearningStyleGlobalValue() {
        return learningStyleGlobalValue;
    }

    public void setLearningStyleGlobalValue(int learningStyleGlobalValue) {
        this.learningStyleGlobalValue = learningStyleGlobalValue;
    }

    @Override
    public String toString() {
        return "LearningMaterial{" + "id=" + id + ", name=" + name + ", type=" + type + ", typical_learning_time=" + typicalLearningTime + ", dificulty=" + difficulty + ", couveredConcepts=" + coveredConcepts + '}';
    }

}
