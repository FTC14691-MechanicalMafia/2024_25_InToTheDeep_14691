package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class ArmDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        /**
         * How many ticks should the viper motor move from the limit switch
         */
        public int viperEndLimit = 1900;

        /**
         * How many ticks above the rest position should the down position be
         */
        public int liftDownPosition = 400;

        /** Variables to store the speed the intake servo should be set at to intake, and deposit game elements. */
        public double intakeCollect = -1.0;
        public double intakeOff =  0.0;
        public double intakeDeposit =  0.5;

        //TODO NEED TO CHANGE TO OUR SETTINGS
        /** Variables to store the positions that the wrist should be set to when folding in, or folding out. */
        public double wristFoldedIn = 0.8333;
        public double wristFoldedOut = 0.5;

        public boolean debugOn = true;
    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static ArmDrive.Params PARAMS = new ArmDrive.Params();

    // Hold on to the telemetry for debugging
    private Telemetry telemetry;

    // Define the hardware this "Drive" cares about
    private DigitalChannel viperLimitSwitch;  // Digital channel Object
    private DcMotorEx armViper = null;
    private DcMotorEx armLift = null;

    private CRServo intake = null; //the active intake servo
    private Servo wrist = null; //the wrist servo
    private DcMotor ascend = null;

    // keep track of the viper motor 'start' position so we can calc the end position correctly
    int viperStartPosition = 0;

    // keep track of the lift arm positions we care about
    int liftRestPosition = 0;
    int liftDownPosition = 0;

    /**
     * This is where all of the hardware is initialized.
     * @param hardwareMap
     */
    public ArmDrive(HardwareMap hardwareMap, Telemetry telemetry) {
        // Keep the reference to telemetry for debugging
        this.telemetry = telemetry;

        // Initialize the arms motors
        armLift = hardwareMap.get(DcMotorEx.class, "armLift");
        armViper = hardwareMap.get(DcMotorEx.class, "armViper");
        ascend = hardwareMap.get(DcMotor.class, "ascend");

        //set power behavior
        armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armViper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //set the directions
        armLift.setDirection(DcMotorSimple.Direction.REVERSE);
        armViper.setDirection(DcMotorSimple.Direction.FORWARD);
        ascend.setDirection(DcMotorSimple.Direction.FORWARD);

        // set this to wherever the viper is currently resting.  This will be reset when we hit the limit switch.
        viperStartPosition = armViper.getCurrentPosition();

        // set this to wherever the lift is currently resting.  This should be on the floor.
        liftRestPosition = armLift.getCurrentPosition();
        liftDownPosition = liftRestPosition - PARAMS.liftDownPosition; // FIXME - depends on motor direction

        // get a reference to our touchSensor object.
        viperLimitSwitch = hardwareMap.get(DigitalChannel.class, "armViperLimit");
        viperLimitSwitch.setMode(DigitalChannel.Mode.INPUT);

        /* Define and initialize servos.*/
        intake = hardwareMap.get(CRServo.class, "intake");
        wrist  = hardwareMap.get(Servo.class, "wrist");


    }

    public class DebugAction implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Only print debug while turned on
            if (PARAMS.debugOn) {
                telemetry.addData("Viper Position", "Start: %d, Current: %d, End: %d",
                        viperStartPosition, armViper.getCurrentPosition(), getViperEndPosition());
                telemetry.addData("Viper Limits Active", "Start: %b, End: %b",
                        isViperStartLimitActive(), isViperEndLimitActive());

                telemetry.addData("Lift Position", "Rest: %d, Down: %d, Current: %d, Up: %d",
                        liftRestPosition, liftDownPosition, armLift.getCurrentPosition(), 0);
            }

            return true; // Always run this in case debug gets turned on
        }
    }

    public DebugAction getDebugAction() {
        return new DebugAction();
    }

    /**
     * Since the hardware switch position is reversed for our use case, provide a helper method to
     * rationalize the current state.
     * @return true if the limit switch is currently pressed
     */
    protected boolean isViperStartLimitActive() {
        return !viperLimitSwitch.getState();
    }

    /**
     * Retract the viper arm until the limit switch is triggered.
     */
    public class ViperToStart implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                armViper.setPower(0.8); // retract at 80% power
                initialized = true;
            }

            // Check if the limit switch is pressed
            if (isViperStartLimitActive()) {
                // Record the motor position
                viperStartPosition = armViper.getCurrentPosition();

                // Stop the motor
                armViper.setPower(0);
            }

            // capture some metrics
            double vel = armViper.getVelocity();
            telemetryPacket.put("viperVelocity", vel);

            // This should run while the limit is not hit
            return !isViperStartLimitActive();
        }
    }

    public ViperToStart viperToStart() {
        return new ViperToStart();
    }

    /**
     * Extend the viper arm until the soft limit is triggered.
     */
    public class ViperToEnd implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Check if the limit switch is pressed
            if (isViperEndLimitActive()) {
                // Stop the motor
                armViper.setPower(0);
                return false; // we are at the end, bail
            }

            if (!initialized) {
                armViper.setPower(-0.8); // retract at 80% power
                initialized = true;
            }

            // capture some metrics
            double vel = armViper.getVelocity();
            telemetryPacket.put("viperVelocity", vel);

            // Stop if we have hit the end limit
            return true; // since we haven't hit the end limit, keep going
        }
    }

    public ViperToEnd viperToEnd() {
        return new ViperToEnd();
    }

    protected int getViperEndPosition() {
        //FIXME - this depends on the direction of the motor
        return viperStartPosition - PARAMS.viperEndLimit;
    }

    /**
     * Since the hardware switch position is reversed for our use case, provide a helper method to
     * rationalize the current state.
     * @return true if the motor position is at the soft limit
     */
    protected boolean isViperEndLimitActive() {
        //FIXME - this depends on the direction of the motor
        return armViper.getCurrentPosition() <= getViperEndPosition();
    }

    /**
     * Sets the power of the Viper motor
     */
    public class ViperPower implements Action {

        //NOTE: power is positive when the stick is down (moving towards start)
        //      power is negative when the stick is up (moving towards end)
        private double power;

        public ViperPower(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Check if we are moving towards the start and the limit is active
            if (isViperStartLimitActive() && power > 0) {
                armViper.setPower(0);
                return false; // we are at the limit, bail
            }
            // Check if we are moving towards the end and the soft limit is hit
            if (isViperEndLimitActive() && power < 0) {
                armViper.setPower(0);
                return false; // we are at the limit, bail
            }

            // Set the motor's power
            armViper.setPower(power);

            // Update the metrics (should be stopped now)
            double vel = armViper.getVelocity();
            telemetryPacket.put("viperVelocity", vel);

            return false; // just run the one time
        }
    }

    public ViperPower setViperPower(double power) {
        return new ViperPower(power);
    }

    /**
     * Retract the viper arm until the limit switch is triggered.
     */
    public class LiftToDown implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Sanity; Bail if we are already at down
            if (armLift.getCurrentPosition() == liftDownPosition) {
                armLift.setPower(0); // stop the motor just in case
                return false; //nothing to do
            }

            // Figure out which direction we need to move to get to down
            // Positive the arm is moving towards the up position
            int direction = armLift.getCurrentPosition() < liftDownPosition ? 1 : -1;
            telemetry.addData("DEBUG: direction", direction);

            if (!initialized) {
                armLift.setPower(0.5 * direction); // 80% power
                initialized = true;
            }

            // capture some metrics
            double vel = armLift.getVelocity();
            telemetryPacket.put("liftVelocity", vel);

            // Are we moving towards rest and hit the down position?
            if (direction > 0 && armLift.getCurrentPosition() < liftDownPosition) {
                armLift.setPower(0);;
                return false; // mission accomplished, stop the action
            }

            // Are we moving towards up and hit the down position?
            if (direction < 0 && armLift.getCurrentPosition() > liftDownPosition) {
                armLift.setPower(0);;
                return false; // mission accomplished, stop the action
            }

            // This should run while the limit is not hit
            return true;
        }
    }

    public LiftToDown liftToDown() {
        return new LiftToDown();
    }

    /**
     * Sets the power of the Viper motor
     */
    public class LiftPower implements Action {

        //NOTE: power is positive when the stick is down (moving towards rest)
        //      power is negative when the stick is up (moving towards up)
        private double power;

        public LiftPower(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // TODO - check limits

            // Set the motor's power
            armLift.setPower(power);

            // Update the metrics (should be stopped now)
            double vel = armLift.getVelocity();
            telemetryPacket.put("liftVelocity", vel);

            return false; // just run the one time
        }
    }

    public LiftPower setLiftPower(double power) {
        return new LiftPower(power);
    }

    /**
     * Put the wrist in the position to collect a sample
     */
    public class IntakeReady implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            /* Make sure that the intake is off, and the wrist is folded in. */
            intake.setPower(ArmDrive.PARAMS.intakeOff);
            wrist.setPosition(ArmDrive.PARAMS.wristFoldedIn);

            return false;
        }
    }

    public IntakeReady intakeReady() {
        return new IntakeReady();
    }

    public class IntakePower implements Action {
        double power;

        public IntakePower(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Just set the power and move on
            intake.setPower(power);

            return false;
        }
    }

    public IntakePower setIntakePower(double power) {
        return new IntakePower(power);
    }

    /**
     * Sets the power of the Viper motor
     */
    public class AscensionPower implements Action {

        //NOTE: power is positive when the stick is down (moving towards start)
        //      power is negative when the stick is up (moving towards end)
        private double power;

        public AscensionPower(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // TODO - should there be limits here?

            // Set the motor's power
            armViper.setPower(power);

            // Update the metrics (should be stopped now)
            double vel = armViper.getVelocity();
            telemetryPacket.put("viperVelocity", vel);

            return false; // just run the one time
        }
    }

    public AscensionPower setAscensionPower(double power) {
        return new AscensionPower(power);
    }
}
