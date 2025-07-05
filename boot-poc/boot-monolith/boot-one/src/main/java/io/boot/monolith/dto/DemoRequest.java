package io.boot.monolith.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import io.boot.commons.tools.utils.Result;
import lombok.Data;

@Data
public class DemoRequest extends BaseRequest<DemoData> {

    private Integer id;

//    @Override
//    public TypeReference<Result<DemoData>> getResponseClass() {
//        return new TypeReference<Result<DemoData>>() {};
//    }

}
