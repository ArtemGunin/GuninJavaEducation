package com.nix11.robot;

import com.nix11.Detail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

public class RobotBasicPartDesigner implements Callable<Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotBasicPartDesigner.class);
    protected static final Random RANDOM = new Random();

    Detail detail;
    String name;

    public RobotBasicPartDesigner(Detail detail, String name) {
        this.detail = detail;
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {
        LOGGER.info(name + " begins to assemble the basic design of the part.");
        while (detail.getBasicPartDesign() < 100) {
            for (int i = 0; i < RANDOM.nextInt(10, 21); i++) {
                detail.setBasicPartDesign(detail.getBasicPartDesign() + 1);;
            }
            LOGGER.info(name + " up the basic design of the part to " + detail.getBasicPartDesign() + " points.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LOGGER.info(e.getMessage());
            }
        }
        return detail.getBasicPartDesign();
    }
}
