package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;
import java.util.List;

public class LimitDrive {

    public enum Status { ACTIVE, INACTIVE }

    protected final HardwareDevice limit;

    private Status status;

    private List<LimitListener> listeners;

    public LimitDrive(HardwareDevice limit) {
        this.limit = limit;
        this.listeners = new ArrayList<>();
        this.status = getStatus();
    }

    public Status getStatus() {
        if (limit instanceof DigitalChannel) {
            DigitalChannel digitalChannel = (DigitalChannel) limit;
            return digitalChannel.getState() ? Status.INACTIVE : Status.ACTIVE;
        }

        throw new IllegalStateException("Unsupported Limit Device:" + limit.getClass().getName());
    }

    public void addListener(LimitListener limitListener) {
        listeners.add(limitListener);
    }

    public Action watchLimit() {
        return new WatchLimit();
    }

    /**
     * Monitor the limit status
     */
    public class WatchLimit implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            // get our current status
            Status currentStatus = getStatus();

            // check if the hardware is now on.  If so send message to the listeners
            if (currentStatus != status) {
                //the status has changed, so send the listeners
                listeners.forEach(listener -> listener.onLimit(currentStatus));
            }

            // Set the status to the current status
            status = currentStatus;

            return true; //run forever
        }
    }
}
