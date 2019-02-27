package org.firstinspires.ftc.teamcode.Disabled_Classes;

import java.util.*;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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


@Autonomous
@Disabled
public class Color_Copy_Copy_Copy extends LinearOpMode {
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
    
    int numwhite;
    ArrayList<Boolean> data = new ArrayList<Boolean>();
    private boolean result;
    private double red_threshold;
    private double percent_error;
    private float color2;
    public void runOpMode() {
        
        
        //winch= hardwareMap.dcMotor.get("winch");
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
        red_threshold = 60; // third meeting house: 60, high school: 150?
        percent_error = 0.7;
        //color2 = JavaUtil.colorToHue(Color.argb(color.alpha(), color.red(), color.green(), color.blue()));
        //telemetry.addData("rd", color2);
        telemetry.update();
        waitForStart();
        
        if(opModeIsActive())
        {
            //moveMe();
            while (centerDistance.getDistance(DistanceUnit.CM) > 5 && opModeIsActive()) {
                if (rightDistance.getDistance(DistanceUnit.CM) > leftDistance.getDistance(DistanceUnit.CM)) {
                    rightFront.setPower(0.35);
                    rightRear.setPower(0.35);
                    leftFront.setPower(0.05);
                    leftRear.setPower(0.05);
                }
                else if (leftDistance.getDistance(DistanceUnit.CM) > rightDistance.getDistance(DistanceUnit.CM)) {
                    rightFront.setPower(0.05);
                    rightRear.setPower(0.05);
                    leftFront.setPower(0.35);
                    leftRear.setPower(0.35);
                }
                else {
                    rightFront.setPower(0.1);
                    rightRear.setPower(0.1);
                    leftFront.setPower(0.1);
                    leftRear.setPower(0.1);
                }
            }
            rightFront.setPower(0.0);
            rightRear.setPower(0.0);
            leftFront.setPower(0.0);
            leftRear.setPower(0.0);
            
            resetTimer();
            
            while(moveTimer.time() <= 0.2 && opModeIsActive()){
                rightFront.setPower(0.0);
                rightRear.setPower(0.0);
                leftFront.setPower(0.0);
                leftRear.setPower(0.0);
            }
            
            sampling();
        }
        
    }
    // sampling
    private boolean redtest() 
    {
        numwhite = 0;
        moveTimer.reset();
        while (moveTimer.time() <= 3 && opModeIsActive()) {
            //JavaUtil.inListSet(data, JavaUtil.AtMode.LAST, 0, true, color.red() < 52);
            data.add(color.red()>red_threshold);
            telemetry.addData("Red",color.red());
            telemetry.addData("Green",color.green());
            telemetry.addData("Blue",color.blue());
            telemetry.addData("Alpha",color.alpha());
            telemetry.update();
            //telemetry.addData("rd", JavaUtil.colorToValue(Color.argb(color.alpha(), color.red(), color.green(), color.blue())));

        }
        for (boolean i : data) {
            if ((boolean)i) {
                numwhite = numwhite + 1;
            }
        }
        //telemetry.addData("white", (int)numwhite);
        //telemetry.update();
        return (boolean)((int)numwhite < (int)data.size() / (int)2);
    }
    
    // reset timer
    private void resetTimer() {
        moveTimer.reset();
    }
    private void setMotors(double power)
    {
        rightFront.setPower(power);
        rightRear.setPower(power);
        leftFront.setPower(power);
        leftRear.setPower(power);
    }
    private void turnRight(double power)
    {
        rightFront.setPower(-power);
        rightRear.setPower(-power);
        leftFront.setPower(power);
        leftRear.setPower(power);
    }
    private void turnLeft(double power)
    {
        rightFront.setPower(power);
        rightRear.setPower(power);
        leftFront.setPower(-power);
        leftRear.setPower(-power);
    }
    private void sampling()
    {
        result = (boolean)redtest();
        telemetry.addData("White?", !result);
        telemetry.addData("numwhite", numwhite);
        telemetry.addData("len", data.size());
        telemetry.addData("dd", numwhite / data.size() > percent_error);
        telemetry.update();
        moveTimer = new ElapsedTime(System.nanoTime());
        if(result){
            resetTimer();
            while(moveTimer.time() <= 0.6 && opModeIsActive()){
                rightFront.setPower(1.0);
                rightRear.setPower(1.0);
                leftFront.setPower(1.0);
                leftRear.setPower(1.0);
            }
            /*resetTimer();
            while(moveTimer.time() <= 0.5 && opModeIsActive()) {
                rightFront.setPower(-1.0);
                rightRear.setPower(-1.0);
                leftFront.setPower(-1.0);
                leftRear.setPower(-1.0);
                telemetry.addData("time",moveTimer.time());
                telemetry.update();
            }
            */
            rightFront.setPower(0.0);
            rightRear.setPower(0.0);
            leftFront.setPower(0.0);
            leftRear.setPower(0.0);
        }else{
            while(moveTimer.time() <=0.2 && opModeIsActive()){
                rightFront.setPower(-1.0);
                rightRear.setPower(-1.0);
                leftFront.setPower(-1.0);
                leftRear.setPower(-1.0);
            }
            rightFront.setPower(0.0);
            rightRear.setPower(0.0);
            leftFront.setPower(0.0);
            leftRear.setPower(0.0);
        }
    }
    private void moveMe()
    {
        resetTimer();
        while (moveTimer.seconds() < 0.85 && opModeIsActive()) {
            setMotors(1.0);
        }
        setMotors(0.0);
        sampling();
        resetTimer();
        while (moveTimer.seconds() < 0.81 && opModeIsActive()) {
            turnLeft(1.0);
        }
        resetTimer();
        while (moveTimer.seconds() < 1.2 && opModeIsActive()) {
            setMotors(1.0);
        }
        resetTimer();
        while (moveTimer.seconds() < 0.7 && opModeIsActive()) {
            setMotors(0.0);
        }
        resetTimer();
        while (moveTimer.seconds() < 0.45 && opModeIsActive()) {
            setMotors(0.5);
        }
        resetTimer();
        while (moveTimer.seconds() < 1 && opModeIsActive()) {
            turnRight(1.0);
        }
        resetTimer();
        while (moveTimer.seconds() < 0.6 && opModeIsActive()) {
            setMotors(1.0);
        }
        resetTimer();
        while (moveTimer.seconds() < 0.1 && opModeIsActive()) {
            turnRight(1.0);
        }
        resetTimer();
        while (moveTimer.seconds() < 1 && opModeIsActive()) {
            setMotors(1.0);
        }
    }
}
