package com.example;

import java.util.*;


// This class is a text-console based menu-driven system.
// If you prefer, you may write either JUnit test cases or otherwise script a sequence of
// automated steps to test that other classes function correctly each time you do some refactoring
// to speed up your checking. However the final assignment needs to allow this
// menu system to function with the same user observations as originally.

public class MenuSystem {
	private Scanner scan;
	private List<Airline> airlines;
		
	public MenuSystem()
	{
		airlines = new ArrayList<Airline>();
		scan = new Scanner(System.in);
		
		setupAirlines();
		runMenu();
	}
	
	public void setupAirlines()
	{
		Airline a1 = new Airline("Qantas");
		airlines.add(a1);
		
		a1.addFlight("Melbourne", "Sydney", "QF241", "8:48am", "22 Sept");
		a1.addFlight("Melbourne", "Adelaide", "QF255", "10:35am", "22 Sept");
		a1.addFlight("Canberra", "Brisbane", "QF374", "1:15pm", "22 Sept");
		a1.addFlight("Canberra", "Perth", "QF366", "5:28pm", "22 Sept");
		a1.addFlight("Hobart", "Sydney", "QF847", "4:15pm", "22 Sept");

		Airline a2 = new Airline("Air New Zealand");
		a2.addFlight("Sydney", "Aukland", "NZ471", "3:33pm", "22 Sept");
		a2.addFlight("Melbourne","Christchurch", "NZ318", "11:22am", "22 Sept");

		airlines.add(a2);

	}
	
	public void runMenu()
	{
		int choice;
		
		boolean stillRunning = true;		// in order to commence program
		
		while (stillRunning)
		{
			showMenuOptions();
			choice = getUserIntSelection(0,10);

			switch (choice)
			{
				case 1:
					bookFlight();
					break;
				case 2:
					cancelBooking();
					break;
				case 3:
					confirmWillFly();
					break;
				case 4:
					checkInAtAirport();
					break;
				case 5:
					boardFlight();
					break;
				case 6:
					viewDepartingFlights();
					break;
				case 7:
					setFlightCheckingIn();
					break;
				case 8:
					allowFlightBoarding();
					break;
				case 9:
					putMealsOntoFlight();
					break;
				case 10:
					noteFlightDeparted();
					break;
				case 0:
					stillRunning = false;				// causes the main loop of program to end (i.e. quits)
					break;
				default:
					System.out.println("Unexpected selection made. Doing nothing.");
					break;
			}
		}
		System.out.println("Goodbye!");
	}

	// METHOD:  showMenuOptions
	// PURPOSE: To present a menu/list of options to the user.
	// PASSED:  nothing
	// RETURNS: nothing
	// EFFECTS: A list of options is displayed on the screen.
	public void showMenuOptions()
	{
		System.out.println();		// ensure a break between previous output and the menu
		System.out.println("What would you like to do?");
		System.out.println("Passenger Actions:");
		System.out.println("1.  Book a passenger on a flight");
		System.out.println("2.  Cancel a passenger's booking (free up the seat)");
		System.out.println("3.  Flight Day: Confirm passenger will be flying");
		System.out.println("4.  Flight Day: Check-in passenger at airport");
		System.out.println("5.  Flight Day: Note passenger as boarded on flight");
		System.out.println("Flight Actions:");
		System.out.println("6.  View list of flights departing from airport");
		System.out.println("7.  Update flight to be ready for check-in");
		System.out.println("8.  Update flight to be ready for boarding");
		System.out.println("9.  Put meals on flight ready for departure");
		System.out.println("10.  Update flight to be departed");
		System.out.println("0.  Exit Program");
	}
	
	// METHOD:  getUserIntSelection
	// PURPOSE: To obtain from the user a selection (an integer) from a range of values
	// PASSED:
	//    lower - the Lowest permissible value the user can enter as their selection.
	//    upper - the Highest permissible value the user can enter
	// RETURNS:
	//    The value entered by the user, unless the "lower" parameter was higher
	//    than the "upper" parameter, in which case 0 is returned.
	// EFFECTS:
	//    A prompt is displayed on the screen to ask the user for a value in the range.
	//    Input is sought from the user via the keyboard (System.in)
	public int getUserIntSelection(int lower, int upper)
	{
		int userInput;
		
		if (lower > upper)
			return 0;
		
		do {
			System.out.print("Enter a selection ("+lower + "-" + upper+"):");
			userInput = scan.nextInt();		// obtain the input
			scan.nextLine();					// gets rid of the newline after the number we just read
			
			if (userInput < lower || userInput > upper)
				System.out.println("Invalid choice.");
		} while (userInput < lower || userInput > upper);
		System.out.println();		// put a space before the next output
		
		return userInput; 
	}
	


	public void bookFlight()
	{
		String flightID;
		System.out.print("Enter the Flight ID to book a seat on:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
				    found = true;    // stops the search, so 'i' will be the correct position. 
				else 
					i++;
			}
			
			if (found) {
				System.out.println("Selected Flight: " + currentFlight.toString());
				int seatCode = 0;
				String name;
				int age;
				boolean dietaryNeeds;
				
				do {
					System.out.print("Enter the seat number (1-100) to try and book: ");
					seatCode = getUserIntSelection(1,100) - 1;
				} while (seatCode < 0);
				
				System.out.print("Enter the name of the passenger: ");
				name = scan.nextLine();
				
				System.out.print("Enter the age of the passenger: ");
				age = getUserIntSelection(0,100);

				System.out.print("Does the passenger have special dietary requirements (0 = no, 1 = yes): ");
				if (getUserIntSelection(0,1) == 1)
					dietaryNeeds = true;
				else
					dietaryNeeds = false;
				
				if (currentFlight.assignPassengerSeat(seatCode, name, age, dietaryNeeds))
				    System.out.println("Customer is booked for flight.\n");
				else
					System.out.println("No Booking Recorded - please check the selections.");
				
				return;   // Exits the method, which prevents the loop continuing.
			}
			else System.out.println("Could not find that flight.");
		}
	}
	
	public void cancelBooking()
	{
		String flightID;
		System.out.print("Enter the Flight ID to cancel a booking on:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
				    found = true;    // stops the search, so 'i' will be the correct position. 
				else 
					i++;
			}
			
			if (found) {
				System.out.println("Selected Flight: " + currentFlight.toString());
				int seatCode = 0;
				String name;
				
				do {
					System.out.print("Enter the seat number (1-100) to cancel for: ");
					seatCode = getUserIntSelection(1,100) - 1;
				} while (seatCode < 0);
				
				System.out.print("Enter the name of the passenger who is cancelling: ");
				name = scan.nextLine();
				
				if (currentFlight.cancelPassengerBooking(seatCode, name))
				    System.out.println("Customer is no longer booked for flight.");
				else
					System.out.println("Not Cancelled - Input mismatch.");
				
				return;		// Prevents further search since already matched.
			}
			else System.out.println("Could not find that flight.");
		}	
	}
	
	public void confirmWillFly()
	{
		String flightID;
		System.out.print("Enter the Flight ID to confirm a booking for:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
				    found = true;    // stops the search, so 'i' will be the correct position. 
				else 
					i++;
			}
			
			if (found) {
				System.out.println("Selected Flight: " + currentFlight.toString());
				int seatCode = 0;
				String name;
				
				do {
					System.out.print("Enter the seat number (1-100) being confirmed: ");
					seatCode = getUserIntSelection(1,100) - 1;
				} while (seatCode < 0);
				
				System.out.print("Enter the name of the passenger who is confirming their booking: ");
				name = scan.nextLine();
				
				if (currentFlight.confirmPassengerBooking(seatCode, name))
				    System.out.println("Customer has confirmed for flight.");
				else
					System.out.println("Either the passenger was already confirmed, or incorrect data was entered.");
				
				return;		// Prevents further search since already matched.
			}
			else System.out.println("Could not find that flight.");
		}
	}
	
	public void checkInAtAirport()
	{
		String flightID;
		System.out.print("Enter the Flight ID that the passenger is checking-in for:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
				    found = true;    // stops the search, so 'i' will be the correct position. 
				else 
					i++;
			}
			
			if (found) {
				System.out.println("Selected Flight: " + currentFlight.toString());
				int seatCode = 0;
				String name;

				System.out.print("Enter the NAME of the passenger who is checking-in: ");
				name = scan.nextLine();

				do {
					System.out.print("Enter the SEAT NUMBER (1-100) they have been allocated: ");
					seatCode = getUserIntSelection(1,100) - 1;
				} while (seatCode < 0);
				
				if (currentFlight.isConfirmed(seatCode))
				{
					if (currentFlight.checkinPassenger(seatCode, name))
					{
						System.out.println("Customer " + name + " has checked-in at the airport for flight and can now lodge any luggage.");
						
						System.out.print("How many items of luggage do they want to submit (0-5):");
						int luggageItems = getUserIntSelection(0,5);
						currentFlight.setCheckedLuggageItems(seatCode, luggageItems);	// Store the luggage data for the passenger
					}
					else
						System.out.println("Either the passenger had already checked-in, or incorrect data was entered.");
				} else
					System.out.println("The customer did not confirm that they were flying, so cannot check in.");
				return;		// Prevents further search since already matched.
			}
		}	
		System.out.println("Could not find that flight.");
	}
	
	public void boardFlight()
	{
		String flightID;
		System.out.print("Enter the Flight ID that the passenger is ready to board:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
				    found = true;    // stops the search, so 'i' will be the correct position. 
				else 
					i++;
			}
			
			if (found) {
				System.out.println("Selected Flight: " + currentFlight.toString());
				int seatCode = 0;
				String name;

				System.out.print("Enter the NAME of the passenger who is checking-in: ");
				name = scan.nextLine();

				do {
					System.out.print("Enter the SEAT NUMBER (1-100) they have been allocated: ");
					seatCode = getUserIntSelection(1,100) - 1;
				} while (seatCode < 0);
				
				if (currentFlight.isCheckedIn(seatCode))
				{
					if (currentFlight.boardPassenger(seatCode, name))
						System.out.println("Customer " + name + " has now boarded.");
					else
						System.out.println("Either the passenger had already boarded, or mismatching data was entered.");
				} else
					System.out.println("The customer had not checked-in, so cannot board the flight.");
				return;		// Prevents further search since already matched.
			}
			else System.out.println("Could not find that flight.");
		}		
		
	}
	
	public void viewDepartingFlights()
	{
		String departFromPlace;
		System.out.print("View flights that depart from which airport? ");
		departFromPlace = scan.nextLine();
		boolean foundSome = false;
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			totalFlights = airline.getNumberOfFlights();
			
			for (int i = 0; i < totalFlights; i++)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getStartAirport().equalsIgnoreCase(departFromPlace)) 
				{
				    System.out.println( currentFlight + " [" + airline.getName() + "]");
				    foundSome = true;
				}
			}
		}
			
		if (!foundSome)
			System.out.println("No flights found departing from " + departFromPlace);
	}
	
	public void setFlightCheckingIn()
	{
		String flightID;
		System.out.print("Enter the Flight ID that is to be set as allowing checking-in:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
					found = true;
				else 
					i++;
			}
			
			if (found) {
				if ( currentFlight.moveToNextStage(Flight.CHECKING_IN) ) {
					System.out.println("Selected Flight: " + currentFlight + " - now in the check-in stage");
				} else {
					System.out.println("Unable to set flight " + currentFlight + " to check-in stage. Does it having any bookings?");
					System.out.println("It is in the " + currentFlight.getStageString() + " stage");
				}
				
				return;				// No more to do, since we found the flight
			}
			else System.out.println("Could not find that flight.");
		}
	}
	
	public void allowFlightBoarding()
	{
		String flightID;
		System.out.print("Enter the Flight ID that is to be set as allowing boarding:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
					found = true;
				else 
					i++;
			}
			
			if (found) {
				try {
					if ( currentFlight.getDepartureGate() <= 0) {		// No departure gate is set yet, so ask for one
						System.out.print("Which gate (1-25) will the flight board from? ");
						int departureGate = getUserIntSelection(1,25);
						if ( !currentFlight.setDepartureGate(departureGate) )
							System.out.println("Unable to set departure gate for flight.");
					}
					
					if ( currentFlight.moveToNextStage(Flight.BOARDING) ) {
						System.out.println("Selected Flight: " + currentFlight + " - now in the boarding stage");
					} else {
						System.out.println("Unable to set flight " + currentFlight + " to boarding stage: " + currentFlight);
						System.out.println("It is in the " + currentFlight.getStageString() + " stage");
					}
				} catch (Exception exc) {
					System.out.println(exc.getMessage());
				}
				return;				// No more to do, since we found the flight
			}
			else System.out.println("Could not find that flight.");
		}
		
	}
	
	public void putMealsOntoFlight()
	{
		String flightID;
		System.out.print("Enter the Flight ID that needs meals to be added on board:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
					found = true;
				else 
					i++;
			}
			
			if (found) {
				if ( currentFlight.setMealsNowOnPlane() ) {
					System.out.println("Selected Flight: " + currentFlight + " - now has meals on board.");
				} else {
					System.out.println("Unable to set flight " + currentFlight + " to have meals on board.");
					System.out.println("(It is in the " + currentFlight.getStageString() + " stage)");
				}
				
				return;				// No more to do, since we found the flight
			}
			else System.out.println("Could not find that flight.");
		}
		
	}
	
	public void noteFlightDeparted()
	{
		String flightID;
		System.out.print("Enter the Flight ID that is to be set as departed/flying:");
		flightID = scan.nextLine();
		
		for (Airline airline : airlines) {
			int totalFlights;
			Flight currentFlight = null;
			int i = 0; 
			boolean found = false;
			totalFlights = airline.getNumberOfFlights();
			
			while (i < totalFlights && found == false)
			{
				currentFlight = airline.getFlight(i);
				if (currentFlight.getFlightID().equalsIgnoreCase(flightID))
					found = true;
				else 
					i++;
			}
			
			if (found) {
				try {
					if ( currentFlight.moveToNextStage(Flight.FLYING) ) {
						System.out.println("Selected Flight: " + currentFlight + " - now in the flying stage");
					} else {
						System.out.println("Unable to set flight " + currentFlight + " to flying stage: " + currentFlight);
						System.out.println("It is in the " + currentFlight.getStageString() + " stage");
					}
				} catch (Exception exc) {
					System.out.println(exc.getMessage());
				}
				return;				// No more to do, since we found the flight
			}
			else System.out.println("Could not find that flight.");
		}
	}
}