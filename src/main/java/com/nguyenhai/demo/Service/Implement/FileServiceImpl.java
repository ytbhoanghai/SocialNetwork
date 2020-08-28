package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Service.FileService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

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
        File file = getFileInResource(PATH_SAVE_PHOTO.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getAvatar(String id) throws com.nguyenhai.demo.Exception.FileNotFoundException, IOException {
        String fileName = getFileNameAvatar(id);
        File file = getFileInResource(PATH_SAVE_AVATAR_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getBackground(String id) throws com.nguyenhai.demo.Exception.FileNotFoundException {
        String fileName = getFileNameBackground(id);
        File file = getFileInResource(PATH_SAVE_BACKGROUND_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getWorkPlace(String id) {
        String fileName = getFileNameWorkPlace(id);
        File file = getFileInResource(PATH_SAVE_WORK_PLACE_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getCollege(String id) throws IOException {
        String fileName = getFileNameCollege(id);
        File file = getFileInResource(PATH_SAVE_COLLEGE_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getPlaceLived(String id) {
        String fileName = getFileNamePlaceLived(id);
        File file = getFileInResource(PATH_SAVE_PLACE_LIVED_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) { throw new com.nguyenhai.demo.Exception.FileNotFoundException(fileName); }
    }

    @Override
    public byte[] getFeeling(String id) throws IOException {
        String fileName = getFileNameFeeling(id);
        File file = getFileInResource(PATH_SAVE_FEELING_DEFAULT.replace("{fileName}", fileName));
        try {
            return FileUtils.readFileToByteArray(file);
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
            File file = getFileInResource(PATH_SAVE_AVATAR_DEFAULT.replace("{fileName}", fileName));
            if (file.createNewFile()) {
                Thumbnails.of(Objects.requireNonNull(response.body()).byteStream())
                        .outputFormat("jpg")
                        .antialiasing(Antialiasing.ON)
                        .scale(1)
                        .outputQuality(1f)
                        .toFile(file);
            }
        }
    }

    @Override
    public void generateBackground(String idUser) throws IOException {
        String fileName = idUser.concat(".").concat(FORMAT_IMAGE_DEFAULT);
        File backgroundDefault = getFileInResource(BACKGROUND_FOR_EVERYONE);

        File file = getFileInResource(PATH_SAVE_BACKGROUND_DEFAULT.replace("{fileName}", fileName));
        if (file.createNewFile()) {
            InputStream inputStream = FileUtils.openInputStream(backgroundDefault);
            Thumbnails.of(inputStream)
                    .outputFormat("jpg")
                    .antialiasing(Antialiasing.ON)
                    .scale(1)
                    .outputQuality(1f)
                    .toFile(file);
        }
    }

    @Override
    public File getFileInResource(String arg) {
        File file = new File("F:/", arg);
        file.getParentFile().mkdirs();
        return file;
    }

    @Override
    public String generatePhotoDefault() {
        String id = UUID.randomUUID().toString();
        for (String s : Arrays.asList(
                "dynamic/default/def.jpg",
                "dynamic/default/def-800x533.jpg",
                "dynamic/default/def-200x200.jpg")) {
            File f = getFileInResource(s);
            String name = s.substring(s.lastIndexOf("/") + 1).replace("def", id);
            File f1 = getFileInResource("dynamic/images/photo/photo-" + name);
            try {
                if (f1.createNewFile()) {
                    FileUtils.copyFile(f, f1);
                }
            } catch (IOException e) {
                return s.replace("dynamic/default/", "dynamic/images/photos/");
            }
        }
        return "/file/photo/" + id;
    }
    private static String getApiGenerateImageByName(String name) {
        Assert.hasLength(name, "name must not be blank");
        return String.format(API_GENERATE_IMAGE_BY_NAME, name);
    }
}
