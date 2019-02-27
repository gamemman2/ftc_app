package org.firstinspires.ftc.teamcode.Disabled_Classes;

import java.util.*;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.*;
import org.firstinspires.ftc.robotcore.*;

@TeleOp
@Disabled
public class DistanceTestTwo extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor winch;
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
    
    private ElapsedTime moveTimer=new ElapsedTime(System.nanoTime());
    
    private int direction;
    
    @Override
    public void runOpMode() {
        rightFront= hardwareMap.dcMotor.get("RightFront");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear= hardwareMap.dcMotor.get("RightRear");
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront= hardwareMap.dcMotor.get("LeftFront");
        leftRear= hardwareMap.dcMotor.get("LeftRear");
        
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        color = hardwareMap.colorSensor.get("color");
        
        rightDistance = hardwareMap.get(DistanceSensor.class, "rightDistance");
        centerDistance = hardwareMap.get(DistanceSensor.class, "centerDistance");
        leftDistance = hardwareMap.get(DistanceSensor.class, "leftDistance");
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            /*
            rightFront.setPower(-gamepad1.right_stick_x * 0.5);
            rightRear.setPower(-gamepad1.right_stick_x * 0.5);
            leftFront.setPower(gamepad1.right_stick_x * 0.5);
            leftRear.setPower(gamepad1.right_stick_x * 0.5);
            */
            
            telemetry.addData("rightDistance",String.format("%.01f cm", rightDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("centerDistance",String.format("%.01f cm", centerDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("leftDistance",String.format("%.01f cm", leftDistance.getDistance(DistanceUnit.CM)));
            direction = 0;
            if (leftDistance.getDistance(DistanceUnit.CM) < 50 && rightDistance.getDistance(DistanceUnit.CM) < 50 && leftDistance.getDistance(DistanceUnit.CM) < 50 && opModeIsActive() && !isStopRequested()){
                if ((leftDistance.getDistance(DistanceUnit.CM) > centerDistance.getDistance(DistanceUnit.CM) )&& ( rightDistance.getDistance(DistanceUnit.CM) > centerDistance.getDistance(DistanceUnit.CM) ) && opModeIsActive() && !isStopRequested()) {
                    //telemetry.addData("Needs to go","centered");
                    
                    rightFront.setPower(0.0);
                    rightRear.setPower(0.0);
                    leftFront.setPower(0.0);
                    leftRear.setPower(0.0);
                    
                    //addTime();
                }
                else if (rightDistance.getDistance(DistanceUnit.CM) < centerDistance.getDistance(DistanceUnit.CM) && opModeIsActive() && !isStopRequested()) {
                    //telemetry.addData("Needs to go","left");
                    resetTimer();
                    while (moveTimer.time() < 0.3 && opModeIsActive() && !isStopRequested()) {
                        rightFront.setPower(0.3);
                        rightRear.setPower(0.3);
                        leftFront.setPower(-0.3);
                        leftRear.setPower(-0.3);
                    }
                    rightFront.setPower(0.0);
                    rightRear.setPower(0.0);
                    leftFront.setPower(0.0);
                    leftRear.setPower(0.0);
                    //direction = -3;
                }
                else if (leftDistance.getDistance(DistanceUnit.CM) < centerDistance.getDistance(DistanceUnit.CM) && opModeIsActive() && !isStopRequested()) {
                    //telemetry.addData("Needs to go","right");
                    resetTimer();
                    while (moveTimer.time() < 0.3 && opModeIsActive() && !isStopRequested()) {
                        rightFront.setPower(-0.3);
                        rightRear.setPower(-0.3);
                        leftFront.setPower(0.3);
                        leftRear.setPower(0.3);
                    }
                    rightFront.setPower(0.0);
                    rightRear.setPower(0.0);
                    leftFront.setPower(0.0);
                    leftRear.setPower(0.0);
                    //direction = 3;
                }
            }
            telemetry.update();
        }
    }
    
    private void resetTimer() {
        moveTimer.reset();
    }
    
    private void addTime() {
        resetTimer();
        while (moveTimer.time() <= 0.05 && opModeIsActive() && !isStopRequested()) {
            rightFront.setPower(direction / 10);
            rightRear.setPower(direction / 10);
            leftFront.setPower(-direction / 10);
            leftRear.setPower(-direction / 10);
        }
        rightFront.setPower(0.0);
        rightRear.setPower(0.0);
        leftFront.setPower(0.0);
        leftRear.setPower(0.0);
    }
}