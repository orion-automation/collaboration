package com.eorion.bo.enhancement.collaboration.config;

import com.eorion.bo.enhancement.collaboration.domain.commom.ErrorMessage;
import com.eorion.bo.enhancement.collaboration.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Log4j2
public class CollaborationRestControllerAdvice {
    /**
     * 参数格式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage wrongFormat(HttpMessageConversionException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("参数格式错误");
    }

    /**
     * 请求体格式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BeanInstantiationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage requestParameterError(BeanInstantiationException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("请求体格式错误");
    }

    /**
     * 资源类型不支持
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage wrongMediaType(HttpMediaTypeNotSupportedException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("资源类型不支持");
    }

    /**
     * 资源类型不支持
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage wrongRequestMethod(HttpRequestMethodNotSupportedException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("请求方式不支持");
    }

    /**
     * 参数缺失
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage missParam(MissingServletRequestParameterException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("参数缺失");
    }


    /**
     * 未找到资源
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage notFound(NoHandlerFoundException e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("请求资源不存在");
    }

    /**
     * 服务器错误
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage throwable(Throwable e) {
        log.error(e.getLocalizedMessage());
        return new ErrorMessage("服务器未知错误");
    }


    /**
     * 参数校验失败
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage bindFailedException(BindException e){
        log.error(e.getMessage());
        ObjectError objectError = e.getAllErrors().get(0);
        return new ErrorMessage(-2, objectError.getDefaultMessage());
    }

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

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage runtimeException(Throwable e) {
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


}
