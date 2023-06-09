package blue.berry.myblog.core.advice;

import blue.berry.myblog.core.exception.csr.ExceptionApi400;
import blue.berry.myblog.core.exception.ssr.Exception400;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Aspect
@Component
public class MyValidAdvice {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Pointcut("execution(* blue.berry.myblog.controller.api.*Controller.*(..))")
    public void api() {
    }

    @Pointcut("execution(* blue.berry.myblog.controller.*Controller.*(..))")
    public void view() {
    }

    @Before("postMapping() && view()")
    public void validationAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;
                if (errors.hasErrors()) {
                    throw new Exception400(
                            errors.getFieldErrors().get(0).getField(),
                            errors.getFieldErrors().get(0).getDefaultMessage()
                    );
                }
            }
        }
    }

    @Before("postMapping() && api()")
    public void validationApiAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;
                if (errors.hasErrors()) {
                    throw new ExceptionApi400(
                            errors.getFieldErrors().get(0).getField(),
                            errors.getFieldErrors().get(0).getDefaultMessage()
                    );
                }
            }
        }
    }
}