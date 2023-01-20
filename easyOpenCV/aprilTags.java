package org.firstinspires.ftc.teamcode.easyOpenCV;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;

import java.nio.file.attribute.FileOwnerAttributeView;
import java.util.ArrayList;

@Autonomous
public class aprilTags extends LinearOpMode {

    /**
     * Variables
    */
    OpenCvWebcam webcam;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int left = 13;
    int middle = 5;
    int right = 4;

    AprilTagDetection tagOfInterest = null;


    DcMotor FR, FL, BR, BL, AM, LAM; // All of the motors
    Servo CS; // All of the servos
    double circumference = 3.14 * 4; // circumference of the wheels
    double closeClaw = 0.50;
    double openClaw = 0.10;

    /**
     * Methods
     */
    // This method will start encoders
    public void startEncoders() {
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // This method will exit encoders
    public void exitEncoders() {
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // This method will restart encoders
    public void resetEncoders() {
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    // This method will start encoders
    public void startEncodersA() {
        LAM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // This method will exit encoders
    public void exitEncodersA() {
        LAM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        AM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // This method will restart encoders
    public void resetEncodersA() {
        LAM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        AM.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    // Set power for all wheels
    public void powerSet(double speed) {
        FR.setPower(speed);
        FL.setPower(speed);
        BR.setPower(speed);
        BL.setPower(speed);
    }

    // Run to position
    public void runToPosition(){
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    // Go forward by inches
    public void ForwardsInch(double inches) {

        resetEncoders();startEncoders();
        double rotationsNeeded = inches / circumference;
        int target = (int) (rotationsNeeded * 537.689);

        FL.setTargetPosition(target);
        FR.setTargetPosition(target);
        BL.setTargetPosition(target);
        BR.setTargetPosition(target);

        powerSet(0.5);

        runToPosition();

        while(FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
        }

        powerSet(0);
        exitEncoders();
    }

    // Turn right if inches + left if inches -
    public void turnInch(double inches) {
        resetEncoders();startEncoders();
        double rotationsNeeded = inches / circumference;
        int targetL = (int) -(rotationsNeeded * 537.689);
        int targetR = (int) (rotationsNeeded * 537.689);

        FR.setTargetPosition(targetR);
        BR.setTargetPosition(targetR);
        BL.setTargetPosition(targetL);
        BL.setTargetPosition(targetL);

        powerSet(0.5);

        runToPosition();

        while(FR.isBusy() || FL.isBusy() || BR.isBusy() || BL.isBusy()) {
        }

        powerSet(0);
        exitEncoders();
    }

    public void strafe(double power, String direction, int tIme) {
        if (direction == "right") {
            FR.setPower(-power);
            BL.setPower(-power);
            FL.setPower(power);
            BR.setPower(power);
            sleep(tIme);
            FR.setPower(0);
            FL.setPower(0);
            BR.setPower(0);
            BL.setPower(0);
        } else if (direction == "left") {
            FR.setPower(power);
            BL.setPower(power);
            FL.setPower(-power);
            BR.setPower(-power);
            sleep(tIme);
            FR.setPower(0);
            FL.setPower(0);
            BR.setPower(0);
            BL.setPower(0);
        }
    }

    // Set arm height
    public void setArm(int level) {
        if (level == 0 || level > 3) {
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
        } else if (level == 1) {
            LAM.setTargetPosition(0);
            LAM.setPower(1);
            LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            AM.setTargetPosition((int) (0.60 * 3895.9));
            AM.setPower(0.50);
            AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (LAM.isBusy() || AM.isBusy()) {
            }
            LAM.setPower(0);
            AM.setPower(0);
        } else if (level == 2) {
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
        } else if (level == 3) {
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
        }
    }

    @Override
    public void runOpMode() {
        FR = hardwareMap.dcMotor.get("Front Right");
        FL = hardwareMap.dcMotor.get("Front Left");
        BR = hardwareMap.dcMotor.get("Back Right");
        BL = hardwareMap.dcMotor.get("Back Left");
        AM = hardwareMap.dcMotor.get("Arm Lift");
        LAM = hardwareMap.dcMotor.get("Linear Actuator");
        CS = hardwareMap.servo.get("Claw Servo");

        BL.setDirection((DcMotor.Direction.REVERSE)); // This will reverse the back right wheel (idk why but only this one needs to be reversed)
        FL.setDirection((DcMotor.Direction.REVERSE)); // This will reverse the front right wheel (idk why but only this one needs to be reversed)


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        webcam.setPipeline(aprilTagDetectionPipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested()) {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFound = false;

                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == left || tag.id == middle || tag.id == right) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if (tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        CS.setPosition(closeClaw);
        sleep(500);
        LAM.setPower(0.5);
        sleep(200);
        LAM.setPower(0);
        sleep(500);

        if (tagOfInterest.id == left) {
            strafe(0.5, "left", 1500);
        } else if (tagOfInterest.id == middle) {
            /**
             * Do nothing here
             */
        } else if (tagOfInterest.id == right) {
            strafe(0.5, "right", 1500);
        } else {
            /**
             * Do nothing
             */
        }

        ForwardsInch(44);
    }

    void tagToTelemetry(AprilTagDetection detection) {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x * FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y * FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z * FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}
