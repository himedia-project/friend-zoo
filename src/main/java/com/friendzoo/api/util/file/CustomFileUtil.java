package com.friendzoo.api.util.file;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.file.upload.path}")
    private String uploadPath;

    private final AwsS3Util s3Util;

    /**
     * s3 사용 - 파일 업로드, 삭제
     */

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);

        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("-------------------------------------");
        log.info(uploadPath);
    }


    /**
     * 파일 업로드
     *
     * @param files 업로드할 파일 리스트
     * @return 업로드된 파일명 리스트
     * @throws RuntimeException 파일 업로드 중 오류 발생 시 예외 발생
     */
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

        if (files == null || files.isEmpty()) {
            return null;
        }

        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile multipartFile : files) {

            String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
            log.info("CustomFileUtil save 식별된 파일네임 savedName: {}", savedName);
            Path savePath = Paths.get(uploadPath, savedName); // 저장할 파일 경로

            List<Path> uploadTargetPaths = new ArrayList<>();

            try {
                Files.copy(multipartFile.getInputStream(), savePath); // 파일 copy: 복사해서 저장, multipartFile.getInputStream() -> savePath 경로로 복사

                uploadTargetPaths.add(savePath);

                String contentType = multipartFile.getContentType();
                log.info("CustomFileUtil save contentType: {}", contentType);

                // 이미지 파일에 webp 형식이 포함되어 있어서 추가
                if (contentType != null && contentType.startsWith("image")) {

                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName); // 썸네일 파일명

                    Thumbnails.of(savePath.toFile())
                            .size(400, 400)
                            .toFile(thumbnailPath.toFile());

                    uploadTargetPaths.add(thumbnailPath);
                }

                uploadNames.add(savedName);

                // s3 upload
                s3Util.uploadFiles(uploadTargetPaths, false);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }//end for
        return uploadNames;
    }


    /**
     * 파일 가져오기
     * @param fileName 파일명
     * @return 파일 리소스
     */
    public ResponseEntity<Resource> getFile(String fileName) {
        try {
            return s3Util.getFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("getFile error: {}", e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 파일 삭제
     *
     * @param fileNames 삭제할 파일명 리스트
     */
    public void deleteFiles(List<String> fileNames) {

        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }

        List<Path> deleteTargetPaths = new ArrayList<>();

        fileNames.forEach(fileName -> {

            //썸네일이 있는지 확인하고 삭제
            String thumbnailFileName = "s_" + fileName;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);

                // s3 delete
                deleteTargetPaths.add(filePath);
                deleteTargetPaths.add(thumbnailPath);

                s3Util.deleteFiles(deleteTargetPaths);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }


    /**
     * 이미지 URL을 MultipartFile로 변환
     * @param imageUrl 이미지 URL
     * @return MultipartFile
     * @throws IOException
     */
    public MultipartFile convert(String imageUrl) throws IOException {

        if (imageUrl == null) {
            return null;
        }

        // url http: 로 시작하면 -> https: 로 변경
        if (imageUrl.startsWith("http:")) {
            imageUrl = imageUrl.replace("http:", "https:");
        }

        log.info("convert imageUrl: {}", imageUrl);

        // Check if the file exists at the given URL
        URLConnection connection = null;
        URL url = null;
        InputStream inputStream = null;

        try {
            url = new URL(imageUrl);
            connection = url.openConnection();
            connection.connect();

            inputStream = connection.getInputStream();
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);

            // Create a MultipartFile object from the byte array
            String finalImageUrl = imageUrl;
            MultipartFile multipartFile = new MultipartFile() {
                @Override
                public String getName() {
                    return "file";
                }

                @Override
                public String getOriginalFilename() {
                    // Extract file name from the URL
                    // https://product-image.wconcept.co.kr/productimg/image/img1/36/303919636_OQ83444.jpg?RS=412
                    // https://thumbnail6.coupangcdn.com/thumbnails/remote/292x292q65ex/image/vendor_inventory/0f9e/46ab581f1bc3e0132e88f32452433bccadc52994cf468a49014498ddbedb.jpg
                    String[] segments = finalImageUrl.split("/");
                    // 뒤에 ?RS=412 같은 쿼리스트링 제거

                    return segments[segments.length - 1].replaceAll("\\?.*", "");
                }

                @Override
                public String getContentType() {
                    // Guess content type based on file extension
                    return URLConnection.guessContentTypeFromName(getOriginalFilename());
                }

                @Override
                public boolean isEmpty() {
                    return bytes.length == 0;
                }

                @Override
                public long getSize() {
                    return bytes.length;
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return bytes;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(bytes);
                }

                @Override
                public void transferTo(File dest) throws IOException, IllegalStateException {
                    new FileOutputStream(dest).write(bytes);
                }
            };

            return multipartFile;

        } catch (FileNotFoundException e) {
            // 실제 FileNotFoundException 일어나면, just return null
            log.error("File not found at the given URL: {}", imageUrl);
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from URL: " + imageUrl);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            // Close the connection
            if (connection != null) {
                connection.getInputStream().close();
            }
        }


    }

    /**
     * 이미지 URL을 MultipartFile로 변환 후 저장
     * @param imagePathList 이미지 URL 리스트
     * @return 저장된 파일명 리스트
     */

    public List<String> saveImagePathFiles(List<String> imagePathList) {
        // imagePAthList -> MultipartFile 변환 -> saveFiles
        List<MultipartFile> multipartFiles = new ArrayList<>();

        for (String imagePath : imagePathList) {
            try {
                MultipartFile multipartFile = convert(imagePath);
                if (multipartFile != null) {
                    multipartFiles.add(multipartFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.saveFiles(multipartFiles);
    }
}
