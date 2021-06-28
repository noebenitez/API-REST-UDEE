package com.utn.udee.controller.converter;

import com.utn.udee.model.Invoice;
import com.utn.udee.model.dto.InvoiceDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceToInvoiceDTOConverter implements Converter<Invoice,InvoiceDto> {

        private final ModelMapper modelMapper;

        public InvoiceToInvoiceDTOConverter(final ModelMapper modelMapper){
            this.modelMapper = modelMapper;
        }

        @Override
        public InvoiceDto convert(Invoice invoice) {
            return modelMapper.map(invoice, InvoiceDto.class);
        }
}
