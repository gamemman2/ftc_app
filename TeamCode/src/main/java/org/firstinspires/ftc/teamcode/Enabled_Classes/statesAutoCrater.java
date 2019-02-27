package org.firstinspires.ftc.teamcode.Enabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.concurrent.TimeUnit;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;

@Autonomous(name = "statesAutoCraterSilver", group = "Auto")

public class statesAutoCrater extends LinearOpMode {
    private VuforiaRoverRuckus vuforiaRoverRuckus;
    private TfodRoverRuckus tfodRoverRuckus;
    private robotControl charlie;

    @Override
    public void runOpMode() {
        charlie = new robotControl();
        charlie.init(hardwareMap);
        /*
        List<Recognition> recognitions;
        double goldMineralX;
        double silverMineral1X;
        double silverMineral2X;
        */
        String position = "";
        vuforiaRoverRuckus = new VuforiaRoverRuckus();
        tfodRoverRuckus = new TfodRoverRuckus();
        vuforiaRoverRuckus.initialize("", hardwareMap.get(WebcamName.class, "Webcam 1"), "teamwebcamcalibrations.xml",
                true, true, VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES,
                0, 0, 0, 0, 0, 0, true);
        tfodRoverRuckus.initialize(vuforiaRoverRuckus, (float) 0.55, true, true);
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            dropFromLander();
            if (tfodRoverRuckus != null) {
                tfodRoverRuckus.activate();
            }
            charlie.resetTimer();
            while (charlie.moveTimer.time(TimeUnit.SECONDS) <3 && passCheck()) {
                position = charlie.sampling(telemetry, tfodRoverRuckus);
            }
        }

        telemetry.addData("Gem", position);
        telemetry.addData("RackPos",charlie.rack.getCurrentPosition());
        telemetry.update();

        wait(500);
        driveTime(300, 0.4);
        charlie.halt();
        switch (position) {
            case "Left":
                driveTime(900, -0.4, 0.4); // -0.4, 0.4
                wait(100);
                driveTime(1200, 0.7); // 0.5
                driveTime(1200, -0.7); // -0.5
                driveTime(1090, 0.4, -0.4); // 0.4, -0.4
                wait(100);
                break;
            case "Right":
                driveTime(900, 0.4, -0.4); // 0.4, -0.4
                driveTime(1200, 0.7); // 0.5
                driveTime(1200, -0.7); // -0.5
                driveTime(900, -0.4, 0.4); // -0.4, 0.4
                break;
            case "Center":
                driveTime(1200, 0.6); // 0.5
                driveTime(1200, -0.6); // -0.5
                break;
        }
        driveTime(300, 0.4); // 0.4  forwards slightly
        wait(200);

        driveTime(1450, -0.5, 0.5); // -0.4, 0.4  turn towards wall
        wait(200);
        //driveTime(1980, 0.43);
        driveTime(1200, 0.6); // 0.4  move to wall
        driveDistanceFront(22, 0.4); // 0.4  inch towards wall w/distance
        driveTime(1550, -0.4, 0.4); // -0.4, 0.4  turn towards depot
        //driveTime(800, 0.48, 0.4);
        //driveTime(1800, 0.48);
        //driveDistanceCenter(30,0.20);
        driveDistanceSF(22, 8, 0.55); // 30, 11, 0.3  drive along wall
        //driveTime(500, -0.4, 0.4); // -0.4, 0.4  turn to drop
        charlie.halt(); // stop
        charlie.markerLock.setPosition(0.0);
        /*
        wait(100);
        charlie.teamMarker.setPosition(0.0); // drop team marker
        wait(1000); // wait for it to drop
        // ** DO NOT EDIT
        //driveTime(500, 0.4, -0.4); // 0.4, -0.4  turn back
        //driveTime(3000, -0.63, -0.4);
        driveDistanceSB(2000, 8, -0.53); // 2000, 8, -0.4  reverse to crater
        // half way recalibration
        driveTime(300, -0.4, 0.4); // -0.4, 0.4  recalibration
        //driveTime(1100, -0.63, -0.4);
        driveDistanceSB(4000, 8, -0.6); // 3000, 8, -0.4  continue reversing to crater
        charlie.halt();
        */
        vuforiaRoverRuckus.close();
        tfodRoverRuckus.close();
    }
    private boolean passCheck() {
        return opModeIsActive() && !isStopRequested();
    }
    private long getTime() {
        return charlie.moveTimer.time(TimeUnit.MILLISECONDS);
    }
    private void moveRackMotor(double pwr) {
        charlie.rack.setPower(pwr);
    }
    private void moveRackMotor() {
        charlie.rack.setPower(0);
    }
    private void wait(int time) {
        charlie.resetTimer();
        while(getTime() < time && passCheck()) {
        }
    }
    private void driveTime(int time, double pwrLeft, double pwrRight) {
        charlie.resetTimer();
        while (getTime() < time && passCheck()) {
            charlie.moveDriveMotors(pwrLeft, pwrRight);

        }
        charlie.halt();
    }
    private void driveTime(int time, double pwr) {
        charlie.resetTimer();
        while (getTime() < time && passCheck()) {
            charlie.moveDriveMotors(pwr);
        }
        charlie.halt();
    }
    private void liftByTime(int time, double pwr) {
        charlie.resetTimer();
        while (getTime() < time && passCheck()) {
            charlie.liftControl(pwr);
        }
        charlie.liftControl();
    }
    private void liftMagnet(double pwr) {
        while (charlie.magnet.getState() && passCheck()) {
            charlie.liftControl(pwr);
        }
        charlie.liftControl();
    }
    private void driveDistanceSF(int dist, int distS, double pwr) {
        double pwrR=pwr;
        double pwrL=pwr;
        int thing;
        while (charlie.rightFrontDistance.getDistance(DistanceUnit.CM) > dist && passCheck()) {
            if (charlie.rightDistance.getDistance(DistanceUnit.CM) < distS)
            {
                pwrL=pwrL*0.8;
                pwrR=pwrR*1.2;
            }
            else {
                pwrL=pwr;
                pwrR=pwr;
            }
            telemetry.addData("pwrL", pwrL);
            telemetry.addData("pwrR", pwrR);
            telemetry.addData("Distance Forward", charlie.rightFrontDistance.getDistance(DistanceUnit.CM));
            telemetry.addData("Distance Right", charlie.rightDistance.getDistance(DistanceUnit.CM));
            telemetry.update();
            charlie.moveDriveMotors(pwrL, pwrR);
        }
        charlie.halt();
    }
    private void driveDistanceSB(int time, int distS, double pwr) {
        double pwrR=pwr;
        double pwrL=pwr;
        charlie.resetTimer();
        while (getTime() < time && passCheck()) {
            if (charlie.rightDistance.getDistance(DistanceUnit.CM) < distS)
            {
                pwrL=pwrL*1.2;
                pwrR=pwrR*0.8;
            }
            else {
                pwrL=pwr;
                pwrR=pwr;
            }
            telemetry.addData("pwrL", pwrL);
            telemetry.addData("pwrR", pwrR);
            telemetry.addData("Distance Forward", charlie.rightFrontDistance.getDistance(DistanceUnit.CM));
            telemetry.addData("Distance Right Rear", charlie.rightDistance.getDistance(DistanceUnit.CM));
            telemetry.update();
            charlie.moveDriveMotors(pwrL, pwrR);
        }
        charlie.halt();
    }
    private void driveDistanceCenter(int dist, double pwr) {
        while (charlie.rightFrontDistance.getDistance(DistanceUnit.CM) > dist && passCheck()) {
            charlie.moveDriveMotors(pwr);
            telemetry.addData("dist", charlie.rightFrontDistance.getDistance(DistanceUnit.CM));
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
        driveTime(910, 0, -0.4);
    }
}
