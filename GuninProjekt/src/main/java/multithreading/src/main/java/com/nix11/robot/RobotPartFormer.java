package com.nix11.robot;

import com.nix11.Detail;
import com.nix11.Fuel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

public class RobotPartFormer implements Callable<Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotPartFormer.class);
    protected static final Random RANDOM = new Random();

    Detail detail;
    Fuel fuel;
    String name;

    public RobotPartFormer(Detail detail, Fuel fuel, String name) {
        this.detail = detail;
        this.fuel = fuel;
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {
        LOGGER.info(name + " begins formed detail.");
        while (detail.getPartFormation() < 100) {
            int fuelConsumption = RANDOM.nextInt(350, 701);
            if (fuel.getFuel() > fuelConsumption) {
                for (int i = 0; i < 10; i++) {
                    detail.setPartFormation(detail.getPartFormation() + 1);
                }
                fuel.setFuel(fuel.getFuel() - fuelConsumption);
                LOGGER.info(name + " up the forming of the part to " + detail.getPartFormation() + " points.");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.info(e.getMessage());
            }
        }
        detail.setDetail(detail.getDetail() + 1);
        LOGGER.info(name + " create detail.");
        return detail.getPartFormation();
    }
}
