package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp

public class teleOpCode extends LinearOpMode {
    DcMotor FR, FL, BR, BL;

    @Override
    public void runOpMode() throws InterruptedException {
        FR = hardwareMap.dcMotor.get("Front Right");
        FL = hardwareMap.dcMotor.get("Front Left");
        BR = hardwareMap.dcMotor.get("Back Right");
        BL = hardwareMap.dcMotor.get("Back Left");

        FL.setDirection((DcMotorSimple.Direction.REVERSE));
        BL.setDirection((DcMotorSimple.Direction.REVERSE));

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                // MOVE ROBOT
                FL.setPower(gamepad1.right_stick_y * 0.5);
                BL.setPower(gamepad1.right_stick_y * 0.5);
                FR.setPower(gamepad1.left_stick_y * 0.5);
                BR.setPower(gamepad1.left_stick_y * 0.5);

                if (gamepad1.left_trigger > 0) {
                    FR.setPower(gamepad1.left_stick_x * .5);
                    BL.setPower(gamepad1.left_stick_x * .5);
                    FL.setPower(-gamepad1.left_stick_x * .5);
                    BR.setPower(-gamepad1.left_stick_x * .5);

                    if (gamepad1.left_trigger == 0) {
                        FR.setPower(0);
                        FL.setPower(0);
                        BR.setPower(0);
                        BL.setPower(0);
                    }
                }
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
            }
        }
    }
}
