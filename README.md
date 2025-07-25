# Parking-Spot-Allocation-System-using-Dijkstra-s-Algorithm

# Overview 

This Smart Parking Management System is designed to address the problem of illegal parking in urban areas by optimizing parking allocations and improving parking facilities' overall safety and efficiency. The system uses a grid-based layout to allocate parking spots based on proximity to the entrance, providing real-time updates to both drivers and administrators.

# Features

  1. Efficient Parking Allocation: The system uses an intelligent approach to assign the closest available parking spot, minimizing time spent searching for parking.
  2. Illegal Parking Prevention: Reduces unauthorized and illegal parking by systematically allocating spots.
  3. Differentiated Parking Zones: Support for long-term and short-term parking with distinct visual representations for easy identification.
  4. Real-Time Monitoring: Tracks user parking duration and calculates fees based on stay duration.
  5. User-Friendly Interface: Drivers receive notifications, including parking spot ID and visual directions to their allocated space.
  6. Extend Stay Option: Allows users to extend their parking duration with an additional fee.
  7. Deallocation and Fee Calculation: Easy exit process with automatic deallocation of parking spots and calculation of fees.

# Installation
https://github.com/bhavika1304/Parking-Spot-Allocation-System-using-Dijkstra-s-Algorithm.git

# How it Works
  - Parking Spot Allocation: The system divides the parking lot into multiple parking spots with a predefined layout. When a user arrives and requests parking, the system automatically assigns the closest available spot based on their vehicle type (4-wheeler or 2-wheeler) and parking preferences (long-term or short-term). The allocation is performed in real-time using Dijkstra's algorithm, ensuring the most efficient use of space.
  - Visual Representation and Tracking: The system displays the parking lot layout to the user, highlighting their allocated spot and providing real-time updates on the occupancy status of other spots. Different colours and borders indicate various types of spots—e.g., long-term parking is represented by pink with a dashed border, while short-term parking is green with a solid border.
  - Illegal Parking Control: Unauthorized parking is reduced by allowing users to park only in the spots that are systematically allocated to them. The system ensures that no vehicle can be parked without prior spot allocation, and any attempt to park illegally is flagged.
  - Timers and Fee Calculation: As soon as a vehicle parks in the allocated spot, a timer begins to track the duration. Fees are automatically calculated based on the time spent. Users are notified when their time is about to expire and can extend their stay if necessary. In case of overstays, an additional charge is applied.
  - Deallocation and Resetting: When the user exits the parking lot, the system deallocates the parking spot, resets its availability status, and removes any active timers. The user is presented with a final bill for their parking duration.

# Technical Details
  - Grid Layout for Parking Lots: The parking lot is represented as a grid, where each node corresponds to a parking spot and each connection between nodes represents the distance between them. The system ensures that parking spots are allocated in a way that minimizes walking distance from the entrance to the spot.
  - Real-Time Monitoring: An admin panel allows real-time monitoring of parking spot availability, with the ability to view which spots are occupied, their corresponding timers, and any potential issues like overstays or unauthorized parking.

# Future Developments
  - Integrating IoT sensors for real-time availability tracking.
  - Mobile app integration for remote parking management.
  - Multi-lot support for large urban areas.
  - Dynamic pricing based on peak hours and demand.
