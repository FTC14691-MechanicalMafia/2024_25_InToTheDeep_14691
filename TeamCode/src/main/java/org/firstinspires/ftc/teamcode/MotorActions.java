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

    /**
     * This is to keep track of the last value set by SetPower.  That way we don't continually set
     * the same power value to the motor.  This is so stick 'non-movements' don't override the
     * to[Position] actions.
     */
    private Double lastManualPower;

    public MotorActions(DcMotorEx motor, Integer startTick, Integer endTick) {
        this.motor = motor;
        this.startTick = startTick;
        this.endTick = endTick;

        this.lastManualPower = motor.getPower(); // init the last manual power to whatever the motor is currently doing.  Should be 0 at startup.
    }

    /**
     * Check to make sure we are not overrunning our limits
     * @param power
     * @return true if the limit has been reached and we set power to 0
     */
    protected boolean enforceLimits(double power) {
        int currentPosition = motor.getCurrentPosition();

        // check if we have overrun the end limit while we are heading towards it
        if (currentPosition >= endTick && power > 0) {
            // we are trying to move past the end limit, stop the motor and bail
            motor.setPower(0);
            return true;
        }

        // check if we have overrun the start limit while we are heading towards it
        if (currentPosition < startTick && power < 0) {
            // we are trying to move past the end limit, stop the motor and bail
            motor.setPower(0);
            return true;
        }
        return false;
    }

    /**
     * Directly sets the power to the motor.
     */
    public class SetPower implements Action {

        private double power;

        public SetPower(double power) {
            this.power = power;
        }

        /**
         * Sets the power to the motor directly, updates the telemetryPackate and exits.
         * Limits are enforced.
         * @param telemetryPacket to update
         * @return false as we only want this to run once
         */
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // make sure our limits are honored
            if (enforceLimits(power)) return false;

            // set the motor's power if the requested power has changed since the last run.
            if (power != lastManualPower) {
                motor.setPower(power);
                lastManualPower = power;
            }

            // Update the metrics
            double vel = motor.getVelocity();
            telemetryPacket.put(motor.getDeviceName() + "Velocity", vel);

            // we only want to run this for the single loop as the power will stay set until something changes it.
            return false;
        }

    }

    /**
     * Gets an instance of the SetPower action.
     * @param power to set the motor to
     * @return the instance of SetPower
     */
    public SetPower setPower(double power) {
        return new SetPower(power);
    }

    /**
     * Sends the motor back to the start position.
     */
    public class ToStart implements Action {

        private boolean initialized = false;

        /**
         * On the first loop it will set the motor power in the direction of the startPosition.
         * Then it will continue to run until the limit is reached, or another action changed the power
         * of the motor.
         * @param telemetryPacket
         * @return true if we haven't reached the limit yet; false if the limit has been reached, or if some other action 'cancelled' this action by changing the motor power.
         */
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Get some initial values
            double power = -0.8; // negative to head to the start

            // make sure our limits are honored
            if (enforceLimits(power)) return false;

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

    /**
     * Sends the motor forward to the end position.
     */
    public class ToEnd implements Action {

        private boolean initialized = false;

        /**
         * On the first loop it will set the motor power in the direction of the endPosition.
         * Then it will continue to run until the limit is reached, or another action changed the power
         * of the motor.
         * @param telemetryPacket
         * @return true if we haven't reached the limit yet; false if the limit has been reached, or if some other action 'cancelled' this action by changing the motor power.
         */
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Get some initial values
            double power = 0.8; // positive to head to the end

            // make sure our limits are honored
            if (enforceLimits(power)) return false;

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

    /**
     * Move the motor to an arbitrary position.
     */
    public class ToPosition implements Action {

        private boolean initialized = false;

        private int position;
        private int powerDirection;

        /**
         * Create an instance with the specified position.
         * @param position to move the motor to.
         */
        public ToPosition(int position) {
            this.position = position;
        }

        /**
         * Sets the motor power on the first loop in the direction of the target position.  Start and end limits are enforced.
         * Stops the motor when the target position is reached.  Another action can 'cancel' this action by setting power to the motor.
         * @param telemetryPacket
         * @return
         */
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Get some initial values
            int currentPosition = motor.getCurrentPosition();

            // Figure out which direction we need to head
            if (!initialized) {
                // only set the power direction on initialization.
                powerDirection = position > currentPosition ? 1 : -1;
            }

            // Set the desired power
            double power = 0.5 * powerDirection;

            // make sure our limits are honored
            if (enforceLimits(power)) return false;

            // check if we have overrun the end limit while we are heading towards it
            if (currentPosition >= position && power > 0) {
                // we are trying to move past the end limit, stop the motor and bail
                motor.setPower(0);
                return false;
            }

            // check if we have overrun the start limit while we are heading towards it
            if (currentPosition < position && power < 0) {
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
     * This action runs in the background as a safety and checks that we haven't overrun our limits.
     */
    public class Limits implements Action {

        /**
         * Checks the start and end limits
         * @param telemetryPacket to record info to
         * @return true to always run.
         */
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // make sure our limits are honored
            enforceLimits(motor.getPower());

            return true; // always stay running
        }
    }

    public Limits limits() {
        return new Limits();
    }

    /**
     * Gets the multiplier for the motor direction.
     * @return 1 if the motor is going foward
     * @return -1 if the motor is going backward
     */
    public int getMotorDirection() {
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
