package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.FileInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileStorageRepository extends JpaRepository<FileInfo, Long> {
    //    @Query("SELECT u FROM FileInfo u WHERE u.user.id =?1 AND u.type =?2")
    //    public List<FileInfo> getFileByUserId(String id, String type, Pageable pageable);

    public List<FileInfo> findFileInfoByUser_IdAndType(String id, String type, Pageable pageable);

    Page<FileInfo> findFileInfoByUser_IdAndType(Pageable pageable, String userId, String type);

    Optional<FileInfo> findByIdAndType(Long id, String type);

    //    @Query("SELECT u FROM FileInfo u WHERE u.user.id =?1 AND u.type=?2")
    //    public List<FileInfo> getAllFile(String id, String type);

    @Query("SELECT fi.path FROM FileInfo fi WHERE fi.user.id = ?1 AND fi.type = ?2")
    List<String> findByUser_IdAndType(String id, String type);

    List<FileInfo> findFileInfoByUser_IdAndType(String id, String type);

    @Query(value = "SELECT Count(*) FROM connectome_file_upload_detail f WHERE f.connectome_id =?1", nativeQuery = true)
    long getCountFileUploadByConnectomeId(String connectomeId);

    void deleteAllByUser_IdAndType(String id, String type);

    long countFileInfoByUser_IdAndType(String id, String type);
}
