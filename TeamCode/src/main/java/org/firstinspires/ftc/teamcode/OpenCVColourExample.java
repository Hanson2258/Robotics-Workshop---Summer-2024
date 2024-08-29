// Pipeline base taken from FTC team Reynolds Reybots 18840

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@TeleOp(name="OpenCV Colour Example", group="OpenCV")
public class OpenCVColourExample extends OpenCvPipeline {
    private final AtomicReference<Bitmap> lastFrame =
            new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

    public static int CAMERA_WIDTH = 320;
    public static double LEFT_X  = 0.25 * (double) CAMERA_WIDTH;
    public static double RIGHT_X = 0.75 * (double) CAMERA_WIDTH;

    public static String propLocation;

    public static int VIEW_DISPLAYED = 1;
    public static int ERODE_PASSES   = 9;

    public static volatile Scalar BOUNDING_RECTANGLE_COLOR = new Scalar(255, 0, 0);

    public static Scalar LOW_HSV_RANGE_BLUE  = new Scalar(97, 100, 0);
    public static Scalar HIGH_HSV_RANGE_BLUE = new Scalar(125, 255, 255);

    private static final Point CV_ANCHOR        = new Point(-1, -1);
    private static final Scalar CV_BORDER_VALUE = new Scalar(-1);
    private static final int CV_BORDER_TYPE     = Core.BORDER_CONSTANT;

    private final Mat hsvMat          = new Mat(),
            threshold0      = new Mat(),
            threshold1      = new Mat(),
            hierarchy       = new Mat(),
            cvErodeKernel   = new Mat(),
            thresholdOutput = new Mat(),
            erodeOutput     = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        // Convert color to HSV
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        // Checks if the image is in range
        Core.inRange(hsvMat, LOW_HSV_RANGE_BLUE, HIGH_HSV_RANGE_BLUE, thresholdOutput);

        // Erode to remove noise
        Imgproc.erode(
                thresholdOutput,
                erodeOutput,
                cvErodeKernel,
                CV_ANCHOR,
                ERODE_PASSES,
                CV_BORDER_TYPE,
                CV_BORDER_VALUE);

        // Finds the contours of the image
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(erodeOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Creates bounding rectangles along all of the detected contours
        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
        }

        Rect biggestBoundingBox = new Rect(0, 0, 0, 0);

        // Gets the biggest bounding box
        for (Rect rect : boundRect) {
            if (rect.area() > biggestBoundingBox.area()) {
                biggestBoundingBox = rect;
            }
        }

        if (biggestBoundingBox.area() != 0) { // If we detect the prop
            if (biggestBoundingBox.x < LEFT_X) { // Check to see if the bounding box is on the left 25% of the screen
                propLocation = "LEFT";
            } else if (biggestBoundingBox.x > RIGHT_X) { // Check to see if the bounding box is on the right 25% of the screen
                propLocation = "RIGHT";
            } else { // If it isn't left or right and the prop is detected it must be in the center
                propLocation = "CENTER";
            }
        } else { // If we don't detect the prop
            propLocation = "NONE";
        }

        // Draw a rectangle over the biggest bounding box
        Imgproc.rectangle(hsvMat, biggestBoundingBox, BOUNDING_RECTANGLE_COLOR);

        if (VIEW_DISPLAYED == 1) {
            Imgproc.rectangle(input, biggestBoundingBox, BOUNDING_RECTANGLE_COLOR);
            return input;
        } else if (VIEW_DISPLAYED == 2) {
            return threshold0;
        } else if (VIEW_DISPLAYED == 3) {
            return threshold1;
        } else if (VIEW_DISPLAYED == 4) {
            return thresholdOutput;
        } else if (VIEW_DISPLAYED == 5) {
            return erodeOutput;
        }


        return hsvMat;
    }

    public String getPropLocation() { return this.propLocation; }
}