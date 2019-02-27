package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp(name = "linear_slide_10_26_18 (Blocks to Java)", group = "")
public class linear_slide_10_26_18 extends LinearOpMode {

  private DcMotor pully;
  private DcMotor linearmotion;
  private Servo bucketTilt;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    double left;
    double right;

    pully = hardwareMap.dcMotor.get("pully");
    linearmotion = hardwareMap.dcMotor.get("linearmotion");
    bucketTilt = hardwareMap.servo.get("bucketTilt");

    // Put initialization blocks here.
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        left = gamepad1.left_trigger * 0.05;
        right = gamepad1.right_trigger * 0.05;
        pully.setPower(gamepad1.left_stick_y / 5);
        linearmotion.setPower(gamepad1.right_stick_y / 5);
        bucketTilt.setPosition(bucketTilt.getPosition() + left);
        bucketTilt.setPosition(bucketTilt.getPosition() - right);
      }
    }
  }
}
