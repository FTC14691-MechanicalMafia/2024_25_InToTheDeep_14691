package org.firstinspires.ftc.teamcode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.Servo;

import org.junit.Assert;
import org.junit.Test;

public class ServoDriveTest {

    @Test
    public void testConstructor_rangeValidation() {
        // set up test data

        // set up mocks
        Servo servo = mock(Servo.class);

        // run method under tests
        try {
            ServoDrive servoDrive = new ServoDrive(servo, 1.0, -1.0) {};
        } catch (IllegalArgumentException e) {
            // verifications
            verifyNoInteractions(servo);
            return; // this is what we expected
        }

        // assertions
        Assert.fail("Should have had an exception thrown");
    }

    @Test
    public void testToPosition() {
        // set up test data
        final double position = 0.8;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        Servo servo = mock(Servo.class);
        ServoDrive servoDrive = new ServoDrive(servo, -1.0, 1.0) {};

        // run method under tests
        ServoDrive.ToPosition motorPower = servoDrive.toPosition(position);
        final boolean result = motorPower.run(packet);

        // verifications
        verify(servo, times(1)).setPosition(position); // set our position
        verifyNoMoreInteractions(servo);

        // assertions
        Assert.assertFalse(result);
    }

    @Test
    public void testToStart() {
        // set up test data
        final double startPosition = -1.0;
        final double endPosition = 1.0;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        Servo servo = mock(Servo.class);
        ServoDrive servoDrive = new ServoDrive(servo, startPosition, endPosition) {};

        // run method under tests
        ServoDrive.ToPosition motorPower = servoDrive.toStart();
        final boolean result = motorPower.run(packet);

        // verifications
        verify(servo, times(1)).setPosition(startPosition); // set our position
        verifyNoMoreInteractions(servo);

        // assertions
        Assert.assertFalse(result);
    }

    @Test
    public void testToEnd() {
        // set up test data
        final double startPosition = -1.0;
        final double endPosition = 1.0;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        Servo servo = mock(Servo.class);
        ServoDrive servoDrive = new ServoDrive(servo, startPosition, endPosition) {};

        // run method under tests
        ServoDrive.ToPosition motorPower = servoDrive.toEnd();
        final boolean result = motorPower.run(packet);

        // verifications
        verify(servo, times(1)).setPosition(endPosition); // set our position
        verifyNoMoreInteractions(servo);

        // assertions
        Assert.assertFalse(result);
    }

    @Test
    public void testIncrement() {
        // set up test data
        final double startPosition = -1.0;
        final double endPosition = 1.0;
        final double increment = 0.15;
        final double currentPosition = 0.0;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        Servo servo = mock(Servo.class);
        ServoDrive servoDrive = new ServoDrive(servo, startPosition, endPosition) {};
        servoDrive.setIncrement(increment);
        when(servo.getPosition()).thenReturn(currentPosition);

        // run method under tests
        ServoDrive.ToPosition motorPower = servoDrive.increment();
        final boolean result = motorPower.run(packet);

        // verifications
//        verify(ser)
        verify(servo, times(1)).setPosition(endPosition); // set our position
        verifyNoMoreInteractions(servo);

        // assertions
        Assert.assertFalse(result);
    }



}
