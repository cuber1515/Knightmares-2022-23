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
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // exit encoders
    public void exitEncoders() {
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // restart encoders
    public void resetEncoders() {
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    DcMotor FR, FL, BR, BL, AL, LA;
    Servo LS, RS, CS;
    ColorSensor subClawS;
    int encoderArmLevel;
    int encoderArmHeight;

    @Override
    public void runOpMode() throws InterruptedException {
        // Motors and servos
        FR = hardwareMap.dcMotor.get("Front Right");
        FL = hardwareMap.dcMotor.get("Front Left");
        BR = hardwareMap.dcMotor.get("Back Right");
        BL = hardwareMap.dcMotor.get("Back Left");
        AL = hardwareMap.dcMotor.get("Arm Lift");
        LA = hardwareMap.dcMotor.get("Linear Actuator");

        AL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        LS = hardwareMap.servo.get("Left Servo");
        RS = hardwareMap.servo.get("Right Servo");
        CS = hardwareMap.servo.get("Claw Servo");

        FR.setDirection((DcMotor.Direction.REVERSE));

        // Sensors
        subClawS = hardwareMap.colorSensor.get("Sub Claw Sensor");

        int height = 0;
        int armHeight = 0;

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                // MOVE ROBOT
                FR.setPower(gamepad1.right_stick_y * .25);
                BR.setPower(gamepad1.right_stick_y * .25);
                FL.setPower(gamepad1.left_stick_y * .25);
                BL.setPower(gamepad1.left_stick_y * .25);

                // Move to the left if the left trigger is pressed
                if (gamepad1.left_trigger > 0) {
                    FR.setPower(gamepad1.left_trigger * .25);
                    BL.setPower(gamepad1.left_trigger * .25);
                    FL.setPower(-gamepad1.left_trigger * .25);
                    BR.setPower(-gamepad1.left_trigger * .25);

                    if (gamepad1.left_trigger == 0) {
                        FR.setPower(0);
                        FL.setPower(0);
                        BR.setPower(0);
                        BL.setPower(0);
                    }
                }

                // Move to the right if the right trigger is pressed
                if (gamepad1.right_trigger > 0) {
                    FR.setPower(-gamepad1.right_trigger * .25);
                    BL.setPower(-gamepad1.right_trigger * .25);
                    FL.setPower(gamepad1.right_trigger * .25);
                    BR.setPower(gamepad1.right_trigger * .25);

                    if (gamepad1.right_trigger == 0) {
                        FR.setPower(0);
                        FL.setPower(0);
                        BR.setPower(0);
                        BL.setPower(0);
                    }
                }

                // Sub claw stuff on blue
                if (subClawS.blue() > 20) {
                    LS.setPosition(45);
                    RS.setPosition(45);

                    sleep(100);

                    FR.setPower(0.25);
                    FL.setPower(0.25);
                    BR.setPower(0.25);
                    BL.setPower(0.25);

                    sleep(500);

                    FR.setPower(0);
                    FL.setPower(0);
                    BR.setPower(0);
                    BL.setPower(0);

                    LS.setPosition(40);
                    RS.setPosition(40);
                }

                // Close/Open claw
                if (gamepad1.b) {
                    if (CS.getPosition() == 50){
                        CS.setPosition(55);
                    } else {
                        CS.setPosition(50);
                    }
                }

                // Lowering/Raising arm
                if (gamepad1.dpad_down) {
                    if (height == 0) {
                        encoderArmLevel = (int) (0);
                    } else if (height == 1) {
                        encoderArmLevel = (int) -(1 * 538);
                    } else if (height == 2) {
                        encoderArmLevel = (int) -(2 * 538);
                    }

                    startEncoders();

                    FL.setTargetPosition(encoderArmLevel);
                    FR.setTargetPosition(encoderArmLevel);
                    BL.setTargetPosition(encoderArmLevel);
                    BR.setTargetPosition(encoderArmLevel);

                    FL.setPower(0.25);
                    FR.setPower(0.25);
                    BL.setPower(0.25);
                    BR.setPower(0.25);

                    FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
                    }

                    FL.setPower(0);
                    FR.setPower(0);
                    BL.setPower(0);
                    BR.setPower(0);

                    exitEncoders();resetEncoders();

                    height = 0;
                }
                if (gamepad1.dpad_left) {
                    if (height == 0) {
                        encoderArmLevel = (int) (1 * 538);
                    } else if (height == 1) {
                        encoderArmLevel = (int) (0);
                    } else if (height == 2) {
                        encoderArmLevel = (int) -(1 * 538);
                    }

                    startEncoders();

                    FL.setTargetPosition(encoderArmLevel);
                    FR.setTargetPosition(encoderArmLevel);
                    BL.setTargetPosition(encoderArmLevel);
                    BR.setTargetPosition(encoderArmLevel);

                    FL.setPower(0.25);
                    FR.setPower(0.25);
                    BL.setPower(0.25);
                    BR.setPower(0.25);

                    FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
                    }

                    FL.setPower(0);
                    FR.setPower(0);
                    BL.setPower(0);
                    BR.setPower(0);

                    exitEncoders();resetEncoders();

                    height = 1;
                }
                if (gamepad1.dpad_up) {
                    if (height == 0) {
                        encoderArmLevel = (int) (2 * 538);
                    } else if (height == 1) {
                        encoderArmLevel = (int) (1 * 538);
                    } else if (height == 2) {
                        encoderArmLevel = (int) (0);
                    }

                    startEncoders();

                    FL.setTargetPosition(encoderArmLevel);
                    FR.setTargetPosition(encoderArmLevel);
                    BL.setTargetPosition(encoderArmLevel);
                    BR.setTargetPosition(encoderArmLevel);

                    FL.setPower(0.25);
                    FR.setPower(0.25);
                    BL.setPower(0.25);
                    BR.setPower(0.25);

                    FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
                    }

                    FL.setPower(0);
                    FR.setPower(0);
                    BL.setPower(0);
                    BR.setPower(0);

                    exitEncoders();resetEncoders();

                    height = 2;
                }

                if (gamepad1.y) {
                    if (armHeight == 0) {
                        encoderArmHeight = (int) (0.50 * 538);
                    } else if (armHeight == 1) {
                        encoderArmHeight = 0;
                    } else if (armHeight == 2) {
                        encoderArmHeight = (int) -(0.25 * 538);
                    }

                    startEncoders();

                    FL.setTargetPosition(encoderArmHeight);
                    FR.setTargetPosition(encoderArmHeight);
                    BL.setTargetPosition(encoderArmHeight);
                    BR.setTargetPosition(encoderArmHeight);

                    FL.setPower(0.25);
                    FR.setPower(0.25);
                    BL.setPower(0.25);
                    BR.setPower(0.25);

                    FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
                    }

                    FL.setPower(0);
                    FR.setPower(0);
                    BL.setPower(0);
                    BR.setPower(0);

                    exitEncoders();resetEncoders();
                    armHeight = 1;
                }
                if (gamepad1.a) {
                    if (armHeight == 0) {
                        encoderArmHeight = (int) (.75 * 538);
                    } else if (armHeight == 1) {
                        encoderArmHeight = (int) (.25 * 538);
                    } else if (armHeight == 2) {
                        encoderArmHeight = 0;
                    }

                    startEncoders();

                    FL.setTargetPosition(encoderArmHeight);
                    FR.setTargetPosition(encoderArmHeight);
                    BL.setTargetPosition(encoderArmHeight);
                    BR.setTargetPosition(encoderArmHeight);

                    FL.setPower(0.25);
                    FR.setPower(0.25);
                    BL.setPower(0.25);
                    BR.setPower(0.25);

                    FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
                    }

                    FL.setPower(0);
                    FR.setPower(0);
                    BL.setPower(0);
                    BR.setPower(0);

                    exitEncoders();resetEncoders();
                    armHeight = 2;
                }
                if (gamepad1.x) {
                    if (armHeight == 0) {
                        encoderArmHeight = 0;
                    } else if (armHeight ==  1) {
                        encoderArmLevel = (int) -(.50 * 538);
                    } else if (armHeight == 2){
                        encoderArmHeight = (int) -(.75 * 538);
                    }

                    startEncoders();

                    FL.setTargetPosition(encoderArmHeight);
                    FR.setTargetPosition(encoderArmHeight);
                    BL.setTargetPosition(encoderArmHeight);
                    BR.setTargetPosition(encoderArmHeight);

                    FL.setPower(0.25);
                    FR.setPower(0.25);
                    BL.setPower(0.25);
                    BR.setPower(0.25);

                    FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
                    }

                    FL.setPower(0);
                    FR.setPower(0);
                    BL.setPower(0);
                    BR.setPower(0);

                    exitEncoders();resetEncoders();
                    armHeight = 0;
                }
            }
        }
    }
}
