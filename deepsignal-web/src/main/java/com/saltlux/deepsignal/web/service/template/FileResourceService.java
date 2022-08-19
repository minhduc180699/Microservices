package com.saltlux.deepsignal.web.service.template;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.FileInfo;
import com.saltlux.deepsignal.web.domain.GetFileData;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.repository.FileStorageRepository;
import com.saltlux.deepsignal.web.service.IFileStorageService;
import com.saltlux.deepsignal.web.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FileResourceService {

    private final FileStorageRepository fileStorageRepository;
    private final IFileStorageService fileStorageService;
    private final ConnectomeRepository connectomeRepository;

    public FileResourceService(
        FileStorageRepository fileStorageRepository,
        IFileStorageService fileStorageService,
        ConnectomeRepository connectomeRepository
    ) {
        this.fileStorageRepository = fileStorageRepository;
        this.fileStorageService = fileStorageService;
        this.connectomeRepository = connectomeRepository;
    }

    //get files by user_id
    public List<FileInfo> getFileByUserId(String id, String type, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<FileInfo> fileInfoList = fileStorageRepository.findFileInfoByUser_IdAndType(id, type, pageable);
        return fileInfoList;
    }

    //get all pages
    public int totalPage(String id, String type) {
        int offset = 10;
        List<FileInfo> fileInfoList = fileStorageRepository.findFileInfoByUser_IdAndType(id, type);
        int all = fileInfoList.size();
        if (all % offset != 0) {
            all = all / offset + 1;
        } else {
            all = all / offset;
        }
        return all;
    }

    //get data by date
    @Deprecated
    public List<GetFileData> getDataByDate(String id, String type, int page) {
        List<FileInfo> fileInfoList = getFileByUserId(id, type, page);
        List<FileInfo> fileInfoList1 = getFileByUserId(id, type, page);
        for (int i = 0; i < fileInfoList1.size(); i++) {
            if (fileInfoList1.get(i).getCreatedDate() != null) {
                for (int j = i + 1; j < fileInfoList1.size(); j++) {
                    if (
                        fileInfoList1
                            .get(i)
                            .getCreatedDate()
                            .toString()
                            .substring(0, 10)
                            .equals(fileInfoList1.get(j).getCreatedDate().toString().substring(0, 10))
                    ) {
                        fileInfoList1.get(j).setCreatedDate(null);
                    }
                }
            }
        }
        List<FileInfo> fileInfoList3 = new ArrayList<>();
        for (Object o : fileInfoList1) {
            FileInfo fileInfo = (FileInfo) o;
            if (fileInfo.getCreatedDate() != null) {
                fileInfoList3.add(fileInfo);
            }
        }
        List<GetFileData> getFileData = new ArrayList<>();
        int total = totalPage(id, type);
        for (int i = 0; i < fileInfoList3.size(); i++) {
            GetFileData getFileData1 = new GetFileData();
            List<FileInfo> fileInfoList2 = new ArrayList<>();
            for (int j = 0; j < fileInfoList.size(); j++) {
                if (
                    fileInfoList3
                        .get(i)
                        .getCreatedDate()
                        .toString()
                        .substring(0, 10)
                        .equals(fileInfoList.get(j).getCreatedDate().toString().substring(0, 10))
                ) {
                    fileInfoList2.add(fileInfoList.get(j));
                }
            }
            getFileData1.setDate(fileInfoList3.get(i).getCreatedDate().toString().substring(0, 10));
            getFileData1.setFileInfoList(fileInfoList2);
            getFileData1.setTotalPage(total);
            getFileData.add(getFileData1);
        }
        return getFileData;
    }

    public GetFileData getAllFileByTypeAndId(String connectomeId, int page, String type) {
        Optional<Connectome> connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId);
        if (!connectome.isPresent()) {
            return null;
        }
        Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<FileInfo> fileInfo = fileStorageRepository.findFileInfoByUser_IdAndType(pageable, connectome.get().getUser().getId(), type);
        int total = (int) fileStorageRepository.countFileInfoByUser_IdAndType(connectome.get().getUser().getId(), type);
        GetFileData getFileData = new GetFileData();
        getFileData.setTotalPage(total);
        getFileData.setFileInfoList(fileInfo.getContent());
        return getFileData;
    }

    //delete data
    public boolean deleteDataById(Long id) {
        try {
            Optional<FileInfo> optionalFileInfo = fileStorageRepository.findById(id);
            if (!optionalFileInfo.isPresent()) {
                return false;
            }
            FileInfo fileInfo = optionalFileInfo.get();
            // if type is file, delete file from storage
            if (fileInfo.getType().equalsIgnoreCase(Constants.UploadType.FILE.name())) {
                String path = fileStorageService.getPathFile(fileInfo.getUser().getId());
                FileUtil.deleteFileIfExist(path + "/" + fileInfo.getName());
            }
            fileStorageRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<FileInfo> pagingFileInfo(Pageable pageable, String userId, String type) {
        return fileStorageRepository.findFileInfoByUser_IdAndType(pageable, userId, type);
    }

    public File getFileById(Long id) {
        String type = Constants.UploadType.FILE.name();
        Optional<FileInfo> fileInfo = fileStorageRepository.findByIdAndType(id, type);
        if (!fileInfo.isPresent()) {
            throw new BadRequestException("Cannot find FileInfo with id = " + id);
        }
        String pathFile = fileInfo.get().getPath();
        return new File(pathFile);
    }
}
