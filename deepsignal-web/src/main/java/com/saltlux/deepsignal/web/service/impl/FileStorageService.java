package com.saltlux.deepsignal.web.service.impl;

import static com.saltlux.deepsignal.web.config.Constants.DOC_CONVERT;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.FileInfo;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.exception.UploadException;
import com.saltlux.deepsignal.web.exception.UploadFileExistedException;
import com.saltlux.deepsignal.web.exception.UploadFileFormatException;
import com.saltlux.deepsignal.web.exception.UploadFileMaxSizeException;
import com.saltlux.deepsignal.web.repository.FileStorageRepository;
import com.saltlux.deepsignal.web.service.IFileStorageService;
import com.saltlux.deepsignal.web.service.dto.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.util.StringHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional
@Log4j2
public class FileStorageService implements IFileStorageService {

    private final FileStorageRepository fileStorageRepository;

    private final ModelMapper modelMapper;

    private final Path rootLocation;

    private final String API;

    private final String URLDocConverter;

    private final String URLUploadFile;

    private final Long maxSize;

    private DateTimeFormatter dateTimeFormatter;

    private final String fomatDate = "yyyyMMddHHmmss";

    @Autowired
    private RestTemplate restTemplate;

    public FileStorageService(FileStorageRepository fileStorageRepository, ModelMapper modelMapper, ApplicationProperties properties) {
        this.fileStorageRepository = fileStorageRepository;
        this.rootLocation = Paths.get(properties.getFilesUpload().getLocation());
        this.modelMapper = modelMapper;
        this.API = properties.getExternalApi().getDeepsignalAdapter();
        this.URLDocConverter = properties.getExternalApi().getDeepsignalDocConverter();
        this.URLUploadFile = properties.getExternalApi().getDeepsignalFile();
        this.maxSize = Long.parseLong(properties.getFilesUpload().getMaxSize());
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(fomatDate);
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public List findAll() {
        return fileStorageRepository.findAll();
    }

    @Override
    public Optional findById(Long id) {
        return fileStorageRepository.findById(id);
    }

    @Override
    public FileInfo save(FileInfo fileInfo) {
        return fileStorageRepository.save(fileInfo);
    }

    @Override
    public void remove(Long id) {
        fileStorageRepository.deleteById(id);
    }

    @Deprecated
    @Override
    public FileUploadDTO saveFile(MultipartFile[] files, String userId, String connectomeId) {
        int numOfSuccess = 0;
        int numOfFail = 0;
        List<FileInfo> fileInfos = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            try {
                String extension = FilenameUtils.getExtension(fileName);
                if (FileFormat.validate(extension, Constants.Type.TYPE_FILE_UPLOAD) == null) {
                    numOfFail++;
                    log.error("File " + fileName + " incorrect formats");
                    continue;
                    //                    throw new UploadFileFormatException("File " + fileName + " incorrect formats");
                }

                Path filePath = Paths.get(this.rootLocation.toRealPath() + "/" + userId);
                if (!Files.exists(filePath)) {
                    Files.createDirectory(filePath);
                }

                assert fileName != null;
                if (Files.exists(filePath.resolve(fileName))) {
                    numOfFail++;
                    log.error("File " + fileName + " is existed");
                    continue;
                    //                    throw new UploadFileExistedException("File " + fileName + " is existed");
                }

                // Upload File
                Files.copy(file.getInputStream(), filePath.resolve(fileName));

                User user = new User();
                Connectome connectome = new Connectome();
                Set<Connectome> connectomes = new HashSet<>();

                user.setId(userId);
                connectome.setConnectomeId(connectomeId);
                connectomes.add(connectome);

                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(file.getOriginalFilename());
                fileInfo.setPath(filePath.resolve(file.getOriginalFilename()).toString());
                fileInfo.setSize(file.getSize());
                fileInfo.setMineType(file.getContentType());
                fileInfo.setType(Constants.UploadType.FILE.toString());
                fileInfo.setUser(user);
                fileInfo.setConnectomes(connectomes);
                //                fileStorageRepository.save(fileInfo);
                numOfSuccess++;
                fileInfos.add(fileInfo);
            } catch (IOException e) {
                // throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
                //                throw new UploadException("Failed to upload file " + fileName, e);
                numOfFail++;
                log.error("Failed to upload file " + fileName, e);
            }
        }
        fileStorageRepository.saveAll(fileInfos);
        // Push File info to Kafka
        if (fileInfos.size() > 0) {
            this.callKafkaPublishApi(fileInfos, connectomeId);
        }
        return new FileUploadDTO(numOfSuccess, numOfFail);
    }

    @Deprecated
    @Override
    public FileUploadDTO saveURL(List<UrlUploadDTO> urlList, String userId, String connectomeId) {
        User user = new User();
        Connectome connectome = new Connectome();
        Set<Connectome> connectomes = new HashSet<>();

        user.setId(userId);
        connectome.setConnectomeId(connectomeId);
        connectomes.add(connectome);

        List<FileInfo> fileInfos = new ArrayList<>();
        String type = Constants.UploadType.URL.toString();
        List<FileInfo> fileInfoList = fileStorageRepository.findFileInfoByUser_IdAndType(userId, type);
        int success = 0;
        for (UrlUploadDTO urlUpload : urlList) {
            boolean isNext = true;
            for (FileInfo fileInfo : fileInfoList) {
                if (urlUpload.getUrl().equals(fileInfo.getPath())) {
                    isNext = false;
                }
            }
            if (isNext) {
                success++;
                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(urlUpload.getName());
                fileInfo.setPath(urlUpload.getUrl());
                fileInfo.setType(Constants.UploadType.URL.toString());
                fileInfo.setUser(user);
                fileInfo.setConnectomes(connectomes);
                fileInfo.setAuthor(urlUpload.getAuthor());
                fileInfo.setOriginDate(urlUpload.getOriginDate());
                fileInfo.setSearchType(urlUpload.getSearchType());
                //                fileStorageRepository.save(fileInfo);
                fileInfos.add(fileInfo);
            }
        }
        fileStorageRepository.saveAll(fileInfos);
        // Push URL info to Kafka
        this.callKafkaPublishApi(fileInfos, connectomeId);
        return new FileUploadDTO(success, urlList.size() - success);
    }

    @Deprecated
    @Override
    public void saveDOC(String docsId, String userId, String connectomeId) {
        User user = new User();
        Connectome connectome = new Connectome();
        Set<Connectome> connectomeSet = new HashSet<>();

        user.setId(userId);
        connectome.setConnectomeId(connectomeId);
        connectomeSet.add(connectome);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath(docsId);
        fileInfo.setName("DOC" + java.time.LocalDateTime.now());
        fileInfo.setType(Constants.UploadType.DOC.toString());
        fileInfo.setUser(user);
        fileInfo.setConnectomes(connectomeSet);

        fileStorageRepository.save(fileInfo);
        List<FileInfo> fileInfos = new ArrayList<>();
        fileInfos.add(fileInfo);
        this.callKafkaPublishApi(fileInfos, connectomeId);
    }

    @SuppressWarnings("Duplicates")
    @Transactional
    @Override
    public FileUploadDTO saveAllFileInfos(
        List<LearningDocument> learningDocuments,
        List<UrlUploadDTO> urlList,
        String docsId,
        @NotNull String userId,
        @NotNull String connectomeId,
        @Nullable String language
    ) {
        Set<Connectome> connectomeSet = new HashSet<>();
        connectomeSet.add(new Connectome(connectomeId));
        User user = new User(userId);

        List<FileInfo> fileInfos = new ArrayList<>();
        String type = Constants.UploadType.URL.toString();
        int success = 0;
        int total = 0;
        List<UrlUploadDTO> downloadList = new ArrayList<>();
        // save URL
        if (Objects.nonNull(urlList) && urlList.size() > 0) {
            total += urlList.size();
            //            List<String> paths = fileStorageRepository.findByUser_IdAndType(userId, type);
            for (UrlUploadDTO urlUploadDTO : urlList) {
                //                boolean isFileExisted = paths.stream().anyMatch(item -> urlUploadDTO.getUrl().equals(item));
                //                if (isFileExisted) {
                //                    log.error("URL: " + urlUploadDTO.getUrl() + " existed");
                //                } else {
                if (
                    !urlUploadDTO.getSearchType().equals("searchFileType:ppt") &&
                    !urlUploadDTO.getSearchType().equals("searchFileType:pdf") &&
                    !urlUploadDTO.getSearchType().equals("searchFileType:docx")
                ) {
                    FileInfo fileInfo = new FileInfo();
                    BeanUtils.copyProperties(urlUploadDTO, fileInfo);
                    fileInfo.setPath(urlUploadDTO.getUrl());
                    fileInfo.setType(urlUploadDTO.getType());
                    fileInfo.setUser(user);
                    fileInfo.setConnectomes(connectomeSet);
                    fileInfo.setLang(language);
                    fileInfos.add(fileInfo);
                    success++;
                } else {
                    downloadList.add(urlUploadDTO);
                }
                //                }
            }
        }

        //save download file
        if (Objects.nonNull(downloadList) && downloadList.size() > 0) {
            String typeDownload = Constants.UploadType.DOWNLOAD.toString();
            //            List<String> paths = fileStorageRepository.findByUser_IdAndType(userId, typeDownload);
            for (UrlUploadDTO urlUploadDTO : downloadList) {
                //                boolean isFileExisted = paths.stream().anyMatch(item -> urlUploadDTO.getUrl().equals(item));
                //                if (!isFileExisted) {
                FileInfo fileInfo = new FileInfo();
                BeanUtils.copyProperties(urlUploadDTO, fileInfo);
                fileInfo.setFileType(fileInfo.getSearchType().replace("searchFileType:", ""));
                if (StringUtils.isNotEmpty(fileInfo.getOriginDate())) {
                    //
                } else {
                    fileInfo.setPublishedDate(fileInfo.getCreatedDate());
                }
                fileInfo.setPath(urlUploadDTO.getUrl());
                fileInfo.setType(Constants.UploadType.DOWNLOAD.toString());
                fileInfo.setUser(user);
                fileInfo.setConnectomes(connectomeSet);
                fileInfo.setLang(language);
                fileInfos.add(fileInfo);
                success++;
                //                }
            }
        }

        // save file
        for (LearningDocument learningDocument : learningDocuments) {
            total += learningDocument.getFiles().length;
            for (MultipartFile file : learningDocument.getFiles()) {
                String fileName = file.getOriginalFilename();
                try {
                    String extension = FilenameUtils.getExtension(fileName);
                    if (FileFormat.validate(extension, Constants.Type.TYPE_FILE_UPLOAD) == null) {
                        log.error("File " + fileName + " incorrect formats");
                        continue;
                    }

                    Path filePath = Paths.get(this.rootLocation.toRealPath() + "/" + userId + "/" + learningDocument.getName());
                    if (!Files.exists(filePath)) {
                        Files.createDirectory(filePath);
                    }

                    assert fileName != null;
                    if (Files.exists(filePath.resolve(fileName))) {
                        log.error("File " + fileName + " is existed");
                        continue;
                    }
                    // Upload File
                    Files.copy(file.getInputStream(), filePath.resolve(fileName));

                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setName(file.getOriginalFilename());
                    fileInfo.setPath(filePath.resolve(fileName).toString());
                    fileInfo.setSize(file.getSize());
                    fileInfo.setMineType(file.getContentType());
                    fileInfo.setType(Constants.UploadType.FILE.toString());
                    fileInfo.setUser(user);
                    fileInfo.setConnectomes(connectomeSet);
                    fileInfo.setLang(language);
                    fileInfos.add(fileInfo);
                    success++;
                } catch (IOException e) {
                    log.error("Failed to upload file " + fileName, e);
                }
            }
        }

        // save Doc
        if (!StringHelper.isNullOrEmptyString(docsId)) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setPath(docsId);
            fileInfo.setName("DOC" + java.time.LocalDateTime.now());
            fileInfo.setType(Constants.UploadType.DOC.toString());
            fileInfo.setUser(user);
            fileInfo.setConnectomes(connectomeSet);
            fileInfo.setLang(language);
            fileInfos.add(fileInfo);
            success++;
        }
        fileStorageRepository.saveAll(fileInfos);
        pushFileToGoogleCloud(fileInfos, connectomeId);
        return new FileUploadDTO(success, total - success);
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String saveInquiry(MultipartFile file, String userId, String folderName) {
        if (file.isEmpty()) return null;

        String fileName = file.getOriginalFilename();
        try {
            // Get length of file in bytes
            long fileSizeInBytes = file.getSize();
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = fileSizeInBytes / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            long fileSizeInMB = fileSizeInKB / 1024;

            if (fileSizeInMB > maxSize) {
                throw new UploadFileMaxSizeException("File > 10 MB");
            }
            String extension = FilenameUtils.getExtension(fileName);
            if (FileFormat.validate(extension, Constants.Type.TYPE_FILE_INQUIRY) == null) {
                throw new UploadFileFormatException("File incorrect format");
            }

            String pathName =
                this.rootLocation.toRealPath() + "/" + folderName + "/" + userId + "/" + dateTimeFormatter.format(LocalDateTime.now());
            Path filePath = Paths.get(pathName);
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }

            if (Files.exists(filePath.resolve(fileName))) {
                throw new UploadFileExistedException("File is existed");
            }

            // Upload File
            Files.copy(file.getInputStream(), filePath.resolve(fileName));

            return pathName.substring(this.rootLocation.toRealPath().toString().length()) + "/" + fileName;
        } catch (IOException e) {
            // throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            throw new UploadException("Could not store the file");
        }
    }

    @Override
    public String getPathFile(String userId) {
        try {
            String path = this.rootLocation.toRealPath() + "/" + userId;
            if (!Files.exists(Paths.get(path))) {
                Files.createDirectory(Paths.get(path));
            }
            return path;
        } catch (IOException e) {
            log.error("Cannot find path!");
        }
        return null;
    }

    @Override
    public long getCountFileUploadByConnectomeId(String connectomeId) {
        return fileStorageRepository.getCountFileUploadByConnectomeId(connectomeId);
    }

    @Override
    public boolean deleteAllByType(String id, String type) {
        if (type.equals(Constants.UploadType.FILE.toString()) || type.equals(Constants.UploadType.URL.toString())) {
            try {
                fileStorageRepository.deleteAllByUser_IdAndType(id, type);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    // push file to google cloud
    @Async
    public void pushFileToGoogleCloud(List<FileInfo> fileInfos, String connectomeId) {
        List<FileInfoDTO> fileInfoDTOS = new ArrayList<>();
        List<FileInfoDTO> fileInfoDTOWithTypeIsFile = new ArrayList<>();
        for (FileInfo fileInfo : fileInfos) {
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            BeanUtils.copyProperties(fileInfo, fileInfoDTO);
            fileInfoDTO.setConnectomeId(connectomeId);
            if (StringUtils.isNotBlank(fileInfo.getName())) {
                fileInfoDTO.setFileType(fileInfo.getFileType());
            }
            fileInfoDTOS.add(fileInfoDTO);
            if (fileInfo.getType().equals(Constants.UploadType.FILE.toString())) {
                fileInfoDTOWithTypeIsFile.add(fileInfoDTO);
            }
        }
        //        RestTemplate restTemplate = new RestTemplate();
        this.pushFileWithTypeIsFile(fileInfoDTOWithTypeIsFile);
        // Wrapper.FileInfoWrapper responseEntity = restTemplate.postForObject(URLDocConverter + DOC_CONVERT, fileInfoDTOS, Wrapper.FileInfoWrapper.class);
        this.uploadFileByAPI(fileInfoDTOS, URLDocConverter);
    }

    // just push file with type = file to local
    private void pushFileWithTypeIsFile(List<FileInfoDTO> fileInfos) {
        this.uploadFileByAPI(fileInfos, URLUploadFile);
    }

    private void uploadFileByAPI(List<FileInfoDTO> fileInfoDTOS, String url) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + DOC_CONVERT);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            builder.encode(StandardCharsets.UTF_8);
            HttpEntity<?> entity = new HttpEntity<>(fileInfoDTOS, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {}
            );
            log.info("Response after push file: Status {}, body: {}", responseEntity.getStatusCode(), responseEntity.getBody());
        } catch (Exception e) {
            log.error("Error when upload file, url: {}", url);
        }
    }

    @Async
    public void callKafkaPublishApi(List<FileInfo> fileInfos, String connectomeId) {
        List<FileInfoDTO> fileInfoDTOS = new ArrayList<>();
        for (FileInfo fileInfo : fileInfos) {
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            fileInfoDTO.setId(fileInfo.getId());
            fileInfoDTO.setConnectomeId(connectomeId);
            fileInfoDTO.setName(fileInfo.getName());
            fileInfoDTO.setSize(fileInfo.getSize());
            fileInfoDTO.setPath(fileInfo.getPath());
            fileInfoDTO.setMineType(fileInfo.getMineType());
            fileInfoDTO.setType(fileInfo.getType());
            fileInfoDTO.setCreatedDate(fileInfo.getCreatedDate());
            fileInfoDTO.setAuthor(fileInfo.getAuthor());
            fileInfoDTO.setOriginDate(fileInfo.getOriginDate());
            fileInfoDTO.setSearchType(fileInfo.getSearchType());
            fileInfoDTOS.add(fileInfoDTO);
        }

        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.postForObject(API + Constants.KAFKA_PUBLISH_FILE_INFO, fileInfoDTOS, FileInfoDTO[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
