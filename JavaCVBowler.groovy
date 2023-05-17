 @Grab(group='org.openpnp', module='opencv', version='4.7.0-0')

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView

import org.opencv.videoio.VideoCapture;

import com.neuronrobotics.bowlerstudio.BowlerStudio

// For proper execution of native libraries
// Core.NATIVE_LIBRARY_NAME must be loaded before
// calling any of the opencv methods
try {
 nu.pattern.OpenCV.loadLocally()
}catch(Throwable t) {
	BowlerStudio.printStackTrace(t)
	return
}
Mat matrix =new Mat();
VideoCapture capture = new VideoCapture(0);
capture.read(matrix);

WritableImage WritableImage = null;

// If camera is opened
if( capture.isOpened()) {
	println "Camera Open"
   // If there is next video frame
   if (capture.read(matrix)) {
	   println "Capture success"
	  // Creating BuffredImage from the matrix
	  BufferedImage image = new BufferedImage(matrix.width(),
		 matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
	  
	  WritableRaster raster = image.getRaster();
	  DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
	  byte[] data = dataBuffer.getData();
	  matrix.get(0, 0, data);
	  // Creating the Writable Image
	  WritableImage = SwingFXUtils.toFXImage(image, null);
   }
}
capture.release()
Tab t=new Tab("Imace capture ");
t.setContent(new ImageView(WritableImage))
return t;
