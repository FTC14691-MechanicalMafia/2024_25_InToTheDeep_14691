package org.firstinspires.ftc.teamcode.mm14691.trajectory;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class RedObservationTrajectories {

    public static void main(String args[]) {
        // Create our MeepMeep instance
        MeepMeep meepMeep = new MeepMeep(865);

        // Create our virtual bot
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints
                // NOTE: these are copied from the MecanumDrive in the TeamCode
                .setConstraints(50, 50, Math.PI, Math.PI, 14.3541)
                .build();


        // Create our list of Trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(myBot.getDrive().actionBuilder(
                new Pose2d(20, -58, Math.toRadians(90))));
        TrajectoryActionBuilder basketToASample1 = toAllianceSample1(startToBasket.endTrajectory());
        TrajectoryActionBuilder aSample1ToBasket = allianceSampleToBasket(basketToASample1.endTrajectory());
        TrajectoryActionBuilder basketToASample2 = toAllianceSample2(aSample1ToBasket.endTrajectory());
        TrajectoryActionBuilder aSample2ToBasket = allianceSampleToBasket(basketToASample2.endTrajectory());
//        TrajectoryActionBuilder basketToASample3 = toAllianceSample3(aSample2ToBasket.endTrajectory());
//        TrajectoryActionBuilder aSample3ToBasket = allianceSampleToBasket(basketToASample3.endTrajectory());
        TrajectoryActionBuilder toPark = toPark(aSample2ToBasket.endTrajectory());

        // Run the trajectories
        myBot.runAction(startToBasket.build());
        myBot.runAction(basketToASample1.build());
        myBot.runAction(aSample1ToBasket.build());
        myBot.runAction(basketToASample2.build());
        myBot.runAction(aSample2ToBasket.build());
//        myBot.runAction(basketToASample3.build());
//        myBot.runAction(aSample3ToBasket.build());
        myBot.runAction(toPark.build());

        // Configure MeepMeep and start it
        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    public static TrajectoryActionBuilder startToBasket(TrajectoryActionBuilder builder) {
        return builder
                .splineTo(new Vector2d(-52, -52), Math.toRadians(225));
    }

    public static TrajectoryActionBuilder toAllianceSample1(TrajectoryActionBuilder builder) {
        return builder
                .turnTo(0)
                .splineToConstantHeading(new Vector2d(33, -40), Math.toRadians(0))
                .strafeTo(new Vector2d(33, -25));
    }

    public static TrajectoryActionBuilder toAllianceSample2(TrajectoryActionBuilder builder) {
        return builder
                .turnTo(0)
                .splineToConstantHeading(new Vector2d(33, -40), Math.toRadians(0))
                .strafeTo(new Vector2d(45, -25));
    }

    public static TrajectoryActionBuilder toAllianceSample3(TrajectoryActionBuilder builder) {
        return builder
                .turnTo(0)
                .splineToConstantHeading(new Vector2d(33, -40), Math.toRadians(0))
                .strafeTo(new Vector2d(57, -25));
    }

    public static TrajectoryActionBuilder allianceSampleToBasket(TrajectoryActionBuilder builder) {
        return builder
                .splineTo(new Vector2d(33, -40), Math.toRadians(225))
                .splineTo(new Vector2d(-52, -52), Math.toRadians(225));
    }

    public static TrajectoryActionBuilder toPark(TrajectoryActionBuilder builder) {
        return builder
                .turnTo(270)
                .splineToConstantHeading(new Vector2d(55, -58), Math.toRadians(0));
    }
}
