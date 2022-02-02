package com.example.filedemo.translator;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;


@Component
public class WordToPdfTranslator {

    @Value("${file.upload-dir}")
    private String baseDirPath;

    @Autowired
    IConverter iConverter;

    public File wordToPdf(Path targetLocation, String fileName) throws IOException {
        String[] newFileName = fileName.split("\\.");
        String tempFileName = newFileName[0] + Math.abs(Math.random());
        String theFile = baseDirPath.concat(tempFileName).concat(".pdf");
        File inputWord = new File(targetLocation.toAbsolutePath() + "\\" + fileName);
        File outputFile = new File(theFile);

        InputStream docxInputStream = new FileInputStream(inputWord);
        OutputStream outputStream = new FileOutputStream(outputFile);

        iConverter.convert(docxInputStream).as(DocumentType.MS_WORD).to(outputStream).as(DocumentType.PDF).execute();

        outputStream.close();

        return outputFile;
    }

    @Bean
    public IConverter iConverter() {

        final File base = new File(baseDirPath + "\\");

        return LocalConverter.builder()
                .baseFolder(base)
                .workerPool(20, 25, 121211, TimeUnit.DAYS)
                .processTimeout(121211, TimeUnit.DAYS)
                .build();
    }
}
