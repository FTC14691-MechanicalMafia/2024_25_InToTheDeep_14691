package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories.startToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample3;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.neutralSampleToBasket;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.startToBasket;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories;

public abstract class MM14691AutoNetSamplesBase extends MM14691BaseAuto {
    @Override
    public abstract Pose2d getInitialPose();

    @Override
    public void start() {
        super.start();

        // Create out trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(mecanumDrive.actionBuilder(getInitialPose()), getNetLocation());
        TrajectoryActionBuilder basketToNSample1 = basketToNSample1(startToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder nSample1ToBasket = neutralSampleToBasket(basketToNSample1.endTrajectory().fresh(), getNetLocation());
        TrajectoryActionBuilder basketToNSample2 = basketToNSample2(nSample1ToBasket.endTrajectory().fresh());
        TrajectoryActionBuilder nSample2ToBasket = neutralSampleToBasket(basketToNSample2.endTrajectory().fresh(), getNetLocation());
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
                        viperDrive.toPosition(1600, 0.8),
                        wristDrive.toIntake(),
                        finalPark.build(),
                        liftDrive.toPosition(1250, 0.5)

                )
        );
    }

    protected abstract Vector2d getNetLocation();

    @NonNull
    protected abstract SequentialAction createDropSampleAction();

    private SequentialAction grabSampleAction() {
        return new SequentialAction(
                viperDrive.toPosition(650),
                intakeDrive.toClosed(),
                viperDrive.toPosition(300, 0.6)
        );
    }
}
