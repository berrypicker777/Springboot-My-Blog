package blue.berry.myblog.core.exception.csr;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import blue.berry.myblog.dto.ResponseDTO;

// 인증 안됨
@Getter
public class ExceptionApi401 extends RuntimeException {
    public ExceptionApi401(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        ResponseDTO<String> responseDto = new ResponseDTO<>();
        responseDto.fail(HttpStatus.UNAUTHORIZED, "unAuthorized", getMessage());
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.UNAUTHORIZED;
    }
}