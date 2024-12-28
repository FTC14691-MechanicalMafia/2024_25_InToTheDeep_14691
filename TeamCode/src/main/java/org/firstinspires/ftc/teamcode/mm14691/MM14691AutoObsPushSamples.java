package org.firstinspires.ftc.teamcode.mm14691;


import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesPushTrajectories.sample1ToZone;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesPushTrajectories.sample3ToZone;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesPushTrajectories.startToSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesPushTrajectories.zoneToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesPushTrajectories.zoneToSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesPushTrajectories.zoneToSample3;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


/**
 * See https://docs.google.com/document/d/1D9uxCZty4LIeQDVSoOdOJbbBigU5Q8DqBo9M7vusQDY/edit?tab=t.0
 */
@Config
@Autonomous
public class MM14691AutoObsPushSamples extends MM14691BaseAuto {
    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }

    @Override
    public void init() {
        super.init();

        // Create out trajectories
        TrajectoryActionBuilder startToSample1 = startToSample1(pinpointDrive.actionBuilder(getInitialPose()));
        TrajectoryActionBuilder sample1ToZone = sample1ToZone(startToSample1.endTrajectory());
        TrajectoryActionBuilder zoneToSample2 = zoneToSample2(sample1ToZone.endTrajectory());
        TrajectoryActionBuilder sample2ToZone = sample1ToZone(zoneToSample2.endTrajectory());
        TrajectoryActionBuilder zoneToSample3 = zoneToSample3(sample2ToZone.endTrajectory());
        TrajectoryActionBuilder sample3ToZone = sample3ToZone(zoneToSample3.endTrajectory());

//        Actions.runBlocking(
        runningActions.add(
                new SequentialAction(
                        // Start position (heading and location),  load sample (yellow)
                        // Drive to basket and Raise viper arm
                        autoActionName("Start to Basket"),
                        startToSample1.build(),
                        autoActionName("Sample 1 to Zone"),
                        sample1ToZone.build(),
                        autoActionName("Zone to Sample 2"),
                        zoneToSample2.build(),
                        autoActionName("Sample 2 to Zone"),
                        sample2ToZone.build(),
                        autoActionName("Zone to Sample 3"),
                        zoneToSample3.build(),
                        autoActionName("Sample 3 to Zone"),
                        sample3ToZone.build()
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