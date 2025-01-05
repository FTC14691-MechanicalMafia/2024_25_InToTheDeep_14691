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



        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(15, -62, Math.toRadians(90)))
                // start w preload to top rung
                .strafeToLinearHeading(new Vector2d(9.0, -40.0), Math.toRadians(90))
                // top rung to sample 1
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(47.0, -2.0, Math.toRadians(180)), Math.toRadians(70))
                                .setReversed(false)
                // sample 1 to obs
//                .strafeToLinearHeading(new Vector2d(54.0, -13.0), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(47.0, -50.0), Math.toRadians(180))
                // obs to sample 2
                .splineToConstantHeading(new Vector2d(55.0, -10.0), -Math.toRadians(45))
                // sample 2 to obs
                .strafeToConstantHeading(new Vector2d(55.0, -57.0))

                // to specimen
                .splineToLinearHeading(new Pose2d(50.5, -57.0 + 4.5, Math.toRadians(225)), Math.PI/2)
                .splineToLinearHeading(new Pose2d(46, -57.0, Math.toRadians(270)), Math.PI)
                // TODO - grab the specimen

                // obs to rung
                .strafeToLinearHeading(new Vector2d(6.0, -40.0), Math.toRadians(90))
                // TODO - hang the specimen

                // rung to obs/spec
                .strafeToLinearHeading(new Vector2d(46, -57.0), Math.toRadians(270))
                // TODO - grab the specimen

                // obs to rung
                .strafeToLinearHeading(new Vector2d(3.0, -40.0), Math.toRadians(90))
                // TODO - hang the specimen
                // rung to obs/parm
                .strafeToLinearHeading(new Vector2d(46, -57.0), Math.toRadians(270))
                // TODO - grab the specimen

                .build());


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

}
