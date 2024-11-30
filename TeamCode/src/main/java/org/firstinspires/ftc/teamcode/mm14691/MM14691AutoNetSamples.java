package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories.startToPark;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample1;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample2;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.basketToNSample3;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.neutralSampleToBasket;
import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetSamplesTrajectories.startToBasket;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous
public class MM14691AutoNetSamples extends MM14691BaseAuto {
    // Create an instance of our params class so the FTC dash can manipulate it.
    public static MM14691AutoNetSamples.Params PARAMS = new MM14691AutoNetSamples.Params();
    protected Pose2d initialPose1 = new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));

    @Override
    public Pose2d getInitialPose() {
        return initialPose1;
    }

    @Override
    public void start() {
        super.start();
        // Create out trajectories
        TrajectoryActionBuilder startToBasket = startToBasket(pinpointDrive.actionBuilder(getInitialPose()));
        TrajectoryActionBuilder basketToNSample1 = basketToNSample1(startToBasket.endTrajectory());
        TrajectoryActionBuilder nSample1ToBasket = neutralSampleToBasket(basketToNSample1.endTrajectory());
        TrajectoryActionBuilder basketToNSample2 = basketToNSample2(nSample1ToBasket.endTrajectory());
        TrajectoryActionBuilder nSample2ToBasket = neutralSampleToBasket(basketToNSample2.endTrajectory());
        TrajectoryActionBuilder basketToNSample3 = basketToNSample3(nSample2ToBasket.endTrajectory());
        TrajectoryActionBuilder nSample3ToBasket = neutralSampleToBasket(basketToNSample3.endTrajectory());

        Actions.runBlocking(
                new SequentialAction(
                        autoActionName("Start to Basket"),
                        startToBasket.build(),
                        autoActionName("Basket to Sample 1"),
                        basketToNSample1.build(),
                        autoActionName("Sample 1 to Basket"),
                        nSample1ToBasket.build(),
                        autoActionName("Basket to Sample 2"),
                        basketToNSample2.build(),
                        autoActionName("Sample 2 to Basket"),
                        nSample2ToBasket.build(),
                        autoActionName("Basket to Sample 3"),
                        basketToNSample3.build(),
                        autoActionName("Sample 3 to Basket"),
                        nSample3ToBasket.build()
                )
        );
    }


    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = -36;
        public int positionY = -58;
        public int heading = 180;
    }
}