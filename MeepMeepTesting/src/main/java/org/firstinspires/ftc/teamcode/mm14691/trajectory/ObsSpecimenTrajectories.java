package org.firstinspires.ftc.teamcode.mm14691.trajectory;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ObsSpecimenTrajectories {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(865);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        TrajectoryActionBuilder startToBar = startToBar(myBot.getDrive().actionBuilder(
                new Pose2d(20, -58, Math.toRadians(90))));
        TrajectoryActionBuilder barToSample1_2 = barToSample1_2(startToBar.endTrajectory().fresh());

        myBot.runAction(startToBar.build());
        myBot.runAction(barToSample1_2.build());
        myBot.runAction(nSample1ToBasket.build());
        myBot.runAction(basketToNSample2.build());
        myBot.runAction(nSample2ToBasket.build());
        myBot.runAction(basketToNSample3.build());
        myBot.runAction(basketToPark.build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

    }

    public static TrajectoryActionBuilder startToBar(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(false)
                .strafeToLinearHeading(new Vector2d(9.0, -40.0), Math.toRadians(90));
    }

    public static TrajectoryActionBuilder barToSample1_2(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .strafeToLinearHeading(new Vector2d(32.0, -24.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(47.0, -2.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(54.0, -13.0), Math.toRadians(180));
    }

    public static TrajectoryActionBuilder Sample1_2toObservationZone(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(false)
                .strafeToLinearHeading(new Vector2d(54.0, -60.0), Math.toRadians(180));
    }

    public static TrajectoryActionBuilder ObservationZonetoSample3(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .strafeToLinearHeading(new Vector2d(-58.0, -40.0), Math.toRadians(90))
                .waitSeconds(0.5);
    }

    public static TrajectoryActionBuilder basketToNSample3(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(54.0, -10.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(61.0, -10.0), Math.toRadians(180));
    }

    public static TrajectoryActionBuilder Sample3toObservationZone(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(-40,-10), Math.toRadians(0))
                .strafeToLinearHeading(new Vector2d(61.0, -57.0), Math.toRadians(180));
    }
    public static TrajectoryActionBuilder SpecimenPickUp(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(53.0, -54.0), Math.toRadians(270));
    }
    public static TrajectoryActionBuilder Specimen2ToBar(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(6.0, -40.0), Math.toRadians(90));
    }
    public static TrajectoryActionBuilder Specimen3ToBar(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(3.0, -40.0), Math.toRadians(90));
    }
    public static TrajectoryActionBuilder BartoPark(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(6.0, -40.0), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(61.0, -10.0), Math.toRadians(90));
    }
}