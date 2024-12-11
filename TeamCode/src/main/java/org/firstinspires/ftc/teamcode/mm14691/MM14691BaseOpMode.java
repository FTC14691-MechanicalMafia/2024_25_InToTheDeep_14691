package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.Time;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.PinpointDrive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class MM14691BaseOpMode extends OpMode {

    // See https://rr.brott.dev/docs/v1-0/guides/teleop-actions/ for documentation
    protected FtcDashboard dash = FtcDashboard.getInstance();
    protected List<Action> runningActions = new ArrayList<>();
    protected PinpointDrive pinpointDrive = null;
    protected WristDrive wristDrive = null;
    protected IntakeDrive intakeDrive = null;
    protected ViperDrive viperDrive = null;
    protected LiftDrive liftDrive = null;
    protected AscendDrive ascendDrive = null;
    // Time tracking
    protected ElapsedTime runtime = new ElapsedTime();

    public abstract Pose2d getInitialPose();

    @Override
    public void init() {
        // Start our Pinpoint Enabled Mechanum Drive
        pinpointDrive = new PinpointDrive(hardwareMap, getInitialPose());
        telemetry.addData("Pinpoint Drive", pinpointDrive.getStatus());

        // Start our Arm Drives
        viperDrive = new ViperDrive(hardwareMap, "armViper", gamepad2.right_stick_button);
        telemetry.addData("Viper Drive", viperDrive.getStatus());

        // TODO use the start limit from the last run
        liftDrive = new LiftDrive(hardwareMap, "armLift", gamepad2.left_stick_button);
        liftDrive.setViperDrive(viperDrive);
        telemetry.addData("Lift Drive", liftDrive.getStatus());

        ascendDrive = new AscendDrive(hardwareMap, "ascend");
        telemetry.addData("Ascend Drive", ascendDrive.getStatus());

        intakeDrive = new IntakeDrive(hardwareMap, "intake");
        telemetry.addData("Intake Drive", intakeDrive.getStatus());

        wristDrive = new WristDrive(hardwareMap, "wrist");
        wristDrive.setIntakeDrive(intakeDrive); // they interact with each other to prevent crashing
        telemetry.addData("Wrist Drive", wristDrive.getStatus());

        // Refresh the driver screen
        telemetry.update();
    }

    @Override
    public void start() {
        super.start();

        TelemetryPacket packet = new TelemetryPacket();

        // restarts runtime so the time starts when the play button is pushed
        runtime.reset();

        // Update the values from the poinpoint hardware
        pinpointDrive.updatePoseEstimate();
        telemetry.addData("Pinpoint Drive", pinpointDrive.getStatus());

        //Add our debugging action
        runningActions.add(new DebugAction());

        //Add the limits for the viper drive
        runningActions.add(viperDrive.limits());
        telemetry.addData("Viper Drive", "Ready");

        //Add the limit checks to the lift drive
        runningActions.add(liftDrive.limits());
        //Allow the lift drive to dynamically update the viper limit
        runningActions.add(liftDrive.adjustViperLimits());
        telemetry.addData("Lift Drive", "Ready");

        //Enforce the ascension limits
        // TODO - Readd when we have an ascension arm
//        runningActions.add(ascendDrive.limits());
//        telemetry.addData("Ascend Drive", "Ready");

        //Prepare the wrist for intake
        telemetry.addData("Wrist Drive", "Ready");

        // Start the intake if needed
        telemetry.addData("Intake Drive", "Ready");

        // Run our actions before we start the loop
        updateRunningActions(packet);

        // Refresh the driver screen
        telemetry.update();

        dash.sendTelemetryPacket(packet);
    }

    protected void updateRunningActions(TelemetryPacket packet) {
        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) {
                newActions.add(action);
            }
        }
        runningActions = newActions;

        // Update all the telemetries
        telemetry.addData("Pinpoint Drive", pinpointDrive.getStatus());
        telemetry.addData("Wrist Drive", wristDrive.getStatus());
        telemetry.addData("Viper Drive", viperDrive.getStatus());
        telemetry.addData("Lift Drive", liftDrive.getStatus());
        telemetry.addData("Ascend Drive", ascendDrive.getStatus());
        telemetry.addData("Intake Drive", intakeDrive.getStatus());
    }

    /**
     * Sets the drive powers based on the specified PoseVelocity (comes from the gamepad).
     * See Tuning.setDrivePowers.
     * @param powers
     */
    protected void setDrivePowers(PoseVelocity2d powers) {
        MecanumKinematics.WheelVelocities<Time> wheelPowers = pinpointDrive.kinematics.inverse(
                PoseVelocity2dDual.constant(powers, 1));
        Optional<DualNum<Time>> maxPowerMagOpt = wheelPowers.all().stream()
                .max((l, r) -> Double.compare(Math.abs(l.value()), Math.abs(r.value())));
        DualNum<Time> maxPowerMag = maxPowerMagOpt.orElse(new DualNum<>(Arrays.asList(Double.valueOf(0))));
        double divisor = Math.max(1.0, maxPowerMag.value());

        //sets power to motors
        pinpointDrive.leftFront.setPower(wheelPowers.leftFront.value() / divisor);
        pinpointDrive.rightFront.setPower(wheelPowers.rightFront.value() / divisor);
        pinpointDrive.leftBack.setPower(wheelPowers.leftBack.value() / divisor);
        pinpointDrive.rightBack.setPower(wheelPowers.rightBack.value() / divisor);
    }

    @Override
    public void stop() {
        super.stop();

        // Clear our running actions, just in case
        runningActions.clear();

        telemetry.addData("Pinpoint Drive", "Stopping");
        telemetry.addData("Wrist Drive", "Stopping");
        viperDrive.rememberStartTick();
        telemetry.addData("Viper Drive", "Stopping");
        telemetry.addData("Lift Drive", "Stopping");
        telemetry.addData("Ascend Drive", "Stopping");
        telemetry.addData("Intake Drive", "Stopping");

        // Refresh the driver screen
        telemetry.addData("Runtime", runtime.seconds());
        telemetry.update();
    }

    public class DebugAction implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            viperDrive.addDebug(telemetry);
            liftDrive.addDebug(telemetry);
            ascendDrive.addDebug(telemetry);
            wristDrive.addDebug(telemetry);
            intakeDrive.addDebug(telemetry);

            return true; // Always run this so we always emit debug info
        }

    }

}
