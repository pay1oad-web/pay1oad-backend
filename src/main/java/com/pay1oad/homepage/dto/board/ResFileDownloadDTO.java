package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.FileEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResFileDownloadDTO {

    private String filename;
    private String fileType;
    private byte[] content;

    @Builder
    public ResFileDownloadDTO(String filename, String fileType, byte[] content) {
        this.filename = filename;
        this.fileType = fileType;
        this.content = content;
    }

    public static ResFileDownloadDTO fromFileResource(FileEntity file, String contentType, byte[] content) {
        return ResFileDownloadDTO.builder()
                .filename(file.getOriginFileName())
                .fileType(contentType)
                .content(content)
                .build();
    }
}