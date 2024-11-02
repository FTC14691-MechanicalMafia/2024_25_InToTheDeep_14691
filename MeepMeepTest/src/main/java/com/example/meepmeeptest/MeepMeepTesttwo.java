package com.example.meepmeeptest;



import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.SampleMecanumDrive;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        MeepMeep meepMeep = new MeepMeep(865);
        Vector2d myVector = new Vector2d(10, -5);
        Pose2d myPose = new Pose2d(10, -5, Math.toRadians(90));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(100, 100, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-64.5, -64.5, 0))

                        .forward(114)
                        .turn(Math.toRadians(90))
                        .strafeRight(10)
                        .forward(5)
                        .forward(114)
                        .turn(Math.toRadians(90))
                        .forward(114)


                        .turn(Math.toRadians(90))
                        .forward(114)
                        .turn(Math.toRadians(90))
                        .build());


    }
}