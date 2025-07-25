/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.dto.region;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 市
 *
 * @author Mark sunlightcs@gmail.com
 */
@Schema(description = "市")
@Data
@EqualsAndHashCode(callSuper = true)
public class RegionCity extends Region {
    @Schema(description = "区、县列表")
    private List<Region> counties = new ArrayList<>();
}
