package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class Auxiliary {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String folder = "/home/gtbavi/NetBeansProjects/PPAtoSCA/src/ppatosca/";
        cleanPrints(folder);
    }

    public static void cleanPrints(String folder) throws FileNotFoundException, IOException {
        File folderFiles = new File(folder);
        File[] files;
        FileReader fr;

        BufferedReader br;

        int bytes = 0;
        ArrayList<Long> positions = new ArrayList<>();
        for (File eachFile : folderFiles.listFiles()) {
            try (RandomAccessFile raf1 = new RandomAccessFile(eachFile,"r")) {
                
                String s;
                do  {
                    long start = raf1.getFilePointer();
                    s = raf1.readLine();
                    if(s!=null){
                        if (s.trim().contains("System.out.print")||s.trim().contains("debbug")) {
                            positions.add(start);
                        }
                    }

                }while(s != null);
                raf1.close();
            }
            try (RandomAccessFile raf2 = new RandomAccessFile(eachFile, "rwd") // Flush/save changes and close resource.
            ) {
                for(Long position: positions){
                    raf2.seek(position);
                    raf2.write(47);
                    raf2.write(47);
                }
                 // Go to byte at offset position 5.
                 // Write byte 70 (overwrites original byte at this offset).
                 raf2.close();
            }
            

        }

    }

}
