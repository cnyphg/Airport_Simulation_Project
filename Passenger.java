package Assignment_5;

public class Passenger implements Runnable {
    private static int passengerNumber = 0;
    private int passengerID;
    private int planeID;
    private boolean embarkStatus;
    
    public Passenger(boolean embarkStatus, int planeID) {
        ++passengerNumber;
        this.passengerID = passengerNumber;
        this.embarkStatus = embarkStatus;
        this.planeID = planeID;
        Thread passenger = new Thread(this);
        passenger.setName("Passenger " + this.passengerID);
        passenger.start();
    }
    
    public void run() {
        if (embarkStatus) {
            System.out.println(Thread.currentThread().getName() + ": Boarding Plane " + planeID);
        } else {
            System.out.println(Thread.currentThread().getName() + ": Disembarking from Plane " + planeID);
        } 
    }
}
