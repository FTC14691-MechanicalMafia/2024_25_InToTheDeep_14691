package org.firstinspires.ftc.teamcode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MotorDriveTest {

    @Test
    public void testSetPower_StartLimit_posPower() {
        // set up test data
        final double power = 0.8;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(-875); // This is under/past the start position
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, -1058, 842) {};

        // run method under tests
        MotorDrive.SetPower motorPower = motorDrive.setPower(power);
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};

        // run method under tests
        MotorDrive.SetPower motorPower = motorDrive.setPower(power);
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};

        // run method under tests
        MotorDrive.SetPower motorPower = motorDrive.setPower(power);
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};

        // run method under tests
        MotorDrive.SetPower motorPower = motorDrive.setPower(power);
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(200);
        when(dcMotorEx.getPower()).thenReturn(-0.8);


        // run method under tests
        MotorDrive.ToStart toStart = motorDrive.toStart();
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(200);
        when(dcMotorEx.getPower()).thenReturn(-0.8) // for the init run
            .thenReturn(-0.6); // some other command updated the motor power

        // run method under tests
        MotorDrive.ToStart toStart = motorDrive.toStart();
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(50);
        when(dcMotorEx.getPower()).thenReturn(-0.8);

        // run method under tests
        MotorDrive.ToStart toStart = motorDrive.toStart();
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(900);
        when(dcMotorEx.getPower()).thenReturn(0.8);

        // run method under tests
        MotorDrive.ToEnd toEnd = motorDrive.toEnd();
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(900);
        when(dcMotorEx.getPower()).thenReturn(0.8) // for the init run
                .thenReturn(-0.6); // some other command updated the motor power

        // run method under tests
        MotorDrive.ToEnd toEnd = motorDrive.toEnd();
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(1050);
        when(dcMotorEx.getPower()).thenReturn(0.8);

        // run method under tests
        MotorDrive.ToEnd toEnd = motorDrive.toEnd();
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(250);
        when(dcMotorEx.getPower()).thenReturn(0.8);

        // run method under tests
        MotorDrive.ToPosition toPosition = motorDrive.toPosition(500);
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
        when(dcMotorEx.getCurrentPosition()).thenReturn(250);
        when(dcMotorEx.getPower()).thenReturn(0.8);
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};

        // run method under tests
        MotorDrive.ToPosition toPosition = motorDrive.toPosition(400);
        final boolean initResult = toPosition.run(packet);
        verify(dcMotorEx, times(2)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0.8); // want to make sure the power was set once
        reset(dcMotorEx);
        when(dcMotorEx.getCurrentPosition()).thenReturn(550);
        Assert.assertTrue(initResult);
        final boolean result = toPosition.run(packet);

        // verifications
        verify(dcMotorEx, times(2)).getCurrentPosition();
        verify(dcMotorEx, times(1)).setPower(0);
        verify(dcMotorEx, times(0)).getPower();

        // assertions
        Assert.assertFalse(result); // first init, so should return true to keep running
    }

    @Test
    public void testToPosition_above() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(750);
        when(dcMotorEx.getPower()).thenReturn(-0.8);

        // run method under tests
        MotorDrive.ToPosition toPosition = motorDrive.toPosition(500);
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
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};
        reset(dcMotorEx); //since get power is called in the constructor
        when(dcMotorEx.getCurrentPosition()).thenReturn(750)
                .thenReturn(750)
                .thenReturn(250)
                .thenReturn(250);
        when(dcMotorEx.getPower()).thenReturn(-0.8);

        // run method under tests
        MotorDrive.ToPosition toPosition = motorDrive.toPosition(500);
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

    @Test
    public void testToStart_keepRunning() {
        // set up test data
        final TelemetryPacket packet = new TelemetryPacket(false);
        final List<Action> runningActions = new ArrayList<>();

        // set up mocks
        DcMotorEx dcMotorEx = mock(DcMotorEx.class);
        when(dcMotorEx.getCurrentPosition()).thenReturn(900);
        when(dcMotorEx.getPower()).thenReturn(-0.8);
        MotorDrive motorDrive = new MotorDrive(dcMotorEx, 100, 1000) {};

        // run method under tests
        /// Do the first loop
        MotorDrive.SetPower setPower = motorDrive.setPower(0); //the gamepad stick is not active, but the 0 is still set
        MotorDrive.ToStart toStart = motorDrive.toStart(); //the 'tostart' button is pushed
        Assert.assertFalse(setPower.run(packet)); //power should be set to zero and make sure the power is actually set
        verify(dcMotorEx, times(1)).setPower(0);
        Assert.assertTrue(toStart.run(packet)); //power should be set to -0.8 and the result should be true to continue to run
        verify(dcMotorEx, times(1)).setPower(-0.8);

        /// Do the second loop; The toStart is still in the 'running actions'
        reset(dcMotorEx); // reset the mock between loops so we can check the instance call count for this loop
        when(dcMotorEx.getCurrentPosition()).thenReturn(800);  //we've moved towards the start
        when(dcMotorEx.getPower()).thenReturn(-0.8); // this is what the current power is
        setPower = motorDrive.setPower(0); //the gamepad stick is not active, but the 0 is still set
        Assert.assertTrue(toStart.run(packet));
        verify(dcMotorEx, times(0)).setPower(anyDouble()); // should not have any power set since we are still moving towards the start
        Assert.assertFalse(setPower.run(packet)); //power should not be set to zero since the stick has not changed
        verify(dcMotorEx, times(0)).setPower(0);
    }


}
