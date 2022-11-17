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

        BR.setDirection((DcMotor.Direction.REVERSE));

        AM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LAM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        CS = hardwareMap.servo.get("Claw Servo");

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        int currentHeight = 1;
        double speed = 0.5;

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                /**
                 * ALL UNDER GAMEPAD 1
                 */
                // MOVE ROBOT
                FR.setPower(gamepad1.right_stick_y * speed);
                BR.setPower(gamepad1.right_stick_y * speed);
                FL.setPower(gamepad1.left_stick_y * speed);
                BL.setPower(gamepad1.left_stick_y * speed);

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
                    FR.setPower(speed);
                    BL.setPower(speed);
                    FL.setPower(-speed);
                    BR.setPower(-speed);
                    sleep(100);
                    FR.setPower(0);
                    FL.setPower(0);
                    BR.setPower(0);
                    BL.setPower(0);
                }

                // Move to the right if the right trigger is pressed
                if (gamepad1.left_trigger > 0) {
                    FR.setPower(-speed);
                    BL.setPower(-speed);
                    FL.setPower(speed);
                    BR.setPower(speed);
                    sleep(100);
                    FR.setPower(0);
                    FL.setPower(0);
                    BR.setPower(0);
                    BL.setPower(0);
                }

                // Set arm and actuator to LOWEST setting
                if (gamepad1.a) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 2) {
                        int armLevel = (int) (-14.8 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 3) {
                        int armLevel = (int) (-19.8 * 145.1);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);
                    } else if (currentHeight == 4) {
                        int armLevel = (int) (-24.8 * 145.1);
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

                    LAM.setPower(0.25);
                    sleep(300);
                    LAM.setPower(0);
                    currentHeight = 1;
                }

                // Set arm and actuator to MIDDLE LOWEST setting
                if (gamepad1.x) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 1) {
                        int armLevel = (int) (15 * 145.1);
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
                        int armLevel = (int) (20 * 145.1);
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
                        int armLevel = (int) (25 * 145.1);
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

                if (gamepad1.dpad_left) {
                    if (speed == 0.50) {
                        speed = 0.15;
                    } else if (speed == 0.15) {
                        speed = 0.5;
                    }
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
