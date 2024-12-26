package org.firstinspires.ftc.teamcode.mm14691;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

/**
 * Base test class for MM14691 Op Modes.  This should mock out everything we use on the robot.
 */
public abstract class MM14691AbstractTest {

    @Mock
    public Context appContext;
    @Mock
    public OpModeManagerNotifier notifier;
    @Spy
    @InjectMocks
    public HardwareMap hardwareMap;
    @Mock
    public DcMotorEx leftFront;
    @Mock
    public DcMotorEx leftBack;
    @Mock
    public DcMotorEx rightBack;
    @Mock
    public DcMotorEx rightFront;
    @Mock
    public DcMotorEx viper;
    @Mock
    public DcMotorEx lift;
    @Mock
    public DcMotorEx ascend;
    @Mock
    public Servo intake;
    @Mock
    public Servo wrist;
    @Mock
    public HardwareMap.DeviceMapping<VoltageSensor> voltageSensorDeviceMapping;
    @Mock
    public VoltageSensor voltageSensor;
    @Mock
    public IMU imu;
    @Mock
    public Telemetry telemetry;
    @Mock
    public Gamepad gamepad2;
    @Mock
    public ElapsedTime runtime;
    @Mock
    public FtcDashboard dash;

    @Before
    public void setUpHardware() {
        // mock out the hardware on the robot
        doReturn(leftFront).when(hardwareMap).tryGet(DcMotorEx.class, "frontLeft");
        doReturn(leftBack).when(hardwareMap).tryGet(DcMotorEx.class, "rearLeft");
        doReturn(rightBack).when(hardwareMap).tryGet(DcMotorEx.class, "rearRight");
        doReturn(rightFront).when(hardwareMap).tryGet(DcMotorEx.class, "frontRight");

        hardwareMap.voltageSensor = voltageSensorDeviceMapping;
        when(voltageSensorDeviceMapping.iterator()).thenAnswer(a -> List.of(voltageSensor).iterator());

        doReturn(imu).when(hardwareMap).tryGet(IMU.class, "imu");
        doReturn(new Quaternion(0, 0, 0, 0, 0)).when(imu).getRobotOrientationAsQuaternion();
        when(imu.getDeviceName()).thenReturn("imu");

        //don't use the pinpoint driver
        doThrow(IllegalArgumentException.class).when(hardwareMap).tryGet(GoBildaPinpointDriverRR.class, "odo");

        doReturn(viper).when(hardwareMap).tryGet(DcMotorEx.class, "armViper");
        doReturn(lift).when(hardwareMap).tryGet(DcMotorEx.class, "armLift");
        doReturn(ascend).when(hardwareMap).tryGet(DcMotorEx.class, "ascend");

        doReturn(intake).when(hardwareMap).tryGet(Servo.class, "intake");
        doReturn(wrist).when(hardwareMap).tryGet(Servo.class, "wrist");
    }
}
