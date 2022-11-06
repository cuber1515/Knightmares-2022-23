package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp

public class teleOpCode extends LinearOpMode {

    // start encoders
    public void startEncoders() {
        LAM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // exit encoders
    public void exitEncoders() {
        LAM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // restart encoders
    public void resetEncoders() {
        LAM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        AM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    DcMotor FR, FL, BR, BL, AM, LAM;
    Servo LS, RS, CS;

    @Override
    public void runOpMode() throws InterruptedException {
        // Motors and servos
        FR = hardwareMap.dcMotor.get("Front Right");
        FL = hardwareMap.dcMotor.get("Front Left");
        BR = hardwareMap.dcMotor.get("Back Right");
        BL = hardwareMap.dcMotor.get("Back Left");
        AM = hardwareMap.dcMotor.get("Arm Lift");
        LAM = hardwareMap.dcMotor.get("Linear Actuator");

        AM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LAM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        LS = hardwareMap.servo.get("Left Servo");
        RS = hardwareMap.servo.get("Right Servo");
        CS = hardwareMap.servo.get("Claw Servo");

        FR.setDirection((DcMotor.Direction.REVERSE));

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                resetEncoders();

                /**
                 * ALL UNDER GAMEPAD 1
                 */
                // MOVE ROBOT
                FR.setPower(gamepad1.right_stick_y * 0.5);
                BR.setPower(gamepad1.right_stick_y * 0.5);
                FL.setPower(gamepad1.left_stick_y * 0.5);
                BL.setPower(gamepad1.left_stick_y * 0.5);

                // Move to the left if the left trigger is pressed
                if (gamepad1.right_trigger > 0) {
                    FR.setPower(gamepad1.left_trigger * 0.5);
                    BL.setPower(gamepad1.left_trigger * .5);
                    FL.setPower(-gamepad1.left_trigger * .5);
                    BR.setPower(-gamepad1.left_trigger * .5);
                    if (gamepad1.left_trigger == 0) {
                        FR.setPower(0);
                        FL.setPower(0);
                        BR.setPower(0);
                        BL.setPower(0);
                    }
                }

                // Move to the right if the right trigger is pressed
                if (gamepad1.left_trigger > 0) {
                    FR.setPower(-gamepad1.right_trigger * 0.5);
                    BL.setPower(-gamepad1.right_trigger * 0.5);
                    FL.setPower(gamepad1.right_trigger * 0.5);
                    BR.setPower(gamepad1.right_trigger * 0.5);

                    if (gamepad1.right_trigger == 0) {
                        FR.setPower(0);
                        FL.setPower(0);
                        BR.setPower(0);
                        BL.setPower(0);
                    }
                }

                // Set the arm and actuator to the STARTING position
                if (gamepad1.dpad_right) {
                    startEncoders();

                    int armLevel = (int) (0);

                    LAM.setTargetPosition(armLevel);
                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    int armHeight = (int) (0.8 * 2786.2);

                    AM.setTargetPosition(armHeight);
                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                }

                // Set arm and actuator to LOWEST setting
                if (gamepad1.a) {
                    startEncoders();

                    int armLevel = (int) (0);

                    LAM.setTargetPosition(armLevel);
                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    int armHeight = (int) (0);

                    AM.setTargetPosition(armHeight);
                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                }

                // Set arm and actuator to MIDDLE LOWEST setting
                if (gamepad1.x) {
                    startEncoders();
                    
                    int armLevel = (int) (5 * 2786.2);

                    LAM.setTargetPosition(armLevel);
                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    int armHeight = (int) (0.25 * 2786.2);

                    AM.setTargetPosition(armHeight);
                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                }

                // Set arm and actuator to MIDDLE HIGHEST setting
                if (gamepad1.b) {
                    startEncoders();
                    
                    int armLevel = (int) (10 * 2786.2);

                    LAM.setTargetPosition(armLevel);
                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    int armHeight = (int) (0.25 * 2786.2);

                    AM.setTargetPosition(armHeight);
                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                }

                // Set arm and actuator to HIGHEST setting
                if (gamepad1.y) {
                    startEncoders();

                    int armLevel = (int) (15 * 2786.2);

                    LAM.setTargetPosition(armLevel);
                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    int armHeight = (int) (0.50 * 2786.2);

                    AM.setTargetPosition(armHeight);
                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                }

                /**
                 * ALL UNDER GAMEPAD 2
                 */

                if (gamepad2.b) {
                    CS.setPosition(0.02);
                }
                if (gamepad2.x) {
                    CS.setPosition(1);
                }
                if (gamepad2.y) {
                    CS.setPosition(0.5);

                }
            }
        }
    }
}
