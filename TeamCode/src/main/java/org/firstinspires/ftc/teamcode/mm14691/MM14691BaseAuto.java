package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.PoseStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MM14691BaseAuto extends MM14691BaseOpMode {

    private static final Logger LOG = LoggerFactory.getLogger(MM14691BaseAuto.class);

    private String runningAction = "";

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();

        //This makes sure update() on the pinpoint driver is called in this loop
        mecanumDrive.updatePoseEstimate();

        updateRunningActions(packet);

        telemetry.addData("AutoAction", runningAction);
        telemetry.update();

        dash.sendTelemetryPacket(packet);
    }

    @Override
    public void stop() {
        super.stop();

        // Store the
        mecanumDrive.updatePoseEstimate(); //TODO - confirm that the stop method is called when the 30 sec timer runs out
        PoseStorage.currentPose = mecanumDrive.pose;

        LOG.info("Stop: complete");
    }

    /**
     * This is for a readout on the driver station about which part of the auto mode we are currently running.
     * It basically just records whatever name to the screen
     */
    public class AutoActionName implements Action {
        private String name;

        public AutoActionName(String name) {
            this.name = name;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            LOG.info("Starting Auto Action: {}", this.name);
            runningAction = this.name;
            return false; // we don't want this to continue running
        }
    }

    public AutoActionName autoActionName(String name) {
        return new AutoActionName(name);
    }
}
