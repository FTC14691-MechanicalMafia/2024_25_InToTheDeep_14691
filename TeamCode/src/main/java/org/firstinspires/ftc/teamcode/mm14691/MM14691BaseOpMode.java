package org.firstinspires.ftc.teamcode.mm14691;

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
    protected ArmDrive armDrive = null;
    // Time tracking
    protected ElapsedTime runtime = new ElapsedTime();
    private Pose2d initialPose = new Pose2d(0, 0, 0); //TODO: should we configure these?

    @Override
    public void init() {
        // Start our Pinpoint Enabled Mechanum Drive
        pinpointDrive = new PinpointDrive(hardwareMap, initialPose);
        telemetry.addData("Pinpoint Drive", "Initialized");

        // Start our Arm driver
        armDrive = new ArmDrive(hardwareMap, telemetry);
        telemetry.addData("Arm Drive", "Initialized");

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
        telemetry.addData("Pinpoint Drive", "Ready");

        //Add our debugging action
        runningActions.add(armDrive.getDebugAction());

        //Retract the viper arm to the limit switch
        runningActions.add(armDrive.viperToStart());

        //Move the lift arm to the 'down' position
//        runningActions.add(armDrive.liftToDown());

        //Fold the wrist in
        runningActions.add(armDrive.intakeReady());

        // Run our actions before we start the loop
        updateRunningActions(packet);
        telemetry.addData("Arm Drive", "Ready");

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
    }

    /**
     * Sets the drive powers based on the specified PoseVelocity (comes from the gamepad).
     * See Tuning.setDrivePowers.
     * TODO add the rotation back in
     *
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
        telemetry.addData("Arm Drive", "Stopping");

        // Refresh the driver screen
        telemetry.addData("Runtime", runtime.seconds());
        telemetry.update();
    }
}
