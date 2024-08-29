package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic Motor with Power Compensation", group="Motor Test")
public class BasicMotorWithPowerCompensation extends LinearOpMode {

    // Velocity to run at (motor ticks per second)
    private double ticksPerSecond = 100;

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx dcMotor = null;
    private VoltageSensor voltageSensor = null;

    @Override
    public void runOpMode() {
        dcMotor = hardwareMap.get(DcMotorEx.class, "motor");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        dcMotor.setDirection(DcMotorEx.Direction.FORWARD);

        // Display waiting for Init
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for Init
        waitForStart();
        runtime.reset();

        // Run until stop is pressed
        while (opModeIsActive()) {
            double power = gamepad1.left_stick_y;
            double currentVoltage = voltageSensor.getVoltage();

            double adjustedPower = power * (12/currentVoltage);

            dcMotor.setPower(adjustedPower);

            // Show power, voltage and runtime
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Raw Motor Power: ", "%4.2f", power);
            telemetry.addData("Motor Voltage: ", "%4.2f", currentVoltage);
            telemetry.addData("Adjusted Motor Power: ", "%4.2f", adjustedPower);
            telemetry.update();
        }
    }
}
