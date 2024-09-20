# Airport Simulation Project
The Asia Pacific Airport Simulation Project aims to model and simulate the operations of a small airport, including the handling of aircraft arrivals, departures, and ground operations. The simulation takes into account various components of an airport, such as the runway, gates, air traffic control, and passenger movement. The primary objective of this project is to provide a realistic representation of the complex interactions and coordination required among different entities in an airport environment. 

This simulation program is done using Java by implementing the concurrency concepts. The classes involved in this simulation are:

> 1.	Air Traffic Control (ATC) - Manages the queueing, landing and takeoff of aircrafts.
> 2.	Plane - Represents an aircraft that needs to land, disembark/embark passengers, refill supplies and cleaning, refuel and take off.
> 3.	Runway – Handles the landing and take-off of aircrafts.
> 4.	Refill – Handles the refilling supplies and cleaning process for a plane.
> 5.	Refuel – Simulates the refueling process for a plane.
> 6.	Passenger – Represents a passenger boarding or disembarking from a plane.
> 7.	Embark State – Handles the embarking and disembarking process of passengers of a plane.
> 8.	Main – Handles the simulation in different scenarios.

The simulation can run in two modes: normal scenario and emergency scenario. In the normal scenario, all planes have normal priority. Each plane follow the queue to land and take-off. In the emergency scenario, one of the planes is set to have maximum priority to simulate a emergency situation due to fuel shortage.A statistics is printed at the end of the simulation.
