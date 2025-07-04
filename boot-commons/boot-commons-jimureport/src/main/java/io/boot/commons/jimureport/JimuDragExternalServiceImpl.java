package io.boot.commons.jimureport;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.drag.service.IOnlDragExternalService;
import org.jeecg.modules.drag.vo.DragDictModel;
import org.jeecg.modules.drag.vo.DragLogDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("onlDragExternalServiceImpl")
public class JimuDragExternalServiceImpl implements IOnlDragExternalService {

    /**
     * 根据多个字典code查询多个字典项
     *
     * @param codeList
     * @return key = dictCode ； value=对应的字典项
     */
    @Override
    public Map<String, List<DragDictModel>> getManyDictItems(List<String> codeList, List<JSONObject> tableDictList) {
        return new HashMap<>();
    }

    /**
     * @param dictCode
     * @return
     */
    @Override
    public List<DragDictModel> getDictItems(String dictCode) {
        List<DragDictModel> dictItems = new ArrayList<>();
        return new ArrayList<>();
    }

    /**
     * 添加日志
     *
     * @param dragLogDTO
     */
    @Override
    public void addLog(DragLogDTO dragLogDTO) {

    }

    /**
     * 保存日志
     *
     * @param logMsg
     * @param logType
     * @param operateType
     */
    @Override
    public void addLog(String logMsg, int logType, int operateType) {
    }
}