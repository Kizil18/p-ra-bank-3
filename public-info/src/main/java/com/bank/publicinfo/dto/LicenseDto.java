package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.LicenseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO для {@link LicenseEntity}
 **/
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LicenseDto implements Serializable {
    Long id;
    Byte[] photoLicense;
    BankDetailsDto bankDetails;
}
