/*
package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@Autonomous(name = "colormoving (Blocks to Java)", group = "")
public class colormoving extends LinearOpMode {

  private ColorSensor color;

  double numwhite;
  List data;

  
  @Override
  public void runOpMode() {
    boolean result;
    double red_threshold;
    double percent_error;
    float color2;

    color = hardwareMap.colorSensor.get("color");
    // Put initialization blocks here.
    waitForStart();
    red_threshold = 160;
    percent_error = 0.7;
    color2 = JavaUtil.colorToHue(Color.argb(color.alpha(), color.red(), color.green(), color.blue()));
    telemetry.addData("rd", color2);
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


  private boolean redtest() {
    ElapsedTime whitetimer;

    numwhite = 0;
    whitetimer = new ElapsedTime(System.nanoTime());
    data = JavaUtil.createListWith();
    while (whitetimer.time() <= 3 && opModeIsActive()) {
      JavaUtil.inListSet(data, JavaUtil.AtMode.LAST, 0, true, color.red() < 52);
      telemetry.addData("key", color.red());
      telemetry.addData("rd", JavaUtil.colorToValue(Color.argb(color.alpha(), color.red(), color.green(), color.blue())));
      telemetry.update();
    }
    for (boolean i : data) {
      if (i) {
        numwhite = numwhite + 1;
      }
    }
    telemetry.addData("white", numwhite);
    telemetry.update();
    return numwhite < data.size() / 2;
  }
}
*/