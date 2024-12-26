package com.file.search.service.model;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(indexName = "image-model1")
public class DetectClassData {

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("image_path")
    private String imagePath;
    
    @Field(type = FieldType.Nested)
    private List<Prediction> predictions;

    @Getter
    @Setter
    public static class Prediction {
        @JsonProperty("class_id")
        private int classId;

        @JsonProperty("class_name")
        private String className;

        @Field(type = FieldType.Float)
        private float confidence;

        @Field(type = FieldType.Float)
        private List<Float> bbox;
    }

}
