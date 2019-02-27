package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
@Disabled
@TeleOp(name = "minibot (Blocks to Java)", group = "")
public class minibot extends LinearOpMode {

  private DcMotor liftMotor;
  private DigitalChannel magnet;
  private DcMotor leftMotor;
  private DcMotor rightMotor;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    double limiter;

    liftMotor = hardwareMap.dcMotor.get("liftMotor");
    magnet = hardwareMap.digitalChannel.get("magnet");
    leftMotor = hardwareMap.dcMotor.get("leftMotor");
    rightMotor = hardwareMap.dcMotor.get("rightMotor");

    // Put initialization blocks here.
    waitForStart();
    liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    limiter = 2;
    if (opModeIsActive()) {
      // Put run blocks here.
      magnet.setState(true);
      while (opModeIsActive()) {
        leftMotor.setPower(gamepad1.left_stick_y / limiter);
        rightMotor.setPower(-(gamepad1.right_stick_y / limiter));
        if (gamepad1.right_trigger > 0.05) {
          liftMotor.setPower(gamepad1.right_trigger);
        } else if (gamepad1.left_trigger > 0.05) {
          liftMotor.setPower(-gamepad1.left_trigger);
        } else {
          liftMotor.setPower(0);
        }
        if (gamepad1.a) {
          limiter = limiter % 4 + 1;
        }
        telemetry.addData("magnet", magnet.getState());
        telemetry.update();
      }
    }
  }
}
