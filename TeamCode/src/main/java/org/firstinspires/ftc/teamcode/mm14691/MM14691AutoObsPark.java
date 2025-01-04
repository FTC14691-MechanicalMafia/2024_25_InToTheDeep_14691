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

        runningActions.add(
                new SequentialAction(
                        autoActionName("Parking"),
                        startToPark.build()
                )
        );

    }

    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = 18;
        public int positionY = -58;
        public int heading = 90;
    }

}