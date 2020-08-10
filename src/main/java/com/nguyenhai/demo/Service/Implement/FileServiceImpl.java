package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Service.FileService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;

// SERVICE DIRECT OF CONTROLLER
@Service(value = "fileService")
public class FileServiceImpl implements FileService {

    private OkHttpClient okHttpClient;

    @Autowired
    public FileServiceImpl(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public byte[] getPhoto(String id) {
        String fileName = getFileNamePhoto(id);
        Path path = Paths.get(PATH_SAVE_PHOTO.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(path.toFile());
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getAvatar(String id) throws com.nguyenhai.demo.Exception.FileNotFoundException {
        String fileName = getFileNameAvatar(id);
        Path path = Paths.get(PATH_SAVE_AVATAR_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(path.toFile());
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getBackground(String id) throws com.nguyenhai.demo.Exception.FileNotFoundException {
        String fileName = getFileNameBackground(id);
        Path path = Paths.get(PATH_SAVE_BACKGROUND_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(path.toFile());
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getWorkPlace(String id) {
        String fileName = getFileNameWorkPlace(id);
        Path path = Paths.get(PATH_SAVE_WORK_PLACE_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(path.toFile());
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getCollege(String id) {
        String fileName = getFileNameCollege(id);
        Path path = Paths.get(PATH_SAVE_COLLEGE_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(path.toFile());
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getPlaceLived(String id) {
        String fileName = getFileNamePlaceLived(id);
        Path path = Paths.get(PATH_SAVE_PLACE_LIVED_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(path.toFile());
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getFeeling(String id) {
        String fileName = getFileNameFeeling(id);
        Path path = Paths.get(PATH_SAVE_FEELING_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(path.toFile());
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public void generateAvatarByName(String name, String idUser) throws IOException {
        String url = getApiGenerateImageByName(name);
        generateAvatarByUrl(url, idUser);
    }

    @Override
    public void generateAvatarByUrl(String url, String idUser) throws IOException {
        String fileName = idUser.concat(".").concat(FORMAT_IMAGE_DEFAULT);
        Request request = new Request.Builder()
                .url(url).build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            Path path = Paths.get(PATH_SAVE_AVATAR_DEFAULT.replace("{fileName}", fileName));
            Thumbnails.of(Objects.requireNonNull(response.body()).byteStream())
                    .outputFormat("jpg")
                    .antialiasing(Antialiasing.ON)
                    .scale(1)
                    .outputQuality(1f)
                    .toFile(path.toString());
        }
    }

    @Override
    public void generateBackground(String idUser) throws IOException {
        String fileName = idUser.concat(".").concat(FORMAT_IMAGE_DEFAULT);
        Path origin = Paths.get(BACKGROUND_FOR_EVERYONE);

        Path path = Paths.get(PATH_SAVE_BACKGROUND_DEFAULT.replace("{fileName}", fileName));
        InputStream inputStream = FileUtils.openInputStream(origin.toFile());
        Thumbnails.of(inputStream)
                .outputFormat("jpg")
                .antialiasing(Antialiasing.ON)
                .scale(1)
                .outputQuality(1f)
                .toFile(path.toString());
    }

    private static String getApiGenerateImageByName(String name) {
        Assert.hasLength(name, "name must not be blank");
        return String.format(API_GENERATE_IMAGE_BY_NAME, name);
    }
}
