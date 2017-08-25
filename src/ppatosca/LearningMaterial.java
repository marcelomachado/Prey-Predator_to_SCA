package ppatosca;

/**
 *
 * @author gtbavi
 */
public class LearningMaterial {

    private String type;
    private Double dificulty;
    private int typical_learning_time;
    private int id;
    private String name;

    public LearningMaterial() {
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

}
