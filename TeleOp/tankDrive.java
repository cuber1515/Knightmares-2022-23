package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Tank drive")
//@Disabled
public class tankDrive extends LinearOpMode {
    /**
     * ALL THE VARIABLES
     */

    DcMotor FR, FL, BR, BL, AM, LAM; // All of the motors
    Servo CS; // All of the servos
    double speed = 0.5; // This is the speed at which the wheels will go

    /**
     * ALL THE METHODS
     */

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
        FR.setDirection((DcMotor.Direction.REVERSE)); // This will reverse the front right wheel (idk why but only this one needs to be reversed)

        telemetry.addData(">", "Press Play to start op mode"); // Will add stuff to the driver hub screen
        telemetry.update(); // Will update the driver hub screen so that the above will appear
        waitForStart(); // When the start button is pressed

        resetEncoders();
        exitEncoders();
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

                // super-duper-speedy-mode
                if (gamepad1.a) {
                    if (speed == 0.50) { // If it is at default speed
                        speed = 1; // Go to HIGH speed
                    } else if (speed == 1) { // If at HIGH speed
                        speed = 0.50; // go to default speed
                    }
                }

                /**
                 * ALL UNDER GAMEPAD 2
                 */

                // This allows you to manually lift the arm
                if (gamepad2.dpad_up) {
                    AM.setPower(0.8); // Set power
                    sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
                } else {
                    AM.setPower(0); // Turn off if it isn't
                }

                // This allows you to manually lower the arm
                if (gamepad2.dpad_down) {
                    AM.setPower(-0.8); // Set power
                    sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
                } else {
                    AM.setPower(0); // Turn off if it isn't
                }

                // This allows you to manually lift the linear actuator
                if (gamepad2.right_bumper) {
                    LAM.setPower(0.8); // Set power
                    sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
                } else {
                    LAM.setPower(0); // Turn off if it isn't
                }

                if (gamepad2.left_bumper) {
                    LAM.setPower(-0.8); // Set power
                    sleep(200); // Wait a 100 milliseconds and check if button is still being pressed
                } else {
                    LAM.setPower(0); // Turn off if it isn't
                }

                if (gamepad2.a) {
                    startEncoders();
                    LAM.setTargetPosition(0);
                    LAM.setPower(1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    AM.setTargetPosition((int) (0));
                    AM.setPower(1);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy() || AM.isBusy()){
                    }
                    LAM.setPower(0);
                    AM.setPower(0);
                    exitEncoders();
                }

/*                if (gamepad2.x) {
                    startEncoders();
                    LAM.setTargetPosition(0);
                    LAM.setPower(1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    AM.setTargetPosition((int) (0.60 * 3895.9));
                    AM.setPower(0.50);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy() || AM.isBusy()){
                    }
                    LAM.setPower(0);
                    AM.setPower(0);
                    exitEncoders();
                }*/

                if (gamepad2.b) {
                    startEncoders();
                    LAM.setTargetPosition(0);
                    LAM.setPower(1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    AM.setTargetPosition((int) (0.90 * 3895.9));
                    AM.setPower(1);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy() || AM.isBusy()){
                    }
                    LAM.setPower(0);
                    AM.setPower(0);
                    exitEncoders();
                }

                if (gamepad2.y) {
                    startEncoders();
                    LAM.setTargetPosition((int) (25 * 145.1));
                    LAM.setPower(1);
                    LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    AM.setTargetPosition((int) (0.90 * 3895.9));
                    AM.setPower(1);
                    AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    while (LAM.isBusy() || AM.isBusy()){
                    }
                    LAM.setPower(0);
                    AM.setPower(0);
                    exitEncoders();
                }

                // self explanatory
                if (gamepad2.left_trigger > 0) {
                    CS.setPosition(0.10); // openClaw
                }
                if (gamepad2.right_trigger > 0) {
                    CS.setPosition(0.55); // closeClaw
                }

                if (gamepad2.left_stick_button) {
                    resetEncoders();
                }
            }
        }
    }
}