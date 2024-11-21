package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MotorActions {

    private final DcMotorEx motor;

    private Integer startTick;
    private Integer endTick;

    public MotorActions(DcMotorEx motor, Integer startTick, Integer endTick) {
        this.motor = motor;
        this.startTick = startTick;
        this.endTick = endTick;
    }

    public class SetPower implements Action {

        private double power;

        public SetPower(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Get some initial values
            int direction = getMotorDirection();
            int currentPosition = motor.getCurrentPosition();

            // check if we have overrun the end limit while we are heading towards it
            if (currentPosition * direction > endTick * direction && power * direction > 0) {
                // we are trying to move past the end limit, stop the motor and bail
                motor.setPower(0);
                return false;
            }

            // check if we have overrun the start limit while we are heading towards it
            if (currentPosition * direction < startTick * direction && power * direction < 0) {
                // we are trying to move past the end limit, stop the motor and bail
                motor.setPower(0);
                return false;
            }

            // set the motor's power
            motor.setPower(power);

            // Update the metrics
            double vel = motor.getVelocity();
            telemetryPacket.put(motor.getDeviceName() + "Velocity", vel);

            // we only want to run this for the single loop as the power will stay set until something changes it.
            return false;
        }

    }

    public SetPower setPower(double power) {
        return new SetPower(power);
    }

    public class ToStart implements Action {

        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Get some initial values
            double power = -0.8; // negative to head to the start (corrected by direction later)
            int direction = getMotorDirection();
            int currentPosition = motor.getCurrentPosition();

            // check if we have overrun the start limit while we are heading towards it
            if (currentPosition * direction < startTick * direction && power * direction < 0) {
                // we are trying to move past the end limit, stop the motor and bail
                motor.setPower(0);
                return false;
            }

            if (!initialized) {
                //first time for everything - set the motor's power
                motor.setPower(power);
                initialized = true;
            }

            // Update the metrics
            double vel = motor.getVelocity();
            telemetryPacket.put(motor.getDeviceName() + "Velocity", vel);

            // We want this to continue running until we reach the limit
            // However, check if some other command may have overridden this one.
            // if the motor power is not what we set it.  If it isn't then we will just terminate this action
            return motor.getPower() == power;
        }

    }

    public ToStart toStart() {
        return new ToStart();
    }

    public class ToEnd implements Action {

        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Get some initial values
            double power = 0.8; // positive to head to the end (corrected by direction later)
            int direction = getMotorDirection();
            int currentPosition = motor.getCurrentPosition();

            // check if we have overrun the end limit while we are heading towards it
            if (currentPosition * direction > endTick * direction && power * direction > 0) {
                // we are trying to move past the end limit, stop the motor and bail
                motor.setPower(0);
                return false;
            }

            if (!initialized) {
                //first time for everything - set the motor's power
                motor.setPower(power);
                initialized = true;
            }

            // Update the metrics
            double vel = motor.getVelocity();
            telemetryPacket.put(motor.getDeviceName() + "Velocity", vel);

            // We want this to continue running until we reach the limit
            // However, check if some other command may have overridden this one.
            // if the motor power is not what we set it.  If it isn't then we will just terminate this action
            return motor.getPower() == power;
        }

    }

    public ToEnd toEnd() {
        return new ToEnd();
    }

    public class ToPosition implements Action {

        private boolean initialized = false;

        private int position;
        private int powerDirection;

        public ToPosition(int position) {
            this.position = position;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Get some initial values
            int direction = getMotorDirection();
            int currentPosition = motor.getCurrentPosition();

            // Figure out which direction we need to head
            if (!initialized) {
                // only set the power direction on initialization.
                powerDirection = position > currentPosition ? 1 : -1;
            }

            // Set the desired power
            double power = 0.8 * powerDirection;

            // check if we have overrun the end limit while we are heading towards it
            if (currentPosition * direction > position * direction && power * direction > 0) {
                // we are trying to move past the end limit, stop the motor and bail
                motor.setPower(0);
                return false;
            }

            // check if we have overrun the start limit while we are heading towards it
            if (currentPosition * direction < position * direction && power * direction < 0) {
                // we are trying to move past the end limit, stop the motor and bail
                motor.setPower(0);
                return false;
            }

            if (!initialized) {
                //first time for everything - set the motor's power
                motor.setPower(power);
                initialized = true;
            }

            // Update the metrics
            double vel = motor.getVelocity();
            telemetryPacket.put(motor.getDeviceName() + "Velocity", vel);

            // We want this to continue running until we reach the limit
            // However, check if some other command may have overridden this one.
            // if the motor power is not what we set it.  If it isn't then we will just terminate this action
            return motor.getPower() == power;
        }

    }

    public ToPosition toPosition(int tickPosition) {
        return new ToPosition(tickPosition);
    }

    /**
     * Gets the multiplier for the motor direction.
     * @return 1 if the motor is going foward
     * @return -1 if the motor is going backward
     */
    protected int getMotorDirection() {
        return motor.getDirection() == DcMotorSimple.Direction.FORWARD ? 1 : -1;
    }

    public Integer getStartTick() {
        return startTick;
    }

    public void setStartTick(Integer startTick) {
        this.startTick = startTick;
    }

    public Integer getEndTick() {
        return endTick;
    }

    public void setEndTick(Integer endTick) {
        this.endTick = endTick;
    }
}
