package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.InvoiceItemNotFoundException;
import esg.esgdocbuilder.exception.exceptions.InvoiceNotFoundException;
import esg.esgdocbuilder.exception.exceptions.ProductNotFoundException;
import esg.esgdocbuilder.mapper.InvoiceItemMapper;
import esg.esgdocbuilder.model.dto.request.InvoiceItemRequest;
import esg.esgdocbuilder.model.dto.response.InvoiceItemResponse;
import esg.esgdocbuilder.model.entity.Invoice;
import esg.esgdocbuilder.model.entity.InvoiceItem;
import esg.esgdocbuilder.model.entity.Product;
import esg.esgdocbuilder.repository.InvoiceItemRepository;
import esg.esgdocbuilder.repository.InvoiceRepository;
import esg.esgdocbuilder.repository.ProductRepository;
import esg.esgdocbuilder.service.InvoiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final InvoiceItemRepository itemRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemMapper itemMapper;
    private final ProductRepository productRepository;

    @Override
    public InvoiceItemResponse addItemToInvoice(Long invoiceId, InvoiceItemRequest itemRequest){
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceItemNotFoundException(ApiErrorMessage.INVOICE_NOT_FOUND.getMessage()));

        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        ApiErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

        InvoiceItem item = itemMapper.toEntity(itemRequest, invoice, product);




        InvoiceItem savedItem = itemRepository.save(item);
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
        return itemMapper.toResponse(savedItem);
    }

    @Override
    public InvoiceItemResponse updateItem(Long itemId, InvoiceItemRequest itemRequest){

        InvoiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new InvoiceItemNotFoundException(
                        ApiErrorMessage.INVOICE_ITEM_NOT_FOUND.getMessage()));


        item.setQuantity(itemRequest.getQuantity());
        item.setUnitPrice(itemRequest.getUnitPrice());
        item.setVatMultiplier(itemRequest.getVatMultiplier());
        item.setTotalPrice(itemRequest.getTotalPrice());


        InvoiceItem updatedItem = itemRepository.save(item);


        Invoice invoice = updatedItem.getInvoice();
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

        return itemMapper.toResponse(updatedItem);

    }


    @Override
    public void deleteItem (Long itemId){
        InvoiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new InvoiceItemNotFoundException(ApiErrorMessage.INVOICE_ITEM_NOT_FOUND.getMessage()));

        Invoice invoice = item.getInvoice();
        itemRepository.delete(item);
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

    }


    @Override
    public void deleteByInvoiceId(Long invoiceId) {
        itemRepository.deleteByInvoiceId(invoiceId);
    }

    @Override
    public List<InvoiceItemResponse> getItemsByInvoiceId(Long invoiceId){
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(ApiErrorMessage.INVOICE_NOT_FOUND.getMessage()));
        return invoice.getInvoiceItems().stream().map(itemMapper :: toResponse)
                                                .collect(Collectors.toList());
    }


    @Override
    public InvoiceItemResponse getItemById(Long itemId) {
        InvoiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new InvoiceItemNotFoundException(
                        ApiErrorMessage.INVOICE_ITEM_NOT_FOUND.getMessage()));
        return itemMapper.toResponse(item);
    }
}
