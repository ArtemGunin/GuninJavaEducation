package com.nix11.robot;

import com.nix11.Detail;
import com.nix11.Fuel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

public class RobotFuelAdder implements Callable<Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotFuelAdder.class);
    protected static final Random RANDOM = new Random();

    Detail detail;
    Fuel fuel;
    String name;

    public RobotFuelAdder(Detail detail, Fuel fuel, String name) {
        this.detail = detail;
        this.fuel = fuel;
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {
        while (detail.getDetail() == 0) {
            fuel.setFuel(fuel.getFuel() + RANDOM.nextInt(500, 1000));
            LOGGER.info(name + " produced fuel. In stock " + fuel.getFuel() + " gallons of fuel.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LOGGER.info(e.getMessage());
            }
        }
        return detail.getDetail();
    }
}
