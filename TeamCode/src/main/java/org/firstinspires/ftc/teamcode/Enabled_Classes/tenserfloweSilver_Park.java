/*
package org.firstinspires.ftc.teamcode.Enabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;
import org.firstinspires.ftc.teamcode.Enabled_Classes.robotControl;

@Autonomous(name = "TenserFlowSilverAuto_NoDepot", group = "Auto")
public class tenserfloweSilver_Park extends LinearOpMode {
///*
    private VuforiaRoverRuckus vuforiaRoverRuckus;
    private TfodRoverRuckus tfodRoverRuckus;

    private robotControl charlie;
    @Override
    public void runOpMode() {
        charlie= new robotControl();
        charlie.init(hardwareMap);

        //setUpMotor();
        List<Recognition> recognitions;
        double goldMineralX;
        double silverMineral1X;
        double silverMineral2X;
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
            charlie.teamMarker.setPosition(1.0);
            charlie.markerLock.setPosition(1.0);
            liftMagnet(-0.7);
            wait(200);
            driveTime(50, -0.4); // 50, -0.4
            driveTime(850, 0, 0.4); // 850, 0, 0.4
            liftByTime(900, .75);
            charlie.rack.setPower(0);
            wait(200);
            driveTime(850, 0, -0.4); // 850, 0, -0.4
            if (tfodRoverRuckus != null) {
                tfodRoverRuckus.activate();
            }
            charlie.resetTimer();
            // Scan For Mineral
            // ToDo: Make this a function
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
        // ** DO NOT EDIT
        telemetry.addData("Gem", position);
        telemetry.update();
        //Drive Time!
        wait(500);
        driveTime(300, 0.4);
        charlie.halt();
        //Action taken is based on which position the gold mineral is determined to be
        switch (position) {
            case "Left":
                driveTime(900, -0.4, 0.4); // -0.4, 0.4  turn toward mineral
                wait(100);
                driveTime(1400, 0.5); // 0.5  knock mineral
                driveTime(1800, 0.4, -0.4); // 0.4, -0.4  turn towards crater
                driveTime(400, 0.5); // 0.5  little bump forward
                break;

            case "Right":
                driveTime(900, 0.4, -0.4); // 0.4, -0.4  turn toward mineral
                driveTime(1600, 0.5); // 0.5  knock mineral
                driveTime(1800, -0.4, 0.4); // -0.4, 0.4  turn towards crater
                driveTime(400, 0.5); // 0.5  little bump forward
                break;

            case "Center":
                driveTime(1550,0.5); // 1350, 0.5  knock mineral and touch crater
                break;
        }
        charlie.halt();
    }

    private boolean passCheck() {
        return opModeIsActive() && !isStopRequested();
        // It is on AND not off
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
            // Wait for it...
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
}
*/