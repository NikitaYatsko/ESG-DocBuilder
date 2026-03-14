package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.InvoiceNotFoundException;
import esg.esgdocbuilder.exception.exceptions.ProductNotFoundException;
import esg.esgdocbuilder.mapper.InvoiceItemMapper;
import esg.esgdocbuilder.mapper.InvoiceMapper;
import esg.esgdocbuilder.model.dto.request.InvoiceItemRequest;
import esg.esgdocbuilder.model.dto.request.InvoiceRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceResponse;
import esg.esgdocbuilder.model.entity.Invoice;
import esg.esgdocbuilder.model.entity.InvoiceItem;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.repository.InvoiceItemRepository;
import esg.esgdocbuilder.repository.InvoiceRepository;
import esg.esgdocbuilder.repository.ProductRepository;
import esg.esgdocbuilder.service.InvoiceItemService;
import esg.esgdocbuilder.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
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


    @Override
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest){
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice savedInvoice = invoiceRepository.save(invoice);


        for (InvoiceItemRequest itemRequest : invoiceRequest.getItems()) {
            itemService.addItemToInvoice(savedInvoice.getId(), itemRequest);
        }

        return getInvoiceById(savedInvoice.getId());
    }


    private String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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


    public InvoiceResponse updateInvoice(Long id,InvoiceRequest invoiceRequest){
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        ApiErrorMessage.INVOICE_NOT_FOUND.getMessage()));

        itemService.getItemsByInvoiceId(id).forEach(item ->
                itemService.deleteItem(item.getId())
        );

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
