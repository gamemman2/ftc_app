package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
@Disabled
@Autonomous(name = "afterSampletowardsDepot (Blocks to Java)", group = "")
public class afterSampletowardsDepot extends LinearOpMode {

  private DcMotor RightFront;
  private DcMotor RightRear;
  private DcMotor LeftFront;
  private DcMotor LeftRear;

  ElapsedTime timer;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    RightFront = hardwareMap.dcMotor.get("RightFront");
    RightRear = hardwareMap.dcMotor.get("RightRear");
    LeftFront = hardwareMap.dcMotor.get("LeftFront");
    LeftRear = hardwareMap.dcMotor.get("LeftRear");

    RightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    RightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    LeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    LeftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    RightFront.setDirection(DcMotorSimple.Direction.REVERSE);
    RightRear.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      resetTimer();
      while (timer.seconds() < 1.0 && opModeIsActive()) {
        move();
      }
      resetTimer();
      while (timer.seconds() < 0.81 && opModeIsActive()) {
        turnL();
      }
      resetTimer();
      while (timer.seconds() < 1.2 && opModeIsActive()) {
        move();
      }
      resetTimer();
      while (timer.seconds() < 0.7 && opModeIsActive()) {
        stopo();
      }
      resetTimer();
      while (timer.seconds() < 0.45 && opModeIsActive()) {
        moveSlow();
      }
      resetTimer();
      while (timer.seconds() < 1 && opModeIsActive()) {
        turnR();
      }
      resetTimer();
      while (timer.seconds() < 0.6 && opModeIsActive()) {
        move();
      }
      resetTimer();
      while (timer.seconds() < 0.1 && opModeIsActive()) {
        turnR();
      }
      resetTimer();
      while (timer.seconds() < 1 && opModeIsActive()) {
        move();
      }
    }
  }

  /**
   * yoink
   */
  private void turnL() {
    RightFront.setPower(1);
    RightRear.setPower(1);
    LeftFront.setPower(-1);
    LeftRear.setPower(-1);
  }

  /**
   * boink
   */
  private void move() {
    RightFront.setPower(1);
    RightRear.setPower(1);
    LeftFront.setPower(1);
    LeftRear.setPower(1);
  }

  /**
   * Baddd
   * 
   */
  private void resetTimer() {
    timer = new ElapsedTime(System.nanoTime());
  }

  /**
   * Describe this function...
   */
  private void stopo() {
    RightFront.setPower(0);
    RightRear.setPower(0);
    LeftFront.setPower(0);
    LeftRear.setPower(0);
  }

  /**
   * Describe this function...
   */
  private void moveSlow() {
    RightFront.setPower(0.3);
    RightRear.setPower(0.3);
    LeftFront.setPower(0.3);
    LeftRear.setPower(0.3);
  }

  /**
   * Describe this function...
   */
  private void turnR() {
    RightFront.setPower(-1);
    RightRear.setPower(-1);
    LeftFront.setPower(1);
    LeftRear.setPower(1);
  }
}
