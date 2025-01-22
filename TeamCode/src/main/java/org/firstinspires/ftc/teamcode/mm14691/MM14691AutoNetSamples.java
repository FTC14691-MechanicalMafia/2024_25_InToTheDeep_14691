package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories.startToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample3;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.neutralSampleToBasket;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.startToBasket;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories;


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
        super.start();

        // Create out trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(mecanumDrive.actionBuilder(getInitialPose()));
        TrajectoryActionBuilder basketToNSample1 = basketToNSample1(startToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder nSample1ToBasket = neutralSampleToBasket(basketToNSample1.endTrajectory().fresh());
        TrajectoryActionBuilder basketToNSample2 = basketToNSample2(nSample1ToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder nSample2ToBasket = neutralSampleToBasket(basketToNSample2.endTrajectory().fresh());
        TrajectoryActionBuilder basketToNSample3 = basketToNSample3(nSample2ToBasket.endTrajectory().fresh());
        // FYI - not enough time to go for the 3rd sample
//        TrajectoryActionBuilder nSample3ToBasket = neutralSampleToBasket(basketToNSample3.endTrajectory().fresh());
//        TrajectoryActionBuilder basketToPark = basketToPark(nSample3ToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder startToPark = startToPark(nSample2ToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder finalPark = NetParkTrajectories.finalPark(startToPark.endTrajectory().fresh());

        runningActions.add(
                new SequentialAction(
                        // Start position (heading and location),  load sample (yellow)
                        // Drive to basket and Raise viper arm
                        autoActionName("Start to Basket"),
                        intakeDrive.toClosed(), //grip the preload
                        startToBasket.build(),

                        // Deposit yellow sample
                        autoActionName("Deposit Sample"),
                        createDropSampleAction(),

                        // Lower arm and Drive to yellow sample 1
                        autoActionName("Basket to Sample 1"),
                        basketToNSample1.build(),

                        grabSampleAction(),

                        // Drive to basket and Raise viper arm
                        autoActionName("Sample 1 to Basket"),
                        nSample1ToBasket.build(),

                        // Deposit yellow sample
                        autoActionName("Deposit Sample"),
                        createDropSampleAction(),

                        // Lower arm and Drive to yellow sample 2
                        autoActionName("Basket to Sample 2"),
                        basketToNSample2.build(),

                        grabSampleAction(),

                        // Drive to basket and Raise viper arm
                        autoActionName("Sample 2 to Basket"),
                        nSample2ToBasket.build(),

                        // Drop yellow sample 2
                        autoActionName("Deposit Sample"),
                        createDropSampleAction(),

                        // Drive to submersion location and Raise Arm
                        autoActionName("Parking"),
                        startToPark.build(),
                        liftDrive.toPosition(1700, 0.8),
                        viperDrive.toPosition(1600,0.8),
                        wristDrive.toIntake(),
                        finalPark.build(),
                        liftDrive.toPosition(1250, 0.5)

                )
        );
    }

    @NonNull
    private SequentialAction createDropSampleAction() {
//        int basketBaseTicks = 2110; //ticks to drop the sample
        int basketBaseTicks = 1700; //ticks to drop the sample
        return new SequentialAction(
                liftDrive.toPosition(basketBaseTicks // + 40
                        , 1), //raise past where we need
                wristDrive.toIntake(),
                viperDrive.toPosition(2400, 1), //extend the viper arm
//                viperDrive.toPosition(3000, 0.9), //extend the viper arm
//                liftDrive.toPosition(basketBaseTicks, 1), //move a tad closer to the basket
                intakeDrive.toOpen(),
                liftDrive.toPosition(basketBaseTicks + 70, 1),

                // wristDrive.toIntake(),
                viperDrive.toStart(0.8),
                liftDrive.toPosition(300,1) //so the claw doesn't drag on the ground
        );
    }

    private SequentialAction grabSampleAction() {
        return new SequentialAction(
                viperDrive.toPosition(650),
                intakeDrive.toClosed(),
                viperDrive.toPosition(300)
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