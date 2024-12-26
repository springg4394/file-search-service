package com.file.search.service.dto.upload;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "파일 업로드 요청")
@Getter
@Setter
public class FileUploadRequestDTO {

    @Schema(description = "업로드할 파일", required = true)
    private MultipartFile file;

    @Schema(description = "오브젝트 이름", required = true)
    private String objectName;

    @Schema(description = "워크스페이스 아이디", required = true)
    private String workspaceId;

    @Schema(description = "디렉토리 아이디", required = false)
    private String directoryId;

    @Schema(description = "detect", required = true)
    private String detect;

    @Schema(description = "text", required = true)
    private String text;

}
