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

import org.opencv.videoio.VideoCapture;

import com.neuronrobotics.bowlerstudio.BowlerStudio
import com.neuronrobotics.bowlerstudio.BowlerStudioController
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

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
capture.open(0)
WritableImage img = null;
CascadeClassifier faceCascade = new CascadeClassifier();
File fileFromGit = ScriptingEngine.fileFromGit("https://github.com/CommonWealthRobotics/harr-cascade-archive.git", "resources/haarcascades/haarcascade_frontalcatface_extended.xml")
faceCascade.load(fileFromGit.getAbsolutePath());
int absoluteFaceSize=0;
Tab t =new Tab()
boolean run = true

while(!Thread.interrupted() && run) {
	Thread.sleep(16)
	try {
		// If camera is opened
		if( capture.isOpened()) {
			//println "Camera Open"
			// If there is next video frame
			if (capture.read(matrix)) {
				MatOfRect faces = new MatOfRect();
				Mat grayFrame = new Mat();
				// face cascade classifier

				// convert the frame in gray scale
				Imgproc.cvtColor(matrix, grayFrame, Imgproc.COLOR_BGR2GRAY);
				// equalize the frame histogram to improve the result
				Imgproc.equalizeHist(grayFrame, grayFrame);

				// compute minimum face size (20% of the frame height, in our case)
				if (absoluteFaceSize == 0)
				{
					int height = grayFrame.rows();
					if (Math.round(height * 0.2f) > 0)
					{
						absoluteFaceSize = Math.round(height * 0.2f);
					}
				}

				// detect faces
				faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
						new Size(absoluteFaceSize, absoluteFaceSize), new Size());

				// each rectangle in faces is a face: draw them!
				Rect[] facesArray = faces.toArray();
				for (int i = 0; i < facesArray.length; i++)
					Imgproc.rectangle(matrix, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);


				//println "Capture success"
				// Creating BuffredImage from the matrix
				BufferedImage image = new BufferedImage(matrix.width(),
						matrix.height(), BufferedImage.TYPE_3BYTE_BGR);

				WritableRaster raster = image.getRaster();
				DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
				byte[] data = dataBuffer.getData();
				matrix.get(0, 0, data);
				// Creating the Writable Image
				if(img==null) {
					img = SwingFXUtils.toFXImage(image, null);
					
					t=new Tab("Imace capture ");
					t.setContent(new ImageView(img))
					BowlerStudioController.addObject(t, null);
				}else{
					SwingFXUtils.toFXImage(image, img);
				}
			}
		}
	}catch(Throwable tr) {
		BowlerStudio.printStackTrace(tr)
		break;
	}
	
}
BowlerStudioController.removeObject(t)
capture.release()

