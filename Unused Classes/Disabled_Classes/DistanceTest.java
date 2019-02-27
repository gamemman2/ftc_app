package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.*;
import org.firstinspires.ftc.robotcore.*;
import java.util.*;
import android.graphics.Color;


@Autonomous
@Disabled
public class DistanceTest extends LinearOpMode {
    private Gyroscope imu;
    private Gyroscope imu_1;
     
    private DcMotor rightFront;
    private DcMotor rightRear;
    private DcMotor leftFront;
    private DcMotor leftRear;
    private Blinker expansion_Hub_1;
    private Blinker expansion_Hub_2;
    private ColorSensor color;
    
    private DistanceSensor rightDistance;
    private DistanceSensor centerDistance;
    private DistanceSensor leftDistance;

    public void runOpMode() {
        rightFront= hardwareMap.dcMotor.get("RightFront");
        rightRear= hardwareMap.dcMotor.get("RightRear");
        leftFront= hardwareMap.dcMotor.get("LeftFront");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear= hardwareMap.dcMotor.get("LeftRear");
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        rightDistance = hardwareMap.get(DistanceSensor.class, "rightDistance");
        centerDistance = hardwareMap.get(DistanceSensor.class, "centerDistance");
        leftDistance = hardwareMap.get(DistanceSensor.class, "leftDistance");
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        
        if (opModeIsActive()) {
            
            
            while (centerDistance.getDistance(DistanceUnit.CM) > 5) {
                rightFront.setPower(0.1);
                rightRear.setPower(0.1);
                leftFront.setPower(0.1);
                leftRear.setPower(0.1);
                /*
                telemetry.addData("Status", "Running");
                telemetry.addData("rightDistance",String.format("%.01f cm", rightDistance.getDistance(DistanceUnit.CM)));
                telemetry.addData("centerDistance",String.format("%.01f cm", centerDistance.getDistance(DistanceUnit.CM)));
                telemetry.addData("leftDistance",String.format("%.01f cm", leftDistance.getDistance(DistanceUnit.CM)));
                telemetry.update();
                */
            }
            rightFront.setPower(0.0);
            rightRear.setPower(0.0);
            leftFront.setPower(0.0);
            leftRear.setPower(0.0);
        }
    }
}