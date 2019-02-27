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
public class MiniBotAuto_rev2 extends LinearOpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor liftMotor;
    private Gyroscope imu;
    private ColorSensor color;
    private DigitalChannel magnet;
    private CRServo collection;
    
    private ElapsedTime moveTimer=new ElapsedTime(System.nanoTime());
    
    int numwhite;
    ArrayList<Boolean> data = new ArrayList<Boolean>();
    private boolean result;
    private double red_threshold;
    private double percent_error;
    private float color2;
    
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
        red_threshold = 160;
        percent_error = 0.7;
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
            stopMotors();
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
            stopMotors();
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
    private boolean redtest() 
    {
        numwhite = 0;
        moveTimer.reset();
        while (moveTimer.time() <= 3 && opModeIsActive()) {
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
            while(moveTimer.time() <= 0.3 && opModeIsActive()){
                leftMotor.setPower(0.01);
                rightMotor.setPower(0.01);
            }
            resetTimer();
            while(moveTimer.time() <= 0.5 && opModeIsActive()) {
                leftMotor.setPower(-0.01);
                rightMotor.setPower(-0.01);
                telemetry.addData("time",moveTimer.time());
                telemetry.update();
            }
            stopMotors();
        }else{
            while(moveTimer.time() <=0.2 && opModeIsActive()){
                rightMotor.setPower(-0.01);
                leftMotor.setPower(-0.01);
            }
            stopMotors();
        }
    }
    private void stopMotors()
    {
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }
}
