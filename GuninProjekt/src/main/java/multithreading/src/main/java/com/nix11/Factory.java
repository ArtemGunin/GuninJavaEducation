package com.nix11;

import com.nix11.robot.RobotBasicPartDesigner;
import com.nix11.robot.RobotChipProgrammer;
import com.nix11.robot.RobotFuelAdder;
import com.nix11.robot.RobotPartFormer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Factory {

    public void runFactory() {
        Detail detail = new Detail();
        Fuel fuel = new Fuel();
        FutureTask<Integer> robotOne = new FutureTask<>(new RobotFuelAdder(detail, fuel, "Robot one"));
        FutureTask<Integer> robotTwo = new FutureTask<>(new RobotBasicPartDesigner(detail, "Robot two"));
        FutureTask<Integer> robotThree = new FutureTask<>(new RobotBasicPartDesigner(detail, "Robot three"));
        FutureTask<Integer> robotFour = new FutureTask<>(new RobotChipProgrammer(detail, "Robot four"));
        FutureTask<Integer> robotFive = new FutureTask<>(new RobotPartFormer(detail, fuel, "Robot five"));

        ExecutorService executorService = Executors.newScheduledThreadPool(3);

        executorService.execute(robotOne);
        executorService.execute(robotTwo);
        executorService.execute(robotThree);
        while (!robotFive.isDone()) {
            if (robotTwo.isDone() && robotThree.isDone()) {
                executorService.execute(robotFour);
            }
            if (robotFour.isDone()) {
                executorService.execute(robotFive);
            }
        }
        executorService.shutdown();
    }
}
