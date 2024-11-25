package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.RedObservationTrajectories.allianceSampleToBasket;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.RedObservationTrajectories.startToBasket;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.RedObservationTrajectories.toAllianceSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.RedObservationTrajectories.toAllianceSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.RedObservationTrajectories.toPark;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous
public class MM14691AutoRedObservation extends MM14691BaseOpMode {
    // Create an instance of our params class so the FTC dash can manipulate it.
    public static MM14691AutoRedObservation.Params PARAMS = new MM14691AutoRedObservation.Params();
    protected Pose2d initialPose2 = new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));

    @Override
    public Pose2d getInitialPose() {
        return initialPose2;
    }

    @Override
    public void loop() {
        // Create our list of Trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(pinpointDrive.actionBuilder(getInitialPose()));
        TrajectoryActionBuilder basketToASample1 = toAllianceSample1(startToBasket.endTrajectory());
        TrajectoryActionBuilder aSample1ToBasket = allianceSampleToBasket(basketToASample1.endTrajectory());
        TrajectoryActionBuilder basketToASample2 = toAllianceSample2(aSample1ToBasket.endTrajectory());
        TrajectoryActionBuilder aSample2ToBasket = allianceSampleToBasket(basketToASample2.endTrajectory());
        TrajectoryActionBuilder toPark = toPark(aSample2ToBasket.endTrajectory());

        // Run the trajectories
        Actions.runBlocking(
                new SequentialAction(
                        // Go to the basket
                        startToBasket.build(),

                        // Place the sample
                        liftDrive.toEnd(),
                        viperDrive.toEnd(),
                        // TODO eject the intake
                        viperDrive.toStart(),
                        liftDrive.toDown(),

                        // Go fetch the first sample
                        basketToASample1.build(),
                        liftDrive.toPosition(300),
                        viperDrive.toPosition(300),
                        // TODO intake
                        viperDrive.toStart(),
                        liftDrive.toDown(),

                        // Back to the basket
                        aSample1ToBasket.build(),

                        // Place the sample
                        liftDrive.toEnd(),
                        viperDrive.toEnd(),
                        // TODO eject the intake
                        viperDrive.toStart(),
                        liftDrive.toDown(),

                        // Go fetch the second sample
                        basketToASample2.build(),
                        liftDrive.toPosition(300),
                        viperDrive.toPosition(300),
                        // TODO intake
                        viperDrive.toStart(),
                        liftDrive.toDown(),

                        // Back to the basket
                        aSample2ToBasket.build(),

                        // Place the sample
                        liftDrive.toEnd(),
                        viperDrive.toEnd(),
                        // TODO eject the intake
                        viperDrive.toEnd(),
                        liftDrive.toDown(),

                        // Go to the parking area
                        toPark.build()
                )
        );
    }

    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = 20;
        public int positionY = -58;
        public int heading = 90;
    }

}