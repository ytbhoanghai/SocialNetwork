package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Exception.FileNotFoundException;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.IOException;

public interface FileService {

    String FORMAT_IMAGE_DEFAULT = "jpg";

    String API_GENERATE_IMAGE_BY_NAME = "https://ui-avatars.com/api/?name=%s&background=50b5ff&color=fff&size=350&length=1";
    String BACKGROUND_FOR_EVERYONE = "dynamic/default/profile-bg1.jpg";

    String PATH_SAVE_AVATAR_DEFAULT = "dynamic/images/avatar/{fileName}";
    String PATH_SAVE_BACKGROUND_DEFAULT = "dynamic/images/background/{fileName}";
    String PATH_SAVE_WORK_PLACE_DEFAULT = "dynamic/images/work-place/{fileName}";
    String PATH_SAVE_COLLEGE_DEFAULT = "dynamic/images/college/{fileName}";
    String PATH_SAVE_PLACE_LIVED_DEFAULT = "dynamic/images/place-lived/{fileName}";
    String PATH_SAVE_FEELING_DEFAULT = "dynamic/images/feeling/{fileName}";
    String PATH_SAVE_IMAGE_TEMP = "dynamic/images/temp/{fileName}";
    String PATH_SAVE_PHOTO = "dynamic/images/photo/{fileName}";

    String PATTERN_URI_DOWNLOAD_AVATAR = "/file/avatar/{idUser}";
    String PATTERN_URI_DOWNLOAD_BACKGROUND = "/file/background/{idUser}";

    String PREFIX_AVATAR_WORK_PLACE = "workPlace";
    String PREFIX_AVATAR_COLLEGE = "college";
    String PREFIX_AVATAR_PLACE_LIVED = "placeLived";
    String PREFIX_AVATAR_FEELING = "feeling";
    String PREFIX_PHOTO = "photo";

    default String getFileNameWorkPlace(String id) { return PREFIX_AVATAR_WORK_PLACE + "-" + id + "." + FORMAT_IMAGE_DEFAULT; }

    default String getFileNameCollege(String id) { return PREFIX_AVATAR_COLLEGE + "-" + id + "." + FORMAT_IMAGE_DEFAULT; }

    default String getFileNamePlaceLived(String id) { return PREFIX_AVATAR_PLACE_LIVED + "-" + id + "." + FORMAT_IMAGE_DEFAULT; }

    default String getFileNameFeeling(String id) { return PREFIX_AVATAR_FEELING + "-" + id + ".png"; }

    default String getFileNamePhoto(String id) { return PREFIX_PHOTO + "-" + id + "." + FORMAT_IMAGE_DEFAULT; }

    default String getFileNameAvatar(String id) { return id + "." + FileService.FORMAT_IMAGE_DEFAULT; }

    default String getFileNameBackground(String id) { return id + "." + FileService.FORMAT_IMAGE_DEFAULT; }

    byte[] getPhoto(String id) throws IOException;

    byte[] getAvatar(String id) throws FileNotFoundException, IOException;

    byte[] getBackground(String id) throws FileNotFoundException, IOException;

    byte[] getWorkPlace(String id) throws IOException;

    byte[] getCollege(String id) throws IOException;

    byte[] getPlaceLived(String id) throws IOException;

    byte[] getFeeling(String id) throws IOException;

    File getFileInResource(String arg) throws IOException;

    String generatePhotoDefault();

    @Async("generateFileTaskExecutor")
    void generateAvatarByName(String name, String idUser) throws IOException;

    @Async("generateFileTaskExecutor")
    void generateAvatarByUrl(String url, String idUser) throws IOException;

    @Async("generateFileTaskExecutor")
    void generateBackground(String idUser) throws IOException;
}
