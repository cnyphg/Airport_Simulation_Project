package Assignment_5;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ATC extends Thread {
    public static ArrayList<Plane> planes = new ArrayList<Plane>();
    private static ArrayList<Integer> queueList = new ArrayList<Integer>();
    private static AtomicInteger availableGates = new AtomicInteger(3);
    private static boolean gates[] = new boolean[3];
    private static boolean notifyQueue[];
    private static int numOfPlane;

    public ATC(int numOfPlane) {
        this.numOfPlane = numOfPlane;
        this.notifyQueue = new boolean[numOfPlane];
        Thread atc = new Thread(this);
        atc.setName("Thread-ATC");
        atc.start();
    }

    public static void addQueue(int planeID) {
        // Give highest priority for planes to be in front of queue.
        if (!queueList.isEmpty()) {
            for (int i = 0; i < queueList.size(); i++) {
                if (planes.get(planeID - 1).getThread().getPriority() > planes.get(queueList.get(i) - 1).getThread().getPriority()) {
                    queueList.add(i, planeID);
                    break;
                }
            }
            queueList.add(planeID);
        } else {
            queueList.add(planeID);
        }
    }

    public static void lockGate(int planeID) {
        notifyQueue[planeID - 1] = false;
        synchronized (gates) {
            for (int i = 0; i < gates.length; i++) {
                if (!gates[i]) {
                    gates[i] = true;
                    planes.get(planeID - 1).setGate(i + 1);
                    synchronized (planes.get(planeID - 1).getThread()) {
                        System.out.println(Thread.currentThread().getName() + ": Plane " + planeID + ", landing permission has been granted for Gate " + (i + 1));
                        planes.get(planeID - 1).getThread().notify();
                    }
                    break;
                }
            }
        }
        availableGates.decrementAndGet();
    }

    public static void releaseGate(int gate) {
        synchronized (gates) {
            gates[gate - 1] = false;
        }
        availableGates.incrementAndGet();
    }

    public static int getNumberOfAvailableGates() {
        return availableGates.get();
    }

    public static boolean[] getGates() {
        return gates;
    }

    public static int getNumOfPlane() {
        return numOfPlane;
    }

    public static void statistics() {
        // Check status of each gate
        System.out.println(Thread.currentThread().getName() + ": Sanity Check...");
        System.out.println("Thread-ATC: Statistics has been printed as below:");

        int count = 0;
        for (Plane plane : planes) {
            if (plane.getTakeOffStatus()) {
                count++;
            }
            if (count == numOfPlane) {
                System.out.println("All gates have been cleared");
                System.out.println("Currently Available Gates: 3");
            }
        }
        //System.out.println("Currently Available Gates: " + ATC.getNumberOfAvailableGates());

        // Number of planes served and number of passengers that disembarked/embarked the planes.
        int NumofPlane = getNumOfPlane();
        int passengerDisembark = 0;
        int passengerEmbark = 0;
        System.out.println("\nPlane statistics: ");

        for (int i = 0; i < numOfPlane; i++) {
            System.out.print("Plane " + (i + 1) + " : ");
            System.out.printf("Waiting Time: %.2f seconds\n", planes.get(i).getWaitingTime());
            System.out.println("Number of passengers disembarked: " + planes.get(i).getDisembarkNo());
            passengerDisembark += planes.get(i).getDisembarkNo();
            System.out.println("Number of passengers embarked: " + planes.get(i).getEmbarkNo() + "\n");
            passengerEmbark += planes.get(i).getEmbarkNo();
        }

        System.out.println("Total planes served: " + numOfPlane);
        System.out.println("Total passengers disembarked: " + passengerDisembark);
        System.out.println("Total passengers embarked: " + passengerEmbark);

        // max, min and average waiting time for a plane
        double maxTime = Long.MIN_VALUE;
        double minTime = Long.MAX_VALUE;
        double totalTime = 0;
        for (int i = 0; i < numOfPlane; i++) {
            double curTime = ATC.planes.get(i).getWaitingTime();
            if (curTime > maxTime) maxTime = curTime;
            if (curTime < minTime) minTime = curTime;
            totalTime += curTime;
        }

        System.out.printf("\nMAX Waiting Time: %.2f seconds\n", maxTime);
        System.out.printf("MIN Waiting Time: %.2f seconds\n", minTime);
        System.out.printf("Average Waiting Time: %.2f seconds\n", (totalTime / numOfPlane));

        System.out.println("_________________________________");
        System.out.println("Thread-ATC: End of Statistic!\n");
    }

    public void run() {
        while (true) {
            if (!queueList.isEmpty()) {
                int planeID = queueList.get(0);
                if (availableGates.get() > 0) {
                    lockGate(planeID);
                    queueList.remove(0);
                } else {
                    if (notifyQueue[planeID - 1] == false) {
                        System.out.println(Thread.currentThread().getName() + ": Plane " + planeID + ", please wait and join the circle queue.");
                        notifyQueue[planeID - 1] = true;
                    }
                }
            }

            // ATC will only check the queue list every 0.2 seconds.
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }

            // Check if all planes have already took off.
            int count = 0;
            for (Plane a : planes) {
                if (a.getTakeOffStatus()) count++;
            }
            if (count == numOfPlane) break;
        }

        // Notify the main thread
        synchronized (Main.mainThread) {
            Main.mainThread.notify();
        }
    }
}
