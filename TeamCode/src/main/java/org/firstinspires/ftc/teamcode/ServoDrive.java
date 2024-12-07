package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class ServoDrive {

    protected final Servo servo;

    private Double startPosition;
    private Double endPosition;
    private Double increment;

    private String status;

    public ServoDrive(Servo servo, Double startPosition, Double endPosition) {
        this.servo = servo;
        this.startPosition = startPosition;
        this.endPosition = endPosition;

        //Sanity check
        if (startPosition > endPosition) {
            throw new IllegalArgumentException("startPosition must be less than the endPosition");
        }

        this.increment = 0.05;

        this.status = "Initialized";
    }

    public ToPosition toStart() {
        status = "Start Position";
        return new ToPosition(startPosition);
    }

    public ToPosition toEnd() {
        status = "End Position";
        return new ToPosition(endPosition);
    }

    /**
     * Sets the servo position forward one 'increment' from the current position
     */
    public ToPosition increment() {
        return new ToPosition(servo.getPosition() + increment);
    }

    /**
     * Sets the servo position back one 'increment' from the current position
     */
    public ToPosition decrement() {
        return new ToPosition(servo.getPosition() - increment);
    }

    /**
     * Move the motor to an arbitrary position.
     */
    public class ToPosition implements Action {

        private double position;

        /**
         * Create an instance with the specified position.
         * @param position to move the motor to.
         */
        public ToPosition(double position) {
            this.position = position; //set the requested position relative to the start
        }

        public double getPosition() {
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
            double currentPosition = servo.getPosition();
            if (currentPosition >= 1) {
                // can't increment, so just return 1
                // Note that this lets us move past the end position, but not past the max allowed value of the hardware
                position = 1;
                status = "INFO: Servo Positive Limit";
            } else if (currentPosition <= -1) {
                // can't decrement, so just return -1
                // Note that this lets us move past the start position, but not past the max allowed value of the hardware
                position = -1;
                status = "INFO: Servo Negative Limit";
            }

            servo.setPosition(position);

            return false;
        }

    }

    public ToPosition toPosition(double tickPosition) {
        status = "Running";
        return new ToPosition(tickPosition);
    }

    public void addDebug(@NonNull Telemetry telemetry) {
        telemetry.addData(this.getClass().getSimpleName(),
                "Pos: " + servo.getPosition());
    }

    public String getStatus() {
        return status;
    }

    public Servo getServo() {
        return servo;
    }

    public Double getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Double startPosition) {
        this.startPosition = startPosition;
    }

    public Double getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Double endPosition) {
        this.endPosition = endPosition;
    }

    public Double getIncrement() {
        return increment;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }
}
