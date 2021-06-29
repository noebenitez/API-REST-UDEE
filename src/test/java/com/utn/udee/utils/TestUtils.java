package com.utn.udee.utils;

import com.utn.udee.model.*;
import com.utn.udee.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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


    public static Integer IDUSER = 1;
    public static Integer IDADDRESS= 1;
    public static Integer IDMEASUREMENT=1;
    public static Integer IDMETER = 1;
    public static Integer IDBRAND = 1;
    public static Integer IDMODEL = 1;

    public static Model aModel = new Model(1,"model1");
    public static ModelDto aModelDto = ModelDto.getModelDto(aModel);

    public static Brand aBrand = new Brand(1,"brand1");
    public static BrandDto aBrandDto =  BrandDto.getBrandDto(aBrand);


    public static Invoice anInvoice = new Invoice(1,"1234",(Client)aUserClient,2.4f,2.5f,4.4f,5.5f,LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now(),TariffType.RESIDENTIAL,PaymentStatus.UNPAID);
    public static List<Invoice> anInvoiceList() {
        return List.of(anInvoice);
    }
    public static Page<Invoice> anInvoicePage()
    {
        return new PageImpl<>(anInvoiceList());
    }
    public static InvoiceDto anInvoiceDto = InvoiceDto.getInvoiceDto(anInvoice);
    public static List<InvoiceDto> anInvoiceDtoList = List.of(anInvoiceDto);
    public static Page<InvoiceDto> anInvoiceDtoPage() {
        return new PageImpl<>(anInvoiceDtoList);
    }

    public static Meter aMeter = new Meter(1,"1234","1222",aBrand,aModel,anAddress,null);
    public static MeterDto aMeterDto = MeterDto.getMeterDto(aMeter);
    public static List<Meter> aMeterList = List.of(aMeter);
    public static Page<Meter> aMeterPage() { return new PageImpl<>(aMeterList);}
    public static List<MeterDto> aMeterDtoList = List.of(aMeterDto);
    public static Page<MeterDto> aMeterDtoPage() { return new PageImpl<>(aMeterDtoList);}

    public static Measurement aMeasurement = new Measurement(1,aMeter,2.1f,2.4f,LocalDateTime.now(),anInvoice);
    public static MeasurementSenderDto aMeasurementSenderDto = MeasurementSenderDto.builder().date(LocalDateTime.now().toString()).password("1234").serialNumber("1234").value(22.3f).build();
    public static MeasurementDto aMeasurementDto = MeasurementDto.builder().measurement(aMeasurement.getMeasurement()).id(aMeasurement.getId()).invoice(InvoiceDto.getInvoiceDto(aMeasurement.getInvoice())).datetime(aMeasurement.getDatetime()).price(aMeasurement.getPrice()).build();
    public static List<MeasurementDto> aMeasurementDtoList = List.of(aMeasurementDto);
    public static List<Measurement> aMeasurementList = List.of(aMeasurement);
    public static Page<Measurement> aMeasurementPage()
    {
        return new PageImpl<>(aMeasurementList);
    }
    public static Page<MeasurementDto> aMeasurementDtoPage() { return new PageImpl<>(aMeasurementDtoList);}



    public static Pageable aPageable() {
        return PageRequest.of(0, 5);
    }
}
