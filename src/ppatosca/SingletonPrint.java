/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
}
