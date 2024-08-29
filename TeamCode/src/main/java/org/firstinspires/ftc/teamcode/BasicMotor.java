package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic Motor", group="Motor Test")
public class BasicMotor extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor dcMotor = null;

    @Override
    public void runOpMode() {
        dcMotor = hardwareMap.get(DcMotor.class, "motor");

        dcMotor.setDirection(DcMotor.Direction.FORWARD);

        // Display waiting for Init
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for Init
        waitForStart();
        runtime.reset();

        // Run until stop is pressed
        while (opModeIsActive()) {
            double power = gamepad1.left_stick_y;

            dcMotor.setPower(power);

            // Show power and runtime
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motor Power: ", "%4.2f", power);
            telemetry.update();
        }
    }
}
