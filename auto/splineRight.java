package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;

@Config
@Autonomous(group = "drive", name = "spline right")
@Disabled
public class splineRight extends LinearOpMode {
    /**
     * EasyOpenCV specific variables
     */
    OpenCvWebcam webcam;
    org.firstinspires.ftc.teamcode.auto.AprilTagDetectionPipeline aprilTagDetectionPipeline;

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

    /**
     * Variables
     */
    DcMotor AM, LAM; // All of the motors
    Servo CS; // All of the servos
    double ticksPerRev = 3895.9; // Ticks for the arm motor
    double lTicksPerRev = 145.1; // Ticks for the linear actuator
    public static double closeClaw = 0.55;
    public static double openClaw = 0.10;


    // This can be edited in the FTC Dashboard
    public static double startX = 36;
    public static double startY = -64;
    public static double startHeading = -90;
    public static double strafeOutX = 5;
    public static double strafeOutY = -64;
    public static double strafeOutHeading = -90;
    public static double headOutX = 5;
    public static double headOutY = -5;
    public static double headOutHeading = -90;
    public static double turn1 = -45;
    public static double turn2 = -45;
    public static double goGrabX = 60;
    public static double goGrabY = -5;
    public static double goGrabHeading = 180;
    public static double goPlaceX = 36;
    public static double goPlaceY = -5;
    public static double goPlaceHeading = -90;
    public static double turn3 = 45;
    public static double turn4 = 45;
    public static double park1X = 12;
    public static double park1Y = -5;
    public static double park2X = 60;
    public static double park2Y = -5;
    public static double turnEnd = -90;

    /**
     * Methods
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

    // Set arm height
    public void setArm(double lHieght, double aHieght) {

        LAM.setTargetPosition((int) (lHieght * lTicksPerRev));
        LAM.setPower(1);
        LAM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        AM.setTargetPosition((int) (aHieght * ticksPerRev));
        AM.setPower(1);
        AM.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (LAM.isBusy() || AM.isBusy()) {
        }
        LAM.setPower(0);
        AM.setPower(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        AM = hardwareMap.dcMotor.get("Arm Lift");
        LAM = hardwareMap.dcMotor.get("Linear Actuator");
        CS = hardwareMap.servo.get("Claw Servo");

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

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPoint = new Pose2d(startX, startY, Math.toRadians(startHeading));

        drive.setPoseEstimate(startPoint);

        Trajectory GOOUT = drive.trajectoryBuilder(startPoint)
                .splineToConstantHeading(new Vector2d(strafeOutX, strafeOutY), Math.toRadians(strafeOutHeading))
                .splineToConstantHeading(new Vector2d(headOutX, headOutY), Math.toRadians(headOutHeading))
                .build();

        Trajectory GOGRAB = drive.trajectoryBuilder(GOOUT.end().plus(new Pose2d(0, 0, Math.toRadians(turn1 + turn2))), false)
                .lineTo(new Vector2d(goGrabX, goGrabY))
                .build();

        Trajectory GOPLACE = drive.trajectoryBuilder(GOGRAB.end())
                .splineToSplineHeading(new Pose2d(goPlaceX, goPlaceY, Math.toRadians(goPlaceHeading)), Math.toRadians(0))
                .build();

        Trajectory PARK1 = drive.trajectoryBuilder(GOPLACE.end().plus(new Pose2d(0, 0, Math.toRadians(turn3 + turn4))), false)
                .lineTo(new Vector2d(park1X, park1Y))
                .build();

        Trajectory PARK3 = drive.trajectoryBuilder(GOPLACE.end().plus(new Pose2d(0, 0, Math.toRadians(turn3 + turn4))), false)
                .lineTo(new Vector2d(park2X, park2Y))
                .build();


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

        final int aprilValue = tagOfInterest.id;

        resetEncoders();
        startEncoders();

        CS.setPosition(closeClaw);
        sleep(1000);
        setArm(5, 0);
        drive.followTrajectory(GOOUT);
        setArm(25, 0.90);
        drive.turn(Math.toRadians(turn1));
        CS.setPosition(openClaw);
        sleep(1000);
        drive.turn(turn2);
        setArm(15, 0);
        drive.followTrajectory(GOGRAB);
        CS.setPosition(closeClaw);
        sleep(1000);
        setArm(25, 0);
        drive.followTrajectory(GOPLACE);
        setArm(25, 0.90);
        drive.turn(turn3);
        CS.setPosition(openClaw);
        sleep(1000);
        drive.turn(turn4);
        setArm(0, 0);
        if (aprilValue == left) {
            drive.followTrajectory(PARK1);
        } else if (aprilValue == right) {
            drive.followTrajectory(PARK3);
        } else {
        }
        drive.turn(turnEnd);
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
