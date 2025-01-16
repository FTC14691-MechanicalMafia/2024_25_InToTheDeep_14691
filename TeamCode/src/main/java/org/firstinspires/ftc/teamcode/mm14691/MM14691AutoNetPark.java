package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories.startToPark;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories;

/**
 * Parks the robot from the net side
 */
@Autonomous
@Config
public class MM14691AutoNetPark extends MM14691BaseAuto {

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }

    @Override
    public void start() {
        super.start();
        TrajectoryActionBuilder startToPark = startToPark(
                mecanumDrive.actionBuilder(getInitialPose()));
        TrajectoryActionBuilder finalPark = NetParkTrajectories.finalPark(startToPark.endTrajectory().fresh());


        runningActions.add(
                new SequentialAction(
                        autoActionName("Parking"),
                        startToPark.build(),
                        liftDrive.toPosition(1580, 0.5),
                        viperDrive.toPosition(1600,0.5),
                        wristDrive.toIntake(),
                        finalPark.build()
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

