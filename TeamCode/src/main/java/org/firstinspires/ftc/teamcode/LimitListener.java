package org.firstinspires.ftc.teamcode;

/**
 * Listener pattern for when a limit switch is activated.
 */
public interface LimitListener {
    /**
     * Called when the limit status changes
     * @param status
     */
    public void onLimit(String status);
}
