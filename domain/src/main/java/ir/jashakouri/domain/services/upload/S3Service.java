package ir.jashakouri.domain.services.upload;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import ir.jashakouri.data.enums.BucketWallet;
import ir.jashakouri.domain.exception.s3.S3BaseException;
import ir.jashakouri.domain.exception.s3.S3EmptyFileException;
import ir.jashakouri.domain.exception.s3.S3MimeTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
@Slf4j(topic = "LOG_DbUploaderJob")
public class S3Service {

    private final AmazonS3 s3;

    public S3Service(AmazonS3 amazonS3) {
        this.s3 = amazonS3;
    }

    private PutObjectResult _upload(String path, String filename,
                                    Optional<Map<String, String>> optionalMetadata,
                                    MultipartFile multipartFile) throws IOException {

        var metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) map.forEach(metadata::addUserMetadata);
        });

        metadata.setContentLength(multipartFile.getBytes().length);
        return s3.putObject(path, filename, multipartFile.getInputStream(), metadata);
    }

    private String _getUrl(String filePath) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);
        return s3.generatePresignedUrl(BucketWallet.WALLET.getName(), filePath, calendar.getTime(), HttpMethod.GET).toString();
    }

    public Optional<String> upload(UUID userId, MultipartFile file) throws S3BaseException, IOException {

        if (file.isEmpty()) throw new S3EmptyFileException();

        if (!Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF).toString().contains(file.getContentType()))
            throw new S3MimeTypeException();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        var path = String.format("%s/%s", BucketWallet.WALLET.getName(), userId);
        var fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        var result = _upload(path, fileName, Optional.of(metadata), file);
        return result != null ? Optional.ofNullable(fileName) : Optional.empty();
    }

    public String getUrl(String filePath) {
        return _getUrl(filePath);
    }
}
