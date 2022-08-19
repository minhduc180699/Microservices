package com.saltlux.deepsignal.adapter.domain.EntityLinking;

import com.saltlux.deepsignal.adapter.domain.StockCodes;
import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class MetaDTO {

    @Field("stock")
    private List<StockCodes> stock;
}
