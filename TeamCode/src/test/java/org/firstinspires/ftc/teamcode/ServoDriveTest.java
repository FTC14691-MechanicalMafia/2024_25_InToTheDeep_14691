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
        final double currentPosition = 0.0;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        Servo servo = mock(Servo.class);
        ServoDrive servoDrive = new ServoDrive(servo, -1.0, 1.0) {};
        when(servo.getPosition()).thenReturn(currentPosition);

        // run method under tests
        ServoDrive.ToPosition toPosition = servoDrive.toPosition(position);
        final boolean result = toPosition.run(packet);

        // verifications
        verify(servo, times(1)).getPosition();
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
        final double currentPosition = 0.0;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        Servo servo = mock(Servo.class);
        ServoDrive servoDrive = new ServoDrive(servo, startPosition, endPosition) {};
        when(servo.getPosition()).thenReturn(currentPosition);

        // run method under tests
        ServoDrive.ToPosition toPosition = servoDrive.toStart();
        final boolean result = toPosition.run(packet);

        // verifications
        verify(servo, times(1)).getPosition();
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
        final double currentPosition = 0.0;
        final TelemetryPacket packet = new TelemetryPacket(false);

        // set up mocks
        Servo servo = mock(Servo.class);
        ServoDrive servoDrive = new ServoDrive(servo, startPosition, endPosition) {};
        when(servo.getPosition()).thenReturn(currentPosition);

        // run method under tests
        ServoDrive.ToPosition toPosition = servoDrive.toEnd();
        final boolean result = toPosition.run(packet);

        // verifications
        verify(servo, times(1)).getPosition();
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
        ServoDrive.ToPosition toPosition = servoDrive.increment();
        final boolean result = toPosition.run(packet);

        // verifications
        verify(servo, times(2)).getPosition(); // for the sanity check and the calc for the incremented position
        verify(servo, times(1)).setPosition(currentPosition + increment); // set our position
        verifyNoMoreInteractions(servo);

        // assertions
        Assert.assertFalse(result);
    }

    @Test
    public void testDecrement() {
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
        ServoDrive.ToPosition toPosition = servoDrive.decrement();
        final boolean result = toPosition.run(packet);

        // verifications
        verify(servo, times(2)).getPosition(); // for the sanity check and the calc for the incremented position
        verify(servo, times(1)).setPosition(currentPosition - increment); // set our position
        verifyNoMoreInteractions(servo);

        // assertions
        Assert.assertFalse(result);
    }



}
