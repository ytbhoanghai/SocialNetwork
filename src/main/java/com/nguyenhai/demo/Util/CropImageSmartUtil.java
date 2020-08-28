package com.nguyenhai.demo.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.validation.constraints.Size;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Component
public class CropImageSmartUtil {

    @Getter
    @AllArgsConstructor
    public static class Resolution {
        private Integer x;
        private Integer y;

        @Override
        public String toString() {
            return (x == -1 && y == -1) ? "" : x + "x" + y;
        }
    }

    @Data
    @AllArgsConstructor
    public static class ResultCropImage {
        private Resolution resolution;
        private BufferedImage bufferedImage;
    }

    @Data private static class Croppings {
        private int target_height;
        private int target_width;
        private int x1;
        private int x2;
        private int y1;
        private int y2;
    }

    @Data private static class Result {
        private List<Croppings> croppings;
    }

    @Data private static class Status {
        private String text;
        private String type;
    }

    @Data private static class Root {
        private Result result;
        private Status status;
    }


    private static final String API_CROP_IMAGGA = "https://api.imagga.com/v2/croppings";
    private static final String AUTHORIZATION = "Basic YWNjXzAwMDljMmNjMjY3ZWZjMjo5YzgzODUyMjVkMTI0ZGQ3NDVlNjI4NGU0MTk2ZWZiNA==";

    private OkHttpClient okHttpClient;

    @Autowired
    public CropImageSmartUtil(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public List<ResultCropImage> crop(String base64, @Size(min = 1) List<Resolution> resolutions) throws IOException {
        List<ResultCropImage> resultCropImages = new ArrayList<>();

        String strResolution = resolutionsToString(resolutions);
        Root root = request(base64, strResolution);

        if (root.status.type.equals("success")) {
            root.result.croppings.forEach(cropping -> {
                InputStream is = new ByteArrayInputStream(strBase64ToBytes(base64));
                try {
                    BufferedImage bi = processing(cropping, is);
                    Resolution resolution = new Resolution(cropping.target_width, cropping.target_height);
                    resultCropImages.add(new ResultCropImage(resolution, bi));
                } catch (IOException e) { e.printStackTrace(); }
            });

        }

        return resultCropImages;
    }



    private Root request(String base64, String resolution) throws IOException {
        RequestBody requestBody = getRequestBody(base64, resolution);
        Request request = getRequest(requestBody);

        Response response = okHttpClient.newCall(request).execute();
        return toRoot(Objects.requireNonNull(response.body()).bytes());
    }

    private RequestBody getRequestBody(String base64, String resolution) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_base64", base64)
                .addFormDataPart("resolution", resolution)
                .build();
    }

    private Request getRequest(RequestBody requestBody) {
        return new Request.Builder()
                .url(API_CROP_IMAGGA)
                .addHeader("Authorization", AUTHORIZATION)
                .post(requestBody)
                .build();
    }

    private Root toRoot(byte[] bytes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                Objects.requireNonNull(bytes),
                new TypeReference<Root>() {});
    }

    private BufferedImage processing(Croppings croppings, InputStream inputStream) throws IOException {
        BufferedImage bi = ImageIO.read(inputStream);

        int     x1 = croppings.x1,
                y1 = croppings.y1,
                x2 = croppings.x2,
                y2 = croppings.y2;

        return bi.getSubimage(x1, y1, (x2 - x1) , (y2 - y1));
    }

    private String resolutionsToString(List<Resolution> resolutions) {
        StringBuilder result = new StringBuilder(
                resolutions.get(0).toString());

        for (int i = 1; i < resolutions.size(); i++) {
            result.append(",").append(resolutions.get(i));
        }

        return result.toString();
    }

    private byte[] strBase64ToBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}


