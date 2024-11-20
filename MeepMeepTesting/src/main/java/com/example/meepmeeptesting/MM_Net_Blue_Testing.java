package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class MM_Net_Blue_Testing {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(865);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(36, 58, Math.toRadians(270)))
                .setReversed(false)  // Unreversed trajectory has hooks on the start and end
                .splineTo(new Vector2d(50.0, 50.0), Math.PI/4)
                .waitSeconds(1.5)
                .splineTo(new Vector2d(23, 24.0), Math.PI)
                .turn(-Math.PI )
                .waitSeconds(1.5)
                .splineTo(new Vector2d(50.0, 50.0), Math.PI/4)
                .waitSeconds(1.5)
                .splineTo(new Vector2d(39, 24.0), Math.PI)
                .turn(-Math.PI )
                .waitSeconds(1.5)
                .splineTo(new Vector2d(50.0, 50.0), Math.PI/4)
                .waitSeconds(1.5)
                .splineTo(new Vector2d(52.0, 24.0), Math.PI/4)
                .turn(-Math.PI/4)
                .waitSeconds(1.5)
                .splineTo(new Vector2d(50.0, 50.0), Math.PI/4)
                .waitSeconds(1.5)
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
