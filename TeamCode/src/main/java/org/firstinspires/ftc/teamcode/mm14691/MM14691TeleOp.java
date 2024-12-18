package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.stream.Collectors;

@TeleOp
public class MM14691TeleOp extends MM14691BaseOpMode {

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();

        //This makes sure update() on the pinpoint driver is called in this loop
        pinpointDrive.updatePoseEstimate();

        // See if the driver wants to "slow down"
        double driverMultiplier = 1;
        if (gamepad1.left_bumper) {
            driverMultiplier = 0.5;
        }
        double driverMultiplier2 = 1;
        if(gamepad1.right_bumper){
            driverMultiplier2 = 0.25;
        }
        // Create actions for the Pinpoint Drive
        PoseVelocity2d drivePose = new PoseVelocity2d(
                new Vector2d(-gamepad1.left_stick_y * driverMultiplier*driverMultiplier2,
                        -gamepad1.left_stick_x * driverMultiplier*driverMultiplier2),
                -gamepad1.right_stick_x * driverMultiplier*driverMultiplier2);
        runningActions.add(new InstantAction(() -> setDrivePowers(drivePose)));
        telemetry.addData("Pinpoint Drive", "Active");

        // Create actions for the Viper
        telemetry.addData("Viper Stick", -gamepad2.right_stick_y);
        if (gamepad2.right_stick_y != 0) { // if the right stick is pushed
            runningActions.add(armDrive.setViperPower(-gamepad2.right_stick_y));
        }
        if (gamepad2.right_bumper) { //send to max extension
            runningActions.add(armDrive.viperToEnd());
        }
        if (gamepad2.right_trigger > 0) { //send to start limit
            runningActions.add(armDrive.viperToStart());
        }
//        if (gamepad1.x) {
//            runningActions.add
//        }

        // Create actions for the list arm
        if (gamepad2.left_stick_y != 0) {
            runningActions.add(armDrive.setLiftPower(-gamepad2.left_stick_y));
        }
        if (gamepad2.left_trigger > 0) {
            runningActions.add(armDrive.liftToDown());
        }
        if (gamepad2.left_bumper) {
            runningActions.add(armDrive.liftToUp());
        }

        // Create actions for the wrist
        if (gamepad2.a) { //Turn on the wheel for collection
            runningActions.add(armDrive.setIntakePower(ArmDrive.PARAMS.intakeCollect));
        } else if (gamepad2.b) { //Turn on the wheel for deposit
            runningActions.add(armDrive.setIntakePower(ArmDrive.PARAMS.intakeDeposit));
        } else {
            runningActions.add(armDrive.setIntakePower(ArmDrive.PARAMS.intakeOff));
        }
        if (gamepad2.dpad_left) { // position the wrist for intake
            runningActions.add(armDrive.sampleReady());
        }
        if (gamepad2.dpad_right){
            runningActions.add(armDrive.specimenReady());
        }

        // Create actions for the ascension arm
        if (gamepad2.dpad_up) {
            runningActions.add(armDrive.setAscensionPower(.8));
        } else if (gamepad2.dpad_down) {
            runningActions.add(armDrive.setAscensionPower(-.8));
        }else {
            runningActions.add(armDrive.setAscensionPower(0));
        }

        telemetry.addData("Arm Drive", "Active");

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
