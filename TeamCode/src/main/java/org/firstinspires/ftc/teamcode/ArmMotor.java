package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp
public class ArmMotor extends LinearOpMode{
    private DcMotor armMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor armMotor = hardwareMap.dcMotor.get("armMotor");
        waitForStart();
        double armup = gamepad2.right_stick_y;
        armMotor.setPower(armup*0.5);//take out 0.5 if we test the arm with right directions

    }
}
