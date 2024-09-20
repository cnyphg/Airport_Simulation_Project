package Assignment_5;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Refuel extends Thread {
    private int planeID;
    
    public Refuel(int planeID) {
        setName("Thread-Refuelling-Truck");
        this.planeID = planeID;
    }
    
    private void refuel() {
        System.out.println(Thread.currentThread().getName() + ": Refuelling Plane " + planeID + "...");
        try {
            Thread.sleep(2000); // Refuelling take 2 seconds.
        } catch (InterruptedException ex) {
            Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run() {
        refuel();
        System.out.println(Thread.currentThread().getName() + ": Plane " + planeID + "'s refuelling has completed!");
    }
}
