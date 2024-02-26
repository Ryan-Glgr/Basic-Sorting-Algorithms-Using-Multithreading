import java.util.Random;

public class Driver{
    public static final int NUM_THREADS = 8;
    public static long[] times;

    public static void main(String[] args) {
        //make an array of threads
        DriverThread[] threads = new DriverThread[NUM_THREADS];
        times = new long[NUM_THREADS];
        //this is populating our array of random numbers, that is global, which is copied by each thread individually
        Random r = new Random();
        for (int i = 0; i < DriverThread.N; i++) DriverThread.numsToSort[i] = r.nextInt(100);

        //instantiate each thread, making a new driver incrementing seed each time to call different methods
        for (int i = 0; i < NUM_THREADS; i++){
            threads[i] = new DriverThread(i);
            threads[i].start();
        }
        //we're going to call join here so that we wait until everyone's done to print out their times.
        for (DriverThread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (DriverThread t : threads){
            System.out.println("\n\nThread: " + t.seed + " executed in: " + t.executionTime + " ms");
            //for (int i : t.sortedNums) System.out.print(i + " ");
        }
    }



    //this method is going to take in a seed which will choose which sort to use, and then we are going to just return the sorted array










}
