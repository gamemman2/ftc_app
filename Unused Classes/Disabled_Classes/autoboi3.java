/* Copyright (c) 2018 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
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

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous
@Disabled
public class autoboi3 extends LinearOpMode {
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

    private int turnTime;

    //private DistanceSensor rightDistance;
    //private DistanceSensor centerDistance;
    //private DistanceSensor leftDistance;

    private DigitalChannel magnet;

    private ElapsedTime moveTimer=new ElapsedTime(System.nanoTime());
    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "AQgDy6j/////AAABmXISaVgPmUMmugAGhFFaTDBJsE9lRd9NNkRieex8wD8vq5MTahYK2UeRXAODvLE6NsZ/89ckvXlkMgAgRqlP/se9OTW0er/VNUu0M/eCAGXWgs0ji6it3/MAVY5tC4T5h/Inzn1NZQDa0XYMzCE2z5ionyuw/Vwk6rcqOsVqpxOYsLGMvcE++a64J8WJBoQHR9zSnStw++AarPQgqL0z9bQFbpopm2bw09FyY47tp4YvKl1kyYmPt0zbToit+D+r+WWQF0/1k1ikMaUt4SOhTRXHz/ZrzRW1jtnJOkYiCJ1qsxH8dH+2sS3dtrX1hzIZ+mKu4o+x1jsYClXc0P9p5x7bR1HGOH/cnsKLiBBly2QW";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        rightFront = hardwareMap.dcMotor.get("RightFront");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear = hardwareMap.dcMotor.get("RightRear");
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront = hardwareMap.dcMotor.get("LeftFront");
        leftRear = hardwareMap.dcMotor.get("LeftRear");
        rack = hardwareMap.dcMotor.get("rack");
        magnet = hardwareMap.digitalChannel.get("magnet");

        turnTime = 1200;

        waitForStart();
        String position="Left";
        while(magnet.getState() && opModeIsActive() && !isStopRequested()) {
            rack.setPower(-0.5);
        }

        resetTimer();
        rack.setPower(0.0);

        while (getTime()<500&&passCheck())
        {
            moveMotors(-0.2,0.4);
        }
        resetTimer();
        while(getTime()<200&&passCheck())
        {
            moveMotors(-.3);
        }
        resetTimer();
        while (getTime()<1000&&passCheck())
        {
            moveMotors(-0.2,0.4);
        }
        moveMotors(0, 0);
        resetTimer();
        while (getTime()<1200&&passCheck())
        {
            rack.setPower(0.5);
        }
        resetTimer();
        rack.setPower(0.0);
        while (getTime()<turnTime&&passCheck())
        {
            moveMotors(0.4,-0.4);
        }
        moveMotors(0, 0);
        resetTimer();
        while (getTime()<500&&passCheck())
        {
            moveMotors(0, 0);
        }
        resetTimer();
        /*
        while (moveTimer.time()<500)
        {
            moveMotors(0.5);
        }
        resetTimer();
        while (moveTimer.time()<500)
        {
            moveMotors(0.2,-0.3);
        }
        */
        switch(position)
        {
            case "Left":
                resetTimer();
                while (getTime()<400&&passCheck())
                {
                    moveMotors(-0.4,0.4);
                }
                moveMotors(0, 0);
                resetTimer();
                while (getTime()<500&&passCheck())
                {
                    moveMotors(0, 0);
                }
                resetTimer();
                while (getTime()<2000&&passCheck())
                {
                    moveMotors(0.8,0.7);
                }
                moveMotors(0, 0);
                break;
            case "Center":
                resetTimer();
                while (getTime()<2000&&passCheck())
                {
                    moveMotors(0.8,0.7);
                }
                moveMotors(0, 0);
                break;
            case "Right":
                resetTimer();
                while (getTime()<400&&passCheck())
                {
                    moveMotors(0.4,-0.4);
                }
                moveMotors(0, 0);
                resetTimer();
                while (getTime()<500&&passCheck())
                {
                    moveMotors(0, 0);
                }
                resetTimer();
                while (getTime()<2000&&passCheck())
                {
                    moveMotors(0.8,0.7);
                }
                moveMotors(0, 0);
                break;
            default:
                resetTimer();
                while (getTime()<2000&&passCheck())
                {
                    moveMotors(0.8,0.7);
                }
                moveMotors(0, 0);
                break;
        }
        moveMotors(0, 0);
    }
    private boolean passCheck() {return opModeIsActive()&&!isStopRequested();}
    private void moveMotors(double power)
    {
        rightFront.setPower(power);
        rightRear.setPower(power);
        leftFront.setPower(power);
        leftFront.setPower(power);
    }
    private void moveMotors()
    {
        rightFront.setPower(0);
        rightRear.setPower(0);
        leftFront.setPower(0);
        leftFront.setPower(0);
    }
    private void moveMotors(double powerLeft, double powerRight)
    {
        rightFront.setPower(powerRight);
        rightRear.setPower(powerRight);
        leftFront.setPower(powerLeft);
        leftFront.setPower(powerLeft);
    }
    private long getTime()
    {
        return moveTimer.time(TimeUnit.MILLISECONDS);
    }

    private void resetTimer() {
        moveTimer.reset();
    }
}
