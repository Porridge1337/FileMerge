package ru.test.filemerge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.test.filemerge.controller.FileMergeController;

@SpringBootApplication
public class FilemergeApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(FilemergeApplication.class, args);
        FileMergeController bean = run.getBean(FileMergeController.class);
        String mergedPath = bean.merge("result");
        System.out.println("Результат слияния файлов находится в " + mergedPath);
    }

}
