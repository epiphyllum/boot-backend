/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 库存
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@Schema(description = "库存")
public class StorageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;
    @Schema(description = "商品编码")
    private String commodityCode;
    @Schema(description = "商品库存数")
    private Integer total;

}