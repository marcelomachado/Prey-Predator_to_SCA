package ppatosca;

/**
 *
 * @author gtbavi
 */
public final class SingletonPrint {
    private static SingletonPrint INSTANCE;
    private static StringBuilder out = new StringBuilder();

    private SingletonPrint() {
    }
    
    
    public static synchronized SingletonPrint getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SingletonPrint();
        }
        return INSTANCE;
    }
    public void addString(String text){
        out.append(text);
    }
    
    public void out(){
        System.out.println(out);
    }
    
    public void free(){
        out.delete(0, out.length());
    }
}
