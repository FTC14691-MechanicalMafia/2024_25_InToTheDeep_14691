package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class DebugIntakeDrive extends OpMode {

    protected Servo intakeDrive = null;

    @Override
    public void init() {
        intakeDrive = hardwareMap.get(Servo.class, "intake");
    }

    @Override
    public void loop() {
        if (gamepad2.a) {
            intakeDrive.setPosition(0.8);
        }
        if (gamepad2.b) {
            intakeDrive.setPosition(0.94);
        }

    }
}
