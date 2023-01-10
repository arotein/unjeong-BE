package spharoom.unjeong.staticData.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharoom.unjeong.global.enumeration.StaticImg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImgServiceImpl implements ImgService {
    @Value("${spring.file.img-base-path}")
    private String imgBasePath;

    @Override
    public InputStreamResource findStoreImg(StaticImg imgName) throws FileNotFoundException {
        String img = String.format("%s/%s", imgBasePath, imgName.getImgName());
        return new InputStreamResource(new FileInputStream(img));
    }
}
