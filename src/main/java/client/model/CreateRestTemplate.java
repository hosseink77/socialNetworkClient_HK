package client.model;

import client.model.entity.FriendEntity;
import client.model.entity.PostEntity;
import client.model.entity.UserEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateRestTemplate {

    static RestTemplate restTemplate;
    private static final String baseUrl = "http://localhost:8080/";

    public static RestTemplate buildObject() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();

//            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//            messageConverters.add(converter);
//            restTemplate.setMessageConverters(messageConverters);

//            return restTemplate;
        }
        return restTemplate;

    }

    public static <T> T buildGet(String url, Class<T> classType, String userNme) throws RestClientException {
        try {
            ResponseEntity<T> response = buildObject().getForEntity(baseUrl + url + "?id=" + userNme, classType);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404 not found");
            return null;
        } catch (ResourceAccessException ex) {
            System.out.println("ResourceAccessException : No Connection");
            return null;
        }

    }

    public static <T> boolean buildPost(String url, Class<T> classType, T body) throws RestClientException {
        boolean result;
        try {

            buildObject().postForEntity(baseUrl + url, body, classType);
            result = true;

        } catch (HttpClientErrorException.NotFound ex) {
            result = false;
            System.out.println("404 not found");
        } catch (ResourceAccessException ex) {
            result = false;
            System.out.println("ResourceAccessException : No Connection");
        }
        return result;
    }

    public static <T> boolean buildPut(String url, T body) throws RestClientException {
        boolean result;
        try {

            buildObject().put(baseUrl + url, body);
            result = true;

        } catch (HttpClientErrorException.NotFound ex) {
            result = false;
            System.out.println("404 not found");
        } catch (ResourceAccessException ex) {
            result = false;
            System.out.println("ResourceAccessException : No Connection");
        }
        return result;

    }

    public static <T> boolean buildDelete(String url, T body) throws RestClientException {
        boolean result;
        try {

            buildObject().delete(baseUrl + url, body);
            result = true;

        } catch (HttpClientErrorException.NotFound ex) {
            result = false;
            System.out.println("404 not found");
        } catch (ResourceAccessException ex) {
            result = false;
            System.out.println("ResourceAccessException : No Connection");
        }
        return result;

    }

    public static boolean isConnected() {
        boolean connection;
        try {

            ResponseEntity<Boolean> response = buildObject().getForEntity(baseUrl + "isConnect", boolean.class);
            connection = response.getBody();

        } catch (HttpClientErrorException.NotFound ex) {
            connection = false;
            System.out.println("404 not found");
        } catch (ResourceAccessException ex) {
            connection = false;
            System.out.println("ResourceAccessException : No Connection");
        }
        return connection;
    }

    public static boolean isExistUserName(String id) {
        boolean result;
        try {

            ResponseEntity<Boolean> response = buildObject().getForEntity(baseUrl + "isExist"+"?id=" + id, boolean.class);
            result = response.getBody();

        } catch (HttpClientErrorException.NotFound ex) {
            result = false;
            System.out.println("404 not found");
        } catch (ResourceAccessException ex) {
            result = false;
            System.out.println("ResourceAccessException : No Connection");
        }
        return result;
    }

    public static boolean isExistPath(String url) {
        boolean result;
        try {

            ResponseEntity<Boolean> response = buildObject().getForEntity(baseUrl+url , boolean.class);
            result = response.getBody();

        } catch (HttpClientErrorException.NotFound ex) {
            result = false;
            System.out.println("404 not found");
        } catch (ResourceAccessException ex) {
            result = false;
            System.out.println("ResourceAccessException : No Connection");
        }
        return result;
    }

    public static <T> T buildGetPath(String url, Class<T> classType, String userNme) throws RestClientException {
        try {
            ResponseEntity<T> response = buildObject().getForEntity(baseUrl + url , classType);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404 not found");
            return null;
        } catch (ResourceAccessException ex) {
            System.out.println("ResourceAccessException : No Connection");
            return null;
        }

    }

    public static List<PostEntity> buildGetListPost(String url) throws RestClientException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseEntity<JsonNode> response = buildObject().getForEntity(baseUrl + url , JsonNode.class);
            JsonNode body =  response.getBody();

            List<PostEntity> list = mapper.convertValue(
                    body,
                    new TypeReference<List<PostEntity>>(){}
            );

            return list;
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404 not found");
            return null;
        } catch (ResourceAccessException ex) {
            System.out.println("ResourceAccessException : No Connection");
            return null;
        }

    }

    public static List<UserEntity> buildGetListFriend(String url) throws RestClientException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseEntity<JsonNode> response = buildObject().getForEntity(baseUrl + url , JsonNode.class);
            JsonNode body =  response.getBody();

            List<UserEntity> list = mapper.convertValue(
                    body,
                    new TypeReference<List<UserEntity>>(){}
            );

            return list;
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404 not found");
            return null;
        } catch (ResourceAccessException ex) {
            System.out.println("ResourceAccessException : No Connection");
            return null;
        }

    }

    public static <T> boolean buildDeleteByPath(String url) throws RestClientException {
        boolean result;
        try {

            buildObject().delete(baseUrl + url);
            result = true;

        } catch (HttpClientErrorException.NotFound ex) {
            result = false;
            System.out.println("404 not found");
        } catch (ResourceAccessException ex) {
            result = false;
            System.out.println("ResourceAccessException : No Connection");
        }
        return result;

    }

}
