package esg.esgdocbuilder.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

public interface BankPdfService {
    ByteArrayOutputStream generateBankOperationsPdf(LocalDate from, LocalDate to);
}