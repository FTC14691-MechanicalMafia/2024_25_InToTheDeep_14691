package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MotorDrive;
import org.firstinspires.ftc.teamcode.ServoDrive;

@Config
public class IntakeDrive extends ServoDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        public Double startPosition = 0.25;
        public Double endPosition = 0.75;
        public Double increment = 0.05;
    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static IntakeDrive.Params PARAMS = new IntakeDrive.Params();

    private DigitalChannel limitSwitch;

    public IntakeDrive(HardwareMap hardwareMap, String servoName) {
        this(hardwareMap.get(Servo.class, servoName));

        servo.setDirection(Servo.Direction.FORWARD);
    }

    public IntakeDrive(Servo servo) {
        // set this to wherever the motor is currently resting.
        super(servo, PARAMS.startPosition, PARAMS.endPosition);

        setIncrement(PARAMS.increment);
    }

}
