package org.firstinspires.ftc.teamcode.mm14691;

import static com.acmerobotics.roadrunner.ftc.Actions.runBlocking;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.PinpointDrive;

import java.util.ArrayList;
import java.util.List;

public class MM14691Auto extends MM14691BaseOpMode {

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
