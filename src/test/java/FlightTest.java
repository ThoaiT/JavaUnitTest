
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.Flight;



class FlightTest {
    Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight("JFK", "LAX", "AA101", "10:00 AM", "2024-09-12");
    }

    @Test
    void testConstructorAndToString() {
        assertEquals("AA101 from JFK to LAX on 2024-09-12 at 10:00 AM", flight.toString());
    }

    @Test
    void testAssignPassengerSeat_Success() {
        boolean success = flight.assignPassengerSeat(1, "John Doe", 25, false);
        assertTrue(success);
        assertEquals("John Doe", flight.getPassengerName(1));
    }

    @Test
    void testAssignPassengerSeat_Failure_StageNotBooking() {
        flight.moveToNextStage(Flight.CHECKING_IN); // Change to CHECKING_IN
        boolean success = flight.assignPassengerSeat(1, "John Doe", 25, false);
        assertFalse(success); // Booking should only happen during the TAKING_BOOKINGS stage
    }

    @Test
    void testCancelPassengerBooking_Success() {
        flight.assignPassengerSeat(1, "John Doe", 25, false);
        boolean success = flight.cancelPassengerBooking(1, "John Doe");
        assertTrue(success);
        assertTrue(flight.isAvailable(1));
    }

    @Test
    void testCancelPassengerBooking_Failure() {
        flight.assignPassengerSeat(1, "John Doe", 25, false);
        boolean success = flight.cancelPassengerBooking(1, "Jane Doe"); // Incorrect name
        assertFalse(success);
        assertFalse(flight.isAvailable(1));
    }

    @Test
    void testConfirmPassengerBooking_Success() {
        flight.assignPassengerSeat(1, "John Doe", 25, false);
        flight.moveToNextStage(Flight.CHECKING_IN); // Move to CHECKING_IN stage
        boolean confirmed = flight.confirmPassengerBooking(1, "John Doe");
        assertTrue(confirmed);
        assertTrue(flight.isConfirmed(1));
    }

    @Test
    void testCheckinPassenger_Success() {
        flight.assignPassengerSeat(1, "John Doe", 25, false);
        flight.moveToNextStage(Flight.CHECKING_IN); // Move to CHECKING_IN stage
        flight.confirmPassengerBooking(1, "John Doe");
        boolean checkinSuccess = flight.checkinPassenger(1, "John Doe");
        assertTrue(checkinSuccess);
        assertTrue(flight.isCheckedIn(1));
    }

    @Test
    void testSetDepartureGate_Success() {
        flight.moveToNextStage(Flight.CHECKING_IN); // Move to CHECKING_IN stage
        boolean gateAssigned = flight.setDepartureGate(5);
        assertTrue(gateAssigned);
        assertEquals(5, flight.getDepartureGate());
    }

    @Test
    void testSetDepartureGate_Failure() {
        boolean gateAssigned = flight.setDepartureGate(5); // Cannot assign gate in TAKING_BOOKINGS stage
        assertFalse(gateAssigned);
    }

    @Test
    void testBoardPassenger_Success() {
        flight.assignPassengerSeat(1, "John Doe", 25, false);
        flight.moveToNextStage(Flight.CHECKING_IN);
        flight.confirmPassengerBooking(1, "John Doe");
        flight.checkinPassenger(1, "John Doe");
        flight.setDepartureGate(5);
        flight.moveToNextStage(Flight.BOARDING); // Move to BOARDING stage
        boolean boardSuccess = flight.boardPassenger(1, "John Doe");
        assertTrue(boardSuccess);
        assertTrue(flight.isBoarded(1));
    }

    @Test
    void testMoveToNextStage_TakingBookingsToCheckingIn_Success() {
        flight.assignPassengerSeat(1, "John Doe", 25, false); // Book at least one passenger
        boolean stageChanged = flight.moveToNextStage(Flight.CHECKING_IN);
        assertTrue(stageChanged);
        assertEquals("Checking In", flight.getStageString());
    }

    @Test
    void testMoveToNextStage_TakingBookingsToCheckingIn_Failure_NoPassengers() {
        boolean stageChanged = flight.moveToNextStage(Flight.CHECKING_IN); // No passengers booked
        assertFalse(stageChanged);
        assertEquals("Taking Bookings", flight.getStageString());
    }

    @Test
    void testSetCheckedLuggageItems_Success() {
        flight.assignPassengerSeat(1, "John Doe", 25, false);
        flight.moveToNextStage(Flight.CHECKING_IN);
        flight.confirmPassengerBooking(1, "John Doe");
        flight.checkinPassenger(1, "John Doe");
        flight.setCheckedLuggageItems(1, 2);
        assertEquals(2, flight.getCheckedLuggageItems(1));
    }

    @Test
    void testSetCheckedLuggageItems_TooEarly() {
        flight.assignPassengerSeat(1, "John Doe", 25, false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flight.setCheckedLuggageItems(1, 2);
        });
        assertEquals("Too early to specify checked luggage", exception.getMessage());
    }
}
