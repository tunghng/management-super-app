package com.im.contact.controller;

import com.im.contact.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/noauth/contact")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class NoAuthContactController extends BaseController {

    @Autowired
    ContactService contactService;

    @GetMapping("{contactId}/avatar")
    @Operation(summary = "Get Contact Avatar by Id (getContactAvatarById)")
    public ResponseEntity<byte[]> getContactById(
            @PathVariable UUID contactId
    ) {
        String data = contactService.getAvatarById(contactId);
        if (data == null || data.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String[] parts = data.split(",");
        if (parts.length != 2) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String base64Image = parts[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
