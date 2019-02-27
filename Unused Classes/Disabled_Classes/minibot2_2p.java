package org.firstinspires.ftc.teamcode.Disabled_Classes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.*;
@Disabled
@TeleOp(name = "minibotRestricted2p", group = "")
public class minibot2_2p extends LinearOpMode {

  private DcMotor liftMotor;
  private DigitalChannel magnet;
  private DcMotor leftMotor;
  private DcMotor rightMotor;
  
  private CRServo collection;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  //@Override
  private int[] speedLimits= {5,4,3,2,1};
  public void runOpMode() {
    int limiter=3;

    liftMotor = hardwareMap.dcMotor.get("liftMotor");
    magnet = hardwareMap.digitalChannel.get("magnet");
    leftMotor = hardwareMap.dcMotor.get("leftMotor");
    rightMotor = hardwareMap.dcMotor.get("rightMotor");
    
    collection = hardwareMap.crservo.get("collection");

    // Put initialization blocks here.
    waitForStart();
    liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    if (opModeIsActive()) {
      // Put run blocks here.
      magnet.setState(true);
      while (opModeIsActive()) {
        leftMotor.setPower(gamepad1.left_stick_y / speedLimits[limiter]);
        rightMotor.setPower(-(gamepad1.right_stick_y / speedLimits[limiter]));
        if (gamepad2.right_trigger > 0.05) {
          liftMotor.setPower(gamepad2.right_trigger);
        } else if (gamepad2.left_trigger > 0.05) {
          liftMotor.setPower(-gamepad2.left_trigger);
        } else {
          liftMotor.setPower(0);
        }
        if (gamepad1.dpad_up)
        {
          collection.setPower(1.0);
        }
        if (gamepad1.dpad_down)
        {
          collection.setPower(-1.0);
        }
        if (gamepad1.a) {
          switch(limiter){
            case 0:
              limiter=1;
            case 1:
              limiter=2;
              break;
            case 2:
              limiter=3;
              break;
            case 3:
              limiter=4;
              break;
            case 4:
              limiter=0;
            case 5:
              limiter=0;
            default:
              limiter=0;
              break;
          }
          if(limiter<0||limiter>=speedLimits.length)
          {
            limiter=0;
          }
        }
        telemetry.addData("magnet", magnet.getState());
        telemetry.addData("left", leftMotor.getPower());
        telemetry.addData("right", rightMotor.getPower());
        //telemetry.addData("collection", collection.getPosition());
        telemetry.update();
      }
    }
  }
}
