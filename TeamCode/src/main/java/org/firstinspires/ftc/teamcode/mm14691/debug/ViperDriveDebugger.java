package org.firstinspires.ftc.teamcode.mm14691.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp
@Disabled
public class ViperDriveDebugger extends OpMode {

    //TODO - add debug mode for the motor
//    protected DcMotorEx viperMotor = null;
    protected DigitalChannel viperLimit = null;

    @Override
    public void init() {
        viperLimit = hardwareMap.get(DigitalChannel.class, "viperLimit");
        viperLimit.setMode(DigitalChannel.Mode.INPUT);
    }

    @Override
    public void loop() {
        // button is pressed if value returned is LOW or false.
        // send the info back to driver station using telemetry function.
        if (!viperLimit.getState()) {
            telemetry.addData("Button", "PRESSED");
        } else {
            telemetry.addData("Button", "NOT PRESSED");
        }

        telemetry.update();
    }
}
