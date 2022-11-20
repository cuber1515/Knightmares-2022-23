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
    /**
     * ALL THE VARIABLES
     */

    DcMotor FR, FL, BR, BL, AM, LAM; // All of the motors
    Servo CS; // All of the servos
    int currentHeight = 1; // This variable is for the preset heights of the arm
    double speed = 0.5; // This is the speed at which the wheels will go

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
        // Assigning all of the servos and motors
        FR = hardwareMap.dcMotor.get("Front Right");
        FL = hardwareMap.dcMotor.get("Front Left");
        BR = hardwareMap.dcMotor.get("Back Right");
        BL = hardwareMap.dcMotor.get("Back Left");
        AM = hardwareMap.dcMotor.get("Arm Lift");
        LAM = hardwareMap.dcMotor.get("Linear Actuator");
        CS = hardwareMap.servo.get("Claw Servo");

        BR.setDirection((DcMotor.Direction.REVERSE)); // This will reverse the back right wheel (idk why but only this one needs to be reversed)

        telemetry.addData(">", "Press Play to start op mode"); // Will add stuff to the driver hub screen
        telemetry.update(); // Will update the driver hub screen so that the above will appear
        waitForStart(); // When the start button is pressed

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                /**
                 * ALL UNDER GAMEPAD 1
                 */
                // Basic robot moving controls
                FR.setPower(gamepad1.right_stick_y * speed);
                BR.setPower(gamepad1.right_stick_y * speed);
                FL.setPower(gamepad1.left_stick_y * speed);
                BL.setPower(gamepad1.left_stick_y * speed);

                // This will strafe to the left
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

                // This will strafe to the right
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

                // This will change the above between slo-mo and normal speed
                if (gamepad1.a) {
                    if (speed == 0.50) { // If it is at default speed
                        speed = 0.15; // Go to slo-mo
                    } else if (speed == 0.15) { // If at slo-mo
                        speed = 0.50; // go to default speed
                    }
                }

                /**
                 * ALL UNDER GAMEPAD 2
                 */

                // This allows you to manually lift the arm
                if (gamepad2.dpad_up) {
                    AM.setPower(0.5); // Set power
                    sleep(100); // Wait a 100 milliseconds and check if button is still being pressed
                    AM.setPower(0); // Turn off if it isn't
                }
                // This allows you to manually lower the arm
                if (gamepad2.dpad_down) {
                    AM.setPower(-0.7); // Set power
                    sleep(100); // Wait a 100 milliseconds and check if button is still being pressed
                    AM.setPower(0); // Turn off if it isn't
                }

                // This allows you to manually lift the linear actuator
                if (gamepad2.right_bumper) {
                    LAM.setPower(1); // Set power
                    sleep(100); // Wait a 100 milliseconds and check if button is still being pressed
                    LAM.setPower(0); // Turn off if it isn't
                }

                if (gamepad2.left_bumper) {
                    LAM.setPower(-1); // Set power
                    sleep(100); // Wait a 100 milliseconds and check if button is still being pressed
                    LAM.setPower(0); // Turn off if it isn't
                }

                // Set arm and actuator to LOWEST setting
                if (gamepad2.a) {
                    resetEncoders();startEncoders(); // resets then starts encoders

                    if (currentHeight == 2) { // If it is at preset "x"

                        int armLevel = (int) (-14.8 * 145.1); // The distance needed to get down from "x"
                        LAM.setTargetPosition(armLevel); // Sets the target

                        int armHeight = (int) (-0.25 * 3895.9); // The distance needed to get down from "x"
                        AM.setTargetPosition(armHeight); // Sets the target

                    } else if (currentHeight == 3) { // If it is at preset "b"

                        int armLevel = (int) (-19.8 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);

                    } else if (currentHeight == 4) { // If it is at preset "y"

                        int armLevel = (int) (-24.8 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (-0.50 * 3895.9);
                        AM.setTargetPosition(armHeight);

                    } else { // Currently at "a"
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(-1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION); // Tells the linear actuator to go to the calculated position
                    while (LAM.isBusy()) { // This is like a sleep method but it last as long as needed to get to the encoder target
                    }
                    LAM.setPower(0); // Turns off once target reached

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);
                    exitEncoders();

                    /*
                    This is needed only for the "a" button, you may have noticed that the
                    calculations for the linear actuator are 0.2 off. This is because if it goes
                    all the way it will run into something and it will never reach the "target"
                    (idk exactly know why) but if it goes *mostly* and then the motor turns on for
                    0.3 seconds it should reach it perfectly fine with no errors
                     */
                    LAM.setPower(-0.25);
                    sleep(300);
                    LAM.setPower(0);

                    currentHeight = 1; // Tells the code that the current height is now 1
                }

                // Set arm and actuator to MIDDLE LOWEST setting
                if (gamepad2.x) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 1) { // If it is at preset "a"

                        int armLevel = (int) (15 * 145.1);// The distance needed to go up from "a"
                        LAM.setTargetPosition(armLevel); // Sets the target

                        int armHeight = (int) (0.25 * 3895.9); // The distance needed to go up from "a"
                        AM.setTargetPosition(armHeight); // Sets the target

                    } else if (currentHeight == 3) { // If it is at preset "b"

                        int armLevel = (int) (-5 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);

                    } else if (currentHeight == 4) { // If it is at preset "y"

                        int armLevel = (int) (-10 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);

                    } else { // Currently at "x"
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION); // Tells the linear actuator to go to the calculated position
                    while (LAM.isBusy()) { // This is like a sleep method but it last as long as needed to get to the encoder target
                    }
                    LAM.setPower(0); // Turns off once target reached

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);

                    exitEncoders();
                    currentHeight = 2; // Tells the code that the current height is now 2
                }

                // Set arm and actuator to MIDDLE HIGHEST setting
                if (gamepad2.b) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 1) { // If it is at preset "a"

                        int armLevel = (int) (20 * 145.1); // The distance needed to go up from "a"
                        LAM.setTargetPosition(armLevel); // Sets the target

                        int armHeight = (int) (0.25 * 3895.9); // The distance needed to go up from "a"
                        AM.setTargetPosition(armHeight); // Sets the target

                    } else if (currentHeight == 2) { // If it is at preset "x"

                        int armLevel = (int) (5 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);

                    } else if (currentHeight == 4) { // If it is at preset "y"

                        int armLevel = (int) (-5 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (-0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);

                    } else { // Currently at "b"
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION); // Tells the linear actuator to go to the calculated position
                    while (LAM.isBusy()) { // This is like a sleep method but it last as long as needed to get to the encoder target
                    }
                    LAM.setPower(0); // Turns off once target reached

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);

                    exitEncoders();
                    currentHeight = 3; // Tells the code that the current height is now 3
                }

                // Set arm and actuator to HIGHEST setting
                if (gamepad2.y) {
                    resetEncoders();startEncoders();

                    if (currentHeight == 1) {
                        // If it is at preset "a"
                        int armLevel = (int) (25 * 145.1); // The distance needed to go up from "a"
                        LAM.setTargetPosition(armLevel); // Sets the target

                        int armHeight = (int) (0.5 * 3895.9); // The distance needed to go up from "a"
                        AM.setTargetPosition(armHeight); // Sets the target

                    } else if (currentHeight == 2) { // If it is at preset "x"

                        int armLevel = (int) (10 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);

                    } else if (currentHeight == 3) { // If it is at preset "b"

                        int armLevel = (int) (5 * 145.1);
                        LAM.setTargetPosition(armLevel);

                        int armHeight = (int) (0.25 * 3895.9);
                        AM.setTargetPosition(armHeight);

                    } else { // Currently at "y"
                        int armLevel = (int) (0);
                        LAM.setTargetPosition(armLevel);
                        int armHeight = (int) (0);
                        AM.setTargetPosition(armHeight);
                    }

                    LAM.setPower(1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION); // Tells the linear actuator to go to the calculated position
                    while (LAM.isBusy()){// This is like a sleep method but it last as long as needed to get to the encoder target
                    }
                    LAM.setPower(0); // Turns off once target reached

                    AM.setPower(0.25);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (AM.isBusy()) {
                    }
                    AM.setPower(0);

                    exitEncoders();
                    currentHeight = 4; // Tells the code that the current height is now 4
                }

                // self explanatory
                if (gamepad2.left_trigger > 0) {
                    CS.setPosition(0.20); // closeClaw
                }
                if (gamepad2.right_trigger > 0) {
                    CS.setPosition(0.55); // openClaw
                }
            }
        }
    }
}
