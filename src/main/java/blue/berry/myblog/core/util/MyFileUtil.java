package blue.berry.myblog.core.util;

import blue.berry.myblog.core.exception.ssr.Exception500;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class MyFileUtil {

    public static String write(String uploadFolder, MultipartFile file) {
        // 롤링 기법(사진명 -> 시간_random값_파일명.png(시간만 넣으면 재수없으면 동시에 들어올때 터지므로, 물론 이래도 정말 희박한 확률로 터질 수 있는데 uuid 사용하는게 더 간편))
        // uuid_파일명(uuid는 수학적으로 동일한 값이 나올 확률이 수백억분의 1정도라 로또 1등보다도 훨씬 희박)
        UUID uuid = UUID.randomUUID();
        String originalFilename = file.getOriginalFilename();
        String uuidFilename = uuid + "_" + originalFilename;
        try {
            // 파일 사이즈 줄이기(aws s3에 던져주면 알아서 줄이기까지 수행해줌)
            Path filePath = Paths.get(uploadFolder + uuidFilename);
            Files.write(filePath, file.getBytes()); // 경로 정하고 파일 저장
        } catch (Exception e) {
            throw new Exception500("파일 업로드 실패 : " + e.getMessage());
        }
        return uuidFilename;
    }
}
