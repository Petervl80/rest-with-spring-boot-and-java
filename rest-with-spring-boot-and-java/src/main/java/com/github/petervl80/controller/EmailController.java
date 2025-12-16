package com.github.petervl80.controller;

import com.github.petervl80.controller.docs.EmailControllerDocs;
import com.github.petervl80.data.dto.request.EmailRequestDTO;
import com.github.petervl80.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        service.sendSimpleEmail(emailRequestDTO);
        return ResponseEntity.ok("Email sent successfully!");
    }

    @PostMapping(value = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequestJson,
            @RequestParam("attachment") MultipartFile multipartFile) {
        service.sendSimpleEmailWithAttachment(emailRequestJson, multipartFile);
        return ResponseEntity.ok("Email with attachment sent successfully!");
    }
}
