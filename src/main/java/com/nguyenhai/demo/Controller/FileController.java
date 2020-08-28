package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/file")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "avatar/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable String id) throws IOException {
        byte[] data = fileService.getAvatar(id);
        String fileName = fileService.getFileNameAvatar(id);
        HttpHeaders httpHeaders = getHttpHeadersForImageType(fileName, data.length, false);
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }

    @GetMapping(value = "background/{id}")
    public ResponseEntity<?> getBackground(@PathVariable String id) throws IOException {
        byte[] data = fileService.getBackground(id);
        String fileName = fileService.getFileNameBackground(id);
        HttpHeaders httpHeaders = getHttpHeadersForImageType(fileName, data.length, false);
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }

    @GetMapping(value = "work-place/{id}")
    public ResponseEntity<?> getImageWorkPlace(@PathVariable String id) throws IOException {
        byte[] data = fileService.getWorkPlace(id);
        String fileName = fileService.getFileNameWorkPlace(id);
        HttpHeaders httpHeaders = getHttpHeadersForImageType(fileName, data.length, true);
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }

    @GetMapping(value = "college/{id}")
    public ResponseEntity<?> getImageCollege(@PathVariable String id) throws IOException {
        byte[] data = fileService.getCollege(id);
        String fileName = fileService.getFileNameCollege(id);
        HttpHeaders httpHeaders = getHttpHeadersForImageType(fileName, data.length, true);
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }

    @GetMapping(value = "place-lived/{id}")
    public ResponseEntity<?> getImagePlaceLived(@PathVariable String id) throws IOException {
        byte[] data = fileService.getPlaceLived(id);
        String fileName = fileService.getFileNamePlaceLived(id);
        HttpHeaders httpHeaders = getHttpHeadersForImageType(fileName, data.length, true);
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }

    @GetMapping(value = "feeling/{id}")
    public ResponseEntity<?> getImageFeeling(@PathVariable String id) throws IOException {
        byte[] data = fileService.getFeeling(id);
        String fileName = fileService.getFileNameFeeling(id);
        HttpHeaders httpHeaders = getHttpHeadersForImageType(fileName, data.length,true);
        // edit content type
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }

    @GetMapping(value = "photo/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable String id) throws IOException {
        byte[] data = fileService.getPhoto(id);
        String fileName = fileService.getFileNamePhoto(id);
        HttpHeaders httpHeaders = getHttpHeadersForImageType(fileName, data.length, true);
        return ResponseEntity.ok().headers(httpHeaders).body(data);
    }

    private HttpHeaders getHttpHeadersForImageType(String fileName, int contentLength, boolean cache) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.set("Content-disposition", "attachment; filename=" + fileName);
        httpHeaders.setContentLength(contentLength);
        if (cache) {
            httpHeaders.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
        }
        return httpHeaders;
    }
}
