package com.utn.udee.utils;

import com.utn.udee.model.*;
import com.utn.udee.model.dto.AddressDto;
import com.utn.udee.model.dto.TariffDto;
import com.utn.udee.model.dto.UserDto;

import java.util.Collections;
import java.util.List;

public class TestUtils {

    public static User aUserClient = new Client(1, "11222333", "John", "Doe", "john1", "1122", Collections.emptyList());
    public static User aUserEmployee = new Employee(2, "99888777", "Rachel", "Smiths", "smiths1", "2211");


    public static UserDto aUserDto = new UserDto(1, "11222333", "John", "Doe", "john1");


    public static List<User> aUsersList = List.of(aUserClient, aUserEmployee);


    public static List<UserDto> aUsersDtoList = List.of(aUserDto,
            new UserDto(2, "99888777", "Rachel", "Smiths", "smiths1"));


    public static Tariff aTariff = new Tariff(1, 1123.21f, TariffType.COMMERCIAL, Collections.emptyList());


    public static TariffDto aTariffDto = TariffDto.builder()
            .tariff(1123.21f)
            .tariffType(TariffType.COMMERCIAL).build();


    public static List<Tariff> aTariffList = List.of(aTariff,
            Tariff.builder().id(2).tariff(222.21f).tariffType(TariffType.RESIDENTIAL).addresses(Collections.emptyList()).build(),
            Tariff.builder().id(3).tariff(333.3f).tariffType(TariffType.SOCIAL).addresses(Collections.emptyList()).build());


    public static List<TariffDto> aTariffDtoList = List.of(aTariffDto,
            TariffDto.builder().id(2).tariff(222.21f).tariffType(TariffType.RESIDENTIAL).build(),
            TariffDto.builder().id(3).tariff(333.3f).tariffType(TariffType.SOCIAL).build());


    public static Address anAddress = new Address(1, "Street 1111", aTariff, (Client) aUserClient);


    public static AddressDto anAddressDto = AddressDto.builder()
            .id(1)
            .address("Street 1111")
            .tariff(aTariffDto)
            .customer(aUserDto)
            .build();


    public static List<Address> anAddressesList = List.of(anAddress,
            new Address(2, "Avenue 2222",
                    new Tariff(2, 222.21f, TariffType.RESIDENTIAL, Collections.emptyList()),
                    new Client(2, "33222111", "Joe", "Doe", "joe1", "2211", Collections.emptyList())));


    public static List<AddressDto> anAddressesDtoList = List.of(anAddressDto,
            new AddressDto(2, "Avenue 2222",
                    new TariffDto(2, 222.21f, TariffType.RESIDENTIAL),
                    new UserDto(2, "33222111", "Joe", "Doe", "joe1")));






}
