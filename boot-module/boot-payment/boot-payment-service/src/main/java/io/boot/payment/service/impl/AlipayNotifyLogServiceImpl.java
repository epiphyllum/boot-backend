/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.boot.commons.mybatis.service.impl.CrudServiceImpl;
import io.boot.payment.dao.AlipayNotifyLogDao;
import io.boot.payment.dto.AlipayNotifyLogDTO;
import io.boot.payment.entity.AlipayNotifyLogEntity;
import io.boot.payment.service.AlipayNotifyLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付宝回调日志
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
public class AlipayNotifyLogServiceImpl extends CrudServiceImpl<AlipayNotifyLogDao, AlipayNotifyLogEntity, AlipayNotifyLogDTO> implements AlipayNotifyLogService {

    @Override
    public QueryWrapper<AlipayNotifyLogEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<AlipayNotifyLogEntity> wrapper = new QueryWrapper<>();

        String outTradeNo = (String)params.get("outTradeNo");
        wrapper.eq(StringUtils.isNotBlank(outTradeNo), "out_trade_no", outTradeNo);

        String notifyId = (String)params.get("notifyId");
        wrapper.eq(StringUtils.isNotBlank(notifyId), "notify_id", notifyId);

        String tradeStatus = (String)params.get("tradeStatus");
        wrapper.eq(StringUtils.isNotBlank(tradeStatus), "trade_status", tradeStatus);

        return wrapper;
    }

}