package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MotorDrive;

@Config
public class LiftDrive extends MotorDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        /**
         * How many ticks should the lift motor move from the limit switch
         */
        public int endLimit = 3082;

        /**
         * What is the motor ticks for the down position
         */
        public int downTicks = 400;

        /**
         * How many ticks for the 90deg position
         */
        public int ninetyTicks = 2000;

        /**
         * Allow overriding the limit from the console.
         */
        public boolean startLimitActive = true;

        /**
         * Allow overriding the limit from the console.
         */
        public boolean endLimitActive = true;

        /**
         * The number of ticks above the start limit that the viper limit should
         * be changed.
         */
        public int viperLimitAngle = 256;
        /**
         * Store the last run's start limit for lift
         */
        public static int lastRunLiftStartLimit = 0;

    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    // Reference to the viper drive so the arm angle can affect the viper end limit
    protected ViperDrive viperDrive;

    public LiftDrive(HardwareMap hardwareMap, String motorName, boolean useLastRunLiftStartLimit) {
        this(hardwareMap.get(DcMotorEx.class, motorName));

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        //in case the motor is REVERSE, the start and end will have already been read wrong.  Reset them
        if (useLastRunLiftStartLimit) {
            setStartTick(PARAMS.lastRunLiftStartLimit);
            setEndTick(PARAMS.lastRunLiftStartLimit + PARAMS.endLimit);
        } else {
            setStartTick(motor.getCurrentPosition());
            setEndTick(motor.getCurrentPosition() + PARAMS.endLimit);
        }


    }

    public LiftDrive(DcMotorEx motor) {
        // set this to wherever the motor is currently resting.
        super(motor,
                motor.getCurrentPosition(), //assume that the motor is at the start position
                motor.getCurrentPosition() + PARAMS.endLimit);

        setStartLimitEnabled(PARAMS.startLimitActive);
        setEndLimitEnabled(PARAMS.endLimitActive);
    }

    public ToPosition toDown() {
        return super.toPosition(PARAMS.downTicks);
    }

    public void setViperDrive(ViperDrive viperDrive) {
        this.viperDrive = viperDrive;
    }

    /**
     * This runs in the background and updates the viper limits based on the lift arm position.
     */
    public class AdjustViperLimits implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Check if we lift is above our angle
            if (motor.getCurrentPosition() > PARAMS.viperLimitAngle) {
                // if so, set the limit to the "up" limit
                viperDrive.setEndTick(ViperDrive.PARAMS.liftUpLimit);
            } else {
                // if not, set the limit to the "down" limit
                viperDrive.setEndTick(ViperDrive.PARAMS.endLimit);
            }

            return true; //always leave this running
        }
    }

    public AdjustViperLimits adjustViperLimits() {
        return new AdjustViperLimits();
    }

}

