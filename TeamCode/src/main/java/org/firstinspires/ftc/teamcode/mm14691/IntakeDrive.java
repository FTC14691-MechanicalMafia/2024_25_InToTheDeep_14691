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
        public Double open = 0.8;
        public Double closed = 0.94;
        public Double increment = 0.01;
    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static Params PARAMS = new Params();

    public IntakeDrive(HardwareMap hardwareMap, String servoName) {
        this(hardwareMap.get(Servo.class, servoName));

        servo.setDirection(Servo.Direction.FORWARD);
    }

    public IntakeDrive(Servo servo) {
        // set this to wherever the motor is currently resting.
        super(servo, PARAMS.open, PARAMS.closed);

        setIncrement(PARAMS.increment);
    }

    /**
     * Convenience method
     * @return
     */
    public ToPosition toOpen() {
        return toStart();
    }

    public ToPosition toClosed() {
        return toEnd();
    }

}
