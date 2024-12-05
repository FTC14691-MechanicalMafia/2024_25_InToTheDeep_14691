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

    public ServoDrive(Servo servo, Double startPosition, Double endPosition) {
        this.servo = servo;
        this.startPosition = startPosition;
        this.endPosition = endPosition;

        this.increment = 0.05;
    }

    public ToPosition toStart() {
        return new ToPosition(startPosition);
    }

    public ToPosition toEnd() {
        return new ToPosition(endPosition);
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

        /**
         * Sets the motor power on the first loop in the direction of the target position.  Start and end limits are enforced.
         * Stops the motor when the target position is reached.  Another action can 'cancel' this action by setting power to the motor.
         * @param telemetryPacket
         * @return
         */
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            servo.setPosition(endPosition);

            return false;
        }

    }

    public ToPosition toPosition(int tickPosition) {
        return new ToPosition(tickPosition);
    }

    public void addDebug(@NonNull Telemetry telemetry) {
        telemetry.addData(this.getClass().getSimpleName(),
                "Pos: %d, Cur: %d, End: %d, Pwr: %f",
                servo.getPosition());
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
