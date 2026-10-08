package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DigitalEdgeDetector {
    private DigitalChannel channel;
    private boolean previousState = true;

    public DigitalEdgeDetector(DigitalChannel channel) {
        this.channel = channel;
        this.previousState = channel.getState();
    }


    // Call once per loop to kep track of state
    public void update() {
        previousState = channel.getState();
    }


    // Returns true only on the loop cycle the button is pressed (transitioning to false).
    public boolean wasPressed() {
        return !channel.getState() && previousState;
    }


    // Returns true only on the loop cycle the button is released (transitioning to true).
    public boolean wasReleased() {
        return channel.getState() && !previousState;
    }
}