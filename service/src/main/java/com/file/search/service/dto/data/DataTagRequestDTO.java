package com.file.search.service.dto.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class DataTagRequestDTO {
    private String tag;

    @Override
    public String toString() {
        return tag;
    }
}
