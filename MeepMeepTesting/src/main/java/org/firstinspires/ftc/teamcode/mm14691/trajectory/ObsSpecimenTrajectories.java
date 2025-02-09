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

//        TrajectoryActionBuilder startToBar = startToBar(myBot.getDrive().actionBuilder(
//                new Pose2d(20, -58, Math.toRadians(90))));

        TrajectoryActionBuilder startToBar = startToBar(myBot.getDrive().actionBuilder(
                new Pose2d(18, -58, Math.toRadians(90))));
        TrajectoryActionBuilder barToSample1 = barToSample1(startToBar.endTrajectory());
        TrajectoryActionBuilder sample1ToObservation = sample1ToObservation(barToSample1.endTrajectory());
        TrajectoryActionBuilder observationToSample2 = observationToSample2(sample1ToObservation.endTrajectory());
        TrajectoryActionBuilder sample2ToObservation = sample2ToObservation(observationToSample2.endTrajectory());
        TrajectoryActionBuilder observationToSpecimen = observationToSpecimen(sample2ToObservation.endTrajectory());
        TrajectoryActionBuilder specimenToBar = specimenToBar(observationToSpecimen.endTrajectory());
        TrajectoryActionBuilder barToSpecimen = barToSpecimen(specimenToBar.endTrajectory());
        TrajectoryActionBuilder observationToBar = observationToBar(barToSpecimen.endTrajectory());
        TrajectoryActionBuilder barToPark = barToPark(observationToBar.endTrajectory());



        // Run the trajectories
        myBot.runAction(startToBar.build());
        //extend the arm to hang the specimen
        myBot.runAction(barToSample1.build());
        //unextend the arm to avoid hitting the pole
        myBot.runAction(sample1ToObservation.build());
        myBot.runAction(observationToSample2.build());
        myBot.runAction(sample2ToObservation.build());
        myBot.runAction(observationToSpecimen.build());
        //lower the arm while extending to pick up the specimen
        myBot.runAction(specimenToBar.build());
        //extend the arm to hang the specimen
        myBot.runAction(barToSpecimen.build());
        //lower the arm immedietly
        //extend the arm to pick up the specimen
        myBot.runAction(observationToBar.build());
        //extend the arm to hang the specimen
        myBot.runAction(barToPark.build());
        //unextend completely

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

    public static TrajectoryActionBuilder barToSample1(TrajectoryActionBuilder builder) {
        return builder
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(47.0, -2.0, Math.toRadians(180)), Math.toRadians(70));
//                .setReversed(false);
    }

    public static TrajectoryActionBuilder sample1ToObservation(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(47.0, -50.0), Math.toRadians(180));

    }

    public static TrajectoryActionBuilder observationToSample2(TrajectoryActionBuilder builder) {
        return builder
                .splineToConstantHeading(new Vector2d(55.0, -10.0), -Math.toRadians(45));

    }

    public static TrajectoryActionBuilder sample2ToObservation(TrajectoryActionBuilder builder) {
        return builder
                .strafeToConstantHeading(new Vector2d(55.0, -57.0));

    }

    public static TrajectoryActionBuilder observationToSpecimen(TrajectoryActionBuilder builder) {
        return builder
                .splineToLinearHeading(new Pose2d(50.5, -57.0 + 4.5, Math.toRadians(225)), Math.PI / 2)
                .splineToLinearHeading(new Pose2d(46, -57.0, Math.toRadians(270)), Math.PI);
    }

    public static TrajectoryActionBuilder specimenToBar(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(6.0, -40.0), Math.toRadians(90));
    }

    public static TrajectoryActionBuilder barToSpecimen(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(46, -57.0), Math.toRadians(270));
    }

    public static TrajectoryActionBuilder observationToBar(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(3.0, -40.0), Math.toRadians(90));
    }

    public static TrajectoryActionBuilder barToPark(TrajectoryActionBuilder builder) {
        return builder
                .strafeToLinearHeading(new Vector2d(46, -57.0), Math.toRadians(270));
    }
}