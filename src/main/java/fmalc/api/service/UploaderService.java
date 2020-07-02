package fmalc.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploaderService {
    String upload(MultipartFile file) throws IOException;
}
