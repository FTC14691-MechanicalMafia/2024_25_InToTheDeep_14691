package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MM14691ParkArm extends MM14691BaseOpMode {

    @Override
    public void start() {
        super.start();

        //defeat the limits
        viperDrive.setEndLimitActive(false);
        viperDrive.setStartLimitActive(false);

        liftDrive.setEndLimitActive(false);
        liftDrive.setStartLimitActive(false);

        ascendDrive.setEndLimitActive(false);
        ascendDrive.setStartLimitActive(false);
    }

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();

        // Create actions for the Viper
        runningActions.add(viperDrive.setPower(-gamepad2.right_stick_y));

        // Create actions for the list arm
        runningActions.add(liftDrive.setPower(-gamepad2.left_stick_y));

        // Create actions for the claws
        if (gamepad2.y) {
            runningActions.add(intakeDrive.toClosed());  //since we store it this way
        }

        // Create actions for the wrist
        if (gamepad2.a) { //Park the wrist
            runningActions.add(wristDrive.toPark());
        }

//        // Create actions for the ascension arm
        //TODO - renable when we have the ascension arm

        // Add some debug about the actions we are about to run.
        telemetry.addData("Running Actions", runningActions.stream()
                .map(action -> action.getClass().getSimpleName())
                        .filter(action -> !action.toLowerCase().contains("debug"))
                .reduce("", (sub, ele) -> sub + ", " + ele));

        // update running actions
        updateRunningActions(packet);

        // Refresh the driver screen
        telemetry.update();

        dash.sendTelemetryPacket(packet);
    }

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(0, 0, 0); // this does not matter for teleop unless we start using paths
    }
}
