package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


/**
 * See https://docs.google.com/document/d/1D9uxCZty4LIeQDVSoOdOJbbBigU5Q8DqBo9M7vusQDY/edit?tab=t.0
 */
@Config
@Autonomous
@Disabled
public class MM14691AutoNetSamplesLow extends MM14691AutoNetSamplesBase {
    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(PARAMS.positionX, PARAMS.positionY, Math.toRadians(PARAMS.heading));
    }

    @Override
    protected Vector2d getNetLocation() {
        return new Vector2d(-54, -48);
    }

    @NonNull
    @Override
    protected SequentialAction createDropSampleAction() {
//        int basketBaseTicks = 2110; //ticks to drop the sample
        int basketBaseTicks = 1700; //ticks to drop the sample
        return new SequentialAction(
                liftDrive.toPosition(basketBaseTicks // + 40
                        , 1), //raise past where we need
                wristDrive.toIntake(),

                viperDrive.toPosition(2400, 1), //extend the viper arm
//                viperDrive.toPosition(3000, 0.9), //extend the viper arm
//                liftDrive.toPosition(basketBaseTicks, 1), //move a tad closer to the basket
                intakeDrive.toOpen(),
                liftDrive.toPosition(basketBaseTicks + 70, 1),

                // wristDrive.toIntake(),
                viperDrive.toStart(0.8),
                liftDrive.toPosition(300,1) //so the claw doesn't drag on the ground
        );
    }


    /**
     * Specific coordinates for different positions
     */
    public static class Params {
        public int positionX = -33;
        public int positionY = -62;
        public int heading = 90;
    }
}