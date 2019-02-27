package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Autonomous
@Disabled
public class autoComp extends LinearOpMode {
    private VuforiaRoverRuckus vuforiaRoverRuckus;
    private TfodRoverRuckus tfodRoverRuckus;
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private Gyroscope imu;
    private File[] webcamConfig;
    private DcMotor winch;
    private DcMotor rightFront;
    private DcMotor rightRear;
    private DcMotor leftFront;
    private DcMotor leftRear;
    private DcMotor rack;
    private Blinker expansion_Hub_1;
    private Blinker expansion_Hub_2;
    private Servo teamMarker;
    private DistanceSensor rightDistance;
    private DistanceSensor centerDistance;
    private DistanceSensor leftDistance;
    private DigitalChannel magnet;
    private int turnTime;
    private ElapsedTime moveTimer = new ElapsedTime(System.nanoTime());
    private static final String VUFORIA_KEY = "AQgDy6j/////AAABmXISaVgPmUMmugAGhFFaTDBJsE9lRd9NNkRieex8wD8vq5MTahYK2UeRXAODvLE6NsZ/89ckvXlkMgAgRqlP/se9OTW0er/VNUu0M/eCAGXWgs0ji6it3/MAVY5tC4T5h/Inzn1NZQDa0XYMzCE2z5ionyuw/Vwk6rcqOsVqpxOYsLGMvcE++a64J8WJBoQHR9zSnStw++AarPQgqL0z9bQFbpopm2bw09FyY47tp4YvKl1kyYmPt0zbToit+D+r+WWQF0/1k1ikMaUt4SOhTRXHz/ZrzRW1jtnJOkYiCJ1qsxH8dH+2sS3dtrX1hzIZ+mKu4o+x1jsYClXc0P9p5x7bR1HGOH/cnsKLiBBly2QW";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private WebcamName webcam;
    @Override
    public void runOpMode() {
        List recognitions;

        vuforiaRoverRuckus = new VuforiaRoverRuckus();
        tfodRoverRuckus = new TfodRoverRuckus();

        // Put initialization blocks here.
        vuforiaRoverRuckus.initialize("", hardwareMap.get(WebcamName.class, "Webcam 1"), "teamwebcamcalibrations.xml",
                true, true, VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES,
                0, 0, 0, 0, 0, 0, true);tfodRoverRuckus.initialize(vuforiaRoverRuckus, (float)0.1, true, true);
        telemetry.addData(">", "Press Play to start");

        teamMarker = hardwareMap.servo.get("teamMarker");
        rightFront = hardwareMap.dcMotor.get("RightFront");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear = hardwareMap.dcMotor.get("RightRear");
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront = hardwareMap.dcMotor.get("LeftFront");
        leftRear = hardwareMap.dcMotor.get("LeftRear");
        rack = hardwareMap.dcMotor.get("rack");

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        magnet = hardwareMap.digitalChannel.get("magnet");
        /* Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();
        String position = "";
        if (opModeIsActive()) {
            tfodRoverRuckus.activate();
            // Put run blocks here.
            telemetry.addData("Status:", "IM READY");
            telemetry.update();
            resetTimer();
            while (moveTimer.time(TimeUnit.SECONDS) < 5) {
                if (tfodRoverRuckus != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfodRoverRuckus.getUpdatedRecognitions();
                    telemetry.addData("Status:", "Im doing stuff");
                    telemetry.update();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        telemetry.update();
                        if (updatedRecognitions.size() == 3) {
                            telemetry.addData("Status:", "I see some stuff");
                            telemetry.update();
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Left");
                                    position = "Left";
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    position = "Right";
                                } else {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    position = "Center";
                                }
                            }
                        }
                        telemetry.update();
                        resetTimer();
                        while (getTime() < 10000) {}
                    }
                }
                else {
                    telemetry.addData("Status:", "Something didn't work");
                    telemetry.update();
                }

            }
        }
            tfodRoverRuckus.deactivate();
        }
    private void resetTimer() {
        moveTimer.reset();
    }
    private boolean passCheck() {
        return opModeIsActive() && !isStopRequested();
    }
    private long getTime() {
        return moveTimer.time(TimeUnit.MILLISECONDS);
    }

}


