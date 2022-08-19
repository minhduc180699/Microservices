package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.FileInfo;
import com.saltlux.deepsignal.web.service.dto.FileUploadDTO;
import com.saltlux.deepsignal.web.service.dto.LearningDocument;
import com.saltlux.deepsignal.web.service.dto.UrlUploadDTO;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService extends GeneralService<FileInfo, Long> {
    void init();

    @Deprecated
    FileUploadDTO saveFile(MultipartFile[] file, String userId, String connectomeId);

    @Deprecated
    FileUploadDTO saveURL(List<UrlUploadDTO> urlList, String userId, String connectomeId);

    @Deprecated
    void saveDOC(String docId, String userId, String connectomeId);

    FileUploadDTO saveAllFileInfos(
        List<LearningDocument> learningDocuments,
        List<UrlUploadDTO> urlList,
        String docsId,
        String userId,
        String connectomeId,
        String language
    );

    Resource load(String filename);

    void deleteAll();

    public String saveInquiry(MultipartFile file, String userId, String folderName);

    String getPathFile(String userId);

    long getCountFileUploadByConnectomeId(String connectomeId);

    boolean deleteAllByType(String id, String type);
}
