package com.project.imagedetect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ImageProcessor {
    private Context context;

    public ImageProcessor(Context context) {
        this.context = context;
    }

    public void processImages() {
        File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = imagesDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                processImage(bitmap, imageFile.getAbsolutePath());
            }
        }
    }

    private void processImage(Bitmap bitmap, String imagePath) {
        detectFaces(bitmap, imagePath);
        detectText(bitmap, imagePath);
    }

    private void detectFaces(Bitmap bitmap, String imagePath) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build();
        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        FileUtils.moveImageToFolder(imagePath, "Faces");
                    }
                })
                .addOnFailureListener(e -> Log.e("FaceDetection", "Error detecting faces", e));
    }

    private void detectText(Bitmap bitmap, String imagePath) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(text -> {
                    if (!text.getText().isEmpty()) {
                        FileUtils.saveTextToFile(imagePath, text.getText());
                        FileUtils.moveImageToFolder(imagePath, "Text");
                    }
                })
                .addOnFailureListener(e -> Log.e("TextDetection", "Error detecting text", e));
    }
}