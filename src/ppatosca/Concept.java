package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class Concept {

    private int id;
    private String name;
    private String abbreviation;
    private ArrayList<Concept> prerequisites;
    private ArrayList<LearningMaterial> LMs;
    private int level;

    public Concept() {
    }
    
    public Concept(int id, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }
    public Concept(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;

    }
    public Concept(int id, String name,String abbreviation, ArrayList<LearningMaterial> LMs) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
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

    public ArrayList<Concept> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(ArrayList<Concept> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public ArrayList<LearningMaterial> getLMs() {
        return LMs;
    }

    public void setLMs(ArrayList<LearningMaterial> LMs) {
        this.LMs = LMs;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }        

}
