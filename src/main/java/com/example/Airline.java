package com.example;

import java.util.*;

public class Airline {
	private String name;
	private ArrayList<Flight> futureFlights;
	
	public Airline(String name)
	{
		this.name = name;
		futureFlights = new ArrayList<Flight>();
	}
	
	public String getName()
	{
		return name;
	}
	
	// Purpose: Add a new flight to this airline - must have a unique (unused) flightID.
	public void addFlight(String startAirport, String destAirport, String flightID, String departTime, String departDate)
	{
		Flight newFlight = new Flight(startAirport, destAirport, flightID, departTime, departDate);
		if (getFlight(flightID) == null)
		{
			futureFlights.add(newFlight);
		}
	}
	
	// Purpose: Report how many flights are registered to this airline.
	public int getNumberOfFlights()
	{
		return futureFlights.size();
	}
	
	// Purpose: Obtain a flight based on its position in the list
	public Flight getFlight(int position)
	{
		if (position >= 0 && position < getNumberOfFlights() )
			return futureFlights.get(position);
		else
			return null;
	}
	
	// Purpose: Obtain a flight, based on the flightID of the flight (e.g. QF332 or QF267)
	public Flight getFlight(String flightID)
	{
		for (Flight current : futureFlights)
		{
			if (current.getFlightID().equalsIgnoreCase(flightID))
				return current;
		}
		
		return null;
	}
}
