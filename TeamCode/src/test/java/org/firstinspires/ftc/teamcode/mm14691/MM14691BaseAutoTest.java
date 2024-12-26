package org.firstinspires.ftc.teamcode.mm14691;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MM14691BaseAutoTest extends MM14691AbstractTest {

    @Test()
    public void testMultipleLoops() {
        // set up mocks
        MM14691BaseAuto opMode = mock(MM14691BaseAuto.class, Answers.CALLS_REAL_METHODS);
        opMode.hardwareMap = hardwareMap; // inject the HardwareMap manually
        opMode.telemetry = telemetry; // inject the Telemetry manually
        opMode.gamepad2 = gamepad2;
        opMode.runtime = runtime;
        opMode.runningActions = new ArrayList<>();
        opMode.dash = dash;
        doReturn(new AngularVelocity()).when(imu).getRobotAngularVelocity(AngleUnit.DEGREES);
        doReturn(0).when(rightFront).getCurrentPosition();
        doReturn(0.0).when(rightFront).getVelocity();
        doReturn(0).when(leftFront).getCurrentPosition();
        doReturn(0.0).when(leftFront).getVelocity();
        doReturn(new YawPitchRollAngles(AngleUnit.DEGREES, 0, 0, 0, 0))
                .when(imu).getRobotYawPitchRollAngles();
        doReturn(new Pose2d(0,0,0)).when(opMode).getInitialPose();

        // prep for running the loops
        opMode.init(); //Run Init because it is needed before start
        opMode.viperDrive.setDebugEnabled(false);
        opMode.liftDrive.setDebugEnabled(false);
        opMode.start(); // Run start because it is needed before the loop
        int numStartActions = opMode.runningActions.size();
        SequentialAction sequentialAction = new SequentialAction( //add our sequential actions to run
                opMode.liftDrive.toPosition(300),
                opMode.viperDrive.toEnd(0.8)
        );
        opMode.runningActions.add(sequentialAction);
        reset(leftFront, leftBack, rightBack, rightFront, viper, lift, ascend, intake, wrist); //clear the mock state for things we may care about

        // Run the first loop and verify/assert
        when(lift.getPower()).thenReturn(0.8);
        when(lift.getCurrentPosition()).thenReturn(100);
        opMode.loop();
        Assert.assertEquals(numStartActions + 1, opMode.runningActions.size()); //we still have the sequential action
        verify(lift, times(1)).setPower(0.8);
        verify(viper, times(0)).setPower(anyDouble()); //viper action should not run until the lift is done

        // Run the second loop and verify/assert
        reset(leftFront, leftBack, rightBack, rightFront, viper, lift, ascend, intake, wrist); //clear the mock state for things we may care about
        when(lift.getPower()).thenReturn(0.8);
        when(lift.getCurrentPosition()).thenReturn(200); //motor advanced 100 ticks since the last loop
        opMode.loop();
        Assert.assertEquals(numStartActions + 1, opMode.runningActions.size()); //we still have the sequential action
        verify(lift, times(0)).setPower(anyDouble()); //lift should already be init/moving, should not be stopped
        verify(viper, times(0)).setPower(anyDouble()); //viper action should not run until the lift is done

        // Run the third loop and verify/assert
        reset(leftFront, leftBack, rightBack, rightFront, viper, lift, ascend, intake, wrist); //clear the mock state for things we may care about
        when(lift.getPower()).thenReturn(0.8);
        when(lift.getCurrentPosition()).thenReturn(301); //motor advanced 101 ticks since the last loop; this should cause it to be past the limit
        when(viper.getPower()).thenReturn(0.8); //we should run the next sequential action since the list should be stopped
        opMode.loop();
        Assert.assertEquals(numStartActions + 1, opMode.runningActions.size()); //we still have the sequential action
        verify(lift, times(1)).getPower(); //limit checking
        verify(lift, times(4)).getCurrentPosition(); //limit and toposition
        verify(lift, times(1)).setPower(0); //lift should be stopped
        verifyNoMoreInteractions(lift);
        verify(viper, times(1)).setPower(0.8); //viper action should not run until the lift is done

        // Run the fourth loop and verify/assert
        reset(leftFront, leftBack, rightBack, rightFront, viper, lift, ascend, intake, wrist); //clear the mock state for things we may care about
        when(viper.getPower()).thenReturn(0.8); //we should run the next sequential action since the list should be stopped
        opMode.loop();
        Assert.assertEquals(numStartActions + 1, opMode.runningActions.size()); //we still have the sequential action
        verify(lift, times(1)).getPower();
        verify(lift, times(2)).getCurrentPosition(); //limit and toposition
        verify(lift, times(0)).setPower(anyDouble()); //shouldn't be doing anything with the lift any more
        verifyNoMoreInteractions(lift);
        verify(viper, times(1)).setPower(0.8); //viper action should not run until the lift is done

    }
}
