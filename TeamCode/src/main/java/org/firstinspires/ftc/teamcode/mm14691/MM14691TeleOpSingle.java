package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MM14691TeleOpSingle extends MM14691BaseOpMode {

    double driverMultiplier = 1;

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();

        // TODO - add intermittent viper limit defeat

        //This makes sure update() on the pinpoint driver is called in this loop
        mecanumDrive.updatePoseEstimate();

        // See if the driver wants to "slow down"
        if (gamepad1.dpad_down) {
            if (driverMultiplier == 1) { //slow to quarter speed
                driverMultiplier = 0.25;
            } else {
                driverMultiplier = 1; //return to full speed
            }
        }

        // Create actions for the Pinpoint Drive
        PoseVelocity2d drivePose = new PoseVelocity2d(
                new Vector2d(-gamepad1.left_stick_y * driverMultiplier,
                        -gamepad1.left_stick_x * driverMultiplier),
                -gamepad1.right_stick_x * driverMultiplier); //TODO - fix the spin multiplier / slowdown
        runningActions.add(new InstantAction(() -> setDrivePowers(drivePose)));

        // Create actions for the Viper
        if (gamepad1.right_trigger > 0) {
            runningActions.add(viperDrive.setPower(-0.8));
        } else if (gamepad1.left_trigger > 0) {
            runningActions.add(viperDrive.setPower(0.8));
        } else if (gamepad1.left_stick_button) {
            runningActions.add(viperDrive.toStart());
        } else {
            runningActions.add(viperDrive.setPower(0)); // stop the viper
        }

        // Create actions for the lift arm
        if (gamepad1.right_bumper) {
            runningActions.add(liftDrive.setPower(-0.8));
        } else if (gamepad1.left_bumper) {
            runningActions.add(liftDrive.setPower(0.8));
        } else if (gamepad1.right_stick_button) {
            runningActions.add(liftDrive.toPosition(LiftDrive.PARAMS.ninetyTicks));
        } else {
            runningActions.add(liftDrive.setPower(0)); // stop the lift
        }

        // Create actions for the claws
        // Note, the intake actions should get added to the running actions before the wrist actions
        //    this will allow the wrist actions to run the crash protection.
        if (gamepad1.a) {
            runningActions.add(intakeDrive.toOpen());
        }
        if (gamepad1.b) {
            runningActions.add(intakeDrive.toClosed());
        }

        // Create actions for the wrist
        if (gamepad1.x) { //Turn on the wheel for collection
            runningActions.add(wristDrive.toIntake());
        }
        if (gamepad1.y) { //Turn on the wheel for deposit
            runningActions.add(wristDrive.toOuttake());
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
