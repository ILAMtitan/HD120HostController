import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import controlP5.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class HostController extends PApplet {





ControlP5 cp5;
ControlP5 HueShiftCP;
ControlP5 SingleSpinnerCP;
ControlP5 RainbowCP;
ControlP5 FourPointCP;
ControlP5 DoubleScanCP;
ControlP5 DoubleSpinnerCP;
ControlP5 BPMCP;
ControlP5 SplitSidesCP;
ControlP5 SplitQuartersCP;
ControlP5 RGBCP;
ControlP5 FadeToBlackCP;

Textarea Status;

Serial LEDPort;  // Create object from Serial class
final int BAUD_RATE = 115200;

//Add more fans here if you have more than four
List FanText = Arrays.asList("1", "2", "3", "4");

//List of potential modes
List ModeText = Arrays.asList("Hue Shift",
                              "Single Spinner",
                              "Rainbow",
                              "Four-Point Spinner",
                              "Double-Scan",
                              "Double Spinner",
                              "BPM",
                              "Split Sides",
                              "Split Quarters", 
                              "RGB",
                              "Fade to Black"
                              );

//Mode Dependent Settings
//many of these are common across modes
int StartingHue = 128;
int EndingHue = 128;
int HueOffset = 128;
int PhaseOffset = 0;
int Speed = 60;
int Clockwise = 0;
int RainbowMode = 0;
int ShiftRate = 60;
int BladeOffset = 0;
int FadeSpeed = 60;
int SparkleChance = 0;
int HueSteps = 21;
int BladeShift = 0;
int RotationOffset = 0;
int ShiftBPM = 60;
int HueMultiple = 1;
int BeatMultiple = 1;
int BPM = 60;
int WSideHue = 60;
int ESideHue = 120;
int PerSideOffset = 0;
int PulseShape = 0;
int NWSideHue = 0;
int NESideHue = 60;
int SESideHue = 120;
int SWSideHue = 180;
int _R = 128;
int _G = 128;
int _B = 128;
int FadeRate = 10;

//more lazy variables
int SelectedModeNum;
int SelectedFan;

public void setup() 
{
  
  
  cp5 = new ControlP5(this);
  HueShiftCP = new ControlP5(this);
  SingleSpinnerCP = new ControlP5(this);
  RainbowCP = new ControlP5(this);
  FourPointCP = new ControlP5(this);
  DoubleScanCP = new ControlP5(this);
  DoubleSpinnerCP = new ControlP5(this);
  BPMCP = new ControlP5(this);
  SplitSidesCP = new ControlP5(this);
  SplitQuartersCP = new ControlP5(this);
  RGBCP = new ControlP5(this);
  FadeToBlackCP = new ControlP5(this);

  SetupControls();
                
  background(0);
  
}

public void draw() {
  
  background(0);
  
}

public void SetupControls(){
  List ports = Arrays.asList(Serial.list());
  
  // create DropdownList to select the com port
  cp5.addScrollableList("COMPort")
     .setPosition(100, 100)
     .setSize(200, 100)
     .setBarHeight(20)
     .setItemHeight(20)
     .addItems(ports)
     .setType(ScrollableList.DROPDOWN)
     ;
     
  // create DropdownList to select the Fan #
  cp5.addScrollableList("FANNumber")
     .setPosition(100, 200)
     .setSize(200, 100)
     .setBarHeight(20)
     .setItemHeight(20)
     .addItems(FanText)
     .setType(ScrollableList.DROPDOWN)
     ;
     
     
  // create DropdownList to select the Mode
  cp5.addScrollableList("MODENumber")
     .setPosition(100, 300)
     .setSize(200, 100)
     .setBarHeight(20)
     .setItemHeight(20)
     .addItems(ModeText)
     .setType(ScrollableList.DROPDOWN)
     ;
  
  // Create a status text box
  Status = cp5.addTextarea("txt")
                .setPosition(500,100)
                .setSize(200,20)
                .setFont(createFont("arial",12))
                .setLineHeight(12)
                .setColor(color(128))
                .setColorBackground(color(255,100))
                .setColorForeground(color(255,100));
                ;
                
  // create a new button with name 'Send'
  cp5.addButton("Send")
     .setValue(128)
     .setPosition(100,700)
     .setSize(200,19)
     ;
  
  // create a new button with name 'SetDefault'
  cp5.addButton("SetDefault")
     .setValue(128)
     .setPosition(500,700)
     .setSize(200,19)
     ;
     
  DrawHueShiftSettings();
  DrawSingleSpinnerSettings();
  DrawRainbowSettings();
  DrawFourPointSettings();
  DrawDoubleScanSettings();
  DrawDoubleSpinnerSettings();
  DrawBPMSettings();
  DrawSplitSidesSettings();
  DrawSplitQuartersSettings();
  DrawRGBSettings();
  DrawFadeToBlackSettings();
  
  HideSettings();
}

public void DrawHueShiftSettings(){
  
  //-- mode0() -----------------------------------------------------------------------------------------------
  // Hue Shift
  // Settings: 1 = Starting Hue; 2 = Ending Hue; 3 = Hue Offset; 5 = Phase Offset; 7 = Rate in BPM
  
  HueShiftCP.addSlider("StartingHue")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  HueShiftCP.addSlider("EndingHue")
     .setPosition(500,250)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  HueShiftCP.addSlider("HueOffset")
     .setPosition(500,300)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  HueShiftCP.addSlider("PhaseOffset")
     .setPosition(500,350)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  HueShiftCP.addSlider("Speed")
     .setPosition(500,400)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
}

public void DrawSingleSpinnerSettings(){
  
  // Single Spinner
  // Settings: 1 = Hue (Overridden by 3); 2 = 0 -> Clockwise, 1+ -> Counterclockwise;
  // 3 = Rainbowmode? 0 -> No, 1+ -> Rainbow; 4 = BPM of Rainbow shift;
  // 5 = Blade Offset; 6 = Fade Speed (good to match BPM); 7 = Spin Speed BPM
  
  SingleSpinnerCP.addSlider("StartingHue")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
  
  SingleSpinnerCP.addToggle("Clockwise")
     .setPosition(500,250)
     .setSize(50,20)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
     
  SingleSpinnerCP.addToggle("RainbowMode")
     .setPosition(500,300)
     .setSize(50,20)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
     
  SingleSpinnerCP.addSlider("ShiftRate")
     .setPosition(500,350)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
  SingleSpinnerCP.addSlider("BladeOffset")
     .setPosition(500,400)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SingleSpinnerCP.addSlider("FadeSpeed")
     .setPosition(500,450)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
  SingleSpinnerCP.addSlider("Speed")
     .setPosition(500,500)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
}

public void DrawRainbowSettings(){
  
  // Rainbow
  // Settings: 1 = Chance of Sparkles (0-255); 2 = Hue Steps per LED -- 21 shows a full rainbow; 7 = Speed of Rotation
  
  RainbowCP.addSlider("SparkleChance")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  RainbowCP.addSlider("HueSteps")
     .setPosition(500,250)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
  RainbowCP.addSlider("Speed")
     .setPosition(500,300)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
}

public void DrawFourPointSettings(){
  
  // Four-point spinner
  // Settings: 1 = Hue (Overridden by 3); 2 = 0 -> Clockwise, 1+ -> Counterclockwise;
  // 3 = Rainbowmode? 0 -> No, 1+ -> Rainbow;
  // 4 = BPM of Rainbow shift; 5 = Per Blade Hue Shift; 6 = Fade Speed (good to match twice BPM);
  // 7 = Spin Speed BPM
  
  FourPointCP.addSlider("StartingHue")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  FourPointCP.addToggle("Clockwise")
     .setPosition(500,250)
     .setSize(50,20)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
     
  FourPointCP.addToggle("RainbowMode")
     .setPosition(500,300)
     .setSize(50,20)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
  
  FourPointCP.addSlider("ShiftRate")
     .setPosition(500,350)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
  FourPointCP.addSlider("BladeShift")
     .setPosition(500,400)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  FourPointCP.addSlider("FadeSpeed")
     .setPosition(500,450)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
  FourPointCP.addSlider("Speed")
     .setPosition(500,500)
     .setSize(150,20)
     .setRange(0,120)
     ;
  
}

public void DrawDoubleScanSettings(){
  
  //Double-scan
  // Settings: 1 = Hue (Overridden by 3); 2 = Rotation Offset; 3 = Rainbowmode? 0 -> No, 1 -> Rainbow
  // 4 = BPM of Rainbow shift; 5 = Per Blade Hue Shift; 6 = Fade Speed (good to match BPM);
  // 7 = Spin Speed BPM
  
  DoubleScanCP.addSlider("StartingHue")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  DoubleScanCP.addSlider("RotationOffset")
     .setPosition(500,250)
     .setSize(150,20)
     .setRange(0,20)
     ;
     
  DoubleScanCP.addToggle("RainbowMode")
     .setPosition(500,300)
     .setSize(50,20)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
     
  DoubleScanCP.addSlider("ShiftBPM")
     .setPosition(500,350)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
  DoubleScanCP.addSlider("BladeOffset")
     .setPosition(500,400)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  DoubleScanCP.addSlider("FadeSpeed")
     .setPosition(500,450)
     .setSize(150,20)
     .setRange(0,120)
     ;
  
  DoubleScanCP.addSlider("Speed")
     .setPosition(500,500)
     .setSize(150,20)
     .setRange(0,120)
     ;

}

public void DrawDoubleSpinnerSettings(){
  
  // Double Spinner
  // Settings: 1 = Hue (Overridden by 3); 2 = 0 -> Clockwise, 1+ -> Counterclockwise;
  // 3 = Rainbowmode? 0 -> No, 1+ -> Rainbow;
  // 4 = BPM of Rainbow shift; 5 = Per Blade Hue Shift; 6 = Fade Speed (good to match BPM);
  // 7 = Spin Speed BPM
  
  DoubleSpinnerCP.addSlider("StartingHue")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  DoubleSpinnerCP.addToggle("Clockwise")
     .setPosition(500,250)
     .setSize(50,20)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
     
  DoubleSpinnerCP.addToggle("RainbowMode")
     .setPosition(500,300)
     .setSize(50,20)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
     
  DoubleSpinnerCP.addSlider("ShiftBPM")
     .setPosition(500,350)
     .setSize(150,20)
     .setRange(0,120)
     ;
     
  DoubleSpinnerCP.addSlider("BladeOffset")
     .setPosition(500,400)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  DoubleSpinnerCP.addSlider("FadeSpeed")
     .setPosition(500,450)
     .setSize(150,20)
     .setRange(0,120)
     ;
  
  DoubleSpinnerCP.addSlider("Speed")
     .setPosition(500,500)
     .setSize(150,20)
     .setRange(0,120)
     ;

}

public void DrawBPMSettings(){
  
  // BPM from 100-line demo
  // Settings: 7 = BPM; 1 = Hue multiplier; 2 = Beat multiplier
  
  BPMCP.addSlider("HueMultiple")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,15)
     ;
  
  BPMCP.addSlider("BeatMultiple")
     .setPosition(500,250)
     .setSize(150,20)
     .setRange(0,15)
     ;
  
  BPMCP.addSlider("BPM")
     .setPosition(500,300)
     .setSize(150,20)
     .setRange(0,255)
     ;
}

public void DrawSplitSidesSettings(){
  
  // Split Sides
  // Settings: 7 = BPM of PulseShape; 1 = W Side hue; 2 = E Side hue; 4 = Fan Phase Offset; 5 = Per side phase offset;
  // 6 = PulseShape? 0 -> No, 1 -> Sin, 2 -> Sawtooth In, 3 -> Sawtooth Out, 4+ -> Triangle
  
  SplitSidesCP.addSlider("WSideHue")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitSidesCP.addSlider("ESideHue")
     .setPosition(500,250)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitSidesCP.addSlider("PhaseOffset")
     .setPosition(500,300)
     .setSize(150,20)
     .setRange(0,255)
     ;

  SplitSidesCP.addSlider("PerSideOffset")
     .setPosition(500,350)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitSidesCP.addSlider("PulseShape")
     .setPosition(500,400)
     .setSize(150,20)
     .setRange(0,4)
     .setNumberOfTickMarks(5)
     ;
     
  SplitSidesCP.addSlider("BPM")
     .setPosition(500,450)
     .setSize(150,20)
     .setRange(0,120)
     ;
      
}

public void DrawSplitQuartersSettings(){
  
  // Split Quarters
  // Settings: 1 = NW Side hue; 2 = NE Side hue; 3 = SE Side hue; 4 = SW Side hue;
  // 5 = Per side phase offset;
  // 6 = PulseShape? 0 -> No, 1 -> Sin, 2 -> Sawtooth In, 3 -> Sawtooth Out, 4+ -> Triangle
  // 7 = BPM of PulseShape;
  
  SplitQuartersCP.addSlider("NWSideHue")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitQuartersCP.addSlider("NESideHue")
     .setPosition(500,250)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitQuartersCP.addSlider("SESideHue")
     .setPosition(500,300)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitQuartersCP.addSlider("SWSideHue")
     .setPosition(500,350)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitQuartersCP.addSlider("PerSideOffset")
     .setPosition(500,400)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  SplitQuartersCP.addSlider("PulseShape")
     .setPosition(500,450)
     .setSize(150,20)
     .setRange(0,4)
     .setNumberOfTickMarks(5)
     ;
     
  SplitQuartersCP.addSlider("BPM")
     .setPosition(500,500)
     .setSize(150,20)
     .setRange(0,120)
     ;

}

public void DrawRGBSettings(){
  
  // Set RGB Color
  // 1 = R; 2 = G; 3 = B
  
  RGBCP.addSlider("_R")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;
  
  RGBCP.addSlider("_G")
     .setPosition(500,250)
     .setSize(150,20)
     .setRange(0,255)
     ;
     
  RGBCP.addSlider("_B")
     .setPosition(500,300)
     .setSize(150,20)
     .setRange(0,255)
     ;

}

public void DrawFadeToBlackSettings(){
  
  // Fade to Black (No further color changes)
  // Settings: 1 = Fade Rate
  
  FadeToBlackCP.addSlider("FadeRate")
     .setPosition(500,200)
     .setSize(150,20)
     .setRange(0,255)
     ;

}

public void HideSettings(){
  
  HueShiftCP.hide();
  SingleSpinnerCP.hide();
  RainbowCP.hide();
  FourPointCP.hide();
  DoubleScanCP.hide();
  DoubleSpinnerCP.hide();
  BPMCP.hide();
  SplitSidesCP.hide();
  SplitQuartersCP.hide();
  RGBCP.hide();
  FadeToBlackCP.hide();
  
  background(0);
  
}


public void COMPort(int n) {
  
  String SelectedPort = cp5.get(ScrollableList.class, "COMPort").getItem(n).get("text").toString() ;
  
  //Set COM Port for application
  if(LEDPort != null){
      LEDPort.stop();
      LEDPort = null;
    }
  
  try{
      LEDPort = new Serial(this,SelectedPort,BAUD_RATE);
      Status.setText("connected to " + SelectedPort);
    }catch(Exception e){
      System.err.println("Error opening serial port " + SelectedPort);
      Status.setText("Failed to Connect");
      e.printStackTrace();
    }
  
}

public void FANNumber(int n) {
  
  //Fetch the selected value from the element
  //By deafult, the value field is indexed from 0, and we need it from 1
  SelectedFan = PApplet.parseInt(cp5.get(ScrollableList.class, "FANNumber").getValue()+1);
  
  Status.setText("Fan " + SelectedFan + " selected");
  
}

public void MODENumber(int n) {
  
  //Fetch the selected value from the element
  //By deafult, the value field is indexed from 0, which is sufficient here
  SelectedModeNum = PApplet.parseInt(cp5.get(ScrollableList.class, "MODENumber").getValue());
  
  //Fetch the selected text
  String SelectedMode = cp5.get(ScrollableList.class, "MODENumber").getItem(n).get("text").toString();
  
  Status.setText(SelectedMode + " mode selected");
  
  switch (SelectedModeNum) {
    case 0:
      HideSettings();
      HueShiftCP.show();
      break;
    case 1:
      HideSettings();
      SingleSpinnerCP.show();
      break;
    case 2:
      HideSettings();
      RainbowCP.show();
      break;
    case 3:
      HideSettings();
      FourPointCP.show();
      break;
    case 4:
      HideSettings();
      DoubleScanCP.show();
      break;
    case 5:
      HideSettings();
      DoubleSpinnerCP.show();
      break;
    case 6:
      HideSettings();
      BPMCP.show();
      break;
    case 7:
      HideSettings();
      SplitSidesCP.show();
      break;
    case 8:
      HideSettings();
      SplitQuartersCP.show();
      break;
    case 9:
      HideSettings();
      RGBCP.show();
      break;
    case 10:
      HideSettings();
      FadeToBlackCP.show();
      break;
    default:
      break;
  }
  
}

public void Send() {
  
  String TransmitString = null;
  
  switch (SelectedModeNum) {
    case 0:
      TransmitString = (">" + SelectedFan + ".0.0" + 
                        ">" + SelectedFan + ".1." + StartingHue +
                        ">" + SelectedFan + ".2." + EndingHue +
                        ">" + SelectedFan + ".3." + HueOffset +
                        ">" + SelectedFan + ".5." + PhaseOffset +
                        ">" + SelectedFan + ".7." + Speed );
      break;
    case 1:
      TransmitString = (">" + SelectedFan + ".0.1" + 
                        ">" + SelectedFan + ".1." + StartingHue +
                        ">" + SelectedFan + ".2." + Clockwise +
                        ">" + SelectedFan + ".3." + RainbowMode +
                        ">" + SelectedFan + ".4." + ShiftRate +
                        ">" + SelectedFan + ".5." + BladeOffset +
                        ">" + SelectedFan + ".6." + FadeSpeed +
                        ">" + SelectedFan + ".7." + Speed );
      break;
    case 2:
      TransmitString = (">" + SelectedFan + ".0.2" + 
                        ">" + SelectedFan + ".1." + SparkleChance +
                        ">" + SelectedFan + ".2." + HueSteps +
                        ">" + SelectedFan + ".7." + Speed );
      break;
    case 3:
      TransmitString = (">" + SelectedFan + ".0.3" + 
                        ">" + SelectedFan + ".1." + StartingHue +
                        ">" + SelectedFan + ".2." + Clockwise +
                        ">" + SelectedFan + ".3." + RainbowMode +
                        ">" + SelectedFan + ".4." + ShiftRate +
                        ">" + SelectedFan + ".5." + BladeShift +
                        ">" + SelectedFan + ".6." + FadeSpeed +
                        ">" + SelectedFan + ".7." + Speed );
      break;
    case 4:
      TransmitString = (">" + SelectedFan + ".0.4" + 
                        ">" + SelectedFan + ".1." + StartingHue +
                        ">" + SelectedFan + ".2." + RotationOffset +
                        ">" + SelectedFan + ".3." + RainbowMode +
                        ">" + SelectedFan + ".4." + ShiftBPM +
                        ">" + SelectedFan + ".5." + BladeOffset +
                        ">" + SelectedFan + ".6." + FadeSpeed +
                        ">" + SelectedFan + ".7." + Speed );
      break;  
    case 5:
      TransmitString = (">" + SelectedFan + ".0.5" + 
                        ">" + SelectedFan + ".1." + StartingHue +
                        ">" + SelectedFan + ".2." + Clockwise +
                        ">" + SelectedFan + ".3." + RainbowMode +
                        ">" + SelectedFan + ".4." + ShiftBPM +
                        ">" + SelectedFan + ".5." + BladeOffset +
                        ">" + SelectedFan + ".6." + FadeSpeed +
                        ">" + SelectedFan + ".7." + Speed );
      break;  
    case 6:
      TransmitString = (">" + SelectedFan + ".0.6" + 
                        ">" + SelectedFan + ".1." + HueMultiple +
                        ">" + SelectedFan + ".2." + BeatMultiple +
                        ">" + SelectedFan + ".7." + BPM );
      break; 
    case 7:
      TransmitString = (">" + SelectedFan + ".0.7" + 
                        ">" + SelectedFan + ".1." + WSideHue +
                        ">" + SelectedFan + ".2." + ESideHue +
                        ">" + SelectedFan + ".4." + PhaseOffset +
                        ">" + SelectedFan + ".5." + PerSideOffset +
                        ">" + SelectedFan + ".6." + PulseShape +
                        ">" + SelectedFan + ".7." + BPM );
      break;  
    case 8:
      TransmitString = (">" + SelectedFan + ".0.8" + 
                        ">" + SelectedFan + ".1." + NWSideHue +
                        ">" + SelectedFan + ".2." + NESideHue +
                        ">" + SelectedFan + ".3." + SESideHue +
                        ">" + SelectedFan + ".4." + SWSideHue +
                        ">" + SelectedFan + ".5." + PerSideOffset +
                        ">" + SelectedFan + ".6." + PulseShape +
                        ">" + SelectedFan + ".7." +BPM );
      break;  
    case 9:
      TransmitString = (">" + SelectedFan + ".0.9" + 
                        ">" + SelectedFan + ".1." + _R +
                        ">" + SelectedFan + ".2." + _G +
                        ">" + SelectedFan + ".3." + _B );
      break;  
    case 10:
      TransmitString = (">" + SelectedFan + ".0.10" + 
                        ">" + SelectedFan + ".1." + FadeRate );
      break; 
    default:
      break;
  }
  
  println(TransmitString);
  
  if(LEDPort == null){
    Status.setText("Controller Not Connected");
  }
  else{
    if(TransmitString == null){
      Status.setText("Config Incomplete");
    }
    else{
      LEDPort.write(TransmitString);
      Status.setText("Sending Config to Controller");
    }
  }

}

public void SetDefault(){
  
  if(LEDPort == null){
    Status.setText("Controller Not Connected");
  }
  else{
    LEDPort.write("!");
    Status.setText("Default Config Set");
  }
  
}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "HostController" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
