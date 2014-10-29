package deadfish;

/**
 *
 * @author estebanfcv
 */
public class DeadFish {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread hilo = new Thread(new Hilo());
        hilo.start();
    }

}
