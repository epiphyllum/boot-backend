/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.message.email;

import cn.hutool.core.map.MapUtil;
import freemarker.template.Template;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.exception.BootException;
import io.boot.message.dao.SysMailTemplateDao;
import io.boot.message.entity.SysMailTemplateEntity;
import io.boot.message.exception.ModuleErrorCode;
import io.boot.message.utils.ModuleConstant;
import io.boot.rbac.remote.ParamsRemoteService;
import io.boot.message.service.SysMailLogService;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件工具类
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Slf4j
@Component
public class EmailUtils {
    @Resource
    private ParamsRemoteService paramsRemoteService;
    @Resource
    private SysMailTemplateDao sysMailTemplateDao;
    @Resource
    private SysMailLogService sysMailLogService;

    private final static String KEY = ModuleConstant.MAIL_CONFIG_KEY;

    private JavaMailSenderImpl createMailSender(EmailConfig config) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(config.getSmtp());
        sender.setPort(config.getPort());
        sender.setUsername(config.getUsername());
        sender.setPassword(config.getPassword());
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "10000");
        p.setProperty("mail.smtp.auth", "false");
        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param templateId 模板ID
     * @param to         收件人
     * @param cc         抄送
     * @param params     模板参数
     * @return true：成功   false：失败
     */
    public boolean sendMail(Long templateId, String[] to, String[] cc, Map<String, Object> params) throws Exception {
        SysMailTemplateEntity template = sysMailTemplateDao.selectById(templateId);
        if (template == null) {
            throw new BootException(ModuleErrorCode.MAIL_TEMPLATE_NOT_EXISTS);
        }

        EmailConfig config = paramsRemoteService.getValueObject(KEY, EmailConfig.class);
        JavaMailSenderImpl mailSender = createMailSender(config);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        //设置utf-8编码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(config.getUsername());

        //收件人
        messageHelper.setTo(to);

        //抄送
        if (cc != null && cc.length > 0) {
            messageHelper.setCc(cc);
        }
        //主题
        messageHelper.setSubject(template.getSubject());

        //邮件正文
        String content = getFreemarkerContent(template.getContent(), params);
        messageHelper.setText(content, true);

        int status = Constant.SUCCESS;
        //发送邮件
        try {
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            status = Constant.FAIL;
            log.error("send error", e);
        }

        sysMailLogService.save(templateId, config.getUsername(), to, cc, template.getSubject(), content, status);

        return status == Constant.SUCCESS;
    }

    /**
     * 获取Freemarker渲染后的内容
     *
     * @param content 模板内容
     * @param params  参数
     */
    private String getFreemarkerContent(String content, Map<String, Object> params) throws Exception {
        if (MapUtil.isEmpty(params)) {
            return content;
        }

        //模板
        StringReader reader = new StringReader(content);
        Template template = new Template("mail", reader, null, "utf-8");

        //渲染模板
        StringWriter sw = new StringWriter();
        template.process(params, sw);

        content = sw.toString();
        sw.close();

        return content;
    }

    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param cc      抄送
     * @param subject 主题
     * @param content 邮件正文
     * @return true：成功   false：失败
     */
    public boolean sendMail(String[] to, String[] cc, String subject, String content) throws Exception {
        EmailConfig config = paramsRemoteService.getValueObject(KEY, EmailConfig.class);
        JavaMailSenderImpl mailSender = createMailSender(config);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //设置utf-8编码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(config.getUsername());

        //收件人
        messageHelper.setTo(to);
        //抄送
        if (cc != null && cc.length > 0) {
            messageHelper.setCc(cc);
        }
        //主题
        messageHelper.setSubject(subject);
        //邮件正文
        messageHelper.setText(content, true);

        int status = Constant.SUCCESS;
        //发送邮件
        try {
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            status = Constant.FAIL;
            log.error("send error", e);
        }

        sysMailLogService.save(null, config.getUsername(), to, cc, subject, content, status);

        return status == Constant.SUCCESS;
    }

}
