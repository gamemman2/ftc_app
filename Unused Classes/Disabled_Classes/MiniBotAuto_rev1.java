package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DigitalChannel;
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
public class MiniBotAuto_rev1 extends LinearOpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor liftMotor;
    private Gyroscope imu;
    private ColorSensor color;
    private DigitalChannel magnet;
    private CRServo collection;
    
    private ElapsedTime moveTimer=new ElapsedTime(System.nanoTime());
    
    public void runOpMode() {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        collection = hardwareMap.crservo.get("collection");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        color = hardwareMap.colorSensor.get("color");
        magnet = hardwareMap.digitalChannel.get("magnet");
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        collection.setPower(1.0);
        waitForStart();
        
        if(opModeIsActive()) {
            while(magnet.getState() && opModeIsActive() && !isStopRequested()) {
                liftMotor.setPower(-0.5);
                collection.setPower(1.0);
            }
            resetTimer();
            liftMotor.setPower(0.0);
            while(moveTimer.time()<1&&opModeIsActive()&& !isStopRequested())
            {
                
            }
            resetTimer();
            while(moveTimer.time()<1&&opModeIsActive()&& !isStopRequested())
            {
                rightMotor.setPower(0.5);
                leftMotor.setPower(-0.5);
            }
            rightMotor.setPower(0);
            leftMotor.setPower(0);
            resetTimer();
            while(moveTimer.time()<1&&opModeIsActive()&& !isStopRequested())
            {
                
            }
            resetTimer();
            resetEncoders();
            while(moveTimer.time()<0.5 && opModeIsActive() && !isStopRequested())
            {
                rightMotor.setPower(0.03);
                leftMotor.setPower(0.03);
            }
            rightMotor.setPower(0);
            leftMotor.setPower(0);
            resetTimer();

        }
    }
    private void resetTimer() {
        moveTimer.reset();
    }
    private void resetEncoders() {
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}