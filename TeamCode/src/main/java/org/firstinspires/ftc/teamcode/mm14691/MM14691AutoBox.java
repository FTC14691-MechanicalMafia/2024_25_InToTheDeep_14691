package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.acmerobotics.roadrunner.ftc.Actions;

@Autonomous
public class MM14691AutoBox extends MM14691BaseAuto {

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static MM14691AutoBlueNet.Params PARAMS = new MM14691AutoBlueNet.Params();


    @Override
    public void start() {
        super.start();
        TrajectoryActionBuilder first = pinpointDrive.actionBuilder(getInitialPose())
                .strafeTo(new Vector2d(-36,58));
        TrajectoryActionBuilder second = first.endTrajectory()
                .strafeTo(new Vector2d(-36,-58));
        TrajectoryActionBuilder third = second.endTrajectory()
                .strafeTo(new Vector2d(36,-58));
        TrajectoryActionBuilder fourth = third.endTrajectory()
                .strafeTo(new Vector2d(36,58));


        runningActions.add(
                new SequentialAction(
                        autoActionName("first"),
                        first.build(),
                armDrive.liftToDown(),
                autoActionName("second"),
                second.build(),
                armDrive.liftToDown(),
                autoActionName("third"),
                third.build(),
                armDrive.liftToDown(),
                autoActionName("fourth"),
                fourth.build()
            )
        );

    }

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(36,58, Math.toRadians(270));
//        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }
}
