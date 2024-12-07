package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
public class MM_Observation_Red_Testing {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(865);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(20, -58, 90))
                .strafeToLinearHeading(new Vector2d(9.0, -40.0), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(32.0, -24.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(47.0, -2.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(54.0, -13.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(54.0, -60.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(54.0, -10.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(61.0, -10.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(61.0, -57.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(53.0, -54.0), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(6.0, -40.0), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(53.0, -54.0), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(3.0, -40.0), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(53.0, -54.0), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(0.0, -40.0), Math.toRadians(90))
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
