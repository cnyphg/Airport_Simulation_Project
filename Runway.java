package Assignment_5;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Runway {
    
    public static void useRunway(int planeID, boolean landing) {
        synchronized (Plane.class) {
            if (landing) {
                System.out.println("Plane " + planeID + ": Landing on the runway now!");
            } else {
                System.out.println("Plane " + planeID + ": Preparing for take-off in runway...");
                System.out.println("Plane " + planeID + ": Taking-off to the next destination!");
            }
            try {
                Thread.sleep(2000); // Landing and preparing to take-off takes 2 seconds.
            } catch (InterruptedException ex) {
                Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Runway is now clear!");
        }
    }
    
}
