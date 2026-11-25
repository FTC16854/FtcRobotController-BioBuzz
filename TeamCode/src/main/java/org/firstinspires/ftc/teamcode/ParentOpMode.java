/* Copyright (c) 2017 FIRST. All rights reserved.
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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

/**
 * Original FTC opmode header block
 *
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 **/

/** Parent OpMode Class
 * All Teleop and Autonomous OpModes should inherit from (extend) ParentOpMode.
 * Each child/subclass OpMode should have its own unique runOpMode() method that will
 * override the ParentOpMode runOpMode() method.
 **/

@TeleOp(name="Parent Opmode", group="Linear Opmode")
@Disabled
public class ParentOpMode extends LinearOpMode {

    // Declare OpMode members, hardware variables
    public ElapsedTime runtime = new ElapsedTime();
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private DcMotor leftfrunt = null;
    private DcMotor leftback = null;

    private DcMotor intake = null;
    private DcMotorEx Shoter = null;

    private CRServo ballloder = null;

    private DigitalChannel countSwitch = null;

    private Servo tinylight= null;

    GoBildaPinpointDriver pinpoint;

    //Other Global Variables
    //put global variables here...

    //Shooter Variables
    double targetVelocity =1000;//pulses per second
    double PPR=28;//pulses per revolution

    double ballloderspeed=0.8;

    //Intake
    double speedintake=0.7;
    double reversspeedintske=0.2;
    // Ball Counter
    public ElapsedTime ballCountTimer = new ElapsedTime();
    double debouncetime=35;//ms
    int ballcount=0;
    boolean currentSwitchState=false;
    boolean previousSwitchState=false;

    // odometry
    Pose2D pose2D;
    double robotwhy=0;
    double robotx=0;
    double headinghi=0;

    //light colors
    double blue= 0.611;
    double green= 0.500;
    double red= 0.277;
    double yellow= 0.388;
    double orange= 0.333;
    double violet= 0.722;
    double white= 1.0;
    double off= 0.0;
    public void initialize(){
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Driver Station app or Driver Hub).
        rightFront = hardwareMap.get(DcMotor.class, "rf_drive");
        rightBack = hardwareMap.get(DcMotor.class, "rb_drive");
        leftback = hardwareMap. get(DcMotor.class,"lb_drive");
        leftfrunt = hardwareMap. get(DcMotor.class,"lf_drive");


        intake = hardwareMap. get(DcMotor.class,"intake");
        Shoter = hardwareMap. get (DcMotorEx.class,"shouter");

        ballloder = hardwareMap. get(CRServo.class,"ballloder");

        countSwitch = hardwareMap. get (DigitalChannel.class, "count_switch");
        tinylight = hardwareMap.get (Servo.class,"tinylight");


        //Set motor run modes
        Shoter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Set Motor  and servo Directions
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftfrunt.setDirection(DcMotor.Direction.FORWARD);
        leftback.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);
        Shoter.setDirection(DcMotor.Direction.FORWARD);
        ballloder.setDirection(DcMotorSimple.Direction.FORWARD);

        //Set brake or coast modes.
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //BRAKE or FLOAT (Coast)
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftfrunt.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Shoter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set Digital I/O Modes
        countSwitch.setMode(DigitalChannel.Mode.INPUT);

        pinpointinit();

        //Update Driver Station Status Message after init
        telemetry.addData("Status:", "Initialized");
        telemetry.update();
    }

    /**
     * runOpMode() will be overridden in child OpMode.
     * Basic structure should remain intact (init, wait for start, while(opModeIsActive),
     * Additionally, Emergency conditions should be checked during every cycle
     */
    @Override
    public void runOpMode() {

        initialize();

        // Init loop - optional
        while(opModeInInit()){
            // Code in here will loop continuously until OpMode is started
        }

        // Wait for the game to start (driver presses PLAY) - May not be needed if using an init Loop
        waitForStart();
        runtime.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // code here should never actually execute in parent opmode.
            // This function will be overridden by child opmode classes


            //include emergency stop check in all runOpMode() functions/methods
            //implementation depends on which E-stop function will be used (boolean/void)

            checkEmergencyStop(); // Stops motors and Terminates if buttons are pressed

            telemetry.update();
        }
    }

    /*****************************/
    //Controls should be mapped here to avoid accessing gamepad directly in other functions/methods
    //This also makes it simpler to re-map controls as desired
    //CONTROLLER MAP

    // Thumbsticks
    public double left_sticky_x(){
        return gamepad1.left_stick_x;
    }
    public double left_sticky_y() { return -gamepad1.left_stick_y;}
    public double right_sticky_x() {return gamepad1.right_stick_x;}
    public double right_sticky_y() {return -gamepad1.right_stick_y;}





    // Buttons
    public boolean emergencyButtons(){
        // check for combination of buttons to be pressed before returning true
        return (gamepad1.y && gamepad1.b) || (gamepad2.y && gamepad2.b);

    }

    public boolean flywheelButton(){
        return gamepad1.x;
    }
    public boolean shooterspeedupbutton(){return gamepad1.dpad_up;}
    public boolean shooterslowdownbutton(){return gamepad1.dpad_down;}
    public boolean intakeButton(){return gamepad1.right_trigger_pressed;}
    public boolean intakeButtonrevers(){return gamepad1.left_trigger_pressed;}

    public boolean ballloderButton(){return gamepad1.right_bumper;}

    public boolean couterresetbutton(){return gamepad1.left_stick_button;}

    /****************************/
    // Emergency Stop Functions

    public void checkEmergencyStop(){
        if(emergencyButtons()){
            //stop all motors, servos, etc.

            stopDrive();
            terminateOpModeNow();   // Force exit of OpMode
        }
    }



    /*****************************/
    //Drive Methods

    // Assign left and right drive speed using arguments/parameters rather than hardcoding
    // thumb stick values inside function body. This will allow tank drive to be reused for
    // autonomous programs without additional work
    public void tankdrive(double left, double right){
        leftfrunt.setPower(left);
        leftback.setPower(left);
        rightFront.setPower(right);
        rightBack.setPower(right);
    }

    public void stopDrive(){
            tankdrive(0,0);
    }

    public void mecanumDrive(double Robotspeed,double Driveangle,double rotationspeed, double Angaleofset){
        Driveangle = Math.toRadians(Driveangle- Angaleofset) ;

        double vlf =(Robotspeed * Math.cos(Driveangle + (Math.PI / 4)) + rotationspeed);
        double vlb =(Robotspeed * Math.sin(Driveangle + (Math.PI / 4)) + rotationspeed);
        double vrf =(Robotspeed * Math.sin(Driveangle + (Math.PI / 4)) - rotationspeed);
        double vrb =(Robotspeed * Math.cos(Driveangle + (Math.PI / 4)) - rotationspeed);

        leftback.setPower(vlb);
        leftfrunt.setPower(vlf);
        rightBack.setPower(vrb);
        rightFront.setPower(vrf);
    }

   public void manuwalMecanumDerive()
    {

        double thumstikspeed= Math.hypot(left_sticky_y(),left_sticky_x());
        double thumsyikangle= Math.atan2(left_sticky_y(),left_sticky_x());
        double rotashonhi= right_sticky_x();

        mecanumDrive(thumstikspeed,thumsyikangle,rotashonhi,headinghi);

    }


    /*****************************/
    //More Methods (Functions)

    public void RunShoter(){
        double TargetRPM=((targetVelocity)/(PPR))*(60);
        double RPM=((Shoter.getVelocity())/(PPR))*(60);

        if (flywheelButton()){
            Shoter.setVelocity(targetVelocity);
        }
        else {
            Shoter.setVelocity(0);
        }

        if (shooterspeedupbutton()) {
            targetVelocity = targetVelocity +5;
        } else if (shooterslowdownbutton()) {
            targetVelocity = targetVelocity -5;
        }

        telemetry.addData("velocity", Shoter.getVelocity());
        telemetry.addData("target velocity", targetVelocity);
        telemetry.addData("RPM",RPM);
        telemetry.addData("Target RPM",TargetRPM);
    }

    public void intakeBoss(){
        if(intakeButton()){
            intake.setPower(speedintake);
            telemetry.addData("Intake:", speedintake);
        } else if (intakeButtonrevers()) {
            intake.setPower(-reversspeedintske);
            telemetry.addData("Intake:", speedintake);
        }
        else{
            intake.setPower(0);
            telemetry.addData("Intake:", 0);
        }

    }

    public void launchr(){
        if (ballloderButton()){
            ballloder.setPower(ballloderspeed);
        }
        else {
            ballloder.setPower(0);
        }
    }

    public void BallCounter(){
        currentSwitchState = countSwitch.getState();

        if (currentSwitchState && !previousSwitchState && ballCountTimer.milliseconds()>debouncetime) {
            ballcount++; // Object just triggered the switch
            ballCountTimer.reset();
        }
        previousSwitchState = currentSwitchState;

        if (couterresetbutton()){
             ballcount=0;
        }

        telemetry.addData("count",ballcount);
    }


    /*****************************/
    //Odometry Functions
    public void pinpointinit() {
        // Get a reference to the sensor
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // Configure the sensor
        configurePinpoint();

        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
    }
    public void configurePinpoint() {
        /*
         *  Set the odometry pod positions relative to the point that you want the position to be measured from.
         *
         *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
         *  Left of the center is a positive number, right of center is a negative number.
         *
         *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
         *  Forward of center is a positive number, backwards is a negative number.
         */
        pinpoint.setOffsets(-84.0, -168.0, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1

        /*
         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
         * number of ticks per unit of your odometry pod.  For example:
         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
         */
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        /*
         * Set the direction that each of the two odometry pods count. The X (forward) pod should
         * increase when you move the robot forward. And the Y (strafe) pod should increase when
         * you move the robot to the left.
         */
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        /*
         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
         * The IMU will automatically calibrate when first powered on, but recalibrating before running
         * the robot is a good idea to ensure that the calibration is "good".
         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
         * This is recommended before you run your autonomous, as a bad initial calibration can cause
         * an incorrect starting value for x, y, and heading.
         */
        pinpoint.resetPosAndIMU();


        /*****************************/
        //Autonomous Functions

        /*****************************/
        //Encoder Functions
   /*
    public double getLeftVerticalEncoder(){
        return rightFront.getCurrentPosition();
    }
    */


    }

    public void moveterack(){
        pose2D = pinpoint.getPosition();
        robotwhy = pose2D.getY(DistanceUnit.INCH);
        robotx = pose2D.getX(DistanceUnit.INCH);
        headinghi = pose2D.getHeading(AngleUnit.DEGREES);

        telemetry.addData("robotx cordnets", robotx);
        telemetry.addData("robotwhy",robotwhy);
        telemetry.addData("headinghi", headinghi);

    }

    public void Tinylightcoller(double lightcoller){

        tinylight.setPosition(lightcoller);
    }


   public void collercount(){

    if (ballcount==0){
        Tinylightcoller(green);
    }
    else if (ballcount==1) {
        Tinylightcoller(blue);}
    else if (ballcount==2) {
        Tinylightcoller(yellow);
    }
    else if (ballcount==3) {
        Tinylightcoller(orange);
    }
    else if (ballcount==4) {
        Tinylightcoller(red);
    }
    else if (ballcount>4) {
        Tinylightcoller(violet);
    }
   }







}