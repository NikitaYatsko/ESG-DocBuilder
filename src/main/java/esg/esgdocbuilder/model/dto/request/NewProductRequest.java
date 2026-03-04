package esg.esgdocbuilder.model.dto.request;

import esg.esgdocbuilder.model.enums.TypeOfUnitEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewProductRequest {

    // Основные данные
    private String name;               // Название продукта
    private Long categoryId;           // ID категории
    private TypeOfUnitEnum typeOfUnit; // Единица измерения

    // Финансовые данные
    private BigDecimal costPrice;      // Себестоимость
    private BigDecimal markupPercent;  // Наценка в %
    private Boolean hasVat;            // Есть ли НДС
}
