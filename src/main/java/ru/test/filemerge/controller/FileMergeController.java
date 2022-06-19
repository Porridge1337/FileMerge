package ru.test.filemerge.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import ru.test.filemerge.service.FileService;

import java.nio.file.Path;

@Controller
@RequiredArgsConstructor
public class FileMergeController {

    @Value(("${folder.path.root}"))
    private String rootFolder;
    @Value("${folder.path.save}")
    private String resultFolder;
    private final FileService fileService;

    public String merge(String savedFileName) {
        String mergedPath = fileService.mergeFile(Path.of(rootFolder), Path.of(resultFolder), savedFileName);
        fileService.readMergedFile(Path.of(mergedPath));
        return mergedPath;
    }

}
