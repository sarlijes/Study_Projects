import java.util.concurrent.Semaphore;

class RunningTaskB implements Runnable {

    private final Semaphore signal;

    // run laps
    private int ann;
    private int bill;
    private int charlie;

    public RunningTaskB(Semaphore signal) {
        ann = 0;
        bill = 0;
        charlie = 0;
        this.signal = signal;
    }

    public void run() {

        for (int i = 1; i <= 10; i++) {
            String runner = Thread.currentThread().getName();

            if (runner.equals("Ann")) ann++;
            else if (runner.equals("Bill")) bill++;
            else charlie++;

            System.out.println(runner + " starts their lap: " + i);

            // Ann  waits after every lap until Charlie has caught up with them

            if (runner.equals("Ann") && charlie < ann) {
                System.out.println("Ann will wait for Charlie because Ann has run " + ann + " laps and Charlie " + charlie);

                try {
                    signal.acquire();
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }

                // Ann  waits after every lap until Charlie has caught up with them

            } else if (runner.equals("Bill") && charlie < bill) {
                System.out.println("Bill will wait for Charlie because Bill has run " + bill + " laps and Charlie " + charlie);

                try {
                    signal.acquire();
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            } else if (charlie >= bill && charlie >= ann) {
                signal.release();
            }
        }
    }

}


