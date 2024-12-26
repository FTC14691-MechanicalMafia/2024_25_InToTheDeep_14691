package org.firstinspires.ftc.teamcode.mm14691;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

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
        Assert.assertNotNull(opMode.pinpointDrive);
        Assert.assertNotNull(opMode.viperDrive);
        Assert.assertNotNull(opMode.liftDrive);
        Assert.assertNotNull(opMode.ascendDrive);
        Assert.assertNotNull(opMode.intakeDrive);
        Assert.assertNotNull(opMode.wristDrive);
    }
}
