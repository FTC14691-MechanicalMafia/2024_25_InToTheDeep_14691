package org.firstinspires.ftc.teamcode.mm14691.trajectory;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class ObsSamplesPushTrajectories {

    public static void main(String[] args) {
        // Create our MeepMeep instance
        MeepMeep meepMeep = new MeepMeep(865);

        // Create our virtual bot
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.PI, Math.PI, 14.3541)
                .build();

        // Create out trajectories
        TrajectoryActionBuilder startToSample1 = startToSample1(myBot.getDrive().actionBuilder(
                new Pose2d(23, -62, Math.toRadians(90))));
        TrajectoryActionBuilder sample1ToZone = sample1ToZone(startToSample1.endTrajectory());
        TrajectoryActionBuilder zoneToSample2 = zoneToSample2(sample1ToZone.endTrajectory());
        TrajectoryActionBuilder sample2ToZone = sample1ToZone(zoneToSample2.endTrajectory());
        TrajectoryActionBuilder zoneToSample3 = zoneToSample3(sample2ToZone.endTrajectory());
        TrajectoryActionBuilder sample3ToZone = sample3ToZone(zoneToSample3.endTrajectory());

        // Run the trajectories
        myBot.runAction(startToSample1.build());
        myBot.runAction(sample1ToZone.build());
        myBot.runAction(zoneToSample2.build());
        myBot.runAction(sample2ToZone.build());
        myBot.runAction(zoneToSample3.build());
        myBot.runAction(sample3ToZone.build());

        // Configure MeepMeep and start it
        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

    public static TrajectoryActionBuilder startToSample1(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(45.0, -7.0), Math.toRadians(50));
    }

    public static TrajectoryActionBuilder sample1ToZone(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(48.0, -55.0), Math.toRadians(0));
    }

    public static TrajectoryActionBuilder zoneToSample2(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(new Vector2d(59.0, -10.0), Math.toRadians(0)), Math.toRadians(30));
    }

    public static TrajectoryActionBuilder zoneToSample3(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(new Vector2d(62, -10.0), Math.toRadians(0)), Math.toRadians(30));
    }

    public static TrajectoryActionBuilder sample3ToZone(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(false)
                .strafeTo(new Vector2d(62.0, -52.0));
    }


}
