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
                .splineTo(new Vector2d(54.0, 54.0), Math.PI/4)
                .waitSeconds(3)
                .splineTo(new Vector2d(43.0, 35.0), -Math.PI / 2)
                .waitSeconds(2)
                .setReversed(false)
                .splineTo(new Vector2d(54.0, 54.0), Math.PI/4)
                .waitSeconds(2)
                .setReversed(false)
                .splineTo(new Vector2d(57.0, 36.0), -Math.PI/2)
                .waitSeconds(2)
                .setReversed(false)
                .splineTo(new Vector2d(54, 54), Math.PI/4)
                .waitSeconds(2)
                .setReversed(false)
                //.splineTo(new Vector2d(65,36 ), -Math.PI/2)TODO: We need to aproach the last specimen from the side to avoid hitting the wall
                //.waitSeconds(3)
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
