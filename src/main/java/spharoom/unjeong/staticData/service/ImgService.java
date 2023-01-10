package spharoom.unjeong.staticData.service;

import org.springframework.core.io.InputStreamResource;
import spharoom.unjeong.global.enumeration.StaticImg;

import java.io.FileNotFoundException;

public interface ImgService {
    InputStreamResource findStoreImg(StaticImg imgName) throws FileNotFoundException;
}
