/*
package org.firstinspires.ftc.teamcode.Enabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.concurrent.TimeUnit;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;
import org.firstinspires.ftc.teamcode.Enabled_Classes.robotControl;

@Autonomous(name = "TenserFlowSilverAuto", group = "Auto")
@Disabled
public class tenserfloweSilver extends LinearOpMode {
    private VuforiaRoverRuckus vuforiaRoverRuckus;
    private TfodRoverRuckus tfodRoverRuckus;

    private robotControl charlie;
    @Override
    public void runOpMode() {
        charlie= new robotControl();
        charlie.init(hardwareMap);

        //setUpMotor();

        vuforiaRoverRuckus = new VuforiaRoverRuckus();
        tfodRoverRuckus = new TfodRoverRuckus();

        String position = "";
        // Put initialization blocks here.
        vuforiaRoverRuckus.initialize("", hardwareMap.get(WebcamName.class, "Webcam 1"), "teamwebcamcalibrations.xml",
                true, true, VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES,
                0, 0, 0, 0, 0, 0, true);
        tfodRoverRuckus.initialize(vuforiaRoverRuckus, (float)0.3, true, true); // 0.55 = school, 0.3 = Manns' house
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // lower From Lander
            dropFromLander();
            if (tfodRoverRuckus != null) {
                tfodRoverRuckus.activate();
            }
            charlie.resetTimer();
            // Scan For Mineral
            // ToDo: Make this a function
            while (charlie.moveTimer.time(TimeUnit.SECONDS) < 3 && passCheck()) {
                // Put loop blocks here.
                charlie.sampling(telemetry, tfodRoverRuckus);
            }
        }
        vuforiaRoverRuckus.close();
        tfodRoverRuckus.close();
        //
        telemetry.addData("Gem", position);
        telemetry.update();
        //Drive Time!
        wait(500);
        driveTime(300, 0.4);
        charlie.halt();
        //Action taken is based on which position the gold mineral is determined to be
        switch (position) {
            case "Left":
                driveTime(900, -0.4, 0.4);
                driveTime(200, 0);
                driveTime(1200, 0.5);
                driveTime(1200, -0.5);
                driveTime(1090, 0.4, -0.4);
                driveTime(200, 0);
                break;

            case "Right":
                driveTime(900, 0.4, -0.4);
                driveTime(1600, 0.5);
                driveTime(1600, -0.5);
                driveTime(900, -0.4, 0.4);
                break;

            case "Center":
                driveTime(1350,0.5);
                driveTime(1350,-0.5);
                break;
        }
        // forwards slightly
        driveTime(300, 0.4);
        driveTime(200, 0.0);
        //turn towards wall
        driveTime(1750, -0.4, 0.4);
        driveTime(200, 0.0);
        //move to wall
        //driveTime(1980, 0.43);
        driveTime(1800, 0.40);
        driveDistanceFront(23, 0.40);//0.2
        //turn towards depot
        driveTime(1780, -0.4, 0.4);
        // move into range
        //driveTime(800, 0.48, 0.4);
        // drive along wall
        //driveTime(1800, 0.48);
        //driveDistanceCenter(30,0.20);
        driveDistanceS(30, 11, 0.50);//0.3
        //turn to drop
        driveTime(500, -0.4, 0.4);
        //stop
        charlie.halt();
        //drop and wait
        charlie.teamMarker.setPosition(0.0);
        wait(1000);
        // turn back
        driveTime(500, 0.4, -0.4);
        //reverse to depot
        driveTime(3000, -0.63, -0.4);
        // half way recalibration
        driveTime(300, -0.4, 0.4);
        // continue on to depot
        driveTime(1100, -0.63, -0.4);
        charlie.halt();
    }

    private boolean passCheck() {
        return opModeIsActive() && !isStopRequested();
        // It is on AND not off
    }
    private long getTime() {
        return charlie.moveTimer.time(TimeUnit.MILLISECONDS);
        // The mitochondria is the powerhouse of the cell
    }

    private void moveRackMotor(double pwr) {
        charlie.rack.setPower(pwr);
    }
    private  void moveRackMotor() {
        charlie.rack.setPower(0);
    }
    private void wait(int time) {
        charlie.resetTimer();
        while(getTime() < time && passCheck()) {
            // Wait for it...
        }
    }
    private void driveTime(int time, double pwrLeft, double pwrRight) {
        charlie.resetTimer();
        while (getTime() < time && passCheck()) {
            charlie.moveDriveMotors(pwrLeft, pwrRight);
        }
        charlie.halt();
        // Deja Vu!
    }
    private void driveTime(int time, double pwr) {
        charlie.resetTimer();
        while (getTime() < time && passCheck()) {
            charlie.moveDriveMotors(pwr);
            telemetry.addData("rightFront", charlie.rightFront.getPower());
            telemetry.addData("rightRear", charlie.rightRear.getPower());
            telemetry.addData("leftFront", charlie.leftFront.getPower());
            telemetry.addData("leftRear", charlie.leftRear.getPower());
            telemetry.update();
        }
        charlie.halt();
        // V = d/t
    }

    private void liftByTime(int time, double pwr) {
        charlie.resetTimer();
        while (getTime() < time && passCheck()) {
            moveRackMotor(pwr);
        }
        moveRackMotor();
    }
    private void liftMagnet(double pwr) {
        while (charlie.magnet.getState() && passCheck()) {
            moveRackMotor(pwr);
        }
        moveRackMotor();
    }
    private void driveDistanceS(int dist, int distS, double pwr) {
        double pwrR=pwr;
        double pwrL=pwr;
        while (charlie.leftDistance.getDistance(DistanceUnit.CM) > dist && passCheck()) {
            if (charlie.rightRearDistance.getDistance(DistanceUnit.CM) < distS)
            {
                pwrL=pwr-(pwr/1.5);
                pwrR=pwr+(pwr/1.5);
            }
            else {
                pwrL=pwr;
                pwrR=pwr;
            }
            telemetry.addData("pwrL", pwrL);
            telemetry.addData("pwrR", pwrR);
            telemetry.addData("Distance Forward", charlie.leftDistance.getDistance(DistanceUnit.CM));
            telemetry.addData("Distance Left", charlie.rightFrontDistance.getDistance(DistanceUnit.CM));
            telemetry.update();
            charlie.moveDriveMotors(pwrL, pwrR);
        }
        charlie.halt();
    }
    private void driveDistanceCenter(int dist, double pwr) {
        while (charlie.leftDistance.getDistance(DistanceUnit.CM) > dist && passCheck()) {
            charlie.moveDriveMotors(pwr);
            telemetry.addData("dist", charlie.leftDistance.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
    private void driveDistanceFront(int dist, double pwr) {
        while (charlie.rightFrontDistance.getDistance(DistanceUnit.CM) > dist && passCheck()) {
            charlie.moveDriveMotors(pwr);
            telemetry.addData("dist", charlie.rightFrontDistance.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
    private void dropFromLander() {
        charlie.teamMarker.setPosition(1.0);
        liftMagnet(-0.7);
        wait(200);
        driveTime(50, -0.4);
        driveTime(850, 0, 0.4);
        liftByTime(900, .75);
        charlie.rack.setPower(0);
        wait(200);
        driveTime(850, 0, -0.4);
    }
}
*/