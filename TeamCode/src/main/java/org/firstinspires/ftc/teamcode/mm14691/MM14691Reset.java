package org.firstinspires.ftc.teamcode.mm14691;

import static org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories.startToPark;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.mm14691.trajectory.NetParkTrajectories;

/**
 * Puts the robot in it's 'start position'
 */
@Autonomous
public class MM14691Reset extends MM14691BaseAuto {


    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(0,0,0); // Don't care about the position while resetting
    }

    @Override
    public void start() {
        super.start();

        runningActions.add(
                new SequentialAction(
                        autoActionName("Intake reset"),
                        intakeDrive.toClosed(),
                        autoActionName("Wrist reset"),
                        wristDrive.toOuttake(),
                        autoActionName("Viper reset"),
                        viperDrive.setPower(-0.5),
                        viperLimitDrive.waitForLimit(), // wait for the drive to hit the hardware limit
                        autoActionName("Lift reset"),
                        liftDrive.setPower(-0.3),
                        liftLimitDrive.waitForLimit(), // wait for the drive to hit the hardware limit
                        autoActionName("Done")
                )
        );
    }

    @Override
    public void stop() {
        super.stop();

        // Clear the persistent current pose
        PoseStorage.currentPose = null;
    }
}

