package io.boot.mp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
 
/**
 * 公众号素材
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@Schema(description = "公众号素材")
public class MpMaterialDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mediaId;


}
