package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class Concept {
    
    private int id;
    private String name;    
    private ArrayList<Concept> outcomes;
    private ArrayList<LearningMaterial> LMs;
    
    public Concept() {
    }

    public Concept(int id, String name, ArrayList<LearningMaterial> LMs) {
        this.id = id;
        this.name = name;
        this.LMs = LMs;
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

    public ArrayList<Concept> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<Concept> outcomes) {
        this.outcomes = outcomes;
    }

    public ArrayList<LearningMaterial> getLMs() {
        return LMs;
    }

    public void setLMs(ArrayList<LearningMaterial> LMs) {
        this.LMs = LMs;
    }
       
}
