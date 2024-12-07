package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.config.Config;
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
        public Double startPosition = 0.0;
        public Double endPosition = 0.9;
        public Double increment = 0.01;
    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static org.firstinspires.ftc.teamcode.mm14691.IntakeDrive.Params PARAMS = new org.firstinspires.ftc.teamcode.mm14691.IntakeDrive.Params();

    private DigitalChannel limitSwitch;

    public WristDrive(HardwareMap hardwareMap, String servoName) {
        this(hardwareMap.get(Servo.class, servoName));

        servo.setDirection(Servo.Direction.FORWARD);
    }

    public WristDrive(Servo servo) {
        // set this to wherever the motor is currently resting.
        super(servo, PARAMS.startPosition, PARAMS.endPosition);

        setIncrement(PARAMS.increment);
    }

}