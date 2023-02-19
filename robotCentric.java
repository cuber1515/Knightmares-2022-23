package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

//@Config
//@Disabled
@TeleOp(group = "drive", name = "robot centric")
public class robotCentric extends LinearOpMode {
    /**
     * ALL THE VARIABLES
     */

    DcMotor AM, LAM; // All of the motors
    Servo CS; // All of the servos

    // Configure editable variables
    public static double speed = 0.5;
    public static double openClaw = 0.10;
    public static double closeClaw = 0.65;
    public static double armSpd = 0.8;
    public static double armHghtA = 0;
    public static double lamHghtA = 0;
    public static double armHghtB = 0.90;
    public static double lamHghtB = 0;
    public static double armHghtY = 0.90;
    public static double lamHghtY = 25;

    /**
     * ALL THE METHODS
     */

    // This method will start encoders
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

        resetEncoders();exitEncoders(); // Sets what ever height the arm is at, at start as 0
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap); // assigns wheel motors

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Sets wheels to encoders

        waitForStart(); // Waits for the start button to be pressed

        while (!isStopRequested()) {
            // Allows for robot movement
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y * speed,
                            -gamepad1.left_stick_x * speed,
                            -gamepad1.right_stick_x * speed
                    )
            );

            // IDK, you just need this
            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();

            // Raises the arm
            if (gamepad2.dpad_up) {
                AM.setPower(armSpd);
                sleep(200);
            } else {
                AM.setPower(0); // Turn off if it isn't
            }

            // Lowers the arm
            if (gamepad2.dpad_down) {
                AM.setPower(-armSpd); // Set power
                sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
            } else {
                AM.setPower(0); // Turn off if it isn't
            }

            // Raises linear actuator
            if (gamepad2.right_bumper) {
                LAM.setPower(armSpd); // Set power
                sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
            } else {
                LAM.setPower(0); // Turn off if it isn't
            }

            // Lowers linear actuator
            if (gamepad2.left_bumper) {
                LAM.setPower(-armSpd); // Set power
                sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
            } else {
                LAM.setPower(0); // Turn off if it isn't
            }

            // Lowest setting
            if (gamepad2.a) {
                startEncoders();
                LAM.setTargetPosition((int) (lamHghtA));
                LAM.setPower(1);
                LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                AM.setTargetPosition((int) (armHghtA));
                AM.setPower(1);
                AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while (LAM.isBusy() || AM.isBusy()){
                }
                LAM.setPower(0);
                AM.setPower(0);
                exitEncoders();
            }

            // Medium junction setting
            if (gamepad2.b) {
                startEncoders();
                LAM.setTargetPosition((int) (lamHghtB));
                LAM.setPower(1);
                LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                AM.setTargetPosition((int) (armHghtB * 3895.9));
                AM.setPower(1);
                AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while (LAM.isBusy() || AM.isBusy()){
                }
                LAM.setPower(0);
                AM.setPower(0);
                exitEncoders();
            }

            // Highest setting
            if (gamepad2.y) {
                startEncoders();
                LAM.setTargetPosition((int) (lamHghtY * 145.1));
                LAM.setPower(1);
                LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                AM.setTargetPosition((int) (armHghtY * 3895.9));
                AM.setPower(1);
                AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                while (LAM.isBusy() || AM.isBusy()){
                }
                LAM.setPower(0);
                AM.setPower(0);
                exitEncoders();
            }

            // Self explanatory
            if (gamepad2.left_trigger > 0) {
                CS.setPosition(openClaw);
            }
            if (gamepad2.right_trigger > 0) {
                CS.setPosition(closeClaw);
            }

            // Sets current arm position as 0
            if (gamepad2.left_stick_button) {
                resetEncoders();
            }
        }
    }
}
