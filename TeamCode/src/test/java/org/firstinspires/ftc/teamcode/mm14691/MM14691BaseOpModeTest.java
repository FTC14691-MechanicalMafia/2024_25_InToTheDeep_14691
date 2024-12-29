package org.firstinspires.ftc.teamcode.mm14691;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MM14691BaseOpModeTest extends MM14691AbstractTest {


    @Test()
    public void testInit() {
        // set up test data

        // set up mocks
        MM14691BaseOpMode opMode = mock(MM14691BaseOpMode.class, Answers.CALLS_REAL_METHODS);
        opMode.hardwareMap = hardwareMap; // inject the HardwareMap manually
        opMode.telemetry = telemetry; // inject the Telemetry manually
        opMode.gamepad2 = gamepad2;

        // method(s) under test
        opMode.init();

        // verifications
        verify(telemetry, times(1)).update();

        // assertions
        Assert.assertNotNull(opMode.viperDrive);
        Assert.assertNotNull(opMode.liftDrive);
        Assert.assertNotNull(opMode.ascendDrive);
        Assert.assertNotNull(opMode.intakeDrive);
        Assert.assertNotNull(opMode.wristDrive);
    }
}
