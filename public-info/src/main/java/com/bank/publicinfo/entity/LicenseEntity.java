package com.bank.publicinfo.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Objects;

/**
 * Entity для таблицы "license"
 **/
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "license", schema = "public_bank_information")
public class LicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "photo")
    Byte[] photoLicense;

    @ManyToOne
    @JoinColumn(name = "bank_details_id")
    BankDetailsEntity bankDetails;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final LicenseEntity license = (LicenseEntity) o;

        return id.equals(license.id) &&
                Arrays.equals(photoLicense, license.photoLicense) &&
                bankDetails.equals(license.bankDetails);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, bankDetails);

        result = 31 * result + Arrays.hashCode(photoLicense);

        return result;
    }
}
