package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger("App");
    public static void main(String[] args){
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
       /* Properties configFile = new Properties();
        try {
            configFile.load(DataBaseConfig.class.getClassLoader().getResourceAsStream("myuup.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = configFile.getProperty("url");
        System.out.println("url = " + url);*/

    }
}
