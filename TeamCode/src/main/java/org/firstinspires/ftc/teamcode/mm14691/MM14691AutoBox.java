package org.firstinspires.ftc.teamcode.mm14691;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.acmerobotics.roadrunner.ftc.Actions;

@Autonomous
public class MM14691AutoBox extends MM14691BaseOpMode {

    @Override
    public void loop() {

        Actions.runBlocking(pinpointDrive.actionBuilder(initialPose)
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
}
