package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name="nier")
public class EasyOpenCV extends LinearOpMode {

    OpenCvWebcam webcam = null;

    @Override
    public void runOpMode() throws InterruptedException {
        /**
         * Setting up camera stuff
         */
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam.setPipeline(new myPipe());

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            public void onOpened() {
                webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        /**
         * When start is pressed
         */
        waitForStart();

        myPipe thePipe = new myPipe();

        if (thePipe.isBlue()) {
            telemetry.addLine("blue has been returned");
        } else if (thePipe.isGreen()) {
            telemetry.addLine("green has been returned");
        } else if (thePipe.isOrange()) {
            telemetry.addLine("orange has been returned");
        } else {
            telemetry.addLine("no color has been returned");
        }
        telemetry.update();

        sleep(1200000);

    }

    /**
     * The pipeline
     */
    class myPipe extends OpenCvPipeline {
        // The booleans that will be used in the rest of the code
        boolean blueDetecc = false;
        boolean greenDetecc = false;
        boolean orangeDetecc = false;

        public Mat processFrame(Mat input) {
            // The variables
            Mat YCbCr = new Mat();
            Mat frame;
            double BFrameavgfin;
            double GFrameavgfin;
            double OFrameavgfin;
            Mat outPut = new Mat();
            Scalar blue = new Scalar(29, 44, 85);
            Scalar green = new Scalar(17, 166, 12);
            Scalar orange = new Scalar(166, 81, 7);



            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("pipeline running");

            Rect frameRect = new Rect(213, 120, 212, 119);

            input.copyTo(outPut);



            /**
             * Find amount of blue in frame
             */
            Imgproc.rectangle(outPut, frameRect, blue, 2);

            frame = YCbCr.submat(frameRect);

            Core.extractChannel(frame, frame, 2);

            Scalar blueAvg = Core.mean(frame);

            BFrameavgfin = blueAvg.val[0]; // Store the info

            /**
             * Find amount of green in frame
             */
            Imgproc.rectangle(outPut, frameRect, green, 2);

            frame = YCbCr.submat(frameRect);

            Core.extractChannel(frame, frame, 2);

            Scalar greenAvg = Core.mean(frame);

            GFrameavgfin = greenAvg.val[0]; // Store the info

            /**
             * Find amount of orange in frame
             */
            Imgproc.rectangle(outPut, frameRect, orange, 2);

            frame = YCbCr.submat(frameRect);

            Core.extractChannel(frame, frame, 2);

            Scalar orangeAvg = Core.mean(frame);

            OFrameavgfin = orangeAvg.val[0]; // Store the info

            /**
             * Find which color there is more of
             */
            if (BFrameavgfin > GFrameavgfin && BFrameavgfin > OFrameavgfin) {
                blueDetecc = true;
                greenDetecc = false;
                orangeDetecc = false;
                telemetry.addLine("I see blue");
            } else if (GFrameavgfin > BFrameavgfin && GFrameavgfin > OFrameavgfin) {
                blueDetecc = false;
                greenDetecc = true;
                orangeDetecc = false;
                telemetry.addLine("I see green");
            } else if (OFrameavgfin > BFrameavgfin && OFrameavgfin > GFrameavgfin) {
                blueDetecc = false;
                greenDetecc = false;
                orangeDetecc = true;
                telemetry.addLine("I see orange");
            } else {
                blueDetecc = false;
                greenDetecc = false;
                orangeDetecc = false;
                telemetry.addLine("I don't see color");
            }
            telemetry.update();

            return (outPut);
        }
        // Usable methods outside of pipeline
        public boolean isBlue() {
            return blueDetecc;
        }
        public boolean isGreen() {
            return greenDetecc;
        }
        public boolean isOrange() {
            return orangeDetecc;
        }
    }
}
