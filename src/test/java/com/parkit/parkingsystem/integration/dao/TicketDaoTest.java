package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketDaoTest {
    private static DataBaseTestConfig dataBaseTestConfig;
    private static TicketDAO ticketDAO;

    @BeforeAll
    private static void setUp() {
        dataBaseTestConfig = new DataBaseTestConfig();
        ticketDAO = new TicketDAO();
    }

    @Test
    public void saveTicket_withATicket_shouldReturnBoolean() throws Exception {
        String sql = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
        Connection connection = dataBaseTestConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, 1);
        ps.setString(2, "test");
        ps.setDouble(3, 0.0);
        ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

        boolean bl = ps.execute();
        Ticket test = ticketDAO.getTicket("test");
        assertEquals(bl, ticketDAO.saveTicket(test));
    }

    @Test
    public void updateTicket_withATicket_shouldReturnBoolean() throws Exception {
        String sql = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
        Connection connection = dataBaseTestConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDouble(1, 1.5);
        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        ps.setInt(3, 2);
        boolean bl = ps.execute();

        Ticket test = ticketDAO.getTicket("test");
        assertEquals(bl, ticketDAO.updateTicket(test));
    }

}
