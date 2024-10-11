package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class MyFIRSTjavaOpmode extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor motorTest;
    private DigitalChannel  digitaltouch;
    private DistanceSensor sensorcolorrange;
    private Servo servotest;
    @Override
    public void runOpMode() throws InterruptedException {

          imu = hardwareMap.get(Gyroscope.class, "imu");
          motorTest = hardwareMap.get(DcMotor.class, "motorTest");
          digitaltouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
          sensorcolorrange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
          servotest = hardwareMap.get(Servo.class, "servoTest");

          telemetry.addData("Status", "Initialized");
          telemetry.update();
          //    Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until end of match (driver presses STOP)
        while (opModeIsActive()) {
        telemetry.addData("Status", "Running");
        telemetry.update();

        }

        }
    }

