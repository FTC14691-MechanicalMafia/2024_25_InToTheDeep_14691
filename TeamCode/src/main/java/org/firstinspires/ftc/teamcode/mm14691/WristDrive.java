package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ServoDrive;

@Config
public class WristDrive extends ServoDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        public Double increment = 0.01;
        public Double park = 0.0;
        public Double intake = 0.0;
        public Double outtake = 0.0;

        //These are for the logic to protected the intake from crashing into the arm.
        public Double intakeClosedStart = -0.5;
        public Double intakeClosedEnd = -0.25;
    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    protected IntakeDrive intakeDrive;

    public WristDrive(HardwareMap hardwareMap, String servoName) {
        this(hardwareMap.get(Servo.class, servoName));

        servo.setDirection(Servo.Direction.FORWARD);
    }

    public WristDrive(Servo servo) {
        // set this to wherever the motor is currently resting.
        super(servo, -1.0, 1.0);

        setIncrement(PARAMS.increment);
    }

    public ToPosition toPark() {
        return new ToPosition(toPosition(PARAMS.park));
    }

    public ToPosition toIntake() {
        return new ToPosition(toPosition(PARAMS.intake));
    }

    public ToPosition toOuttake() {
        return new ToPosition(toPosition(PARAMS.outtake));
    }

    public void setIntakeDrive(IntakeDrive intakeDrive) {
        this.intakeDrive = intakeDrive;
    }

    /**
     * Wrap the base ToPosition so we can add logic to prevent crashing
     */
    public class ToPosition extends ServoDrive.ToPosition {

        public ToPosition(double position) {
            super(position);
        }

        /**
         * Create this specialized instance from a regular instance.
         * @param toPosition
         */
        public ToPosition(ServoDrive.ToPosition toPosition) {
            super(toPosition.getPosition());
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            //prevent crashing of the intake into the lift arm
            if (getPosition() >= PARAMS.intakeClosedStart && getPosition() <= PARAMS.intakeClosedEnd) {
                // The wrist is moving into the danger range, move the intake to accommodate
                intakeDrive.toClosed().run(telemetryPacket);
            }

            return super.run(telemetryPacket);
        }
    }

}