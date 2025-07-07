/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.tools.validator;

import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.SpringContextUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import java.util.Locale;
import java.util.Set;

/**
 * hibernate-validator校验工具类
 * 参考文档：http://docs.jboss.org/hibernate/validator/6.0/reference/en-US/html_single/
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public class ValidatorUtils {

    private static volatile ResourceBundleMessageSource bundleMessageSource;
    static {
        if (bundleMessageSource == null) {
            bundleMessageSource = new ResourceBundleMessageSource();
            bundleMessageSource.setDefaultEncoding("UTF-8");
            bundleMessageSource.setBasenames("i18n/validation", "i18n/validation_common");

            String names = SpringContextUtils.applicationContext.getEnvironment().getProperty("spring.validations.basename");
            if (StringUtils.isNotBlank(names)) {
                String[] split = names.split(",");
                for (String name : split) {
                    System.out.println("add validation bundle:" + name);
                    bundleMessageSource.addBasenames(name);
                }
            }
        }
    }

    public static ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setDefaultEncoding("UTF-8");
        bundleMessageSource.setBasenames("i18n/validation", "i18n/validation_common");
        return bundleMessageSource;
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws BootException  校验不通过，则报RenException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BootException {
        Locale.setDefault(LocaleContextHolder.getLocale());

        Validator validator = Validation.byDefaultProvider().configure().messageInterpolator(
                        new ResourceBundleMessageInterpolator(
                                new MessageSourceResourceBundleLocator(bundleMessageSource)))
                .buildValidatorFactory().getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
            throw new BootException(constraint.getMessage());
        }
    }
}