package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MotorDrive;

@Config
public class ViperDrive extends MotorDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        /**
         * How many ticks should the viper motor move from the limit switch
         */
        public int endLimit = 2100;

        /**
         * Allow overriding the limit from the console.
         */
        public boolean startLimitActive = true;

        /**
         * Allow overriding the limit from the console.
         */
        public boolean endLimitActive = true;

    }

    // Create an instance of our params class so the FTC dash can manipulate it.
    public static ViperDrive.Params PARAMS = new ViperDrive.Params();

    public ViperDrive(HardwareMap hardwareMap, String motorName, String limitSwitchName) {
        this(hardwareMap.get(DcMotorEx.class, motorName),
                hardwareMap.get(DigitalChannel.class, limitSwitchName));

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public ViperDrive(DcMotorEx motor, DigitalChannel limitSwitch) {
        // set this to wherever the viper is currently resting.  This will be reset when we hit the limit switch.
        super(motor,
                motor.getCurrentPosition(),
                motor.getCurrentPosition() + PARAMS.endLimit);

        setStartLimitActive(PARAMS.startLimitActive);
        setEndLimitActive(PARAMS.endLimitActive);
    }

}
