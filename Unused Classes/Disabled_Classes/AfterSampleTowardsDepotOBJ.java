package org.firstinspires.ftc.teamcode.Disabled_Classes;
import java.util.*;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.*;

@Autonomous
@Disabled
public class AfterSampleTowardsDepotOBJ extends LinearOpMode {
    private Gyroscope imu_1;
    private DcMotor RightFront;
    private DcMotor RightRear;
    private DcMotor LeftFront;
    private DcMotor LeftRear;
    private DcMotor winch;//unitilized vars
    private Blinker expansion_Hub_1;
    private Blinker expansion_Hub_2;
    private DistanceSensor coloras;
    private Timer time;
    //@Override
    public void runOpMode() {
        int vole=0;
        //winch= hardwareMap.dcMotor.get("winch");
        RightFront= hardwareMap.dcMotor.get("RightFront");
        RightRear= hardwareMap.dcMotor.get("RightRear");
        LeftFront= hardwareMap.dcMotor.get("LeftFront");
        LeftRear= hardwareMap.dcMotor.get("LeftRear");
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.addData("hello world", 0xff);
            telemetry.update();
            winch.setPower(1.0);
            
            
        }
    }
}