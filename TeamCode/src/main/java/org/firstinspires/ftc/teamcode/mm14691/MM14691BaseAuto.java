package org.firstinspires.ftc.teamcode.mm14691;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public abstract class MM14691BaseAuto extends MM14691BaseOpMode {

    private String runningAction = "";

    @Override
    public void loop() {
        TelemetryPacket packet = new TelemetryPacket();

        //This makes sure update() on the pinpoint driver is called in this loop
        pinpointDrive.updatePoseEstimate();

        updateRunningActions(packet);

        telemetry.addData("AutoAction", runningAction);
        telemetry.update();

        dash.sendTelemetryPacket(packet);
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
            runningAction = this.name;
            return false; // we don't want this to continue running
        }
    }

    public AutoActionName autoActionName(String name) {
        return new AutoActionName(name);
    }
}
