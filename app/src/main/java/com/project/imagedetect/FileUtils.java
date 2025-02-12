package com.project.imagedetect;

import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void moveImageToFolder(String imagePath, String folderName) {
        File source = new File(imagePath);
        File destinationDir = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }
        File destination = new File(destinationDir, source.getName());
        source.renameTo(destination);
    }

    public static void saveTextToFile(String imagePath, String text) {
        String fileName = new File(imagePath).getName().replace(".jpg", ".txt").replace(".png", ".txt");
        File file = new File(Environment.getExternalStorageDirectory() + "/ExtractedText", fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}