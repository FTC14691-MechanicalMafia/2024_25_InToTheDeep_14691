package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsParkTrajectories.startToPark;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


/**
 * Parks the robot from the observation side
 */
@Autonomous
public class MM14691AutoObsPark extends MM14691BaseAuto {
    @Override
    public void start() {
        super.start();
        TrajectoryActionBuilder startToPark = startToPark(
                pinpointDrive.actionBuilder(getInitialPose()));


        runningActions.add(
                new SequentialAction(
                        autoActionName("Parking"),
                        startToPark.build()
                )
        );

    }

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(18,-58, Math.toRadians(90));
    }
}