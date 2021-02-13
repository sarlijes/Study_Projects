
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Running {

    static Semaphore s = new Semaphore(1);
    private static final Semaphore signal = s;

    HashMap<String, Integer> roundCounts = new HashMap<String, Integer>();
    int totalMetres = 0;

    public void addRunner(String name) {
        roundCounts.put(name, 0);
        //new Thread(task, "Helsinki").start();
    }

    public void runLap(String runner) {
        System.out.println(runner  + " starts their lap");
        roundCounts.put(runner, roundCounts.get(runner) + 1);
        totalMetres = totalMetres + 400;

        System.out.println(totalMetres);
    }

    public static void main(String[] args) {
        Running r = new Running();
        r.addRunner("Ann");
        r.addRunner("Bill");

        for (int i = 1; i <= 10; i++) {
            r.runLap("Ann");
            if (r.roundCounts.get("Bill") < r.roundCounts.get("Ann")) {

                try {
                    signal.acquire();
                    System.out.println("Ann is ahead of Bill, so will wait");

                } catch (InterruptedException e) {
                    Thread.interrupted();
                } finally {
                    System.out.println("Ann and Bill continue together");
                    signal.release();
                }


            }
        }

        for (int i = 1; i <= 10; i++) {
            r.runLap("Bill");
        }

    }
}