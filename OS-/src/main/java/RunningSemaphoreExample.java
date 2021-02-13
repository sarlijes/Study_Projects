import java.util.concurrent.Semaphore;

public class RunningSemaphoreExample {

    public static void main(String[] args) {

        // A

        Semaphore sA = new Semaphore(0);
        RunningTaskA task = new RunningTaskA(sA);

        new Thread(task, "Ann").start();
        new Thread(task, "Bill").start();

        // B

        Semaphore sB = new Semaphore(0);
        RunningTaskB taskB = new RunningTaskB(sB);

        new Thread(taskB, "Ann").start();
        new Thread(taskB, "Bill").start();
        new Thread(taskB, "Charlie").start();

    }

}
