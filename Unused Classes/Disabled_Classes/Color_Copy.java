package org.firstinspires.ftc.teamcode.Disabled_Classes;

import java.util.*;

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
public class Color_Copy extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor winch;
    private DcMotor rightFront;
    private DcMotor rightRear;
    private DcMotor leftFront;
    private DcMotor leftRear;
    private Blinker expansion_Hub_1;
    private Blinker expansion_Hub_2;
    private ColorSensor color;
    
    int numwhite;
    ArrayList<Boolean> data = new ArrayList<Boolean>();
    
    public void runOpMode() {
        boolean result;
        double red_threshold;
        double percent_error;
        float color2;
        
        winch= hardwareMap.dcMotor.get("winch");
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
        
        telemetry.addData("Status", "Initialized");
        red_threshold = 160;
        percent_error = 0.7;
        //color2 = JavaUtil.colorToHue(Color.argb(color.alpha(), color.red(), color.green(), color.blue()));
        //telemetry.addData("rd", color2);
        telemetry.update();
        waitForStart();
        
        if(opModeIsActive())
        {
            result = (boolean)redtest();
            telemetry.addData("White?", !result);
            telemetry.addData("numwhite", numwhite);
            telemetry.addData("len", data.size());
            telemetry.addData("dd", numwhite / data.size() > percent_error);
            telemetry.update();
            ElapsedTime movetimer = new ElapsedTime();
            movetimer = new ElapsedTime(System.nanoTime());
            while(opModeIsActive()){
                if(result){
                    while(movetimer.time() <= 0.5 && opModeIsActive()){
                        rightFront.setPower(1.0);
                        rightRear.setPower(1.0);
                        leftFront.setPower(1.0);
                        leftRear.setPower(1.0);
                    }
                    rightFront.setPower(0.0);
                    rightRear.setPower(0.0);
                    leftFront.setPower(0.0);
                    leftRear.setPower(0.0);
                }else{
                    while(movetimer.time() <=0.5 && opModeIsActive()){
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
        }
    }
    // sampling
    private boolean redtest() 
    {
        ElapsedTime whitetimer = new ElapsedTime();
        numwhite = 0;
        whitetimer = new ElapsedTime(System.nanoTime());
        while (whitetimer.time() <= 3 && opModeIsActive()) {
            //JavaUtil.inListSet(data, JavaUtil.AtMode.LAST, 0, true, color.red() < 52);
            data.add(color.red()>150);
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
}
