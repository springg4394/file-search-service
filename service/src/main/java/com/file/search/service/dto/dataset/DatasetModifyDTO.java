package com.file.search.service.dto.dataset;

import java.util.List;

import com.file.search.service.dto.data.DataFileIdRequestDTO;
import com.file.search.service.dto.data.DataTagRequestDTO;

import lombok.Getter;

@Getter
public class DatasetModifyDTO {
    private String datasetId;
    private String workspaceId;
    private String datasetName;
    private List<DataFileIdRequestDTO> fileIds;
    private List<DataTagRequestDTO> tags;
}
