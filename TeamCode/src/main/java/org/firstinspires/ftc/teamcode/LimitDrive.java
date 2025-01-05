package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;
import java.util.List;

public class LimitDrive {

    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";
    protected final HardwareDevice limit;

    private String status;

    private List<LimitListener> listeners;

    public LimitDrive(HardwareDevice limit) {
        this.limit = limit;
        this.listeners = new ArrayList<>();
        this.status = getStatus();
    }
//hi
    public String getStatus() {
        if (limit instanceof DigitalChannel) {
            DigitalChannel digitalChannel = (DigitalChannel) limit;
            return digitalChannel.getState() ? ACTIVE : INACTIVE; //TODO - should these be reversed?
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
            String currentStatus = getStatus();

            // check if the hardware is now on.  If so send message to the listeners
            if (!currentStatus.equals(status)) {
                //the status has changed, so send the listeners
                listeners.forEach(listener -> listener.onLimit(currentStatus));
            }

            // Set the status to the current status
            status = currentStatus;

            return true; //run forever
        }
    }
}
