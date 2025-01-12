package org.firstinspires.ftc.teamcode.mm14691;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class MM14691BaseAutoTest extends MM14691AbstractTest {

    @Test()
    public void testLoop() {
        // set up mocks
        MM14691BaseAuto opMode = mock(MM14691BaseAuto.class, Answers.CALLS_REAL_METHODS);
        opMode.hardwareMap = hardwareMap; // inject the HardwareMap manually
        opMode.telemetry = telemetry; // inject the Telemetry manually
        opMode.gamepad2 = gamepad2;
        opMode.runtime = runtime;
        opMode.runningActions = new ArrayList<>();
        opMode.dash = dash;
        when(imu.getRobotAngularVelocity(AngleUnit.DEGREES)).thenReturn(new AngularVelocity());
        when(imu.getRobotYawPitchRollAngles()).thenReturn(new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0, 0));
        opMode.init(); //sets a bunch of stuff up for us
        opMode.mecanumDrive = spy(opMode.mecanumDrive); //convert to a spy
        opMode.mecanumDrive.pose = new Pose2d(0,0,0);
        reset(telemetry);

        // call method under test
        opMode.loop();

        // verifications
        verify(opMode.mecanumDrive, times(1)).updatePoseEstimate();
//        verify(telemetry, times(1)).addData("AutoAction",null); //empty since we didn't set it
        verify(telemetry, times(6)).addData(matches(".*Drive"), anyString());
        verify(telemetry, times(1)).update();
        verify(dash, times(1)).sendTelemetryPacket(any());

        //assertions
    }
}
