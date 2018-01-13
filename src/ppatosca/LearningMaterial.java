package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class LearningMaterial {
    private int id;
    private String name;
    private String type;    
    private int typical_learning_time;
    private Double dificulty;       
    private ArrayList<Concept> coveredConcepts;
    
    public LearningMaterial() {
    }

    public LearningMaterial(int id, String name, String type, int typical_learning_time, Double dificulty) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.typical_learning_time = typical_learning_time;
        this.dificulty = dificulty;
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

    @Override
    public String toString() {
        return "LearningMaterial{" + "id=" + id + ", name=" + name + ", type=" + type + ", typical_learning_time=" + typical_learning_time + ", dificulty=" + dificulty + ", couveredConcepts=" + coveredConcepts + '}';
    }
    
    

}
