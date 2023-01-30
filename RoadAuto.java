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

    public static double stX = 36;
    public static double stY = -64;
    public static double stHead = 90;
    public static double endX = 0;
    public static double endY = -24;
    public static double endHead = 90;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d start = new Pose2d(stX, stY, Math.toRadians(stHead));

        drive.setPoseEstimate(start);

        Trajectory traj1 = drive.trajectoryBuilder(start)
                .splineToConstantHeading(new Vector2d(endX, endY), Math.toRadians(endHead))
                .build();

        waitForStart();

        drive.followTrajectory(traj1);
    }
}
