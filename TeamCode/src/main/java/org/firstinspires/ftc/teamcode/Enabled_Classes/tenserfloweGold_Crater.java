/*
package org.firstinspires.ftc.teamcode.Enabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;
import org.firstinspires.ftc.teamcode.Enabled_Classes.robotControl;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous(name = "TenserFlowGoldAuto_Crater", group = "Auto")
public class tenserfloweGold_Crater extends LinearOpMode {

    private VuforiaRoverRuckus vuforiaRoverRuckus;
    private TfodRoverRuckus tfodRoverRuckus;
    private robotControl charlie;

    private DistanceUnit CM = DistanceUnit.CM;
    @Override
    public void runOpMode() {
        //ToDo: #6
        charlie=new robotControl();
        charlie.init(hardwareMap);
        List<Recognition> recognitions;
        double goldMineralX;
        double silverMineral1X;
        double silverMineral2X;
        vuforiaRoverRuckus = new VuforiaRoverRuckus();
        tfodRoverRuckus = new TfodRoverRuckus();
        String position = "";
        vuforiaRoverRuckus.initialize("", hardwareMap.get(WebcamName.class, "Webcam 1"), "teamwebcamcalibrations.xml",
                true, true, VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES,
                0, 0, 0, 0, 0, 0, true);
        tfodRoverRuckus.initialize(vuforiaRoverRuckus, (float)0.55, true, true);
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            charlie.teamMarker.setPosition(1.0);
            dropFromLander();
            if (tfodRoverRuckus != null) {
                tfodRoverRuckus.activate();
            }
            // Put run blocks here.
            charlie.resetTimer();

            while (charlie.moveTimer.time(TimeUnit.SECONDS) < 3 && passCheck()) {
                // Put loop blocks here.
                if (tfodRoverRuckus != null) {
                    recognitions = tfodRoverRuckus.getRecognitions();
                    telemetry.addData("# Objects Recognized", recognitions.size());
                    // Changed from 3 to 2

                    if (recognitions.size() == 2)
                    {
                        goldMineralX = -1;
                        silverMineral1X = -1;
                        silverMineral2X = -1;
                        // This just records values, and is unchanged
                        for (Recognition recognition : recognitions)
                        {
                            if (recognition.getLabel().equals("Gold Mineral"))
                            {
                                goldMineralX = (int) recognition.getLeft();
                            }
                            else if (silverMineral1X == -1)
                            {
                                silverMineral1X = (int) recognition.getLeft();
                            }
                            else
                            {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        // If there is no gold (-1) and there two silvers (not -1) the gold
                        // is not visible, and must be on the right
                        if (goldMineralX == -1 && silverMineral1X != -1 && silverMineral2X != -1)
                        {
                            telemetry.addData("Gold Mineral Position", "Right");
                            position = "Right";
                        }
                        // If you can see one gold and one silver ...
                        else if (goldMineralX != -1 && silverMineral1X != -1)
                        {
                            // ... if the gold is to the right of the silver, the gold is in the center ...
                            if (goldMineralX > silverMineral1X)
                            {
                                telemetry.addData("Gold Mineral Position", "Center");
                                position = "Center";
                            }
                            // ... otherwise it is on the left
                            else
                            {
                                telemetry.addData("Gold Mineral Position", "Left");
                                position = "Left";
                            }
                        }
                    }
                }
                telemetry.update();
            }
        }
        vuforiaRoverRuckus.close();
        tfodRoverRuckus.close();
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
                driveTime(1200, 0.4);
                driveTime(900,0.8, -0.8);
                driveTime(1200, 0.4);
                charlie.teamMarker.setPosition(0.0);
                driveTime(1200, -0.4);
                driveTime(900,-0.8, 0.8);
                driveTime(1200, -0.4);
                driveTime(900, 0.4, -0.4);
                break;

            case "Right":
                driveTime(900, 0.4, -0.4);
                driveTime(1600, 0.4);
                driveTime(900,-0.8, 0.8);
                driveTime(1200, 0.4);
                charlie.teamMarker.setPosition(0.0);
                driveTime(1200, -0.4);
                driveTime(900, 0.8, -0.8);
                driveTime(1200, -0.4);
                driveTime(900, -0.4, 0.4);

                break;

            case "Center":
                driveTime(1350,0.8);
                charlie.teamMarker.setPosition(0.0);
                driveTime(1350,-0.8);
        }
        // forwards slightly
        driveTime(300, 0.4);
        driveTime(100, 0.0);
        //turn towards wall
        driveTime(1750, 0.4, -0.4);
        driveTime(100, 0.0);
        //align with wall
        //move to wall
        //driveTime(1980, 0.43);
        driveTime(1500, 0.40);
        driveDistanceFront(27, 0.20);
        alignClockwise(0.4);


    }
    private boolean passCheck() {
        return opModeIsActive() && !isStopRequested();
        // It is on AND not off
    }
    private long getTime() {
        return charlie.moveTimer.time(TimeUnit.MILLISECONDS);
        // The mitochondria is the powerhouse of the cell
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
        }
        charlie.halt();
        // V = d/t
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
    private void alignClockwise(double pwr) {
        while (!equalDist(.5)) {
            charlie.moveDriveMotors(0.4,-0.4);
        }
        charlie.halt();
    }
    private boolean equalDist(double tolerance) {
        boolean lower = charlie.rightDistance.getDistance(CM) < charlie.rightRearDistance.getDistance(CM)+tolerance;
        boolean higher = charlie.rightDistance.getDistance(CM) > charlie.rightDistance.getDistance(CM)-tolerance;
        return lower && higher;
    }
    private void driveDistanceSF(int dist, int distS, double pwr) {
        double pwrR=pwr;
        double pwrL=pwr;
        int thing;
        while (charlie.leftDistance.getDistance(DistanceUnit.CM) > dist && passCheck()) {
            if (charlie.rightRearDistance.getDistance(DistanceUnit.CM) < distS)
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
            telemetry.addData("Distance Forward", charlie.leftDistance.getDistance(DistanceUnit.CM));
            telemetry.addData("Distance Right", charlie.rightRearDistance.getDistance(DistanceUnit.CM));
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
            telemetry.addData("Distance Forward", charlie.leftDistance.getDistance(DistanceUnit.CM));
            telemetry.addData("Distance Right Rear", charlie.rightFrontDistance.getDistance(DistanceUnit.CM));
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