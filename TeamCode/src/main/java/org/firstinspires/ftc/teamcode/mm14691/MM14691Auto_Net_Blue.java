package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous
public class MM14691Auto_Net_Blue extends MM14691BaseOpMode {
    // Create an instance of our params class so the FTC dash can manipulate it.
    public static MM14691Auto_Net_Blue.Params PARAMS = new MM14691Auto_Net_Blue.Params();
    protected Pose2d initialPose4 = new Pose2d(PARAMS.positionX, PARAMS.positionY, PARAMS.heading);

    @Override
    public Pose2d getInitialPose() {
        return initialPose4;
    }

    @Override
    public void loop() {

        Actions.runBlocking(pinpointDrive.actionBuilder(getInitialPose())
                .lineToX(47)
                .turn(Math.toRadians(90))
                .lineToY(46)
                .turn(Math.toRadians(90))
                .lineToX(-47)
                .turn(Math.toRadians(90))
                .lineToY(-46)
                .turn(Math.toRadians(90))
                .build());
    }

    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = 36;
        public int positionY = 65;
        public int heading = 270;
    }
}