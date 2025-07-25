/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.form.controller;

import io.boot.commons.tools.utils.Result;
import io.boot.commons.tools.validator.ValidatorUtils;
import io.boot.commons.tools.validator.group.AddGroup;
import io.boot.commons.tools.validator.group.DefaultGroup;
import io.boot.form.dto.CorrectionDTO;
import io.boot.form.service.CorrectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 转正申请
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("form/correction")
@AllArgsConstructor
@Tag(name = "转正申请")
public class CorrectionController {
    private final CorrectionService correctionService;


    @PostMapping("start")
    @Operation(summary = "启动流程")
    public Result startProcess(@RequestBody CorrectionDTO dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);


        correctionService.save(dto);

        return new Result();
    }

    @GetMapping("{instanceId}")
    @Operation(summary = "表单信息")
    public Result<CorrectionDTO> info(@PathVariable("instanceId") String instanceId) {
        CorrectionDTO correction = correctionService.get(instanceId);

        return new Result<CorrectionDTO>().ok(correction);
    }

}
