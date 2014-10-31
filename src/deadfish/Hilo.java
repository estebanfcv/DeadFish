package deadfish;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author estebanfcv
 */
public class Hilo implements Runnable {

    File file;
    private long filePointer;

    public Hilo() {
        file = new File(Constantes.LOG);
        filePointer = file.length();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                long len = file.length();
                if (len < filePointer) {
                    filePointer = len;
                } else if (len > filePointer) {
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    raf.seek(filePointer);
                    String line;
                    while ((line = raf.readLine()) != null) {
                        if (line.contains("java.lang.OutOfMemoryError")) {
                            System.out.println(line);
                            Process p = Runtime.getRuntime().exec(Constantes.PS);
                            List<String> procesos = new ArrayList<>();
                            String s;
                            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            while ((s = stdInput.readLine()) != null) {
                                if (s.contains("glassfish")) {
                                    StringTokenizer token = new StringTokenizer(s, " ");
                                    token.nextToken();
                                    procesos.add(token.nextToken());
                                }
                            }
                            String comando = Constantes.KILL;
                            if (!procesos.isEmpty()) {
                                for (String proceso : procesos) {
                                    comando += proceso + " ";
                                }
                                String[] cmd = {"/bin/bash", "-c", "echo sica2014| sudo -S " + comando};
                                Runtime.getRuntime().exec(cmd);
                                Thread.sleep(1000);
                                Runtime.getRuntime().exec(Constantes.INICIAR_GLASSFISH);
                            }
                        }
                    }
                    filePointer = raf.getFilePointer();
                    raf.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
