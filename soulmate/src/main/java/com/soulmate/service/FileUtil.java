package com.soulmate.service;

import com.soulmate.domain.attachFile.AttachFile;
import com.soulmate.domain.attachFile.PetAttachFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {
    private final String BASE_DIR = "D:/test";

    public List<AttachFile> upload(List<MultipartFile> files, String type) throws IOException {
        Path path = Paths.get(BASE_DIR + File.separator + type);
        List<AttachFile> attachFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));

            AttachFile saved = AttachFile.builder()
                    .seq(0)
                    .savedName(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli() + extension)
                    .originalName(originalName)
                    .dir(BASE_DIR)
                    .build();

            Path savePath = Paths.get(BASE_DIR + File.separator + type + File.separator + saved.getSavedName());
            if (!Files.isDirectory(path)) Files.createDirectories(path);
            file.transferTo(new File(savePath.toString())); //파일 저장

            attachFiles.add(saved);
        }

        return attachFiles;
    }

    public AttachFile uploadOne(MultipartFile file, String type) throws IOException {
        Path path = Paths.get(BASE_DIR + File.separator + type);
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));

        AttachFile saved = AttachFile.builder()
                .seq(0)
                .savedName(LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli() + extension)
                .originalName(originalName)
                .dir(BASE_DIR)
                .build();

        Path savePath = Paths.get(BASE_DIR + File.separator + type + File.separator + saved.getSavedName());

        if (!Files.isDirectory(path)) Files.createDirectories(path);
        file.transferTo(new File(savePath.toString())); //파일 저장

        return saved;
    }
}
