package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class Wrapper {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedWrapper {

        private String connectome_id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfoWrapper {

        private List<FileInfoDTO> fileInfoDTOS;
    }
}
