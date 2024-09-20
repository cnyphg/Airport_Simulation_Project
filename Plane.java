package Assignment_5;

import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Plane implements Runnable {
    private int planeID;
    private int gate;
    private int disembarkNo;
    private int embarkNo;
    private double waitingTime;
    private Lock refuelLock;
    private boolean takeOff = false;
    private final Thread thread;
    
    public Plane(int planeID, int priority, Lock refuelLock) {
        this.planeID = planeID;
        this.refuelLock = refuelLock;
        
        Thread plane = new Thread(this);
        plane.setName("Plane " + planeID);
        plane.setPriority(priority);
        this.thread = plane;
        
        thread.start();
    }
    
    public Thread getThread() {
        return thread;
    }
    
    public boolean getTakeOffStatus() {
        return takeOff;
    }
    
    private int getGate() {
        return gate;
    }
    
    public void setGate(int gate) {
        this.gate = gate;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }
    
    private void coastingToGate() {
        // Coasting to Gate
        System.out.println(Thread.currentThread().getName() + ": Coasting to Gate " + getGate() + ".");
        try {
            Thread.sleep(2000); // coasting takes 2 second.
        } catch (InterruptedException e) {
            Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void coastingToRunway() {
        // Coasting to Runway
        System.out.println(Thread.currentThread().getName() + ": Coasting to runway.");
        ATC.releaseGate(getGate());
        try {
            Thread.sleep(2000); // Coasting takes 2 second.
        } catch (InterruptedException e) {
            Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void dockingGate() {
        System.out.println(Thread.currentThread().getName() + ": Docking to Gate " + gate);
        try {
            Thread.sleep(1500); // Docking takes 1.5 seconds.
        } catch (InterruptedException e) {
            Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void undockingGate() {
        System.out.println(Thread.currentThread().getName() + ": Undocking Gate " + gate);
        try {
            Thread.sleep(1500); // Undocking takes 1.5 seconds.
        } catch (InterruptedException e) {
            Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public int getDisembarkNo() {
        return disembarkNo;
    }

    public void setDisembarkNo(int disembarkNo) {
        this.disembarkNo = disembarkNo;
    }

    public int getEmbarkNo() {
        return embarkNo;
    }

    public void setEmbarkNo(int embarkNo) {
        this.embarkNo = embarkNo;
    }
    
    public void run() {
        // Requesting ATC permission to land and wait for response.
        double startTime = System.nanoTime();
        
        synchronized (Plane.class) {
            if (getThread().getPriority() == Thread.MAX_PRIORITY) {
                System.out.println(Thread.currentThread().getName() + ": EMERGENCY! Plane is currently low on fuel! Requesting permission to land immediately !");
            } else {
                System.out.println(Thread.currentThread().getName() + ": Requesting permission to land!");
            }
            ATC.addQueue(planeID);
        }

        try {
            synchronized (thread) {
                thread.wait();
            }
        } catch (InterruptedException e) {
            System.out.println("Program interrupted!");
        }
        
        // Landing on Runway and docking to specified gate.
        Runway.useRunway(planeID, true);
        coastingToGate();
        dockingGate();

        // Processes for disembarking passengers, refilling supplies, refueling,
        // and embarking of new passengers.
        EmbarkState disembark = new EmbarkState(planeID, false);
        Refill supplies = new Refill(planeID);
        Refuel refuel = new Refuel(planeID);
        EmbarkState embark = new EmbarkState(planeID, true);

        try {
            disembark.start();
            disembark.join();
            supplies.start();
            supplies.join();
            refuelLock.lock();
            refuel.start();
            refuel.join();
            refuelLock.unlock();
            embark.start();
            embark.join();
        } catch (InterruptedException e) {
            Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, e);
        }
        
        ATC.planes.get(planeID-1).setDisembarkNo(disembark.getPassengers());
        ATC.planes.get(planeID-1).setEmbarkNo(embark.getPassengers());
        
        // Undocking the gate, coasting to runway, preparing for take-off.
        undockingGate();
        coastingToRunway();
        Runway.useRunway(planeID, false);
        takeOff = true;
        
        double endTime = System.nanoTime();
        setWaitingTime((endTime - startTime)/1000000000); // Get time in seconds.
    }
}
