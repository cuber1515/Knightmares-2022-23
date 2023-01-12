import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MyOpMode extends LinearOpMode {
    OpenCvCamera phoneCam;
    DcMotor motorA;
    DcMotor motorB;
    DcMotor motorC;

    @Override
    public void runOpMode() {
        motorA = hardwareMap.get(DcMotor.class, "motorA");
        motorB = hardwareMap.get(DcMotor.class, "motorB");
        motorC = hardwareMap.get(DcMotor.class, "motorC");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(new MultiColorDetectionPipeline());
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
        waitForStart();

            while (opModeIsActive()) {
        int color = phoneCam.getColor();
        if(color == 0) {
            motorA.setPower(1);
            motorB.setPower(0);
            motorC.setPower(0);
        }else if(color == 1) {
            motorA.setPower(0);
            motorB.setPower(1);
            motorC.setPower(0);
        }else if(color == 2) {
            motorA.setPower(0);
            motorB.setPower(0);
            motorC.setPower(1);
        }
        telemetry.update();
    }
}

class MultiColorDetectionPipeline extends OpenCvPipeline {
    //Variable to store the submat
    private Mat submat;
    //Variables for color ranges
    private Scalar minGreen = new Scalar(40, 100, 20);
    private Scalar maxGreen = new Scalar(90, 255, 255);
    private Scalar minBlue = new Scalar(100, 150, 0);
    private Scalar maxBlue = new Scalar(140, 255, 255);
    private Scalar minOrange = new Scalar(5,50,50);
    private Scalar maxOrange = new Scalar(20,255,255);

    //Method to set the submat
    public void setSubmat(Mat submat){
        this.submat = submat;
    }

    public int getColor(){
        return color;
    }

    @Override
    public Mat processFrame(Mat input) {
        //If no submat is selected, return the input frame
        if(submat == null){
            return input;
        }
        //Convert the submat to HSV color space
        Imgproc.cvtColor(submat, submat, Imgproc.COLOR_RGB2HSV);
        //Create a Mat to store the result of inRange
        Mat threshold = new Mat();
        //Create a variable to store the color with most pixels
        int maxColor = -1;
        int maxPixels = -1;
        //Iterate over colors
        for (int color = 0; color < 3; color++) {
            Scalar min, max;
            if (color == 0) {
                min = minGreen;
                max = maxGreen;
            } else if (color == 1) {
                min = minBlue;
                max = maxBlue;
            } else {
                min = minOrange;
                max = maxOrange;
            }
            //Threshold the submat
            Core.inRange(submat, min, max, threshold);
            //Get the number of pixels of the color
            int pixels = Core.countNonZero(threshold);
            if(pixels > maxPixels){
                maxColor = color;
                maxPixels = pixels;
            }
        }
        return input;
    }
}
