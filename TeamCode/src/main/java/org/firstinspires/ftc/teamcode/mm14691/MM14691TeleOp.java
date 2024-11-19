package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.InstantAction;
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

        // Create actions for the Pinpoint Drive
        PoseVelocity2d drivePose = new PoseVelocity2d(
                new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x),
                -gamepad1.right_stick_x);
        runningActions.add(new InstantAction(() -> setDrivePowers(drivePose)));
        telemetry.addData("Pinpoint Drive", "Active");

        // Create actions for the Viper
        // Once a viper button is pushed, only run that until completion.  This checks if
        // there is already a button based action in the running actions list.
        if (runningActions.stream().noneMatch(action -> action instanceof ArmDrive.ViperToEnd) &&
                runningActions.stream().noneMatch(action -> action instanceof ArmDrive.ViperToStart)) {
            runningActions.add(armDrive.setViperPower(gamepad2.right_stick_y));
            if (ArmDrive.PARAMS.debugOn) {
                telemetry.addData("DEBUG: VIPER POWER", gamepad2.right_stick_y);
            }

            if (gamepad2.right_bumper) { //send to max extension
                runningActions.add(armDrive.viperToEnd());
                // clear the ViperPower so it doesn't conflict
                runningActions = runningActions.stream().filter(
                        action -> !(action instanceof ArmDrive.ViperPower)
                ).collect(Collectors.toList());
            }
            if (gamepad2.right_trigger > 0) { //send to start limit
                runningActions.add(armDrive.viperToStart());
                // clear the ViperPower so it doesn't conflict
                runningActions = runningActions.stream().filter(
                        action -> !(action instanceof ArmDrive.ViperPower)
                ).collect(Collectors.toList());
            }
        }

        // Create actions for the list arm
        // Once a lift button is pushed, only run that until completion.  This checks if
        // there is already a button based action in the running actions list.
        if (runningActions.stream().noneMatch(action -> action instanceof ArmDrive.LiftToDown)) {
            runningActions.add(armDrive.setLiftPower(gamepad2.left_stick_y));
            if (ArmDrive.PARAMS.debugOn) {
                telemetry.addData("DEBUG: LIFT POWER", gamepad2.left_stick_y);
            }

            // TODO - Debug and reenable this
//            if (gamepad2.left_trigger > 0) {
//                runningActions.add(armDrive.liftToDown());
//                // clear the ViperPower so it doesn't conflict
//                runningActions = runningActions.stream().filter(
//                        action -> !(action instanceof ArmDrive.LiftPower)
//                ).collect(Collectors.toList());
//            }
        }

        // Create actions for the wrist
        if (gamepad2.a) { //Turn on the wheel for collection
            runningActions.add(armDrive.setIntakePower(ArmDrive.PARAMS.intakeCollect));
        } else

        if (gamepad2.b) { //Turn on the wheel for deposit
            runningActions.add(armDrive.setIntakePower(ArmDrive.PARAMS.intakeDeposit));
        } else {
            runningActions.add(armDrive.setIntakePower(ArmDrive.PARAMS.intakeOff));
        }
        if (gamepad2.dpad_left) { // position the wrist for intake
            runningActions.add(armDrive.intakeReady());
            runningActions.add(armDrive.intakeReady());
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
                .reduce("", (sub, ele) -> sub + "," + ele));

        // update running actions
        updateRunningActions(packet);

        // Refresh the driver screen
        telemetry.addData("Runtime", runtime.seconds());
        telemetry.update();

        dash.sendTelemetryPacket(packet);
    }

}
