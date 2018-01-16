package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class Learner {
    
    private int id;
    private String registrationCode;
    private HashMap<Concept, Double> score;
    private int lowerTime;
    private int upperTime;    
    private ArrayList<Concept> learningGoals;  
    // Learning Style
    private int atvRef;
    private int senInt;
    private int visVer;
    private int seqGlo;
    

    public Learner() {
    }

    public Learner(int id, String registrationCode, int lowerTime, int upperTime, int atvref, int senint, int visver, int seqglo, ArrayList<Concept> learningGoals) {
        this.id = id;
        this.registrationCode = registrationCode;
        this.lowerTime = lowerTime;
        this.upperTime = upperTime;
        this.learningGoals = learningGoals;
        //TODO change range of learning style
        this.atvRef = atvref;
        this.senInt = senint;
        this.visVer = visver;
        this.seqGlo = seqglo;
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

    public int getLowerTime() {
        return lowerTime;
    }

    public void setLowerTime(int lowerTime) {
        this.lowerTime = lowerTime;
    }

    public int getUpperTime() {
        return upperTime;
    }

    public void setUpperTime(int upperTime) {
        this.upperTime = upperTime;
    }

    public int getAtvRef() {
        return atvRef;
    }

    public void setAtvRef(int atvRef) {
        this.atvRef = atvRef;
    }

    public int getSenInt() {
        return senInt;
    }

    public void setSenInt(int senInt) {
        this.senInt = senInt;
    }

    public int getVisVer() {
        return visVer;
    }

    public void setVisVer(int visVer) {
        this.visVer = visVer;
    }

    public int getSeqGlo() {
        return seqGlo;
    }

    public void setSeqGlo(int seqGlo) {
        this.seqGlo = seqGlo;
    }

    
    
    public ArrayList<Concept> getLearningGoals() {
        return learningGoals;
    }

    public void setLearningGoals(ArrayList<Concept> learningGoals) {
        this.learningGoals = learningGoals;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }            
    
}
