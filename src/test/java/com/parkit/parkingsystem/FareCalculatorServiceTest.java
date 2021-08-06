package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @ParameterizedTest
    @MethodSource("withDiscountOrNot")
    public void calculateFareCar(boolean recurringClient, double discount){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, recurringClient);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR * discount);
    }

    @ParameterizedTest
    @MethodSource("withDiscountOrNot")
    public void calculateFareBike(boolean recurringClient, double discount){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, recurringClient);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR * discount);
    }

    @ParameterizedTest
    @ValueSource(booleans =  {true, false})
    public void calculateFareUnknownType(boolean recurringClient){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, recurringClient));
    }

    @ParameterizedTest
    @ValueSource(booleans =  {true, false})
    public void calculateFareBikeWithFutureInTime(boolean recurringClient){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, recurringClient));
    }

    @ParameterizedTest
    @MethodSource("carOrBikeWithDiscountOrNot")
    public void calculateFareWithMoreThanADayParkingTime(ParkingType type, double rate, boolean recurringClient, double discount){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  25 * 60 * 60 * 1000) );//25 hours parking time should give 25 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, type,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, recurringClient);
        assertEquals( (25 * rate * discount) , ticket.getPrice());
    }

    private static Stream<Arguments> withDiscountOrNot() {
        return Stream.of(
                Arguments.of(true, 0.95),
                Arguments.of(false, 1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("carOrBikeWithDiscountOrNot")
    public void calculateFareWithLessThanHalfHourParkingTime(ParkingType type, double rate, boolean recurringClient, double discount){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  29 * 60 * 1000) );//29 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, type,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, recurringClient);
        assertEquals( (0.0 * rate * discount) , ticket.getPrice());
    }

    @ParameterizedTest
    @MethodSource("carOrBikeWithDiscountOrNot")
    public void calculateFareWithEqualToHalfHourParkingTime(ParkingType type, double rate, boolean recurringClient, double discount){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );//30 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, type,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, recurringClient);
        assertEquals((0.0 * rate * discount), ticket.getPrice() );
    }

    @ParameterizedTest
    @MethodSource("carOrBikeWithDiscountOrNot")
    public void calculateFareWithLessThanOneHourParkingTime(ParkingType type, double rate, boolean recurringClient, double discount) {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 0.75 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, type,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, recurringClient);
//        System.out.println(ticket.getPrice());
        assertEquals(((0.75 * rate) * discount), ticket.getPrice() );
    }

    private static Stream<Arguments> carOrBikeWithDiscountOrNot() {
        return Stream.of(
                Arguments.of("CAR", Fare.CAR_RATE_PER_HOUR, true, 0.95),
                Arguments.of("CAR", Fare.CAR_RATE_PER_HOUR, false, 1.0),
                Arguments.of("BIKE", Fare.BIKE_RATE_PER_HOUR, true, 0.95),
                Arguments.of("BIKE", Fare.BIKE_RATE_PER_HOUR, false, 1.0)
        );
    }

}
