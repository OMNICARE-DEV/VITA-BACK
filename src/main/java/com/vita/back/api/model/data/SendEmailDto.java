package com.vita.back.api.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
public class SendEmailDto {
    /** 수신자 이메일 주소 */
    @Schema(description = "수신자 이메일 주소")
    private String toEmail;
    /** 수신자 이름 */
    @Schema(description = "수신자 이름")
    private String toName;
    /** 이메일 제목 */
    @JsonIgnore
    private String subject;
    /** 이메일 내용 */
    @JsonIgnore
    private String content;
    /** 이메일 발송 타입 (HTML, TEXT)
     * html인 경우 templatePath 필수
     */
    @JsonIgnore
    private String type = "HTML";
    /** 템플릿 경로 */
    @JsonIgnore
    private String templatePath;
    /** HTML 템플릿에 전달할 파라미터 */
    @JsonIgnore
    private Map<String, Object> params;
}