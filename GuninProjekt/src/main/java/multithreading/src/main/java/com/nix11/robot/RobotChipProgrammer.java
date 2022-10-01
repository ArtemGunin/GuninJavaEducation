package com.nix11.robot;

import com.nix11.Detail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

public class RobotChipProgrammer implements Callable<Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotChipProgrammer.class);
    protected static final Random RANDOM = new Random();

    Detail detail;
    String name;

    public RobotChipProgrammer(Detail detail, String name) {
        this.detail = detail;
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {
        LOGGER.info(name + " begins to programming chip of detail.");
        while (detail.getChipProgramming() < 100) {
            for (int i = 0; i < RANDOM.nextInt(25, 36); i++) {
                detail.setChipProgramming(detail.getChipProgramming() + 1);
            }
            if (RANDOM.nextInt(100) < 30) {
                detail.setChipProgramming(0);
                LOGGER.info(name + " broke chip, programming process restarted.");
            }
            LOGGER.info(name + " processed chip programming are " + detail.getChipProgramming() + " points.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.info(e.getMessage());
            }
        }
        return detail.getChipProgramming();
    }
}
