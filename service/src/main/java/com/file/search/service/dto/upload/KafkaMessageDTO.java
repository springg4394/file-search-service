package com.file.search.service.dto.upload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaMessageDTO {
    private String bucketName;
    private String objectName;
    private String fileId;
    private String detect;
    private String text;

}
