/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.boot.commons.mybatis.service.impl.CrudServiceImpl;
import io.boot.dao.WeChatNotifyLogDao;
import io.boot.dto.WeChatNotifyLogDTO;
import io.boot.entity.WeChatNotifyLogEntity;
import io.boot.service.WeChatNotifyLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信支付回调日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class WeChatNotifyLogServiceImpl extends CrudServiceImpl<WeChatNotifyLogDao, WeChatNotifyLogEntity, WeChatNotifyLogDTO> implements WeChatNotifyLogService {

    @Override
    public QueryWrapper<WeChatNotifyLogEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<WeChatNotifyLogEntity> wrapper = new QueryWrapper<>();

        String outTradeNo = (String) params.get("outTradeNo");
        wrapper.eq(StringUtils.isNotBlank(outTradeNo), "out_trade_no", outTradeNo);

        String notifyId = (String) params.get("notifyId");
        wrapper.eq(StringUtils.isNotBlank(notifyId), "notify_id", notifyId);

        String tradeStatus = (String) params.get("tradeStatus");
        wrapper.eq(StringUtils.isNotBlank(tradeStatus), "trade_status", tradeStatus);

        return wrapper;
    }

}