package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MotorDrive;

@Config
public class ViperDrive extends MotorDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        /**
         * How many ticks should the viper motor move from the limit switch
         */
        public int endLimit = 20100;

        /**
         * Allow overriding the limit from the console.
         */
        public boolean startLimitActive = true;

        /**
         * Allow overriding the limit from the console.
         */
        public boolean endLimitActive = true;

    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static ViperDrive.Params PARAMS = new ViperDrive.Params();

    private DigitalChannel limitSwitch;

    public ViperDrive(HardwareMap hardwareMap, String motorName, String limitSwitchName) {
        this(hardwareMap.get(DcMotorEx.class, motorName),
                hardwareMap.get(DigitalChannel.class, limitSwitchName));

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        limitSwitch.setMode(DigitalChannel.Mode.INPUT);
    }

    public ViperDrive(DcMotorEx motor, DigitalChannel limitSwitch) {
        // set this to wherever the viper is currently resting.  This will be reset when we hit the limit switch.
        super(motor,
                motor.getCurrentPosition() - (PARAMS.endLimit / 2), //assume we are slack in the middle
                motor.getCurrentPosition() + (PARAMS.endLimit / 2));

        this.limitSwitch = limitSwitch;

        setStartLimitActive(PARAMS.startLimitActive);
        setEndLimitActive(PARAMS.endLimitActive);
    }

    /**
     * Update the limits when the limit switch is pressed.
     */
    public class limitSwitch implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Check if the limit switch is pressed
            if (isStartLimitTouched()) {
                // Record the motor position
                int currentPosition = motor.getCurrentPosition();
                setStartTick(currentPosition);
                setEndTick(currentPosition + PARAMS.endLimit);
            }

            return true; // we want this always running
        }
    }

    public Action limitSwitch() {
        return new limitSwitch();
    }

    /**
     * Since the hardware switch position is reversed for our use case, provide a helper method to
     * rationalize the current state.
     * @return true if the limit switch is currently pressed
     */
    protected boolean isStartLimitTouched() {
        return !limitSwitch.getState();
    }

}
