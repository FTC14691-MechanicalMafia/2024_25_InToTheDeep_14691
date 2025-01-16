package org.firstinspires.ftc.teamcode.mm14691;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp
@Disabled
public class LiftDriveDebugger extends OpMode {

    //TODO - add debug mode for the motor
//    protected DcMotorEx viperMotor = null;
    protected DigitalChannel liftLimit = null;

    @Override
    public void init() {
        liftLimit = hardwareMap.get(DigitalChannel.class, "liftLimit");
        liftLimit.setMode(DigitalChannel.Mode.INPUT);
    }

    @Override
    public void loop() {
        // button is pressed if value returned is LOW or false.
        // send the info back to driver station using telemetry function.
        if (!liftLimit.getState()) {
            telemetry.addData("Button", "PRESSED");
        } else {
            telemetry.addData("Button", "NOT PRESSED");
        }

        telemetry.update();
    }
}
