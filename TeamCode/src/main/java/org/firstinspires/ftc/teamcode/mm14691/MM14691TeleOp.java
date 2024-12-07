package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MM14691TeleOp extends MM14691BaseOpMode {

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();

        //This makes sure update() on the pinpoint driver is called in this loop
        pinpointDrive.updatePoseEstimate();

        // See if the driver wants to "slow down"
        double driverMultiplier = 1;
        if (gamepad1.left_bumper) { //slow to half speed
            driverMultiplier = 0.5;
        }
        if(gamepad1.right_bumper){ //this order means that the 1/4 speed takes precedence
            driverMultiplier = 0.25;
        }
        // Create actions for the Pinpoint Drive
        PoseVelocity2d drivePose = new PoseVelocity2d(
                new Vector2d(-gamepad1.left_stick_y * driverMultiplier,
                        -gamepad1.left_stick_x * driverMultiplier),
                -gamepad1.right_stick_x * driverMultiplier);
        runningActions.add(new InstantAction(() -> setDrivePowers(drivePose)));

        // Create actions for the Viper
        runningActions.add(viperDrive.setPower(-gamepad2.right_stick_y));
        if (gamepad2.right_bumper) { //send to max extension
            runningActions.add(viperDrive.toEnd());
        }
        if (gamepad2.right_trigger > 0) { //send to start limit
            runningActions.add(viperDrive.toStart());
        }

        // Create actions for the list arm
        runningActions.add(liftDrive.setPower(-gamepad2.left_stick_y));
        if (gamepad2.left_trigger > 0) {
            runningActions.add(liftDrive.toDown());
        }
        if (gamepad2.left_bumper) {
            runningActions.add(liftDrive.toEnd());
        }

        // Create actions for the claws
        // Note, the intake actions should get added to the running actions before the wrist actions
        //    this will allow the wrist actions to run the crash protection.
        if (gamepad2.x) {
            runningActions.add(intakeDrive.toOpen());
        }
        if (gamepad2.y) {
            runningActions.add(intakeDrive.toClosed());
        }

        // Create actions for the wrist
        if (gamepad2.a) { //Turn on the wheel for collection
            runningActions.add(wristDrive.toIntake());
        }
        if (gamepad2.b) { //Turn on the wheel for deposit
            runningActions.add(wristDrive.toOuttake());
        }
        if (gamepad2.dpad_left) { // bump the wrist position a bit
            runningActions.add(wristDrive.increment());
        }
        if (gamepad2.dpad_right) { // bump the wrist position a bit
            runningActions.add(wristDrive.decrement());
        }

//        // Create actions for the ascension arm
        //TODO - renable when we have the ascension arm
//        if (gamepad2.dpad_up) {
//            runningActions.add(ascendDrive.setPower(.8));
//        } else if (gamepad2.dpad_down) {
//            runningActions.add(ascendDrive.setPower(-.8));
//        }else {
//            runningActions.add(ascendDrive.setPower(0));
//        }

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
