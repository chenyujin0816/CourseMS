package cn.edu.bit.utils;

import feign.Response;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

public class ConvertUtil {
    @SneakyThrows
    public static void feignResp2ServletResp(Response feignResponse, HttpServletResponse response) {
        Response.Body body = feignResponse.body();

        try (InputStream inputStream = body.asInputStream(); OutputStream outputStream = response.getOutputStream()) {
            response.setHeader("Authorization", feignResponse.headers().get("Authorization").stream().findFirst().get());
            response.setContentType(feignResponse.headers().get(HttpHeaders.CONTENT_TYPE).stream().findFirst().get());
            IOUtils.copy(inputStream, outputStream);

        }
    }

}
