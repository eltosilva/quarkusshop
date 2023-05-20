package com.targa.labs.services.dtos;

import com.targa.labs.models.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String address1;
    private String address2;
    private String city;
    private String postcode;
    private String country;

    public static AddressDto mapToDto(Address address) {
        return new AddressDto(address.getAddress1(), address.getAddress2(), address.getCity(), address.getPostcode(), address.getCountry());
    }

    public static Address mapToModel(AddressDto addressDto) {
        return new Address(addressDto.getAddress1(), addressDto.getAddress2(), addressDto.getCity(), addressDto.getPostcode(), addressDto.getCountry());
    }
}
