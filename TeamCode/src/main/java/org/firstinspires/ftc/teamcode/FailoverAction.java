package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

/**
 * Generalized Cancellation Action.
 *
 * Modeled after https://rr.brott.dev/docs/v1-0/guides/cancellation/
 */
public class FailoverAction implements Action {

    private final Action mainAction;
    private final Action failoverAction;
    private boolean failedOver = false;

    public FailoverAction(Action mainAction, Action failoverAction) {
        this.mainAction = mainAction;
        this.failoverAction = failoverAction;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (failedOver) {
            return failoverAction.run(telemetryPacket);
        }

        return mainAction.run(telemetryPacket);
    }

    public void failover() {
        failedOver = true;
    }
}
