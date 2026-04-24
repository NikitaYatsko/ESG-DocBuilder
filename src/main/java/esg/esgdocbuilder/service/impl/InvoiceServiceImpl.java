package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.InvoiceNotFoundException;
import esg.esgdocbuilder.mapper.InvoiceMapper;
import esg.esgdocbuilder.mapper.InvoiceTitleMapper;
import esg.esgdocbuilder.model.dto.request.InvoiceItemRequest;
import esg.esgdocbuilder.model.dto.request.InvoiceRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceResponse;
import esg.esgdocbuilder.model.dto.response.InvoiceTitleResponse;
import esg.esgdocbuilder.model.entity.Invoice;
import esg.esgdocbuilder.repository.InvoiceRepository;
import esg.esgdocbuilder.service.InvoiceItemService;
import esg.esgdocbuilder.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.el.lang.ELArithmetic.divide;


@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemService itemService;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceTitleMapper invoiceTitleMapper;


    @Override
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest){
        Invoice invoice = new Invoice();
        invoice.setInvoiceName(invoiceRequest.getInvoiceName());
        invoice.setPowerKwt(invoiceRequest.getPower());
        invoice.setVatAmount(invoiceRequest.getVat_amount());
        invoice.setSumAmount(invoiceRequest.getSum());
        invoice.setSumMarginality(invoiceRequest.getSumMarginality());
        invoice.setDiscountPercent(invoiceRequest.getDiscountPercent() != null
                ? invoiceRequest.getDiscountPercent() : BigDecimal.ZERO);

        Invoice savedInvoice = invoiceRepository.save(invoice);


        for (InvoiceItemRequest itemRequest : invoiceRequest.getItems()) {
            itemService.addItemToInvoice(savedInvoice.getId(), itemRequest);
        }

        return getInvoiceById(savedInvoice.getId());
    }




    @Override
    public InvoiceResponse getInvoiceById(Long id){
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        ApiErrorMessage.INVOICE_NOT_FOUND.getMessage()));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    public List<InvoiceResponse> getAllInvoices (){
        return invoiceRepository.findAll().stream().map(invoiceMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceTitleResponse> getInvoiceAllTitle (){
        return invoiceRepository.findAll().stream().map(invoiceTitleMapper::toResponse).collect(Collectors.toList());
    }


    public InvoiceResponse updateInvoice(Long id,InvoiceRequest invoiceRequest){
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(ApiErrorMessage.INVOICE_NOT_FOUND.getMessage()));

        invoice.setInvoiceName(invoiceRequest.getInvoiceName());
        invoice.setPowerKwt(invoiceRequest.getPower());
        invoice.setVatAmount(invoiceRequest.getVat_amount());
        invoice.setSumAmount(invoiceRequest.getSum());
        invoice.setSumMarginality(invoiceRequest.getSumMarginality());
        invoice.setDiscountPercent(invoiceRequest.getDiscountPercent() != null
                ? invoiceRequest.getDiscountPercent() : BigDecimal.ZERO);

        itemService.deleteByInvoiceId(id);

        invoice.getInvoiceItems().clear();

        for (InvoiceItemRequest itemRequest : invoiceRequest.getItems()) {
            itemService.addItemToInvoice(id, itemRequest);
        }

        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

        return getInvoiceById(id);
    }


    @Override
    public void deleteInvoice(Long id){
        if(!invoiceRepository.existsById(id)){
            throw new InvoiceNotFoundException(ApiErrorMessage.INVOICE_NOT_FOUND.getMessage());
        }
        invoiceRepository.deleteById(id);
    }

}
