package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingSpotDaoTest {
    private static DataBaseTestConfig dataBaseTestConfig;
    private static ParkingSpotDAO parkingSpotDAO;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        dataBaseTestConfig = new DataBaseTestConfig();
    }

    @Test
    public void getNextAvailableSlot_withParkingType_shouldReturnASpot() throws Exception {
        String sql = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";

        Connection connection = dataBaseTestConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "CAR");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int reInt = rs.getInt(1);

        int slot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(reInt, slot);
    }

    @Test
    public void updateParking_withParkingSlot_shouldReturnBoolean() throws Exception {
        String sql =  "update parking set available = ? where PARKING_NUMBER = ?";
        Connection connection = dataBaseTestConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setBoolean(1, false);
        ps.setInt(2, 1);
        int i = ps.executeUpdate();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        boolean b = parkingSpotDAO.updateParking(parkingSpot);

        assertEquals(i == 1, b);
    }

}
