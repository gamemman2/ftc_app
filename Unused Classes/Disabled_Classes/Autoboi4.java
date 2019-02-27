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

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous
@Disabled
public class Autoboi4 extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private Gyroscope imu;
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

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
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
            teamMarker.setPosition(1);
            /* Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }
            resetTimer();
            while (moveTimer.time(TimeUnit.SECONDS) < 5) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
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
                    }
                }
            }
        }
        if (tfod != null) {
            tfod.shutdown();
        }
        telemetry.addData("Gem", position);
        telemetry.update();
        turnTime = 1100;
        while (magnet.getState() && opModeIsActive() && !isStopRequested()) {
            rack.setPower(-0.5);
        }
        resetTimer();
        rack.setPower(0.0);
        while (getTime() < 500 && passCheck()) {
            moveMotors(-0.2, 0.4);
        }
        resetTimer();
        while (getTime() < 200 && passCheck()) {
            moveMotors(-.3);
        }
        resetTimer();
        while (getTime() < 1000 && passCheck()) {
            moveMotors(-0.2, 0.4);
        }
        moveMotors(0, 0);
        resetTimer();
        while (getTime() < 1200 && passCheck()) {
            rack.setPower(0.5);
        }
        resetTimer();
        rack.setPower(0.0);
        while (getTime() < turnTime && passCheck()) {
            moveMotors(0.4, -0.4);
        }
        moveMotors(0, 0);
        resetTimer();
        while (getTime() < 500 && passCheck()) {
            moveMotors(0, 0);
        }
        resetTimer();
        switch (position) {
            case "Left":
                resetTimer();
                while (getTime() < 450 && passCheck()) {
                    moveMotors(-0.4, 0.4);
                }
                moveMotors(0, 0);
                resetTimer();
                while (getTime() < 500 && passCheck()) {
                    moveMotors(0, 0);
                }
                resetTimer();
                while (getTime() < 2000 && passCheck()) {
                    moveMotors(0.8, 0.7);
                }
                moveMotors(0, 0);
                break;
            case "Center":
                resetTimer();
                while (getTime() < 2000 && passCheck()) {
                    moveMotors(0.8, 0.7);
                }
                moveMotors(0, 0);
                break;
            case "Right":
                resetTimer();
                while (getTime() < 470 && passCheck()) {
                    moveMotors(0.4, -0.4);
                }
                moveMotors(0, 0);
                resetTimer();
                while (getTime() < 500 && passCheck()) {
                    moveMotors(0, 0);
                }
                resetTimer();
                while (getTime() < 2000 && passCheck()) {
                    moveMotors(0.8, 0.7);
                }
                moveMotors(0, 0);
                break;
            default:
                resetTimer();
                while (getTime() < 2000 && passCheck()) {
                    moveMotors(0.8, 0.7);
                }
                moveMotors(0, 0);
                break;
        }
        moveMotors(0, 0);
        teamMarker.setPosition(0.3);
    }
    private void moveMotors(double power) {
        rightFront.setPower(power);
        rightRear.setPower(power);
        leftFront.setPower(power);
        leftFront.setPower(power);
    }
    private void moveMotors() {
        rightFront.setPower(0);
        rightRear.setPower(0);
        leftFront.setPower(0);
        leftFront.setPower(0);
    }
    private void moveMotors(double powerLeft, double powerRight) {
        rightFront.setPower(powerRight);
        rightRear.setPower(powerRight);
        leftFront.setPower(powerLeft);
        leftFront.setPower(powerLeft);
    }
    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }
    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
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