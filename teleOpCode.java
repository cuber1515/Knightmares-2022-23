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
    Servo CS;

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

        CS = hardwareMap.servo.get("Claw Servo");

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        int currentHeight = 1;

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                /**
                 * ALL UNDER GAMEPAD 1
                 */
                // MOVE ROBOT
                FR.setPower(gamepad1.right_stick_y * 0.5);
                BR.setPower(gamepad1.right_stick_y * 0.5);
                FL.setPower(gamepad1.left_stick_y * 0.5);
                BL.setPower(gamepad1.left_stick_y * 0.5);

                if (gamepad1.dpad_up) {
                    AM.setPower(0.5);
                    sleep(100);
                    AM.setPower(0);
                }
                if (gamepad1.dpad_down) {
                    AM.setPower(-0.7);
                    sleep(100);
                    AM.setPower(0);
                }

                if (gamepad1.right_bumper) {
                    LAM.setPower(0.5);
                    sleep(0);
                    LAM.setPower(0);
                }

                if (gamepad1.left_bumper) {
                    LAM.setPower(-0.5);
                    sleep(0);
                    LAM.setPower(0);
                }

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

                // Set arm and actuator to LOWEST setting
                if (gamepad1.a) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 2) {
                        int armLevel = (int) (-4.9 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 3) {
                        int armLevel = (int) (-9.9 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 4) {
                        int armLevel = (int) (-14.9 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (-0.50 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else {
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                    currentHeight = 1;
                }

                // Set arm and actuator to MIDDLE LOWEST setting
                if (gamepad1.x) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 1) {
                        int armLevel = (int) (5 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 3) {
                        int armLevel = (int) (-5 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 4) {
                        int armLevel = (int) (-10 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else {
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                    currentHeight = 2;
                }

                // Set arm and actuator to MIDDLE HIGHEST setting
                if (gamepad1.b) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 1) {
                        int armLevel = (int) (10 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 2) {
                        int armLevel = (int) (5 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 4) {
                        int armLevel = (int) (-5 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else {
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                    currentHeight = 3;
                }

                // Set arm and actuator to HIGHEST setting
                if (gamepad1.y) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 1) {
                        int armLevel = (int) (15 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0.5 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 2) {
                        int armLevel = (int) (10 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 3) {
                        int armLevel = (int) (5 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else {
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();
                    currentHeight = 4;
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
