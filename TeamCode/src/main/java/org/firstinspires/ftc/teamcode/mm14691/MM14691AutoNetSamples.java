package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample3;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.neutralSampleToBasket;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.startToBasket;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


/**
 * See https://docs.google.com/document/d/1D9uxCZty4LIeQDVSoOdOJbbBigU5Q8DqBo9M7vusQDY/edit?tab=t.0
 */
@Config
@Autonomous
public class MM14691AutoNetSamples extends MM14691BaseAuto {
    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }

    @Override
    public void start() {
        super.init();

        // Create out trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(mecanumDrive.actionBuilder(getInitialPose()));
        TrajectoryActionBuilder basketToNSample1 = basketToNSample1(startToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder nSample1ToBasket = neutralSampleToBasket(basketToNSample1.endTrajectory().fresh());
        TrajectoryActionBuilder basketToNSample2 = basketToNSample2(nSample1ToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder nSample2ToBasket = neutralSampleToBasket(basketToNSample2.endTrajectory().fresh());
        TrajectoryActionBuilder basketToNSample3 = basketToNSample3(nSample2ToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder nSample3ToBasket = neutralSampleToBasket(basketToNSample3.endTrajectory().fresh());
        TrajectoryActionBuilder basketToPark = basketToPark(nSample3ToBasket.endTrajectory().fresh());

        runningActions.add(
                new SequentialAction(
                        // Start position (heading and location),  load sample (yellow)
                        // Drive to basket and Raise viper arm
                        autoActionName("Start to Basket"),
                        new ParallelAction(
                                startToBasket.build(),
                                liftDrive.toPosition(2800),
                                viperDrive.toEnd()
                        ),

                        // Deposit yellow sample
                        autoActionName("Deposit Sample"),
                        wristDrive.toOuttake(),
                        intakeDrive.toOpen(),
                        wristDrive.toIntake(),

                        // Lower arm and Drive to yellow sample 1
                        autoActionName("Basket to Sample 1"),
                        new ParallelAction(
                                basketToNSample1.build(),
                                liftDrive.toStart(),
                                viperDrive.toStart()),

                        // TODO - Pick up the yellow sample 1
                        // Drive to basket and Raise viper arm
                        autoActionName("Sample 1 to Basket"),
                        new ParallelAction(
                                nSample1ToBasket.build(),
                                liftDrive.toPosition(liftDrive.getEndTick() / 2),
                                viperDrive.toPosition(viperDrive.getEndTick() / 4)
                        ),

                        // Deposit yellow sample
                        autoActionName("Deposit Sample"),
                        wristDrive.toOuttake(),
                        intakeDrive.toOpen(),
                        wristDrive.toIntake(),

                        // Lower arm and Drive to yellow sample 2
                        autoActionName("Basket to Sample 2"),
                        new ParallelAction(
                                basketToNSample2.build(),
                                liftDrive.toStart(),
                                viperDrive.toStart()),

                        // TODO - Pick yellow sample 2
                        // Drive to basket and Raise viper arm
                        autoActionName("Sample 2 to Basket"),
                        new ParallelAction(
                                nSample2ToBasket.build(),
                                liftDrive.toPosition(liftDrive.getEndTick() / 2),
                                viperDrive.toPosition(viperDrive.getEndTick() / 4)
                        ),
                                viperDrive.toEnd(0.8)
////                        ),
//
//                        // Deposit yellow sample
//                        autoActionName("Deposit Sample"),
//                        wristDrive.toOuttake(),
//                        intakeDrive.toOpen(),
//                        wristDrive.toIntake(),
//
//                        // Lower arm and Drive to yellow sample 1
//                        autoActionName("Basket to Sample 1"),
//                        new ParallelAction(
//                                basketToNSample1.build(),
//                                liftDrive.toStart(),
//                                viperDrive.toStart())
//                        //,

                        // Drop yellow sample 2
                        autoActionName("Deposit Sample"),
                        wristDrive.toOuttake(),
                        intakeDrive.toOpen(),
                        wristDrive.toIntake(),

                        // Lower arm and Drive to yellow sample 3
                        autoActionName("Basket to Sample 3"),
                        new ParallelAction(
                                basketToNSample3.build(),
                                liftDrive.toStart(),
                                viperDrive.toStart()),

                        // TODO - Pick the yellow sample 3--we plan to push the sample 3 to netzone
                        // Drive to basket and Raise viper arm
                        autoActionName("Sample 3 to Net Zone"),
                        new ParallelAction(
                                nSample3ToBasket.build(),
                                liftDrive.toPosition(liftDrive.getEndTick() / 2),
                                viperDrive.toPosition(viperDrive.getEndTick() / 4)
                        ),

                        // TODO - Drop yellow sample 3----we plan to push the sample 3 to netzone
                        // Drive to submersion location and Raise Arm
                        autoActionName("Basket to Park"),
                        new ParallelAction(
                                basketToPark.build(),
                                liftDrive.toPosition(liftDrive.getEndTick() - 200),  //FIXME - needs tuning
                                viperDrive.toPosition(200))  //FIXME - needs tuning

                        // TODO - Lower Arm touch the low rung

                )
        );
    }


    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = -33;
        public int positionY = -62;
        public int heading = 90;
    }
}