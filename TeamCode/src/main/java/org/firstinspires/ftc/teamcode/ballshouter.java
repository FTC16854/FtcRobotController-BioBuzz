/* Copyright (c) 2024 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This OpMode demonstrates how to use a digital channel.
 *
 * The OpMode assumes that the digital channel is configured with a name of "digitalTouch".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@TeleOp(name = "ball shoter", group = "Sensor")
//@Disabled
public class ballshouter extends LinearOpMode {

    public ElapsedTime timer = new ElapsedTime();





    @Override
    public void runOpMode() {
        DcMotorEx Shoter = hardwareMap.get(DcMotorEx.class,"shouter");

        Shoter.setDirection(DcMotorSimple.Direction.FORWARD);
        Shoter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double velocity = 1000; //pulses per second
        double PPR=28;



        telemetry.addData("Velocity Test", "Press start to continue...");
        telemetry.update();

        // wait for the start button to be pressed.
        waitForStart();
        timer.reset();

        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {
            double TargetRPM=((velocity)/(PPR))*(60);
            double RPM=((Shoter.getVelocity())/(PPR))*(60);

            if (gamepad1.x){
                Shoter.setVelocity(velocity);
            }
            else {
                Shoter.setVelocity(0);
            }

            if (gamepad1.y) {
               velocity=velocity+10;


            } else if (gamepad1.a) {
                velocity=velocity-10;


            }

            telemetry.addData("velocity", Shoter.getVelocity());
            telemetry.addData("target velocity", velocity);
            telemetry.addData("RPM",RPM);
            telemetry.addData("Target RPM",TargetRPM);


            telemetry.update();
        }
    }


}
