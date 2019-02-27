package org.firstinspires.ftc.teamcode.Disabled_Classes;

import android.graphics.Color;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.List;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
@Disabled
@Autonomous(name = "colormoving2 (Blocks to Java)", group = "")
public class IANE2 extends LinearOpMode {

  private ColorSensor coloras;

  double numwhite;
  List<Boolean> data = new ArrayList<>();

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    boolean result;
    double red_threshold;
    double percent_error;
    float color;

    coloras = hardwareMap.colorSensor.get("coloras");

    // Put initialization blocks here.
    waitForStart();
    red_threshold = 160;
    percent_error = 0.7;
    color = JavaUtil.colorToHue(Color.argb(coloras.alpha(), coloras.red(), coloras.green(), coloras.blue()));
    telemetry.addData("rd", color);
    telemetry.update();
    if (opModeIsActive()) {
      result = redtest();
      telemetry.addData("White?", result);
      telemetry.addData("numwhite", numwhite);
      telemetry.addData("len", data.size());
      telemetry.addData("dd", numwhite / data.size() > percent_error);
      telemetry.update();
    }
    while (opModeIsActive()) {
    }
  }

  /**
   * Describe this function...
   */
  private boolean redtest() {
    ElapsedTime whitetimer;

    numwhite = 0;
    whitetimer = new ElapsedTime(System.nanoTime());
    //data = JavaUtil.createListWith();
    while (whitetimer.time() <= 3 && opModeIsActive()) {
      data.add(true);
    }
    for (int i=0;i<data.size();i++) {
      if (data.get(i)) {
        numwhite = numwhite + 1;
      }
    }
    telemetry.addData("white", numwhite);
    telemetry.update();
    return numwhite < data.size() / 2;
  }
}
