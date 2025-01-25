package com.example.restcontrollertryings.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

@RestController
public class ExampleController {

    @PostMapping("/postWithString")
    public String doSomething(@RequestBody String x) throws JsonProcessingException {
        System.out.println("the val being printed is " + x);
        X x1 = new ObjectMapper().readValue(x,X.class);
        System.out.println("parsed into object properly ? ");
        System.out.println(x1.getCat());
        return x;
    }

    @GetMapping("/requestParamMultipart")
    public void getSoemthing(@RequestParam("test") String b) {
        System.out.println(b + " + is printed properly");
    }

    @PostMapping("/forFile")
    public void getFiley(@RequestParam("file") MultipartFile b) throws IOException {
        System.out.println("entering another method...");
        String r = b.getName();
        //System.out.println(r);
        byte[] bytes = r.getBytes();
        //System.out.println(bytes.length);

        String content = new String(b.getBytes());
        System.out.println("File contents:\n" + content);
    }

    @GetMapping("/sendFile")
    public void requestParams(@RequestParam("filepath") String filepath) throws IOException {
        System.out.println("entering initial method");
        try {
            File file = new File(filepath);

            // Check if the file exists
            if (!file.exists()) {
                System.out.println("haiz");
                return;
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + filepath);
            }

            // Prepare the multipart form data request
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            // Create headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Wrap the body and headers into a HttpEntity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send the POST request to /uploadFile endpoint
            String uploadUrl = "http://localhost:8080" + "/forFile";
            RestTemplate restTemplate = new RestTemplate();
            System.out.println(" bye!");
            ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

            // Return the response


        } catch (Exception e) {
            System.out.println("something went wrong");
            System.out.println(e.getMessage());
        }

        System.out.println("ok byeb ye");
    }
}


class X {
    private String cat;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
