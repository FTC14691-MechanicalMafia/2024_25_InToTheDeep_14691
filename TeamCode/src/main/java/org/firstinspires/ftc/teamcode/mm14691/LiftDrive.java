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

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MotorDrive;

@Config
public class LiftDrive extends MotorDrive {

    /**
     * Configure all of the team specific settings here
     */
    public static class Params {
        /**
         * How many ticks should the viper motor move from the limit switch
         */
        public int endLimit = 3082;

        /**
         * How many ticks above the rest position should the down position be
         */
        public int liftDownPosition = 400;

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
    public static LiftDrive.Params PARAMS = new LiftDrive.Params();

    private DigitalChannel limitSwitch;

    public LiftDrive(HardwareMap hardwareMap, String motorName) {
        this(hardwareMap.get(DcMotorEx.class, motorName));

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public LiftDrive(DcMotorEx motor) {
        // set this to wherever the motor is currently resting.
        super(motor,
                motor.getCurrentPosition(), //assume that the motor is at the start position
                motor.getCurrentPosition() + PARAMS.endLimit);

        setStartLimitActive(PARAMS.startLimitActive);
        setEndLimitActive(PARAMS.endLimitActive);
    }

    public ToPosition toDown() {
        return toPosition(PARAMS.liftDownPosition);
    }

//    @Override
//    public void addDebug(@NonNull Telemetry telemetry) {
//        telemetry.addData(this.getClass().getSimpleName(),
//                "St: %d, Dwn: %d, Cur: %d, End: %d, Pwr: %f",
//                getStartTick(), getStartTick() + PARAMS.liftDownPosition,
//                motor.getCurrentPosition(), getEndTick(),
//                motor.getPower());
//    }

}
