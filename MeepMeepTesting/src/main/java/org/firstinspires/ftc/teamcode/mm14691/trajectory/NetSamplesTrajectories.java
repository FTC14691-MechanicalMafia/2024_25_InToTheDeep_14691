package org.firstinspires.ftc.teamcode.mm14691.trajectory;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class    NetSamplesTrajectories {

    public static void main(String[] args) {
        // Create our MeepMeep instance
        MeepMeep meepMeep = new MeepMeep(865);

        // Create our virtual bot
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.PI, Math.PI, 14.3541)
                .build();

        // Create out trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(myBot.getDrive().actionBuilder(
                new Pose2d(-36, -58, Math.toRadians(180))));
        TrajectoryActionBuilder basketToNSample1 = basketToNSample1(startToBasket.endTrajectory());
        TrajectoryActionBuilder nSample1ToBasket = neutralSampleToBasket(basketToNSample1.endTrajectory());
        TrajectoryActionBuilder basketToNSample2 = basketToNSample2(nSample1ToBasket.endTrajectory());
        TrajectoryActionBuilder nSample2ToBasket = neutralSampleToBasket(basketToNSample2.endTrajectory());
        TrajectoryActionBuilder basketToNSample3 = basketToNSample3(nSample2ToBasket.endTrajectory());
        TrajectoryActionBuilder nSample3ToBasket = neutralSampleToBasket(basketToNSample3.endTrajectory());
        //TODO - park the robot

        // Run the trajectories
        myBot.runAction(startToBasket.build());
        myBot.runAction(basketToNSample1.build());
        myBot.runAction(nSample1ToBasket.build());
        myBot.runAction(basketToNSample2.build());
        myBot.runAction(nSample2ToBasket.build());
        myBot.runAction(basketToNSample3.build());
        myBot.runAction(nSample3ToBasket.build());

        // Configure MeepMeep and start it
        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    public static TrajectoryActionBuilder startToBasket(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(false)  // Unreversed trajectory has hooks on the start and end
                .strafeToLinearHeading(new Vector2d(-50.0, -50.0), Math.toRadians(225));
    }

    public static TrajectoryActionBuilder basketToNSample1(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(new Vector2d(-37.25, -24), Math.toRadians(180)), Math.toRadians(120));
    }

    public static TrajectoryActionBuilder neutralSampleToBasket(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(false)
                .strafeToLinearHeading(new Vector2d(-50.0, -50.0), Math.toRadians(225));
    }

    public static TrajectoryActionBuilder basketToNSample2(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .strafeToLinearHeading(new Vector2d(-44, -24.0), Math.toRadians(180));
    }

    public static TrajectoryActionBuilder basketToNSample3(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .strafeToLinearHeading(new Vector2d(-52, -24.0), Math.toRadians(180));
    }
}
