package ru.test.filemerge.service;

import java.nio.file.Path;

public interface FileService {

    String mergeFile(Path rootPath, Path resultPath, String savedFileName);

    void readMergedFile(Path mergedFile);
}
