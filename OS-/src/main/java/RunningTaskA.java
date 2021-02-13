import java.util.concurrent.Semaphore;

class RunningTaskA implements Runnable {

    private final Semaphore signal;

    // run laps
    private int ann;
    private int bill;

    public RunningTaskA(Semaphore signal) {
        ann = 0;
        bill = 0;
        this.signal = signal;
    }

    public void run() {

        for (int i = 1; i <= 10; i++) {
            String runner = Thread.currentThread().getName();

            if (runner.equals("Ann")) ann++;
            else bill++;


            System.out.println(runner + " starts their lap:" + i);

            if (runner.equals("Ann") && bill < ann) {
                System.out.println("Ann will wait for Bill because Ann has run " + ann + " laps and Bill " + bill);

                try {
                    signal.acquire();
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            } else if (bill >= ann) {
                System.out.println("Ann continues running");
                System.out.println("Ann has run " + ann + " laps and Bill " + bill);
                signal.release();
            }
        }
    }

}


