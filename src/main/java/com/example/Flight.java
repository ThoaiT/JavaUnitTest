package com.example;
public class Flight {
    private String startAirport;
    private String destAirport;
    private String flightID;
    private String departTime;
    private String departDate;
    private int departureGate;
    private int stage;
    
    public static final int TAKING_BOOKINGS = 1;
    public static final int CHECKING_IN = 2;
    public static final int BOARDING = 3;
    public static final int FLYING = 4;

    private String[] passengerName = new String[100];
    private int[] passengerAge = new int[100];
    private boolean[] dietaryNeeds = new boolean[100];
    private int[] checkedLuggageItems = new int[100];
    private boolean[] available = new boolean[100];		// Whether each of the seats is available for booking, or not.
    private boolean[] confirmed = new boolean[100];		// Has confirmed that they will be on the flight (24 hours before)
    private boolean[] arrived = new boolean[100];		// Has arrived and checked-in at the airport
    private boolean[] boarded = new boolean[100];		// Has had ticket scanned, and now boarded the plane

    private int mealsOnBoard;
    private boolean luggageOnBoard;
    
    // Constructor
    public Flight(String startAirport, String destAirport, String flightID, String departTime, String departDate) {
        this.startAirport = startAirport;
        this.destAirport = destAirport;
        this.flightID = flightID;
        this.departTime = departTime;
        this.departDate = departDate;
        
        for (int i = 0; i < 100; i++)
        {
        	passengerName[i] = "";
        	dietaryNeeds[i] = false;
        	passengerAge[i] = -1;
        	checkedLuggageItems[i] = 0;
        	available[i] = true;
        }
        
        stage = TAKING_BOOKINGS;
    }

    public String toString()
    {
    	return (flightID + " from " + startAirport + " to " + destAirport + " on " + departDate + " at " + departTime);
    }
    
    // Accessors and Mutators
    
    
    public String getStartAirport() {
        return startAirport;
    }

    public String getDestAirport() {
        return destAirport;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getDepartTime() {
        return departTime;
    }

    public String getDepartDate() {
        return departDate;
    }

    public int getDepartureGate() {
        return departureGate;
    }

    // Assign the departure gate (only valid during the checking-in phase).
    public boolean setDepartureGate(int departureGate) {
    	if (this.departureGate == 0 && stage == CHECKING_IN) {
    		this.departureGate = departureGate;
    		return true;
    	}
    	else return false;
    }

    public String getPassengerName(int index) {
        return passengerName[index];
    }

    public boolean assignPassengerSeat(int seatIndex, String name, int age,boolean needs) {
    	if (seatIndex >= 0 && seatIndex < available.length)
    	{
    		if (available[seatIndex] && stage == TAKING_BOOKINGS)
    		{
    			if (name != null && !name.equals(""))
    			{
    				available[seatIndex] = false;
    				passengerName[seatIndex] = name;
    				passengerAge[seatIndex] = age;
    				dietaryNeeds[seatIndex] = needs;
    				checkedLuggageItems[seatIndex] = 0;
    				confirmed[seatIndex] = false;
    				arrived[seatIndex] = false;
    				boarded[seatIndex] = false;
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    // If the supplied name matches the recorded name of a passenger in a particular seat, the booking will be cancelled.
    public boolean cancelPassengerBooking(int index, String name)
    {
    	if (index >= 0 && index < available.length)
    	{
    		if ( !available[index] && (name != null) && passengerName[index].equalsIgnoreCase(name) ) {
    			available[index] = true;
    			passengerName[index] = "";
    			return true;
    		}
    	}
        
    	return false;
    }

    // If the supplied name matches the recorded name of a passenger in a particular seat, the booking will be confirmed.
    public boolean confirmPassengerBooking(int index, String name)
    {
    	if (index >= 0 && index < available.length)
    	{
    		if ( !available[index] && (stage == CHECKING_IN) && 
    		 (name != null) && passengerName[index].equalsIgnoreCase(name)
    		 && confirmed[index] == false				// No point in confirming, if they have already previously confirmed 
    		 ) {
    			confirmed[index] = true;
    			return true;
    		}
    	}
        
    	return false;
    }
    
   // If the supplied name matches the recorded name of a passenger in a particular seat, and they have been recorded as arrived, 
   // their booking will now be set to boarded.
   public boolean boardPassenger(int index, String name)
    {
    	if (index >= 0 && index < available.length)
    	{
    		if ( !available[index] && (stage == BOARDING) && 
    		 (name != null) && passengerName[index].equalsIgnoreCase(name)
    		 && arrived[index] == true
    		 && boarded[index] == false 		// No point in recording, if they have already been recorded as boarded 
    		 ) {
    			boarded[index] = true;
    			return true;
    		}
    	}
        
    	return false;
    }
  
   
    public boolean getDietaryNeeds(int index) {
        return dietaryNeeds[index];
    }

    // Once a seat is booked by a passenger, any time before check-in we can set whether the passenger has dietary requirements 
    public void setDietaryNeeds(int index, boolean needs) {
		if (available[index] == false && stage == TAKING_BOOKINGS)
		{
			this.dietaryNeeds[index] = needs;
		}
    }

    public String getNumMealsToGoOnPlane()
    {
		int plainMeals = 0;
		int specialMeals = 0;

		if (mealsOnBoard == 0) {
			for (int i = 0; i < available.length; i++)
	    	{
	    		if (arrived[i] == true)			// For each person that has arrived (checked-in)
	    		{
	    			if (dietaryNeeds[i])
	    				specialMeals++;
	    			else
	    				plainMeals++;
	    		}
	    	}
	    	// Return a string description of the meals required to go on plane
	    	return "" + specialMeals + " special dietary meals and " + plainMeals + " ordinary meals";
		}
		else
			return "Meals should already be on the plane";
    }
    
    public boolean setMealsNowOnPlane()
    {
		int plainMeals = 0;
		int specialMeals = 0;

		if (mealsOnBoard == 0) {			// Only valid, if not already added the meals.
			if (stage == BOARDING) {		// Only valid in the boarding phase.
		    	for (int i = 0; i < available.length; i++)
		    	{
		    		if (arrived[i] == true)			// For each person that has arrived (checked-in)
		    		{
		    			if (dietaryNeeds[i])
		    				specialMeals++;
		    			else
		    				plainMeals++;
		    		}
		    	}
		
		    	mealsOnBoard= specialMeals + plainMeals;
		    	return true;
			}
		}
		
		return false;
    }
    
    public boolean getMealsAreOnPlane()
    {
    	return (mealsOnBoard > 0);
    }
    
    public int getMealsOnPlaneCount()
    {
    	return mealsOnBoard;
    }
    
    public int getPassengerAge(int index) {
        return passengerAge[index];
    }
    
    // If the supplied name matches the recorded name of a passenger in a particular seat, and they have confirmed they are travelling, 
    // the booking will now be set to arrived, meaning they have checked in. They will then be allowed to lodge luggage if necessary.
    public boolean checkinPassenger(int index, String name)
     {
     	if (index >= 0 && index < available.length)
     	{
     		if ( !available[index] && (stage == CHECKING_IN) && 
     		 (name != null) && passengerName[index].equalsIgnoreCase(name)
     		 && confirmed[index] == true
     		 && arrived[index] == false 		// No point in confirming, if they have already been noted as arrived 
     		 ) {
     			arrived[index] = true;
     			return true;
     		}
     	}
         
     	return false;
     }
   

    public boolean isAvailable(int index) {
        return available[index];
    }

    public boolean isConfirmed(int index) {
        return confirmed[index];
    }
    
    public boolean isCheckedIn(int index) {
        return arrived[index];
    }

    public boolean isBoarded(int index) {
        return boarded[index];
    }


    // Once checking-in time has begun, we can find out how many checked luggage items the passenger has
    public int getCheckedLuggageItems(int index) 
    {
		if (stage == TAKING_BOOKINGS)
			throw new RuntimeException("Too early to determine");
		else {
			if (available[index] == false)
				return checkedLuggageItems[index];
			else return 0;
		}
    }

    // If the flight is open for checking-in at the airport, we can set the number of checked luggage items
    public void setCheckedLuggageItems(int index, int items) 
    {
		if (stage == TAKING_BOOKINGS)
			throw new RuntimeException("Too early to specify checked luggage");
		else if (stage == CHECKING_IN) {
			if (items >= 0)
				checkedLuggageItems[index] = items;
			else
				checkedLuggageItems[index] = 0;
		} else
			throw new RuntimeException("Too late to specify checked luggage");
    }
    
    public boolean moveToNextStage(int nextStage)
    {
    	boolean validChange = false;
    	if (stage == TAKING_BOOKINGS && nextStage == CHECKING_IN)
    	{
    		int totalPassengers = 0;
    		for (int i = 0; i < available.length; i++) {
    			if (available[i] == false)
    				totalPassengers++;
    		}
    		if (totalPassengers > 0) {
    		    stage = CHECKING_IN;
    		    validChange = true;
    		}
    	}
    	else if (stage == CHECKING_IN && nextStage == BOARDING)
    	{
    		int totalPassengers = 0;
    		int confirmedPassengers = 0;
    		int arrivedPassengers = 0;

    		for (int i = 0; i < available.length; i++) {
    			if (arrived[i] == true)
    				arrivedPassengers++;
    			if (available[i] == false)
    				totalPassengers++;
    			if (confirmed[i] == true)
    				confirmedPassengers++;
     		} 

    		// If at least 75% of total passengers have arrived, and a gate has been designated, then we can allow the stage to move to boarding.
    		if (((1.0 * arrivedPassengers)/confirmedPassengers) < 0.66)
    			throw new RuntimeException("Not enough passengers have arrived yet.");
    		else if (departureGate <=  0) {
    			throw new RuntimeException("No departure gate assigned for flight " + flightID);
    		} else {		// all is fine so advance to next stage 
    		    stage = BOARDING;
    		    validChange = true;
    		} 

    	}
    		
    	else if (stage == BOARDING && nextStage == FLYING)
    	{
    		int arrivedPassengers = 0;
    		int boardedPassengers = 0;
    		for (int i = 0; i < available.length; i++) {
    			if (arrived[i] == true)
    				arrivedPassengers++;
    			if (boarded[i] == true)
    			{System.out.println(passengerName[i]);
    				boardedPassengers++;
    			}
    		}
    		// If all checked-in passengers have now boarded, and enough meals have been placed we can allow the stage to move to boarding.
    		if (boardedPassengers == arrivedPassengers && mealsOnBoard == arrivedPassengers){		
    		    stage = FLYING;
    		    validChange = true;
    	        mealsOnBoard = 0;				// No meals or luggage will be on board yet.
    	        luggageOnBoard = false;
    		} else if (boardedPassengers < arrivedPassengers) {
    			throw new RuntimeException("Not enough passengers have boarded yet.");
    		} else if (mealsOnBoard < arrivedPassengers)
    			throw new RuntimeException("Not enough meals have been put on-board yet.");
    	}
    	
    	return validChange;
    }
    
    public String getStageString()
    {
    	String descript = "unknown";
    	switch (stage) {
    		case TAKING_BOOKINGS:
    			descript = "Taking Bookings";
    			break;
    		case CHECKING_IN:
    			descript = "Checking In";
    			break;
    		case BOARDING:
    			descript = "Boarding";
    			break;
    		case FLYING:
    			descript = "Flying";
    			break;
    	}
    	return descript;
    }
}
