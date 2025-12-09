package com.eorion.bo.enhancement.collaboration.config;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class CollaborationRestControllerAdvice {
    @ExceptionHandler(InsertFailedException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage insertFailed(Throwable e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage(e.getLocalizedMessage());
    }

    /**
     * 修改失败
     */
    @ExceptionHandler(UpdateFailedException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage updateFailed(Throwable e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(DataNotExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage dataNotExist(Throwable e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage(e.getMessage());
    }

    /**
     * 参数格式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RequestParamException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage requestParamWrong(RequestParamException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("请求参数错误！");
    }

    @ExceptionHandler(IllegalParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage requestIllegalParameterException(IllegalParameterException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<?> resourceConflictException(ResourceConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getConflict());
    }

    /**
     * 参数校验失败
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage bindFailedException(BindException e) {
        log.error(e.getMessage());
        ObjectError objectError = e.getAllErrors().get(0);
        return new ErrorMessage(-2, objectError.getDefaultMessage());
    }
}
