package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.BranchEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO для {@link BranchEntity}
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchDto implements Serializable {
    Long id;
    String address;
    Long phoneNumber;
    String city;
    LocalTime startOfWork;
    LocalTime endOfWork;
}
