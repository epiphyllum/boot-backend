/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.message.sms;

import cn.hutool.core.map.MapUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.JsonUtils;
import io.boot.commons.tools.utils.SpringContextUtils;
import io.boot.message.dto.SmsConfig;
import io.boot.message.enums.PlatformEnum;
import io.boot.message.exception.ModuleErrorCode;
import io.boot.message.service.SysSmsLogService;

import java.util.LinkedHashMap;

/**
 * 阿里云短信服务
 *
 * @author epiphyllum.zhou@gmail.com
 */
public class AliyunSmsService extends AbstractSmsService {
    /**
     * 短信API产品名称（短信产品名固定，无需修改）
     */
    private final String PRODUCT = "Dysmsapi";
    /**
     * 短信API产品域名（接口地址固定，无需修改）
     */
    private final String DOMAIN = "dysmsapi.aliyuncs.com";

    private IClientProfile profile;


    public AliyunSmsService(SmsConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
        try {
            //初始化acsClient，暂不支持region化
            profile = DefaultProfile.getProfile("cn-hangzhou", config.getAliyunAccessKeyId(), config.getAliyunAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSms(String smsCode, String mobile, LinkedHashMap<String, String> params) {
        this.sendSms(smsCode, mobile, params, config.getAliyunSignName(), config.getAliyunTemplateCode());
    }

    @Override
    public void sendSms(String smsCode, String mobile, LinkedHashMap<String, String> params, String signName, String template) {
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        //待发送手机号，支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码，批量调用相对于单条调用及时性稍有延迟，验证码类型的短信推荐使用单条调用的方式
        //发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如"0085200000000"
        request.setPhoneNumbers(mobile);
        //短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //短信模板-可在短信控制台中找到
        request.setTemplateCode(template);
        //参数
        if(MapUtil.isNotEmpty(params)){
            request.setTemplateParam(JsonUtils.toJsonString(params));
        }

        SendSmsResponse response;
        try {
            IAcsClient acsClient = new DefaultAcsClient(profile);
            response = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new BootException(ModuleErrorCode.SEND_SMS_ERROR, e, "");
        }

        int status = Constant.SUCCESS;
        if(!Constant.OK.equalsIgnoreCase(response.getCode())){
            status = Constant.FAIL;
        }

        //保存短信记录
        SysSmsLogService sysSmsLogService = SpringContextUtils.getBean(SysSmsLogService.class);
        sysSmsLogService.save(smsCode, PlatformEnum.ALIYUN.value(), mobile, params, status);

        if(status == Constant.FAIL){
            throw new BootException(ModuleErrorCode.SEND_SMS_ERROR, response.getMessage());
        }
    }
}
