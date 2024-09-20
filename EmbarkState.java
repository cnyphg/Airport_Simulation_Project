package Assignment_5;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmbarkState extends Thread {
    private int planeID;
    private int passengerNo;
    private final int MAX_PASSENGER_NO = 50;
    private boolean embarkStatus;

    public EmbarkState(int planeID, boolean embarkStatus) {
        this.planeID = planeID;
        this.embarkStatus = embarkStatus;
    }

    public int getPassengers() {
        return passengerNo;
    }

    private void setPassengers(int passengerNo) {
        this.passengerNo = passengerNo;
    }

    public void run() {
        Random rand = new Random();
        int passengerNo = rand.nextInt(MAX_PASSENGER_NO); // Number of passengers between 0 and 50
        setPassengers(passengerNo);

        if (embarkStatus) {
            System.out.println("Plane-" + planeID + ": Embarking new passengers...");
        } else {
            System.out.println("Plane-" + planeID + ": Disembarking passengers...");
        }

        for (int i = 1; i <= passengerNo; i++) {
            Passenger p = new Passenger(embarkStatus, planeID);
            try {
                Thread.sleep(100); // Embarking/disembarking each passenger takes 0.1 seconds.
            } catch (InterruptedException e) {
                Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        if (embarkStatus) {
            System.out.println("Plane-" + planeID + ": All new passengers have boarded the plane.");
        } else {
            System.out.println("Plane-" + planeID + ": All passengers have disembarked from the plane.");
        }
    }
}