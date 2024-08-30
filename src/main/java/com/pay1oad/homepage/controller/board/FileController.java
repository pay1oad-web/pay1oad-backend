package com.pay1oad.homepage.controller.board;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.security.TokenProvider;
import com.pay1oad.homepage.service.login.MemberService;
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
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/category/{categoryId}/board/{boardId}/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<List<ResFileUploadDTO>>> upload(
            HttpServletRequest request,
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("boardId") Long boardId,
            @RequestParam("file") List<MultipartFile> files) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String token = request.getHeader("Authorization").substring(7);
                int userid = Integer.parseInt(tokenProvider.validateAndGetUserId(token));
                Member member = memberService.getMemberByID(userid);

                // 파일 업로드 시작 로그
                System.out.println("Starting file upload...");

                // 파일 정보 출력
                for (MultipartFile file : files) {
                    System.out.println("File name: " + file.getOriginalFilename());
                    System.out.println("File size: " + file.getSize());
                    System.out.println("File type: " + file.getContentType());
                }

                List<ResFileUploadDTO> saveFile = fileService.upload(boardId, categoryId, files);
                return ResponseEntity.status(HttpStatus.CREATED).body(saveFile);
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        });
    }


    @GetMapping("/download")
    public ResponseEntity<Resource> download(
            @RequestParam("fileId") Long fileId,
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("boardId") Long boardId) {
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
    public ResponseEntity<Long> delete(
            @RequestParam("fileId") Long fileId,
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("boardId") Long boardId) {
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
