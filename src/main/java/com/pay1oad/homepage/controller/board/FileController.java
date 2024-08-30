package com.pay1oad.homepage.controller.board;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pay1oad.homepage.dto.board.ResFileDownloadDTO;
import com.pay1oad.homepage.dto.board.ResFileUploadDTO;
import com.pay1oad.homepage.service.board.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category/{categoryId}/board/{boardId}/file")
@RequiredArgsConstructor
public class FileController {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_FILE_TYPES = List.of("image/jpeg", "image/png", "application/pdf");


    private final FileService fileService;

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<List<ResFileUploadDTO>>> upload(
            @PathVariable("boardId") Long boardId,
            @PathVariable("categoryid") Long categoryId,
            @RequestParam("file") List<MultipartFile> files) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 파일 유형 및 크기 검사
                for (MultipartFile file : files) {
                    if (file.getSize() > MAX_FILE_SIZE) {
                        throw new IllegalArgumentException("File size exceeds the allowed limit of 5MB");
                    }
                    if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
                        throw new IllegalArgumentException("File type " + file.getContentType() + " is not allowed");
                    }
                    // 파일 이름 유효성 검사
                    if (!isValidFileName(file.getOriginalFilename())) {
                        throw new IllegalArgumentException("Invalid file name: " + file.getOriginalFilename());
                    }
                }

                List<ResFileUploadDTO> saveFile = fileService.upload(boardId, categoryId, files);
                return ResponseEntity.status(HttpStatus.CREATED).body(saveFile);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        });
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileId") Long fileId) {
        try {
            ResFileDownloadDTO downloadDto = fileService.download(fileId);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(downloadDto.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadDto.getFilename() + "\"")
                    .body(new ByteArrayResource(downloadDto.getContent()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Long> delete(@RequestParam("fileId") Long fileId) {
        try {
            fileService.delete(fileId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private boolean isValidFileName(String fileName) {
        String safeFileName = FilenameUtils.getName(fileName);
        return fileName.equals(safeFileName);
    }
}