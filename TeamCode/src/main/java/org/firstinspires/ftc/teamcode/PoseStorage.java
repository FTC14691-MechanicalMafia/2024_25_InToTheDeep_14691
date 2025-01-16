package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;

/**
 * Store the initial pose between auto and teleop.
 *
 * See https://learnroadrunner.com/advanced.html#using-road-runner-in-teleop
 */
public class PoseStorage {
    // See this static keyword? That's what lets us share the data between opmodes.
    public static Pose2d currentPose = null;
}
