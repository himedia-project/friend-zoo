package com.friendzoo.api.util.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.file.upload.path}")
    private String uploadPath;

    private final AwsS3Util s3Util;



//    @PostConstruct
//    public void init() {
//        File tempFolder = new File(uploadPath);
//
//        if (!tempFolder.exists()) {
//            tempFolder.mkdir();
//        }
//
//        uploadPath = tempFolder.getAbsolutePath();
//
//        log.info("-------------------------------------");
//        log.info(uploadPath);
//    }


    /**
     * 파일 s3 업로드
     *
     * @param files 업로드할 파일 리스트
     * @return 업로드된 파일명 리스트
     * @throws RuntimeException
     */
    public List<String> uploadS3Files(List<MultipartFile> files) {
        return s3Util.uploadFiles(files);
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
     * s3 파일 삭제
     * @param fileNames 삭제할 파일명 리스트
     */
    public void deleteS3Files(List<String> fileNames) {
        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }
        s3Util.deleteFiles(fileNames);
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
     * 이미지 URL을 MultipartFile로 변환 후 s3에 저장
     * @param imagePathList 이미지 URL 리스트
     * @return 저장된 파일명 리스트
     */

    public List<String> uploadImagePathS3Files(List<String> imagePathList) {
        // imagePathList -> MultipartFile 변환 -> saveFiles
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
        return this.uploadS3Files(multipartFiles);
    }

}
