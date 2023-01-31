package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Config
@Autonomous(group = "drive", name = "Auto-code")
public class RoadAuto extends LinearOpMode {
    /**
     * Variables
     */
    DcMotor AM, LAM; // All of the motors
    Servo CS; // All of the servos
    double ticksPerRev = 537.7;
    double lTicksPerRev = 145.1;
    public static double closeClaw = 0.55;
    public static double openClaw = 0.10;


    public static double strafe1 = 38;
    public static double forward1 = 64;
    public static double turn1 = -90;
    public static double strafe2 = 12;
    public static double forward2 = 48;


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

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d());

        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(strafe1)
                .build();

        Trajectory traj2 = drive.trajectoryBuilder(traj1.end())
                .forward(forward1)
                .build();

        Trajectory traj3 = drive.trajectoryBuilder(traj2.end())
                .strafeRight(strafe2)
                .build();

        Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                .forward(forward2)
                .build();

        Trajectory traj5 = drive.trajectoryBuilder(traj4.end())
                .forward(-forward2)
                .build();

        Trajectory traj6 = drive.trajectoryBuilder(traj5.end())
                .strafeRight(strafe2)
                .build();

        waitForStart();
        resetEncoders();
        startEncoders();

        // pre-loaded cone
        CS.setPosition(closeClaw);
        sleep(1000);
        setArm(5, 0);
        drive.followTrajectory(traj1);
        setArm(25, 0.90);
        drive.followTrajectory(traj2);
        drive.turn(Math.toRadians(turn1));
        CS.setPosition(openClaw);
        sleep(1000);


        // 1st cone; stack
        drive.followTrajectory(traj3);
        setArm(10, 0);
        drive.followTrajectory(traj4);
        CS.setPosition(closeClaw);
        sleep(1000);
        setArm(25, 0);
        drive.followTrajectory(traj5);
        setArm(25, 0.90);
        drive.followTrajectory(traj6);
        CS.setPosition(openClaw);

    }
}
