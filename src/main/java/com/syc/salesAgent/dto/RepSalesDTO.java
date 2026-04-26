// RepSalesDTO.java
package com.syc.salesAgent.dto;

import java.math.BigDecimal;
/*销售员
* */

public record RepSalesDTO(
        Long repId,
        String repName,
        Long regionId,
        String regionName,
        BigDecimal totalAmount,
        Integer orderCount
) {}