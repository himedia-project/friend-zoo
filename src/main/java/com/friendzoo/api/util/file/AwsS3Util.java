package com.friendzoo.api.util.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AwsS3Util {

    @Value("${app.props.aws.s3.bucket-name}")
    private String bucket;

    @Value("${app.props.aws.s3.region}")
    private String region;

    private final AmazonS3 s3Client;

    /**
     * S3에 파일 업로드
     *
     * @param filePaths 파일 경로 리스트
     * @param delFlag   업로드 후 파일 삭제 여부
     */
    public void uploadFiles(List<Path> filePaths, boolean delFlag) {

        if (filePaths == null || filePaths.isEmpty()) {
            return;
        }

        for (Path filePath : filePaths) {
            //upload
            PutObjectRequest request = new PutObjectRequest(bucket, filePath.toFile().getName(), filePath.toFile());
            s3Client.putObject(request);

            if (delFlag) {
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    /**
     * S3에 있는 파일 가져오기
     * @param fileName 파일 이름
     * @return 파일 리소스
     * @throws IOException 파일이 없을 경우 예외 발생
     */
    public ResponseEntity<Resource> getFile(String fileName) throws IOException {
        // fileName = dbac534f-f3b6-4b33-9b83-e308e3c2c29d_e52319408af1ee349da788ec09ca6d92ff7bd70a3b99fa287c599037efee.jpg
        // https://mall-s3.s3.ap-northeast-2.amazonaws.com/dbac534f-f3b6-4b33-9b83-e308e3c2c29d_e52319408af1ee349da788ec09ca6d92ff7bd70a3b99fa287c599037efee.jpg
        // 로 전환!
        String urlStr = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
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
     *
     * @param filePaths 파일 경로 리스트
     */
    public void deleteFiles(List<Path> filePaths) {

        if (filePaths == null || filePaths.isEmpty()) {
            return;
        }

        for (Path filePath : filePaths) {
            s3Client.deleteObject(bucket, filePath.toFile().getName());
        }
    }

}
