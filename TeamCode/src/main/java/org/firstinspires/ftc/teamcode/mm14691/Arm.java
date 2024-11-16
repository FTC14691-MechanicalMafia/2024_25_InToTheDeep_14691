package org.firstinspires.ftc.teamcode.mm14691;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
@TeleOp
public class Arm extends LinearOpMode {

    private DcMotor armViper = null;
    private DcMotor armLift= null;

    //TODO SET THIS VALUE
    public static final int VIPER_LIMIT = 10000;
    public static final int VIPER_HOME = 0;
    public static final int VIPER_HOLD_POWER = 0;

    //Set TargetPositions TODO
    int TargetHeight = 4125;

    DigitalChannel digitalTouch;  // Digital channel Object

    @Override
    public void runOpMode() throws InterruptedException {
        //initialize motors
        armLift = hardwareMap.get(DcMotor.class, "armLift");
        armViper = hardwareMap.get(DcMotor.class, "armViper");
        //setdirection for motors
        armLift.setDirection(DcMotorSimple.Direction.FORWARD);
        armViper.setDirection(DcMotorSimple.Direction.REVERSE);

        /* Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to slow down
        much faster when it is coasting. This creates a much more controllable drivetrain. As the robot
        stops much quicker. */
        armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armViper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //This will make sure that each time you hit run it starts at 0
        armViper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Command the robot to 0 to begin with so it doesn't move
        armViper.setTargetPosition(0);

        // get a reference to our touchSensor object.
        digitalTouch = hardwareMap.get(DigitalChannel.class, "armViperLimit");

        digitalTouch.setMode(DigitalChannel.Mode.INPUT);
        telemetry.addData("DigitalTouchSensorExample", "Press start to continue...");

        //add telemetry
        telemetry.addData("Arm Lift Status", "initialized");
        telemetry.addData("Viper starting position",  armViper.getCurrentPosition());
        telemetry.addData("Viper End Limit",  VIPER_LIMIT);
        telemetry.addData("Viper Current Position",armViper.getCurrentPosition());
        telemetry.update();

        // keep track of the viper motor 'start' position so we can calc the end position correctly
        int viperStartPosition = armViper.getCurrentPosition();

        // Waiting for start
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double armLiftPower = gamepad2.left_stick_y;
            double armViperPower = -gamepad2.right_stick_y;

            // Send telemetry message to indicate successful Encoder reset and current position
            telemetry.addData("Viper End Limit",  VIPER_LIMIT);
            telemetry.addData("Viper Current Position",armViper.getCurrentPosition());
            telemetry.addData("Viper Start Positin",viperStartPosition);
            telemetry.addData("ArmViperPower",armViperPower);

            // End limit hit, set power to 0;TODO temporary comment out to test the Viper Position
//            if (armViper.getCurrentPosition() - viperStartPosition >= viperLimit) {
//                armViperPower = VIPER_HOLD_POWER;
//                telemetry.addData("Viper End Limit", "Activated");
//            } else {
//                telemetry.addData("Viper End Limit", "Inactive");
//
//            }

            // Start limit hit, set power to 0
            if (digitalTouch.getState() == false && armViperPower < 0) {
                viperStartPosition = armViper.getCurrentPosition();
                telemetry.addData("Arm Lift Status", "calibrated");
                armViperPower = 0;
                telemetry.addData("Viper Start Limit", "Activated");
            } else {
                telemetry.addData("Viper Start Limit", "Inactive");
            }
            //run to target position
            //Tell the motor than you want to put it in closed loop position control
            //TODO if gamepad2.right_bumper pressed --do run to position
            //Now in your code, if you want to send the lift to a preset position (example coding it to the A,B,X,Y buttons):
            if(gamepad2.right_bumper) {
                armViper.setTargetPosition(TargetHeight);
                ((DcMotorEx) armViper).setVelocity(2100);
                armViper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            //To change the speed of the motor, it looks something like this: (You can adjust the 4000 down to say 500 and it will move much slower)
            //((DcMotorEx) LiftMotor).setVelocity(4000);

            armLift.setPower(armLiftPower);
            armViper.setPower(armViperPower);
            telemetry.addData("Arm Viper Current Position", armViper.getCurrentPosition());
            telemetry.addData("Arm Lift Power/ ArmViper Power ", "%4.2f, %4.2f", armLiftPower,armViperPower);
            telemetry.update();
        }
    }
}