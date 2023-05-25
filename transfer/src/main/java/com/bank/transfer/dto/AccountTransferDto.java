package com.bank.transfer.dto;

import com.bank.transfer.entity.AccountTransferEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ДТО {@link AccountTransferEntity}
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountTransferDto implements Serializable {

    Long id;

    @DecimalMin(value = "4081781051000000000", message = "Указан некорректный номер счета")
    @DecimalMax(value = "4081781051999999999", message = "Указан некорректный номер счета")
    Long accountNumber;

    @Positive(message = "Сумма перевода должна быть больше нуля")
    BigDecimal amount;

    String purpose;
    Long accountDetailsId;
}
