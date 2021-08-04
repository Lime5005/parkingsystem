package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out inTime provided is incorrect:"+ticket.getOutTime().toString());
        }

        LocalDateTime inTime;
        inTime = ticket.getInTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("inTime = " + inTime);

        LocalDateTime outTime;
        outTime = ticket.getOutTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("outTime = " + outTime);

        Duration duration = Duration.between(inTime, outTime);
        // minutes between from and to
        double durationHour = duration.toMinutes() / 60f; // d = double, f = float
        System.out.println("durationHour = " + durationHour);

        //TODO: Some tests are failing here. Need to check if this logic is correct
        if (durationHour <= 0.5) {
            ticket.setPrice(0.0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticket.setPrice(durationHour * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationHour * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: throw new IllegalArgumentException("Unknown Parking Type");
            }
        }

    }
}