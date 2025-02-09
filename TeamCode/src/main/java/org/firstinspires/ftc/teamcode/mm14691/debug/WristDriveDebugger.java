package org.firstinspires.ftc.teamcode.mm14691.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Disabled
public class WristDriveDebugger extends OpMode {

    protected Servo wristDrive = null;

    @Override
    public void init() {
        wristDrive = hardwareMap.get(Servo.class, "wrist");

    }

    @Override
    public void loop() {
        if (gamepad2.x) {
//            wristDrive.toOpen().run(packet);
            wristDrive.setPosition(0.25);
        }
        if (gamepad2.y) {
//            wristDrive.toClosed().run(packet);
            wristDrive.setPosition(-0.25);
        }

    }
}
