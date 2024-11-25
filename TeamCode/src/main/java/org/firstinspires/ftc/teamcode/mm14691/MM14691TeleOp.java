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
        telemetry.addData("Pinpoint Drive", "Active");

        // Create actions for the Viper
        telemetry.addData("Viper Stick", -gamepad2.right_stick_y);
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

        // Create actions for the wrist
        if (gamepad2.a) { //Turn on the wheel for collection
            runningActions.add(wristDrive.setIntakePower(WristDrive.PARAMS.intakeCollect));
        } else if (gamepad2.b) { //Turn on the wheel for deposit
            runningActions.add(wristDrive.setIntakePower(WristDrive.PARAMS.intakeDeposit));
        } else {
            runningActions.add(wristDrive.setIntakePower(WristDrive.PARAMS.intakeOff));
        }
        if (gamepad2.dpad_left) { // position the wrist for intake
            runningActions.add(wristDrive.sampleReady());
        }
        if (gamepad2.dpad_right){
            runningActions.add(wristDrive.specimenReady());
        }

        // Create actions for the ascension arm
        if (gamepad2.dpad_up) {
            runningActions.add(ascendDrive.setPower(.8));
        } else if (gamepad2.dpad_down) {
            runningActions.add(ascendDrive.setPower(-.8));
        }else {
            runningActions.add(ascendDrive.setPower(0));
        }

        // Add some debug about the actions we are about to run.
        telemetry.addData("Running Actions", runningActions.stream()
                .map(action -> action.getClass().getSimpleName())
                        .filter(action -> !action.toLowerCase().contains("debug"))
                .reduce("", (sub, ele) -> sub + ", " + ele));

        // update running actions
        updateRunningActions(packet);

        // Refresh the driver screen
        telemetry.addData("Runtime", runtime.seconds());
        telemetry.update();

        dash.sendTelemetryPacket(packet);
    }

    @Override
    public Pose2d getInitialPose() {
        return new Pose2d(0, 0, 0); // this does not matter for teleop unless we start using paths
    }
}
