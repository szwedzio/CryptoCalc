package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Szwedzio on 06.03.2018.
 */
public class pairs {

    public pairs(){
    }

    @Getter @Setter
    private String product_id;
    @Getter @Setter
    private BigDecimal price;
    @Getter @Setter
    private String trade_id;


}
