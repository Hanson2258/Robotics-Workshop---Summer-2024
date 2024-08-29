package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic Motor with Velocity Control", group="Motor Test")
public class BasicMotorWithVelocityControl extends LinearOpMode {

    // Velocity to run at (motor ticks per second)
    private double ticksPerSecond = 100;

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx dcMotor = null;

    @Override
    public void runOpMode() {
        dcMotor = hardwareMap.get(DcMotorEx.class, "motor");

        dcMotor.setDirection(DcMotorEx.Direction.FORWARD);

        // Display waiting for Init
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for Init
        waitForStart();
        runtime.reset();

        // Run until stop is pressed
        while (opModeIsActive()) {
            ticksPerSecond += -gamepad1.left_stick_y;

            dcMotor.setVelocity(ticksPerSecond);

            // Show power and runtime
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motor Power: ", "%4.2f", dcMotor.getPower());
            telemetry.addData("Desired Motor Velocity: ", "%4.2f", ticksPerSecond);
            telemetry.addData("Motor Velocity: ", "%4.2f", dcMotor.getVelocity());
            telemetry.update();
        }
    }
}
