package com.friendzoo.api.util.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Util {

    @Value("${app.props.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${app.props.aws.s3.region}")
    private String region;

    private final AmazonS3 s3Client;

    /**
     * S3에 파일 업로드
     * @param files 파일 리스트
     * @return 업로드된 파일 URL 리스트
     */

    public List<String> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadFile)
                .toList();
    }

    /**
     * S3에 파일 업로드
     *
     * @param file 파일
     * @return 업로드된 파일 URL
     */
    public String uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String thumbnailFileName = "s_" + UUID.randomUUID().toString() + "-" + originalFilename;
        Path thumbnailPath = null;
        try {
            thumbnailPath = Paths.get(thumbnailFileName);
            // 썸네일 생성
            Thumbnails.of(file.getInputStream())
                    .size(400, 400)
                    .toFile(thumbnailPath.toFile());

            // S3에 썸네일 업로드
            s3Client.putObject(new PutObjectRequest(bucketName, thumbnailPath.toFile().getName(), thumbnailPath.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            // 썸네일 로컬 파일 삭제
            if (thumbnailPath != null && Files.exists(thumbnailPath)) {
                log.info("local thumbnailPath exist! {}", thumbnailPath);
                try {
                    Files.delete(thumbnailPath);

                } catch (IOException e) {
                    // 예외 발생 시 로그 남기기
                    log.error("Failed to delete local thumbnail file: {}", e.getMessage());
                }
            }
        }
        return thumbnailFileName;
    }


    /**
     * S3에 있는 파일 가져오기
     *
     * @param fileName 파일 이름
     * @return 파일 리소스
     * @throws IOException 파일이 없을 경우 예외 발생
     */
    public ResponseEntity<Resource> getFile(String fileName) throws IOException {
        // fileName = dbac534f-f3b6-4b33-9b83-e308e3c2c29d_e52319408af1ee349da788ec09ca6d92ff7bd70a3b99fa287c599037efee.jpg
        // https://mall-s3.s3.ap-northeast-2.amazonaws.com/dbac534f-f3b6-4b33-9b83-e308e3c2c29d_e52319408af1ee349da788ec09ca6d92ff7bd70a3b99fa287c599037efee.jpg
        // 로 전환!
        String urlStr = s3Client.getUrl(bucketName, fileName).toString();
        Resource resource;
        HttpHeaders headers = new HttpHeaders();
        try {
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            resource = new InputStreamResource(inputStream);

            // MIME 타입 설정
            String mimeType = urlConnection.getContentType();
            if (mimeType == null) {
                Path path = Paths.get(fileName);
                mimeType = Files.probeContentType(path);
            }
            headers.add("Content-Type", mimeType);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);

    }


    /**
     * S3에 파일 삭제
     * @param fileNames 파일 이름 리스트
     */
    public void deleteFiles(List<String> fileNames) {

        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }

        for (String fileName : fileNames) {
            s3Client.deleteObject(bucketName, fileName);
        }
    }

    /**
     * S3에 있는 파일 URL 가져오기
     * @param fileName 파일 이름
     * @return 파일 URL
     */
    public String getUrl(String fileName) {
        return s3Client.getUrl(bucketName, fileName).toString();
    }
}
