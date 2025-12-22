package com.ikram.gestiondestock.controller;

import com.ikram.gestiondestock.services.strategy.StrategyPhotoContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.ikram.gestiondestock.utils.constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/photos")
public class PhotoController {

    private final StrategyPhotoContext strategyPhotoContext;

    public PhotoController(StrategyPhotoContext strategyPhotoContext) {
        this.strategyPhotoContext = strategyPhotoContext;
    }

    @PostMapping("/{type}/{id}")
    public ResponseEntity<?> uploadPhoto(
            @PathVariable String type,
            @PathVariable Integer id,
            @RequestParam("photo") MultipartFile photo
    ) throws IOException {

        Object response = strategyPhotoContext.savePhoto(
                type + "PhotoLocalStrategy",
                id,
                photo.getInputStream(),
                photo.getOriginalFilename()
        );

        return ResponseEntity.ok(response);
    }
}
