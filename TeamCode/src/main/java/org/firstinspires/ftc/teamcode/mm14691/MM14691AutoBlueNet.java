package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.BlueNetTrajectories.basketToNSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.BlueNetTrajectories.basketToNSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.BlueNetTrajectories.basketToNSample3;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.BlueNetTrajectories.neutralSampleToBasket;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.BlueNetTrajectories.startToBasket;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous
public class MM14691AutoBlueNet extends MM14691Box {
    // Create an instance of our params class so the FTC dash can manipulate it.
    public static MM14691AutoBlueNet.Params PARAMS = new MM14691AutoBlueNet.Params();

    // Create the initial pose for this auto mode
    protected Pose2d initialPose4 = new Pose2d(PARAMS.positionX, PARAMS.positionY, PARAMS.heading);

    @Override
    public Pose2d getInitialPose() {
        return initialPose4;
    }

    @Override
    public void start() {
        super.start();

        // Create our list of Trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(pinpointDrive.actionBuilder(getInitialPose()));
        TrajectoryActionBuilder basketToNSample1 = basketToNSample1(startToBasket.endTrajectory());
        TrajectoryActionBuilder nSample1ToBasket = neutralSampleToBasket(basketToNSample1.endTrajectory());
        TrajectoryActionBuilder basketToNSample2 = basketToNSample2(nSample1ToBasket.endTrajectory());
        TrajectoryActionBuilder nSample2ToBasket = neutralSampleToBasket(basketToNSample2.endTrajectory());
        TrajectoryActionBuilder basketToNSample3 = basketToNSample3(nSample2ToBasket.endTrajectory());
        TrajectoryActionBuilder nSample3ToBasket = neutralSampleToBasket(basketToNSample3.endTrajectory());

        // Run the trajectories
        runningActions.add(
            new SequentialAction(
                    // Go to the basket
                    autoActionName("startToBasket"),
                    startToBasket.build(),

                    // Place the sample
                    autoActionName("placeSample"),
                    armDrive.liftToUp(),
                    armDrive.viperToEnd(),
                    // TODO eject the intake
                    armDrive.viperToStart(),
                    armDrive.liftToDown()
            )
        );

    }

    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = 36;
        public int positionY = 58;
        public int heading = 270;
    }
}