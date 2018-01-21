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
    private int atvref;
    private int senint;
    private int visver;
    private int seqglo;

    public Learner() {
    }

    public Learner(String registrationCode, int lowerTime, int upperTime, int atvref, int senint, int visver, int seqglo, ArrayList<Concept> learningGoals) {
        this.registrationCode = registrationCode;
        this.lowerTime = lowerTime;
        this.upperTime = upperTime;
        this.learningGoals = learningGoals;
        if (atvref == -11 || atvref == -9) {
            this.atvref = -3;
        } else if (atvref == -7 || atvref == -5) {
            this.atvref = -2;
        } else if (atvref == -3 || atvref == -1) {
            this.atvref = -1;
        }else if (atvref == 11 || atvref == 9) {
            this.atvref = 3;
        } else if (atvref == 7 || atvref == 5) {
            this.atvref = 2;
        } else if (atvref == 3 || atvref == 1) {
            this.atvref = 1;
        }
        
        if (senint == -11 || senint == -9) {
            this.senint = -3;
        } else if (senint == -7 || senint == -5) {
            this.senint = -2;
        } else if (senint == -3 || senint == -1) {
            this.senint = -1;
        }else if (senint == 11 || senint == 9) {
            this.senint = 3;
        } else if (senint == 7 || senint == 5) {
            this.senint = 2;
        } else if (senint == 3 || senint == 1) {
            this.senint = 1;
        }
        
        if (visver == -11 || visver == -9) {
            this.visver = -3;
        } else if (visver == -7 || visver == -5) {
            this.visver = -2;
        } else if (visver == -3 || visver == -1) {
            this.visver = -1;
        }else if (visver == 11 || visver == 9) {
            this.visver = 3;
        } else if (visver == 7 || visver == 5) {
            this.visver = 2;
        } else if (visver == 3 || visver == 1) {
            this.visver = 1;
        }
        
        if (seqglo == -11 || seqglo == -9) {
            this.seqglo = -3;
        } else if (seqglo == -7 || seqglo == -5) {
            this.seqglo = -2;
        } else if (seqglo == -3 || seqglo == -1) {
            this.seqglo = -1;
        }else if (seqglo == 11 || seqglo == 9) {
            this.seqglo = 3;
        } else if (seqglo == 7 || seqglo == 5) {
            this.seqglo = 2;
        } else if (seqglo == 3 || seqglo == 1) {
            this.seqglo = 1;
        }
        
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

    public int getAtvref() {
        return atvref;
    }

    public void setAtvref(int atvref) {
        this.atvref = atvref;
    }

    public int getSenint() {
        return senint;
    }

    public void setSenint(int senint) {
        this.senint = senint;
    }

    public int getVisver() {
        return visver;
    }

    public void setVisver(int visver) {
        this.visver = visver;
    }

    public int getSeqglo() {
        return seqglo;
    }

    public void setSeqglo(int seqglo) {
        this.seqglo = seqglo;
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
