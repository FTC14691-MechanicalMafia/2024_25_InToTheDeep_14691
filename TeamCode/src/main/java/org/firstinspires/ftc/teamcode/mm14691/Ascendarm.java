package org.firstinspires.ftc.teamcode.mm14691;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@TeleOp
public class Ascendarm extends LinearOpMode {
    private DcMotor ascend = null;


    @Override
    public void runOpMode() throws InterruptedException {
        //initialize the motor
        ascend = hardwareMap.get(DcMotor.class, "ascend");
        //TODO set direction for the motor
        ascend.setDirection(DcMotorSimple.Direction.FORWARD);
        // run until the end of the match (driver presses STOP)
        waitForStart();
        while (opModeIsActive()) {
            //when dpad is not press, no power for ascend
            boolean ascendUp=gamepad2.dpad_up;
            boolean ascendDown=gamepad2.dpad_down;
            if (ascendUp == true){
                ascend.setPower(1.0);
            }
            if(ascendDown == true){
                ascend.setPower(-1.0);
            }
            else {
                ascend.setPower(0);
            }
        }
    }
}
