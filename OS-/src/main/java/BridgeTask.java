import java.util.concurrent.Semaphore;

public class BridgeTask implements Runnable {

    private final Semaphore westBoundSemaphore;
    private final Semaphore eastBoundSemaphore;
    private int carsOnTheBridge;


    public BridgeTask(Semaphore westBoundSemaphore, Semaphore eastBoundSemaphore) {
        this.westBoundSemaphore = westBoundSemaphore;
        this.eastBoundSemaphore = eastBoundSemaphore;
        carsOnTheBridge = 0;
    }

    public void enter_bridge_west(Thread car) {
        // If flag is down, can go
        try {
            eastBoundSemaphore.acquire();

            if (westBoundSemaphore.availablePermits() == 1) {
                // Eka auto nostaa oman lipun
                westBoundSemaphore.acquire();
            }
            carsOnTheBridge++;
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    public void exit_bridge_west(Thread car) {
        // If current car is the only car on the bridge, put the flag down
        if (carsOnTheBridge == 1)
            westBoundSemaphore.release();
        carsOnTheBridge--;
    }

    public void enter_bridge_east(Thread car) {
        // If flag is down, can go
        try {
            westBoundSemaphore.acquire();
            carsOnTheBridge++;
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    public void exit_bridge_east(Thread car) {
        // If current car is the only car on the bridge, put the flag down
        if (carsOnTheBridge == 1)
            eastBoundSemaphore.release();
        carsOnTheBridge--;
    }

    public void run() {

        String car = Thread.currentThread().getName();

        if (Integer.parseInt(car) % 2 == 0) {
            System.out.println("Car " + car + " is entering the bridge, going EAST");
            enter_bridge_east(Thread.currentThread());
            System.out.println("-- Car " + car + " is exiting the bridge, going EAST");
            exit_bridge_east(Thread.currentThread());
        } else {
            System.out.println("Car " + car + " is entering the bridge, going WEST");
            enter_bridge_west(Thread.currentThread());
            System.out.println("-- Car " + car + " is exiting the bridge, going EAST");
            exit_bridge_west(Thread.currentThread());
        }
    }

}
