package ru.test.filemerge.service.impl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.test.filemerge.service.FileService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    private List<Path> txtFilePaths = new ArrayList<>();

    @Override
    @SneakyThrows
    public String mergeFile(Path rootPath, Path resultPath, String savedFileName) {
        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }
        long count = Files.list(rootPath).map(this::collectTxtPaths).count();
        List<Path> sortedList = txtFilePaths.stream().sorted().collect(Collectors.toList());
        return merge(resultPath, savedFileName, sortedList);
    }

    @Override
    @SneakyThrows
    public void readMergedFile(Path mergedFile) {
        try (BufferedReader br = Files.newBufferedReader(mergedFile)) {
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
        }
    }

    @SneakyThrows
    private Path collectTxtPaths(Path filesInRoot) {
        if (isDirectory(filesInRoot) && !isDirectoryEmpty(filesInRoot)) {
            return Files.list(filesInRoot).map(this::collectTxtPaths).collect(Collectors.toList()).iterator().next();
        } else if (isTxtFile(filesInRoot)) {
            txtFilePaths.add(filesInRoot);
            return filesInRoot;
        }
        return null;
    }

    private boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    private boolean isTxtFile(Path path) {
        if (isDirectory(path)) {
            return false;
        }
        String fileName = path.getFileName().toString();
        if (!Objects.isNull(fileName) && !fileName.isEmpty()) {
            int dotIndex = fileName.lastIndexOf(".");
            return dotIndex < fileName.length() ? fileName.substring(dotIndex).equals(".txt") : false;
        }
        return false;
    }

    private boolean isDirectoryEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }
        return false;
    }

    private String merge(Path resultPath, String resultFilename, List<Path> sortedFiles) throws IOException {
        if (!Files.exists(resultPath)) {
            Files.createDirectories(resultPath);
            Files.createFile(Path.of(resultPath + "/" + resultFilename + ".txt"));
        }
        Path savedFile = Path.of(resultPath + "/" + resultFilename + ".txt");
        try (BufferedWriter out = Files.newBufferedWriter(savedFile)) {
            for (Path inFileName : sortedFiles) {
                if (Files.readString(inFileName) != "") {
                    out.write(Files.readString(inFileName));
                    out.append('\n');
                }
            }
        }
        return savedFile.toAbsolutePath().toString();
    }
}
