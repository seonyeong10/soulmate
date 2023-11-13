package com.soulmate.service;

import com.soulmate.domain.attachFile.AttachFile;
import com.soulmate.domain.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private String BASE_DIR = "D:/test";

    private final AttachFileRepository fileRepository;

    /**
     * 이미지 파일을 보여준다.
     */
    public ResponseEntity view(Long fileId) {
        AttachFile attachFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다. file_id = " + fileId));
        String filePath = BASE_DIR + attachFile.getDir() + File.separator + attachFile.getSavedName();

        FileSystemResource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        Path path = Paths.get(filePath);
        try {
            headers.add("Content-type", Files.probeContentType(path));
        } catch (IOException e) {
            log.error("파일을 찾을 수 없습니다.", e);
            throw new RuntimeException(e);
        }

        return new ResponseEntity(resource, headers, HttpStatus.OK);
    }
}
