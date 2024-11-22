package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.roadrunner.Pose2d;
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

        runningActions.add(pinpointDrive.actionBuilder(getInitialPose())
                .strafeTo(new Vector2d(-36,58))
                .strafeTo(new Vector2d(-36,-58))
                .strafeTo(new Vector2d(36,-58))
                .strafeTo(new Vector2d(36,58))
                .build());

    }

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(36,58, Math.toRadians(270));
//        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }
}
