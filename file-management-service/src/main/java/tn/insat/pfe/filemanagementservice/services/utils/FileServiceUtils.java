package tn.insat.pfe.filemanagementservice.services.utils;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

public class FileServiceUtils {

    private FileServiceUtils() {

    }

    public static List<String> renameFiles(List<String> fileNames) {
        List<String> newFileNames = new ArrayList<>();
        for (int i = 0; i < fileNames.size(); i++) {
            List<String> otherNames = new ArrayList<>(fileNames);
            otherNames.remove(i);
            String fileName = fileNames.get(i);
            String fileNameWithoutExtension = FilenameUtils.getBaseName(fileName);
            String extension = FilenameUtils.getExtension(fileName);
            int j = 1;
            while (otherNames.contains(fileName)) {
                fileName = fileNameWithoutExtension + " (" + j + ")." + extension;
                j++;
            }
            fileNames.set(i, fileName);
            newFileNames.add(fileName);
        }
        return newFileNames;
    }
}
