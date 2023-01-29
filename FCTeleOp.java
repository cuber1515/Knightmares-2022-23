package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(group = "drive")
public class FCTeleOp extends LinearOpMode {
    /**
     * ALL THE VARIABLES
     */
    DcMotor AM, LAM; // All of the motors
    Servo CS; // All of the servos

    /**
     * ALL THE METHODS
     */

    public void startEncoders() {
        LAM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // This method will exit encoders
    public void exitEncoders() {
        LAM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // This method will restart encoders
    public void resetEncoders() {
        LAM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        AM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        LAM = hardwareMap.dcMotor.get("Linear Actuator");
        AM = hardwareMap.dcMotor.get("Arm Lift");
        CS = hardwareMap.servo.get("Claw Servo");

        resetEncoders();exitEncoders();
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (!isStopRequested()) {
            // Read pose
            Pose2d poseEstimate = drive.getPoseEstimate();

            // Create a vector from the gamepad x/y inputs
            // Then, rotate that vector by the inverse of that heading
            Vector2d input = new Vector2d(
                    -gamepad1.left_stick_y * 0.5,
                    -gamepad1.left_stick_x * 0.5
            ).rotated(-poseEstimate.getHeading());

            // Pass in the rotated input + right stick value for rotation
            // Rotation is not part of the rotated input thus must be passed in separately
            drive.setWeightedDrivePower(
                    new Pose2d(
                            input.getX(),
                            input.getY(),
                            -gamepad1.right_stick_x * 0.5
                    )
            );

            drive.update();

            if (gamepad2.dpad_up) {
                AM.setPower(0.8); // Set power
                sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
            } else {
                AM.setPower(0); // Turn off if it isn't
            }

            // This allows you to manually lower the arm
            if (gamepad2.dpad_down) {
                AM.setPower(-0.8); // Set power
                sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
            } else {
                AM.setPower(0); // Turn off if it isn't
            }

            // This allows you to manually lift the linear actuator
            if (gamepad2.right_bumper) {
                LAM.setPower(0.8); // Set power
                sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
            } else {
                LAM.setPower(0); // Turn off if it isn't
            }

            if (gamepad2.left_bumper) {
                LAM.setPower(-0.8); // Set power
                sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
            } else {
                LAM.setPower(0); // Turn off if it isn't
            }

            if (gamepad2.a) {
                startEncoders();
                LAM.setTargetPosition(0);
                LAM.setPower(1);
                LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                AM.setTargetPosition((int) (0));
                AM.setPower(1);
                AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while (LAM.isBusy() || AM.isBusy()){
                }
                LAM.setPower(0);
                AM.setPower(0);
                exitEncoders();
            }

            if (gamepad2.b) {
                startEncoders();
                LAM.setTargetPosition(0);
                LAM.setPower(1);
                LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                AM.setTargetPosition((int) (0.90 * 3895.9));
                AM.setPower(1);
                AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while (LAM.isBusy() || AM.isBusy()){
                }
                LAM.setPower(0);
                AM.setPower(0);
                exitEncoders();
            }

            if (gamepad2.y) {
                startEncoders();
                LAM.setTargetPosition((int) (25 * 145.1));
                LAM.setPower(1);
                LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                AM.setTargetPosition((int) (0.90 * 3895.9));
                AM.setPower(1);
                AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while (LAM.isBusy() || AM.isBusy()){
                }
                LAM.setPower(0);
                AM.setPower(0);
                exitEncoders();
            }

            // self explanatory
            if (gamepad2.left_trigger > 0) {
                CS.setPosition(0.10); // openClaw
            }
            if (gamepad2.right_trigger > 0) {
                CS.setPosition(0.55); // closeClaw
            }

            if (gamepad2.left_stick_button) {
                resetEncoders();
            }
        }
    }
}
