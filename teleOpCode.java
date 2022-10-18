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
    DcMotor FR, FL, BR, BL, AL, LA;
    Servo LS, RS, CS;
    ColorSensor subClawS;

    @Override
    public void runOpMode() throws InterruptedException {
        // Motors and servos
        FR = hardwareMap.dcMotor.get("Front Right");
        FL = hardwareMap.dcMotor.get("Front Left");
        BR = hardwareMap.dcMotor.get("Back Right");
        BL = hardwareMap.dcMotor.get("Back Left");
        AL = hardwareMap.dcMotor.get("Arm Lift");
        LA = hardwareMap.dcMotor.get("Linear Actuator");

        LS = hardwareMap.servo.get("Left Servo");
        RS = hardwareMap.servo.get("Right Servo");
        CS = hardwareMap.servo.get("Claw Servo");

        FR.setDirection((DcMotor.Direction.REVERSE));

        // Sensors
        subClawS = hardwareMap.colorSensor.get("Sub Claw Sensor");

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
                if (gamepad1.a) {
                    if (CS.getPosition() == 50){
                        CS.setPosition(55);
                    } else {
                        CS.setPosition(50);
                    }
                }

                // Raising claw
                if (gamepad1.b) {
                    AL.setPower(0.10);
                    if (!gamepad1.b) {
                        AL.setPower(0);
                    }
                }

                // Raising arm
                if (gamepad1.y) {
                    LA.setPower(0.25);
                    if (!gamepad1.y) {
                        LA.setPower(0);
                    }
                }
            }
        }
    }
}
