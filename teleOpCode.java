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
        LAM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // restart encoders
    public void resetEncoders() {
        LAM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    DcMotor FR, FL, BR, BL, AM, LAM;
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
        AM = hardwareMap.dcMotor.get("Arm Lift");
        LAM = hardwareMap.dcMotor.get("Linear Actuator");
        int a = 10;

        AM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LAM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        LS = hardwareMap.servo.get("Left Servo");
        RS = hardwareMap.servo.get("Right Servo");
        CS = hardwareMap.servo.get("Claw Servo");

        FR.setDirection((DcMotor.Direction.REVERSE));

        // Sensors
//        subClawS = hardwareMap.colorSensor.get("Sub Claw Sensor");

        int height = 0;
        int armHeight = 0;

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

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

          /*      if (gamepad1.y) {
                    resetEncoders();startEncoders();
                    AM.setTargetPosition(5);

                    AM.setPower(0.5);

                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while(AM.isBusy()){
                    }
                    AM.setPower(0);
                    exitEncoders();
                }

               if (gamepad1.y) {
                    AM.setPower(-0.5);
                }
                if (gamepad1.a) {
                    AM.setPower(0.5);
                }
                if (gamepad1.x) {
                    AM.setPower(-0.25);
                }
                if (gamepad1.b) {
                    AM.setPower(0.25);
                }
                if (gamepad1.dpad_right) {
                    AM.setPower(0);
                }*/

                if (gamepad1.dpad_up){
                    resetEncoders();startEncoders();
                    LAM.setTargetPosition(a);
                    LAM.setPower(0.25);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy()) {
                    }
                    LAM.setPower(0);
                    exitEncoders();
                    a = a+150;
                }
                if (gamepad1.dpad_down) {
                    LAM.setPower(-0.25);
                    a = 10;
                }
                if (gamepad1.dpad_left) {
                    LAM.setPower(0);
                }



                if (gamepad2.b) {
                    CS.setPosition(0.02);
                }
                if (gamepad2.x) {
                    CS.setPosition(1);
                }
                if (gamepad2.dpad_left) {
                    CS.setPosition(0.5);
                }

                if (gamepad1.y) {
                    AM.setPower(0.5);
                }
                if (gamepad1.a) {
                    AM.setPower(-0.5);
                }
                if (gamepad1.x) {
                    AM.setPower(0);
                }


   /*             if (gamepad2.left_bumper) {
                    RS.setPosition(0.02);
                    LS.setPosition(1);
                }
                if (gamepad2.right_bumper) {
                    RS.setPosition(1);
                    LS.setPosition(0.02);
                }
                if (gamepad2.dpad_right) {
                    RS.setPosition(0.5);
                    LS.setPosition(0.5);
                }



                // Sub claw stuff on blue
              /*  if (subClawS.blue() > 20) {
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
                if (gamepad1.b) {
                    CS.setPosition(0.02);
                }
                if (gamepad1.x) {
                    CS.setPosition(1);
                }
                if (gamepad1.dpad_left) {
                    CS.setPosition(0.5);
                }

                // Lowering/Raising arm
                if (gamepad1.dpad_down) {
                    if (height == 0) {
                        encoderArmLevel = (int) (0);
                    } else if (height == 1) {
                        encoderArmLevel = (int) -(((((1+(46/1))) * (1+(46/1))) * 28));
                    } else if (height == 2) {
                        encoderArmLevel = (int) -(((((1+(46/2))) * (1+(46/2))) * 28));
                    }

                    resetEncoders();startEncoders();

                    LAM.setTargetPosition(encoderArmLevel);

                    LAM.setPower(0.25);

                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (LAM.isBusy()) {
                    }

                    LAM.setPower(0);

                    exitEncoders();

                    height = 0;
                }
                if (gamepad1.dpad_left) {
                    if (height == 0) {
                        encoderArmLevel = (int) (((((1+(46/1))) * (1+(46/1))) * 28));
                    } else if (height == 1) {
                        encoderArmLevel = (int) (0);
                    } else if (height == 2) {
                        encoderArmLevel = (int) -(((((1+(46/1))) * (1+(46/1))) * 28));
                    }

                    resetEncoders();startEncoders();

                    LAM.setTargetPosition(encoderArmLevel);

                    LAM.setPower(0.25);

                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (LAM.isBusy()) {
                    }

                    LAM.setPower(0);

                    exitEncoders();

                    height = 1;
                }
                if (gamepad1.dpad_up) {
                    if (height == 0) {
                        encoderArmLevel = (int) (((((1+(46/2))) * (1+(46/2))) * 28));
                    } else if (height == 1) {
                        encoderArmLevel = (int) (((((1+(46/1))) * (1+(46/1))) * 28));
                    } else if (height == 2) {
                        encoderArmLevel = (int) (0);
                    }

                    resetEncoders();startEncoders();

                    LAM.setTargetPosition(encoderArmLevel);

                    LAM.setPower(0.25);

                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (LAM.isBusy()) {
                    }

                    LAM.setPower(0);

                    exitEncoders();

                    height = 2;
                }

                if (gamepad1.y) {
                    if (armHeight == 0) {
                        encoderArmHeight = (int) -(((((1+(46/0.5))) * (1+(46/0.5))) * 28));
                    } else if (armHeight == 1) {
                        encoderArmHeight = 0;
                    } else if (armHeight == 2) {
                        encoderArmHeight = (int) (((((1+(46/0.25))) * (1+(46/0.25))) * 28));
                    }

                    resetEncoders();startEncoders();

                    AM.setTargetPosition(encoderArmHeight);

                    AM.setPower(0.25);

                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (AM.isBusy()) {
                    }

                    AM.setPower(0);

                    exitEncoders();

                    armHeight = 1;
                }
                if (gamepad1.a) {
                    if (armHeight == 0) {
                        encoderArmHeight = (int) -(((((1+(46/0.75))) * (1+(46/0.75))) * 28));
                    } else if (armHeight == 1) {
                        encoderArmHeight = (int) -(((((1+(46/0.25))) * (1+(46/0.25))) * 28));
                    } else if (armHeight == 2) {
                        encoderArmHeight = 0;
                    }

                    resetEncoders();startEncoders();

                    AM.setTargetPosition(encoderArmHeight);

                    AM.setPower(0.50);

                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (AM.isBusy()) {
                    }

                    AM.setPower(0);

                    exitEncoders();

                    armHeight = 2;
                }
                if (gamepad1.x) {
                    if (armHeight == 0) {
                        encoderArmHeight = 0;
                    } else if (armHeight ==  1) {
                        encoderArmLevel = (int) (((((1+(46/0.5))) * (1+(46/0.5))) * 28));
                    } else if (armHeight == 2){
                        encoderArmHeight = (int) (((((1+(46/0.75))) * (1+(46/0.75))) * 28));
                    }

                    resetEncoders();startEncoders();

                    AM.setTargetPosition(encoderArmHeight);

                    AM.setPower(0.50);

                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    while (AM.isBusy()) {
                    }

                    AM.setPower(0);

                    exitEncoders();

                    armHeight = 0;
                }

                if (gamepad1.y) {
                    AM.setPower(-0.25);
                    if(!gamepad1.y) {
                        AM.setPower(0);
                    }
                }
                if (gamepad1.a) {
                    AM.setPower(0.25);
                    if(!gamepad1.a) {
                        AM.setPower(0);
                    }
                }
                if (gamepad1.dpad_up) {
                    AM.setPower(0.3);
                    if(!gamepad1.dpad_up) {
                        AM.setPower(0);
                    }
                }
                if (gamepad1.dpad_down) {
                    AM.setPower(-0.3);
                    if(!gamepad1.dpad_down) {
                        AM.setPower(0);
                    }
                }*/

            }
        }
    }
}
