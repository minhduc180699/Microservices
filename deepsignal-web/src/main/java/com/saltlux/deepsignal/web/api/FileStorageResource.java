package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.FileInfo;
import com.saltlux.deepsignal.web.exception.UploadException;
import com.saltlux.deepsignal.web.exception.UploadFileExistedException;
import com.saltlux.deepsignal.web.exception.UploadFileFormatException;
import com.saltlux.deepsignal.web.exception.UploadFileMaxSizeException;
import com.saltlux.deepsignal.web.service.IFileStorageService;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.service.dto.FileInfoFindParam;
import com.saltlux.deepsignal.web.service.dto.UrlUploadDTO;
import com.saltlux.deepsignal.web.service.template.FileResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file-storage")
@Tag(name = "File Storage Management", description = "The file storage management API")
public class FileStorageResource {

    private final IFileStorageService storageService;

    @Autowired
    UserService userService;

    private final FileResourceService fileResourceService;

    public FileStorageResource(IFileStorageService storageService, FileResourceService fileResourceService) {
        this.storageService = storageService;
        this.fileResourceService = fileResourceService;
    }

    /**
     * {@code POST  /upload/{userId}/{connectomeId}} : upload file.
     *
     * @param files        file uploaded
     * @param userId       user_id of current user
     * @param connectomeId connectome_id of current connectome
     * @return
     */
    @Deprecated
    @PostMapping("/upload/{userId}/{connectomeId}")
    @Operation(summary = "Upload file", tags = { "File Storage Management" })
    public ResponseEntity<?> uploadFile(
        @RequestParam("files") MultipartFile[] files,
        @PathVariable("userId") String userId,
        @PathVariable("connectomeId") String connectomeId
    ) {
        try {
            //            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(storageService.saveFile(files, userId, connectomeId));
        } catch (UploadFileFormatException ufe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_FILE_INCORRECT_FORMAT.description);
        } catch (UploadFileExistedException uee) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.ErrorCode.DEEPSINAL_FILE_EXISTS.description);
        } catch (UploadException ue) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Constants.ErrorCode.DEEPSINAL_FILE_UPLOAD_ERROR.description);
        }
    }

    @Deprecated
    @PostMapping("/uploadURL/{userId}/{connectomeId}")
    @Operation(summary = "Upload URL", tags = { "URL Storage Management" })
    public ResponseEntity<?> uploadURL(
        @PathVariable("userId") String userId,
        @PathVariable("connectomeId") String connectomeId,
        @RequestBody List<UrlUploadDTO> urlList
    ) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(connectomeId) || urlList.size() < 1) {
            return new ResponseEntity(new ApiResponse(false, "UserId or ConnectomeId or Name or List Url is null"), HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok().body(storageService.saveURL(urlList, userId, connectomeId));
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, "Save fail!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    @Operation(summary = "Get file data", tags = { "File Storage Management" })
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }

    @PostMapping("/upload/{userId}")
    @Operation(summary = "Upload file", tags = { "File Storage Management" })
    public ResponseEntity<?> uploadFile(
        @RequestParam("file") MultipartFile file,
        @PathVariable("userId") String userId,
        @RequestParam("type") Integer type
    ) {
        try {
            String path = null;

            if (Constants.Type.TYPE_FILE_INQUIRY_QUESTION.equals(type)) {
                path = storageService.saveInquiry(file, userId, "question");
            } else {
                path = storageService.saveInquiry(file, userId, "answer");
            }
            return ResponseEntity.status(HttpStatus.OK).body(path);
        } catch (UploadFileMaxSizeException ufe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_FILE_MAX_SIZE.description);
        } catch (UploadFileFormatException ufe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_FILE_INCORRECT_FORMAT.description);
        } catch (UploadFileExistedException uee) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.ErrorCode.DEEPSINAL_FILE_EXISTS.description);
        } catch (UploadException ue) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Constants.ErrorCode.DEEPSINAL_FILE_UPLOAD_ERROR.description);
        }
    }

    @Deprecated
    @GetMapping("/getAllFile/{id}/{page}")
    public ResponseEntity<?> getAllFile(@PathVariable String id, @PathVariable int page) {
        if (id == "" || id == null) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.description,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        if (userService.findUserById(id) == null) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.description,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        String type = Constants.UploadType.FILE.toString();
        return ResponseEntity.ok().body(fileResourceService.getDataByDate(id, type, page));
    }

    @GetMapping("/getAllFileByIdAndType")
    @Operation(summary = "Get all file by id and type", tags = { "File Storage Management" })
    public ResponseEntity<?> getAllUrl(
        @RequestParam("connectomeId") String connectomeId,
        @RequestParam("type") String type,
        @RequestParam("page") int page
    ) {
        if (StringUtils.isEmpty(connectomeId) || StringUtils.isEmpty(type)) {
            return ResponseEntity.badRequest().body("connectomeId is null");
        }
        if (Arrays.stream(Constants.UploadType.values()).allMatch(d -> d.equals(type))) {
            return ResponseEntity.badRequest().body(("type doesn't exist"));
        }
        return ResponseEntity.ok().body(fileResourceService.getAllFileByTypeAndId(connectomeId, page, type));
    }

    //delete data
    @DeleteMapping("/deleteData/{id}")
    public ResponseEntity<?> deleteData(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.description,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        if (fileResourceService.deleteDataById(id)) {
            return ResponseEntity.ok().body(null);
        } else {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.description,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    //upload doc
    @Deprecated
    @PostMapping("/uploadDOC/{userId}/{connectomeId}")
    public ResponseEntity<?> uploadDOC(
        @PathVariable("userId") String userId,
        @PathVariable("connectomeId") String connectomeId,
        @RequestParam("docs") String docs
    ) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(connectomeId) || StringUtils.isEmpty(docs)) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.description,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }

        try {
            storageService.saveDOC(docs, userId, connectomeId);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // get count files upload by connectomeId
    @GetMapping("/file/count/{connectomeId}")
    public ResponseEntity<?> getCountFileUploadByConnectomeId(@PathVariable("connectomeId") String connectomeId) {
        if (StringUtils.isEmpty(connectomeId)) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.description,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        return ResponseEntity.ok().body(storageService.getCountFileUploadByConnectomeId(connectomeId));
    }

    @DeleteMapping("/deleteAll/{id}/{type}")
    @Operation(summary = "Delete all file", tags = { "File Storage Management" })
    public ResponseEntity<?> deletaAllFile(@PathVariable("id") String id, @PathVariable("type") String type) {
        if (StringUtils.isEmpty(type) && StringUtils.isEmpty(id)) {
            return ResponseEntity.badRequest().body("Params is null!");
        }
        if (!storageService.deleteAllByType(id, type)) {
            return ResponseEntity.badRequest().body("Delete Fail!");
        } else {
            return ResponseEntity.ok().body("Delete success");
        }
    }

    /**
     * {@code GET  /getAllDoc/{userId}: get all doc uploaded in learning center
     *
     * @param userId   user_id of current user
     * @param pageable pageable to paging
     * @return
     */
    @GetMapping("/getAllDoc/{userId}")
    @Operation(summary = "Get all DOC in learning center", tags = { "DOC Storage Management" })
    public ResponseEntity<?> getAllDoc(Pageable pageable, @PathVariable String userId) {
        return ResponseEntity.ok().body(fileResourceService.pagingFileInfo(pageable, userId, Constants.UploadType.FILE.name()));
    }

    /**
     * {@code GET  /downloadDoc/{id}: download file uploaded by id
     *
     * @param id: id of document uploaded
     * @return
     */
    @GetMapping("/downloadDoc/{id}")
    @Operation(summary = "Download file from server by id", tags = { "DOC Storage Management" })
    public ResponseEntity<?> downloadByDoc(@PathVariable Long id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            File file = fileResourceService.getFileById(id);
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new FileNotFoundException("File is not exist!"));
        }
    }

    @GetMapping("/searchDynamic")
    public ResponseEntity<Page<FileInfo>> searchDynamic(Pageable page, FileInfoFindParam fileInfoFindParam) {
        return new ResponseEntity<Page<FileInfo>>(fileResourceService.searchDynamic(page, fileInfoFindParam), HttpStatus.OK);
    }

    @PostMapping("/saveForChromeEx/{connectoneId}")
    public ResponseEntity<?> searchDynamic(@RequestBody FileInfo fileInfo, @PathVariable String connectoneId) {
        try {
            fileResourceService.saveForChromeEx(fileInfo, connectoneId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("OK");
    }
}
