package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class MotorDrive {

    protected final DcMotorEx motor;

    private Integer startTick;
    private Integer endTick;

    // Allow overriding of the limits
    private boolean startLimitEnabled = true;
    private boolean endLimitEnabled = true;

    //are we using limit listeners
    private boolean startLimitListenerUsed = false;
    private boolean startLimitListenerTriggered = false;

    private String status;

    /**
     * This is to keep track of the last value set by SetPower.  That way we don't continually set
     * the same power value to the motor.  This is so stick 'non-movements' don't override the
     * to[Position] actions.
     */
    private Double lastManualPower;

    public MotorDrive(DcMotorEx motor, Integer startTick, Integer endTick) {
        this.motor = motor;
        this.startTick = startTick;
        this.endTick = endTick;

        this.lastManualPower = motor.getPower(); // init the last manual power to whatever the motor is currently doing.  Should be 0 at startup.
        this.status = "Initialized";
    }


    /**
     * Check to make sure we are not overrunning our limits
     * @param power
     * @return true if the limit has been reached and we set power to 0
     */
    protected boolean enforceLimits(double power) {
        int currentPosition = motor.getCurrentPosition();

        // Update the status
        // Note: we do this here, because all of the motor Actions should be calling this method to enforce the limits.
        if (currentPosition < startTick) {
            status = "WARN: current position < start tick";
        } else if (currentPosition > endTick) {
            status = "WARN: current position > end tick";
        } else {
            status = "Running";
        }

        // check if we have overrun the end limit while we are heading towards it
        if (currentPosition >= endTick && power > 0 && isEndLimitEnabled()) {
            // we are trying to move past the end limit, stop the motor and bail
            motor.setPower(0);
            return true;
        }

        // check if we have overrun the start limit while we are heading towards it
        if (currentPosition < startTick // we are beyond the start tick
                && power < 0 // power is moving us towards the start
                && isStartLimitEnabled() // the limit is configured on
                && startLimitTriggered()) { // are we using the hardware limit and it has been hit
            // we are trying to move past the end limit, stop the motor and bail
            motor.setPower(0);
            return true;
        }
        return false;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Directly sets the power to the motor.
     */
    public class SetPower implements Action {

        private final double power;

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
            this.position = startTick + position; //set the requested position relative to the start
        }

        public int getPosition() {
            return position;
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
     * Listens for state changes on a hardware start limit
     */
    public class StartLimitListener implements LimitListener {
        @Override
        public void onLimit(String status) {
            if (LimitDrive.ACTIVE.equals(status)) {
                // set the updated start and end limits
                int endDiff = getEndTick() - getStartTick(); // get the current difference so we can recalc
                int curPos = motor.getCurrentPosition();
                setStartTick(curPos);
                setEndTick(curPos + endDiff);
                // TODO - add logging

                // record that we have been here
                startLimitListenerTriggered = true;
            }
        }
    }
    
    public StartLimitListener startLimitListener() {
        //since this was called we infer that it will be used
        startLimitListenerUsed = true;
        return new StartLimitListener();
    }

    public boolean startLimitTriggered() {
        if (!startLimitListenerUsed) {
            return true; // we are not using a start limit, so always return true to not affect the existing limits
        }

        return startLimitListenerTriggered;  // if not triggered than we should ignore limits, if triggered than use the limit
    }
    
    // TODO - implement end limit listener

    public void addDebug(@NonNull Telemetry telemetry) {
        telemetry.addData(this.getClass().getSimpleName(),
                "St: %d, Cur: %d, End: %d, Pwr: %f",
                getStartTick(), motor.getCurrentPosition(), getEndTick(),
                motor.getPower());
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

    public boolean isEndLimitEnabled() {
        return endLimitEnabled;
    }

    public void setEndLimitEnabled(boolean endLimitEnabled) {
        this.endLimitEnabled = endLimitEnabled;
    }

    public boolean isStartLimitEnabled() {
        return startLimitEnabled;
    }

    public void setStartLimitEnabled(boolean startLimitEnabled) {
        this.startLimitEnabled = startLimitEnabled;
    }
}
