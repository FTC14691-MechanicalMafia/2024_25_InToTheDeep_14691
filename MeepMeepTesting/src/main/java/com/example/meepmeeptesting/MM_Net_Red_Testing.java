package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
public class MM_Net_Red_Testing {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(865);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-36, -58, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-50.0, -50.0), Math.toRadians(45))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(-48.0, -40.0), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(-50.0, -50.0), Math.toRadians(45))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(-58.0, -40.0), Math.toRadians(90))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(-50.0, -50.0), Math.toRadians(45))
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(-58.0, -40.0), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(-50.0, -10.0), Math.toRadians(0))
                .strafeToLinearHeading(new Vector2d(-61.0, -14.0), Math.toRadians(0))
                .strafeToLinearHeading(new Vector2d(-61.0, -47.0), Math.toRadians(0))
                .strafeToLinearHeading(new Vector2d(-61.0, 47.0), Math.toRadians(0))
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

