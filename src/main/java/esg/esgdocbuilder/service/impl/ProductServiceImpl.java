package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {
    private final ProductRepository productRepository;
}
