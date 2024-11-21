package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MotorActions;

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
        public int liftDownPosition = 200;
        public int liftUpLimit = 2500;

        /**
         * How many ticks should the ascend arm be allowed to move
         */
        public int ascendEndLimit = 2000;

        /** Variables to store the speed the intake servo should be set at to intake, and deposit game elements. */
        public double intakeCollect = -1.0;
        public double intakeOff =  0.0;
        public double intakeDeposit =  0.5;

        //TODO NEED TO CHANGE TO OUR SETTINGS
        /** Variables to store the positions that the wrist should be set to when folding in, or folding out. */
        public double wristFoldedIn = 0.358;
        public double wristFoldedOut = 0.75;

        public boolean debugOn = true;
    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static ArmDrive.Params PARAMS = new ArmDrive.Params();

    // Hold on to the telemetry for debugging
    private Telemetry telemetry;

    // Define the hardware this "Drive" cares about
    private DigitalChannel viperLimitSwitch;  // Digital channel Object
    private DcMotorEx armViper = null;
    private MotorActions viperActions = null;
    private DcMotorEx armLift = null;
    private MotorActions liftActions = null;

    private CRServo intake = null; //the active intake servo
    private Servo wrist = null; //the wrist servo
    private DcMotorEx ascend = null;
    private MotorActions ascendActions = null;

    // keep track of the viper motor 'start' position so we can calc the end position correctly
    int viperStartPosition = 0;

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
        ascend = hardwareMap.get(DcMotorEx.class, "ascend");

        //set power behavior
        armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armViper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //set the directions
        armLift.setDirection(DcMotorSimple.Direction.REVERSE);
        armViper.setDirection(DcMotorSimple.Direction.REVERSE);
        ascend.setDirection(DcMotorSimple.Direction.FORWARD);

        // set this to wherever the viper is currently resting.  This will be reset when we hit the limit switch.
        viperActions = new MotorActions(armViper,
                armViper.getCurrentPosition(),
                armViper.getCurrentPosition() + PARAMS.viperEndLimit); //FIXME - depends on the motor direction

        // set this to wherever the lift is currently resting.  This should be on the floor.
        liftActions = new MotorActions(armLift,
                armLift.getCurrentPosition(),
                armLift.getCurrentPosition() + PARAMS.liftUpLimit);

        // get a reference to our touchSensor object.
        viperLimitSwitch = hardwareMap.get(DigitalChannel.class, "armViperLimit");
        viperLimitSwitch.setMode(DigitalChannel.Mode.INPUT);

        // Set the limits for the ascend motor
        ascendActions = new MotorActions(ascend,
                ascend.getCurrentPosition(),
                ascend.getCurrentPosition() - PARAMS.ascendEndLimit); //FIXME - depends on the motor direction

        /* Define and initialize servos.*/
        intake = hardwareMap.get(CRServo.class, "intake");
        wrist  = hardwareMap.get(Servo.class, "wrist");
    }

    public class DebugAction implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Only print debug while turned on
            if (PARAMS.debugOn) {
                telemetry.addData("Viper", "St: %d, Cur: %d, End: %d, Pwr: %f",
                        viperActions.getStartTick(), armViper.getCurrentPosition(), viperActions.getEndTick(),
                        armViper.getPower());

                telemetry.addData("Lift", "St: %d, Dwn: %d, Cur: %d, End: %d, Pwr: %f",
                        liftActions.getStartTick(), getLiftDownPosition(),
                        armLift.getCurrentPosition(), liftActions.getEndTick(),
                        armLift.getPower());

                telemetry.addData("Wrist", "Pos: %f",
                        wrist.getPosition());
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
    public class ViperLimitSwitchActive implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // Check if the limit switch is pressed
            if (isViperStartLimitActive()) {
                // Record the motor position
                int currentPosition = armViper.getCurrentPosition();
                viperActions.setStartTick(currentPosition);
                viperActions.setEndTick(
                        currentPosition + PARAMS.viperEndLimit);
            }

            return true;
        }
    }

    public Action viperLimitSwitch() {
        return new ViperLimitSwitchActive();
    }

    public Action viperToStart() {
        return viperActions.toStart();
    }

    public Action viperToEnd() {
        return viperActions.toEnd();
    }

    public Action viperToPosition(int position) {
        return viperActions.toPosition(viperActions.getStartTick() + position);
    }

    public Action setViperPower(double power) {
        return viperActions.setPower(power);
    }

    public Action liftToPosition(int position) {
        return liftActions.toPosition(liftActions.getStartTick() + position);
    }

    public Action liftToDown() {
        return liftActions.toPosition(getLiftDownPosition());
    }

    public Action liftToUp() {
        return liftActions.toEnd();
    }

    public Action setLiftPower(double power) {
        return liftActions.setPower(power);
    }

    public int getLiftDownPosition() {
        return liftActions.getStartTick() - PARAMS.liftDownPosition;
    }

    /**
     * Put the wrist in the position to collect a sample
     */
    public class SampleReady implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            /* Make sure that the intake is off, and the wrist is folded in. */
//            intake.setPower(ArmDrive.PARAMS.intakeOff);
            wrist.setPosition(ArmDrive.PARAMS.wristFoldedIn);

            return false;
        }
    }

    public SampleReady sampleReady() {
        return new SampleReady();
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
    * Put the wrist in the position to collect a sample
     */
    public class SpecimenReady implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            /* Make sure that the intake is off, and the wrist is folded in. */
//            intake.setPower(ArmDrive.PARAMS.intakeOff);
            wrist.setPosition(ArmDrive.PARAMS.wristFoldedOut);

            return false;
        }
    }

    public SpecimenReady specimenReady() {
        return new SpecimenReady();
    }

    public Action setAscensionPower(double power) {
        return ascendActions.setPower(power);
    }
}
