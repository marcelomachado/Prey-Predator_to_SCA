package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class Learner {
    
    private int id;
    private HashMap<Concept, Double> score;
    private Double abilityLevel;
    private int lower_time;
    private int upper_time;
    private ArrayList<Concept> learningGoals;
    

    public Learner() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Concept, Double> getScore() {
        return score;
    }

    public void setScore(HashMap<Concept, Double> score) {
        this.score = score;
    }        

    public Double getAbilityLevel() {
        return abilityLevel;
    }

    public void setAbilityLevel(Double abilityLevel) {
        this.abilityLevel = abilityLevel;
    }

    public int getLower_time() {
        return lower_time;
    }

    public void setLower_time(int lower_time) {
        this.lower_time = lower_time;
    }

    public int getUpper_time() {
        return upper_time;
    }

    public void setUpper_time(int upper_time) {
        this.upper_time = upper_time;
    }

    public ArrayList<Concept> getLearningGoals() {
        return learningGoals;
    }

    public void setLearningGoals(ArrayList<Concept> learningGoals) {
        this.learningGoals = learningGoals;
    }
    
    
    
    
}
