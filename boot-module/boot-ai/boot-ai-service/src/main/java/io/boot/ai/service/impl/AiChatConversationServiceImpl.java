package io.boot.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.boot.ai.service.AiChatConversationService;
import io.boot.ai.service.AiModelService;
import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.commons.security.user.SecurityUser;
import io.boot.commons.tools.utils.ConvertUtils;
import io.boot.ai.dao.AiChatConversationDao;
import io.boot.ai.dto.AiChatConversationDTO;
import io.boot.ai.dto.AiModelDTO;
import io.boot.ai.entity.AiChatConversationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AI聊天对话
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
@AllArgsConstructor
public class AiChatConversationServiceImpl extends BaseServiceImpl<AiChatConversationDao, AiChatConversationEntity> implements AiChatConversationService {
    private final AiModelService aiModelService;

    @Override
    public List<AiChatConversationDTO> getList() {
        LambdaQueryWrapper<AiChatConversationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AiChatConversationEntity::getId);
        List<AiChatConversationEntity> list = baseDao.selectList(wrapper);

        return ConvertUtils.sourceToTarget(list, AiChatConversationDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiChatConversationDTO save(AiChatConversationDTO dto) {
        AiChatConversationEntity entity = ConvertUtils.sourceToTarget(dto, AiChatConversationEntity.class);

        // 获取第一个模型
        List<AiModelDTO> modelList = aiModelService.getList();
        if (modelList.isEmpty()) {
            throw new RuntimeException("请先添加模型");
        }

        // 设置默认模型
        AiModelDTO modelDTO = aiModelService.getList().get(0);
        entity.setModelId(modelDTO.getId());
        entity.setModel(modelDTO.getModel());

        // 设置用户ID
        entity.setUserId(SecurityUser.getUserId());

        baseDao.insert(entity);

        return ConvertUtils.sourceToTarget(entity, AiChatConversationDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AiChatConversationDTO dto) {
        AiChatConversationEntity entity = new AiChatConversationEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());

        // 修改模型
        if (dto.getModelId() != null) {
            AiModelDTO modelDTO = aiModelService.get(dto.getModelId());
            entity.setModelId(dto.getModelId());
            entity.setModel(modelDTO.getModel());
        }

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        baseDao.deleteByIds(idList);
    }

}