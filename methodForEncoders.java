package org.firstinspires.ftc.teamcode;

public class methodForEncoders {
    public int calculateToPlaceDistance(double distance) {
        double circumference = 3.14 * 4.72441;
        double rotationsNeeded = distance / circumference;
        int encoderTarget = (int) (rotationsNeeded * 1338);
        return encoderTarget;
    }

    public int calculateToPlaceRotations(double rotations) {
        int encoderTarget = (int) (rotations * 1120);
        return encoderTarget;
    }
}