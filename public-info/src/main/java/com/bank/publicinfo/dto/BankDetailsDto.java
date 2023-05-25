package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.BankDetailsEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO для {@link BankDetailsEntity}
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankDetailsDto implements Serializable {
    Long id;

    @DecimalMin(value = "40000000", message = "Указан некорректный номер БИК")
    @DecimalMax(value = "249999999", message = "Указан некорректный номер БИК")
    Long bik;

    @DecimalMin(value = "1000000000", message = "Указан некорректный номер ИНН")
    @DecimalMax(value = "999999999999", message = "Указан некорректный номер ИНН")
    Long inn;

    @DecimalMin(value = "100000000", message = "Указан некорректный номер КПП")
    @DecimalMax(value = "999999999", message = "Указан некорректный номер КПП")
    Long kpp;

    @DecimalMin(value = "30100000000000000000", message = "Указан некорректный Корреспондентский счет")
    @DecimalMax(value = "30199999999999999999", message = "Указан некорректный Корреспондентский счет")
    BigDecimal corAccount;

    String city;
    String jointStockCompany;
    String name;
}
