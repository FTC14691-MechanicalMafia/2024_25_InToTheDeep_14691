package org.firstinspires.ftc.teamcode.mm14691;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.MotorActions;
import org.junit.Assert;
import org.junit.Test;

public class MotorActionsTest {

    @Test
    public void testSetPower_StartLimit_posPower() {
        // set up test data
        final double power = 0.8;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(19); // This is under/past the start position
        MotorActions motorActions = new MotorActions(dcMotorEx, 64, 1964);

        // run method under tests
        MotorActions.SetPower motorPower = motorActions.setPower(power);
        final boolean result = motorPower.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(power); // motor direction is fine
        verify(dcMotorEx, times(0)).setPower(0); // correcting the limit, so don't power down

        // assertions
        Assert.assertFalse(result);
    }

    @Test
    public void testSetPower_StartLimit_negPower() {
        // set up test data
        final double power = -0.8;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(50); // This is under/past the start position
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.SetPower motorPower = motorActions.setPower(power);
        final boolean result = motorPower.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0); // should have set the power to 0 because we are at the limit

        // assertions
        Assert.assertFalse(result);
    }


    @Test
    public void testSetPower_EndLimit_posPower() {
        // set up test data
        final double power = 0.8;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(1050); // This is over/past the end position
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.SetPower motorPower = motorActions.setPower(power);
        final boolean result = motorPower.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0); // should have set the power to 0 because we are at the limit

        // assertions
        Assert.assertFalse(result);
    }




    @Test
    public void testSetPower_EndLimit_negPower() {
        // set up test data
        final double power = -0.8;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(1050); // This is over/past the end position
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.SetPower motorPower = motorActions.setPower(power);
        final boolean result = motorPower.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(power); // motor direction is fine
        verify(dcMotorEx, times(0)).setPower(0); // correcting the limit, so don't power down

        // assertions
        Assert.assertFalse(result);
    }

    @Test
    public void testToStart() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(200);
        when(dcMotorEx.getPower()).thenReturn(-0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToStart toStart = motorActions.toStart();
        final boolean result = toStart.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(anyDouble()); // want to make sure the power was set once
        verify(dcMotorEx, times(1)).getPower();

        // assertions
        Assert.assertTrue(result); // first init, so should return true to keep running
    }


    @Test
    public void testToStart_overridden() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(200);
        when(dcMotorEx.getPower()).thenReturn(-0.8) // for the init run
            .thenReturn(-0.6); // some other command updated the motor power
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToStart toStart = motorActions.toStart();
        final boolean initResult = toStart.run(packet); // run once to init
        Assert.assertTrue(initResult); // should have returned true
        final boolean result = toStart.run(packet); // the second call is the one we care about

        // verifications
        verify(dcMotorEx, times(2)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(anyDouble()); // want to make sure the power was set once (on the first call)
        verify(dcMotorEx, times(2)).getPower();

        // assertions
        Assert.assertFalse(result); // first init, so should return true to keep running
    }

    @Test
    public void testToStart_pastLimit() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(50);
        when(dcMotorEx.getPower()).thenReturn(-0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToStart toStart = motorActions.toStart();
        final boolean result = toStart.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0); // past the limit, should stop the motor
        verify(dcMotorEx, times(0)).getPower(); //never made it this far

        // assertions
        Assert.assertFalse(result); // past the limit, should return false
    }

    @Test
    public void testToEnd() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(900);
        when(dcMotorEx.getPower()).thenReturn(0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToEnd toEnd = motorActions.toEnd();
        final boolean result = toEnd.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0.8); // want to make sure the power was set once
        verify(dcMotorEx, times(1)).getPower();

        // assertions
        Assert.assertTrue(result); // first init, so should return true to keep running
    }

    @Test
    public void testToEnd_overridden() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(900);
        when(dcMotorEx.getPower()).thenReturn(0.8) // for the init run
                .thenReturn(-0.6); // some other command updated the motor power
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToEnd toEnd = motorActions.toEnd();
        final boolean initResult = toEnd.run(packet); // run once to init
        Assert.assertTrue(initResult); // should have returned true
        final boolean result = toEnd.run(packet); // the second call is the one we care about

        // verifications
        verify(dcMotorEx, times(2)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(anyDouble()); // want to make sure the power was set once (on the first call)
        verify(dcMotorEx, times(2)).getPower();

        // assertions
        Assert.assertFalse(result); // first init, so should return true to keep running
    }

    @Test
    public void testToEnd_pastLimit() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(1050);
        when(dcMotorEx.getPower()).thenReturn(0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToEnd toEnd = motorActions.toEnd();
        final boolean result = toEnd.run(packet);

        // verifications
        verify(dcMotorEx, times(1)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0); // past the limit, should stop the motor
        verify(dcMotorEx, times(0)).getPower(); //never made it this far

        // assertions
        Assert.assertFalse(result); // past the limit, should return false
    }

    @Test
    public void testToPosition_below() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(250);
        when(dcMotorEx.getPower()).thenReturn(0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToPosition toPosition = motorActions.toPosition(500);
        final boolean result = toPosition.run(packet);

        // verifications
        verify(dcMotorEx, times(2)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0.8); // want to make sure the power was set once
        verify(dcMotorEx, times(1)).getPower();

        // assertions
        Assert.assertTrue(result); // first init, so should return true to keep running
    }

    @Test
    public void testToPosition_belowThenPast() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(250)
                .thenReturn(550);
        when(dcMotorEx.getPower()).thenReturn(0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToPosition toPosition = motorActions.toPosition(500);
        final boolean initResult = toPosition.run(packet);
        Assert.assertTrue(initResult);
        final boolean result = toPosition.run(packet);

        // verifications
        verify(dcMotorEx, times(4)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0.8); // want to make sure the power was set once
        verify(dcMotorEx, times(1)).setPower(0);
        verify(dcMotorEx, times(1)).getPower();

        // assertions
        Assert.assertFalse(result); // first init, so should return true to keep running
    }

    @Test
    public void testToPosition_above() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(750);
        when(dcMotorEx.getPower()).thenReturn(-0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToPosition toPosition = motorActions.toPosition(500);
        final boolean result = toPosition.run(packet);

        // verifications
        verify(dcMotorEx, times(2)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(-0.8); // want to make sure the power was set once
        verify(dcMotorEx, times(1)).getPower();

        // assertions
        Assert.assertTrue(result); // first init, so should return true to keep running
    }

    @Test
    public void testToPosition_aboveThenPast() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(750)
                .thenReturn(750)
                .thenReturn(250)
                .thenReturn(250);
        when(dcMotorEx.getPower()).thenReturn(-0.8);
        MotorActions motorActions = new MotorActions(dcMotorEx, 100, 1000);

        // run method under tests
        MotorActions.ToPosition toPosition = motorActions.toPosition(500);
        final boolean initResult = toPosition.run(packet);
        Assert.assertTrue(initResult);
        final boolean result = toPosition.run(packet);

        // verifications
        verify(dcMotorEx, times(4)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(-0.8); // want to make sure the power was set once
        verify(dcMotorEx, times(1)).setPower(0);
        verify(dcMotorEx, times(1)).getPower();

        // assertions
        Assert.assertFalse(result); // first init, so should return true to keep running
    }


}
