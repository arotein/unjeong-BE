package spharoom.unjeong.staticData.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spharoom.unjeong.global.enumeration.StaticImg;
import spharoom.unjeong.staticData.service.ImgService;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("${global.api.static-img-base-path}")
@RequiredArgsConstructor
public class ImgController {
    private final ImgService imgService;

    @GetMapping("/{name}")
    public ResponseEntity storeImg(@PathVariable("name") String imgName) throws FileNotFoundException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imgService.findStoreImg(StaticImg.findByString(imgName)));
    }
}
