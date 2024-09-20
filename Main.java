package Assignment_5;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Thread {
    public static Thread mainThread;

    private static void normalScenario(Lock refuelLock) {
        // This simulation will include 6 airplanes all with normal priorities.
        System.out.println("NORMAL SCENARIO: Simulation of the operations in Asia Pacific Airport begins now!");
        new ATC(6);

        for (int i = 1; i <= 6; i++) {
            Plane a = new Plane(i, Thread.NORM_PRIORITY, refuelLock);
            ATC.planes.add(a);

            Random rand = new Random();
            try {
                Thread.sleep(rand.nextInt(2000)); // New airplane arrives every 2 seconds.
            } catch (InterruptedException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private static void emergencyScenario(Lock refuelLock) {
        // Simulates plane 1 - 5 landing normally where plane 6 requires emergency landing.
        System.out.println("EMERGENCY SCENARIO: Simulation of the operations in Asia Pacific Airport begins now!");
        new ATC(6);

        for (int i = 1; i <= 6; i++) {
            if (i == 6) {
                Plane a = new Plane(i, Thread.MAX_PRIORITY, refuelLock);
                ATC.planes.add(a);
            } else {
                Plane a = new Plane(i, Thread.NORM_PRIORITY, refuelLock);
                ATC.planes.add(a);
            }

            Random rand = new Random();
            try {
                Thread.sleep(rand.nextInt(2000)); // New airplane arrives every 2 seconds.
            } catch (InterruptedException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }


    private static double begin(int choice) {
        Thread.currentThread().setName("Main-Thread");
        mainThread = Thread.currentThread();

        double startSimTime = 0;

        Lock refuelLock = new ReentrantLock();

        switch (choice) {
            case 1: // Normal Scenario
                normalScenario(refuelLock);
                break;
            case 2: // Emergency Scenario
                emergencyScenario(refuelLock);
                break;
        }

        return startSimTime;
    }

    public static void main(String args[]) {
        System.out.println("Please select the scenario that you want the simulation of based on the options below.");
        System.out.println("1. Normal Scenario\n2. Emergency Scenario");
        System.out.print("Your Option: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice <= 0 || choice > 2) {
            System.out.println("Invalid input. Please enter '1' or '2'.");
            System.out.println("1. Normal Scenario\n2. Emergency Scenario");
            System.out.print("Your Option: ");
            choice = scanner.nextInt();
        }

        // Choose to demonstrate normal scenario or emergency senario
        double startTime;
        startTime = System.nanoTime();
        begin(choice);

        synchronized(mainThread) {
            try {
                mainThread.wait();
            } catch (InterruptedException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        double endTime = System.nanoTime();
        double duration = (endTime - startTime)/1000000000;

        System.out.printf("\nAll planes have already took off. Simulation of Asia Pacific Airport has been completed in %.2f seconds.\n\n", duration);

        ATC.statistics();
        System.out.println("Simulation Program Ended.\n");
    }  }