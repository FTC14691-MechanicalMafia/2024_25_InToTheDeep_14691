package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MM14691ParkArm extends MM14691BaseOpMode {

    @Override
    public void start() {
        super.start();

        //defeat the limits
        viperDrive.setEndLimitEnabled(false);
        viperDrive.setStartLimitEnabled(false);

        liftDrive.setEndLimitEnabled(false);
        liftDrive.setStartLimitEnabled(false);
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

        // Create actions for the claws
        // Note, the intake actions should get added to the running actions before the wrist actions
        //    this will allow the wrist actions to run the crash protection.
        if (gamepad2.a) {
            runningActions.add(intakeDrive.toOpen());
        }
        if (gamepad2.b) {
            runningActions.add(intakeDrive.toClosed());
        }

        // Create actions for the wrist
        if (gamepad2.x) { //Turn on the wheel for collection
            runningActions.add(wristDrive.toIntake());
        }
        if (gamepad2.y) { //Turn on the wheel for deposit
            runningActions.add(wristDrive.toOuttake());
        }
        if (gamepad2.dpad_left) { // bump the wrist position a bit
            runningActions.add(wristDrive.increment());
        }
        if (gamepad2.dpad_right) { // bump the wrist position a bit
            runningActions.add(wristDrive.decrement());
        }

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
