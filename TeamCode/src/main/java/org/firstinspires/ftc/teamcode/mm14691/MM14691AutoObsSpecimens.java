package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsParkTrajectories.startToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.barToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.barToSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.barToSpecimen;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.observationToBar;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.observationToSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.observationToSpecimen;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.sample1ToObservation;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.sample2ToObservation;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.specimenToBar;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.ObsSpecimenTrajectories.startToBar;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


/**
 * Parks the robot from the observation side
 */
@Autonomous
public class MM14691AutoObsSpecimens extends MM14691BaseAuto {

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }


    @Override
    public void start() {
        super.start();
        TrajectoryActionBuilder startToBar = startToBar(mecanumDrive.actionBuilder(
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

        runningActions.add(new SequentialAction(autoActionName("Parking"), startToBar.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), barToSample1.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), sample1ToObservation.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), observationToSample2.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), sample2ToObservation.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), observationToSpecimen.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), specimenToBar.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), barToSpecimen.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), observationToBar.build()));
        runningActions.add(new SequentialAction(autoActionName("Parking"), barToPark.build()));

    }

    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = 18;
        public int positionY = -58;
        public int heading = 90;
    }

}