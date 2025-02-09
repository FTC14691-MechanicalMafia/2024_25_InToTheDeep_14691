package org.firstinspires.ftc.teamcode.mm14691;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FailoverAction;
import org.firstinspires.ftc.teamcode.PoseStorage;

@TeleOp
public class MM14691TeleOp extends MM14691BaseOpMode {

    // We can only have one 'active' action affecting the drive motors.  Keep track of it.
    //  Null when there is no 'active' auto drive action (manual drive only)
    protected FailoverAction autoDrive = null;

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();

        //This makes sure update() on the pinpoint driver is called in this loop
        mecanumDrive.updatePoseEstimate();

        // See if the driver wants to "slow down"
        double driverMultiplier = 1;
        if (gamepad1.left_bumper) { //slow to half speed
            driverMultiplier = 0.5;
        }
        if (gamepad1.right_bumper) { //this order means that the 1/4 speed takes precedence
            driverMultiplier = 0.25;
        }
        // Create actions for the Pinpoint Drive
        if (autoDrive == null) { // only do the stick control when we aren't running an 'auto' control
            PoseVelocity2d drivePose = new PoseVelocity2d(
                    new Vector2d(-gamepad1.left_stick_y * driverMultiplier,
                            -gamepad1.left_stick_x * driverMultiplier),
                    -gamepad1.right_stick_x * driverMultiplier);
            runningActions.add(new InstantAction(() -> mecanumDrive.setDrivePowers(drivePose)));
        }

        // Create actions for the Viper
        double viperMultiplier = gamepad2.right_stick_button ? 0.5 : 1;
        runningActions.add(viperDrive.setPower(-gamepad2.right_stick_y * viperMultiplier));
        if (gamepad2.right_bumper) { //send to max extension
            runningActions.add(viperDrive.toEnd(0.8));
        }
        if (gamepad2.right_trigger > 0) { //send to start limit
            runningActions.add(viperDrive.toStart(0.8));
        }

        // Create actions for the lift arm
        double liftMultiplier = gamepad2.left_stick_button ? 0.5 : 1;
        runningActions.add(liftDrive.setPower(-gamepad2.left_stick_y * liftMultiplier));
        if (gamepad2.left_trigger > 0) {
            runningActions.add(liftDrive.toDown());
        }
        if (gamepad2.left_bumper) {
            runningActions.add(liftDrive.toEnd(0.8));
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

        // "Auto Actions" section

        // Take us to the basket
        if (gamepad1.dpad_up) {
            if (autoDrive == null) {
                //Create the path and do the heading
                autoDrive = new FailoverAction(
                        mecanumDrive.actionBuilder(mecanumDrive.pose)
                                .strafeToLinearHeading(new Vector2d(-54, -48), Math.toRadians(225))
                                .build(),
                        new InstantAction(() -> mecanumDrive.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), 0)))
                );
                runningActions.add(autoDrive);
            } else {
                //we are already in an auto mode.  Pushing the button again should cancel the mode
                autoDrive.failover();
                autoDrive = null; // reset the autoDrive
            }
        }

        // Prepare to drop in the basket
        if (gamepad2.dpad_up) {
//            runningActions.add(
//                    new SequentialAction(
//                            liftDrive.toPosition(2000, 0.9),
//                            viperDrive.toPosition(ViperDrive.PARAMS.liftUpLimit, 0.9),
//                            wristDrive.toOuttake()
//                    )
//            );
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

        //Send the robot position to the dashboard
        dash.sendTelemetryPacket(packet);
    }

    @Override
    public Pose2d getInitialPose() {
        if (PoseStorage.currentPose == null) {
            telemetry.addData("WARN", "Empty Initial Pose");
            return new Pose2d(0, 0, 0); // We don't know were we are
        } else {
            return PoseStorage.currentPose; //use the position from the last auto op
        }
    }
}
