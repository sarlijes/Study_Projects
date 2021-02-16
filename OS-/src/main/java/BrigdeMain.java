import java.util.concurrent.Semaphore;

public class BrigdeMain {

    public static void main(String[] args) {

        Semaphore westBoundSemaphore = new Semaphore(0);
        Semaphore eastBoundSemaphore = new Semaphore(0);

        BridgeTask task = new BridgeTask(westBoundSemaphore, eastBoundSemaphore);

        new Thread(task, String.valueOf(1)).start();
        new Thread(task, String.valueOf(3)).start();
        new Thread(task, String.valueOf(5)).start();
        new Thread(task, String.valueOf(2)).start();


//        for (int i = 1; i <= 10; i++) {
//            new Thread(task, String.valueOf(i)).start();
//        }

    }

}
