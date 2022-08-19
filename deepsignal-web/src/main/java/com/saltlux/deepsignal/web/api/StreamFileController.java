package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.FileInfo;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.service.impl.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
@Tag(name = "Stream File", description = "Stream File API")
public class StreamFileController {

    private ApplicationProperties applicationProperties;

    public StreamFileController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     *
     * @param response
     * @param userId
     * @param folderName
     * @param fileName
     * @param type
     * @throws IOException
     */
    @GetMapping("/{type}/{userId}/{folderName}/{fileName}")
    @Operation(summary = "Stream file", tags = { "Stream File" })
    public void getFileQuestion(
        HttpServletResponse response,
        @PathVariable("userId") String userId,
        @PathVariable("folderName") String folderName,
        @PathVariable("fileName") String fileName,
        @PathVariable("type") String type
    ) throws IOException {
        String path = applicationProperties.getFilesUpload().getLocation() + "/" + type + "/" + userId + "/" + folderName + "/" + fileName;
        File file = new File(path);
        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
