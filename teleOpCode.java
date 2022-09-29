package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class teleOpCode extends LinearOpMode {
    DcMotor FR, FL, BR, BL;
    Servo LS, RS;

    @Override
    public void runOpMode() throws InterruptedException {
        FR = hardwareMap.dcMotor.get("Front Right");
        FL = hardwareMap.dcMotor.get("Front Left");
        BR = hardwareMap.dcMotor.get("Back Right");
        BL = hardwareMap.dcMotor.get("Back Left");

        LS = hardwareMap.servo.get("Left Servo");
        RS = hardwareMap.servo.get("Right Servo");

        FL.setDirection((DcMotorSimple.Direction.REVERSE));
        FR.setDirection((DcMotorSimple.Direction.REVERSE));
        BR.setDirection((DcMotorSimple.Direction.REVERSE));

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                // MOVE ROBOT
                FR.setPower(gamepad1.right_stick_y * 0.5);
                BR.setPower(gamepad1.right_stick_y * 0.5);
                FL.setPower(gamepad1.left_stick_y * 0.5);
                BL.setPower(gamepad1.left_stick_y * 0.5);

                // Move to the left if the left trigger is pressed
                if (gamepad1.left_trigger > 0) {
                    FR.setPower(gamepad1.left_trigger * .5);
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
                if (gamepad1.right_trigger > 0) {
                    FR.setPower(-gamepad1.right_trigger * .5);
                    BL.setPower(-gamepad1.right_trigger * .5);
                    FL.setPower(gamepad1.right_trigger * .5);
                    BR.setPower(gamepad1.right_trigger * .5);

                    if (gamepad1.right_trigger == 0) {
                        FR.setPower(0);
                        FL.setPower(0);
                        BR.setPower(0);
                        BL.setPower(0);
                    }
                }
                if (gamepad1.a) {
                    LS.setPosition(0.45);
                    RS.setPosition(0.50);
                }
            }
        }
    }
}
