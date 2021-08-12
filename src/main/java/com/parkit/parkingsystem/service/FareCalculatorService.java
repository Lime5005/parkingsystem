package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FareCalculatorService {

    /**
     * This method calculate the parking fare by durationHour for CAR and BIKE respectively.
     * If the durationHour is less or equal than 30 minutes, then the parking is free.
     * If the client is already in our database record, then the client will get -5% discount.
     * @param ticket a ticket with inTime and outTime record.
     * @param recurringClient a client whose vehicle number that can be found in our database.
     */
    public void calculateFare(Ticket ticket, boolean recurringClient) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out inTime provided is incorrect:" + ticket.getOutTime().toString());
        }
        LocalDateTime inTime;
        inTime = ticket.getInTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
//        System.out.println("inTime = " + inTime);

        LocalDateTime outTime;
        outTime = ticket.getOutTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
//        System.out.println("outTime = " + outTime);

        Duration duration = Duration.between(inTime, outTime);
        // minutes between from and to
        double durationHour = duration.toMinutes() / 60f; // d = double, f = float
//        System.out.println("durationHour = " + durationHour);

        //TODO: Some tests are failing here. Need to check if this logic is correct
        if (durationHour <= 0.5) {
            ticket.setPrice(0.0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if (recurringClient) {
                        ticket.setPrice((durationHour * Fare.CAR_RATE_PER_HOUR) * 0.95);
                    } else {
                        ticket.setPrice(durationHour * Fare.CAR_RATE_PER_HOUR);
                    }
                    break;
                }
                case BIKE: {
                    if (recurringClient) {
                        ticket.setPrice((durationHour * Fare.BIKE_RATE_PER_HOUR) * 0.95);
                    } else {
                        ticket.setPrice(durationHour * Fare.BIKE_RATE_PER_HOUR);
                    }
                    break;
                }
                default: throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}