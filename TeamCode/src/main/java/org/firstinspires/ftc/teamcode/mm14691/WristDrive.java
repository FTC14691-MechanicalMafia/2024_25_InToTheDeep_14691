package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class WristDrive {
    /**
     * Configure all of the team specific settings here
     */
    public static class Params {

        /** Variables to store the speed the intake servo should be set at to intake, and deposit game elements. */
        public double intakeCollect = -1.0;
        public double intakeOff =  0.0;
        public double intakeDeposit =  0.5;

        /** Variables to store the positions that the wrist should be set to when folding in, or folding out. */
        public double wristFoldedIn = 0.46;
        public double wristFoldedOut = 0.75;

        public boolean debugOn = true;
    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static WristDrive.Params PARAMS = new WristDrive.Params();

    // Define the hardware this "Drive" cares about
    private CRServo intake = null; //the active intake servo
    private Servo wrist = null; //the wrist servo

    /**
     * This is where all of the hardware is initialized.
     * @param hardwareMap
     */
    public WristDrive(HardwareMap hardwareMap) {
        /* Define and initialize servos.*/
        intake = hardwareMap.get(CRServo.class, "intake");
        wrist  = hardwareMap.get(Servo.class, "wrist");
    }

    /**
     * Put the wrist in the position to collect a sample
     */
    public class SampleReady implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            wrist.setPosition(WristDrive.PARAMS.wristFoldedIn);

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
            wrist.setPosition(WristDrive.PARAMS.wristFoldedOut);

            return false;
        }
    }

    public SpecimenReady specimenReady() {
        return new SpecimenReady();
    }

}
