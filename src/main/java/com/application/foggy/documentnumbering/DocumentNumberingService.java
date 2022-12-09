package com.application.foggy.documentnumbering;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentNumberingService {
    public static final int PRODUCT = 0;
    public static final int CUSTOMER = 1;
    public static final int ORDER = 2;
    public static final int TRANSACTION = 3;


    private DocumentNumberingRepository documentNumberingRepository;

    private DocumentNumbering getDocumentNumber() {
        try {
            return documentNumberingRepository.findAll().get(0);
        } catch (Exception e) {
            return documentNumberingRepository.save(DocumentNumbering.builder()
                    .product("P-00001")
                    .order("ORD-00001")
                    .customer("C-00001")
                    .transaction("TRN-00001")
                    .build());
        }
    }

    public String getDocumentNumber(int type) {
        DocumentNumbering documentNumber = getDocumentNumber();
        if (type == PRODUCT) {
            return documentNumber.getProduct();
        } else if (type == CUSTOMER) {
            return documentNumber.getCustomer();
        } else if (type == ORDER) {
            return documentNumber.getOrder();
        } else if (type == TRANSACTION) {
            return documentNumber.getTransaction();
        } else {
            return null;
        }
    }

    public void increment(int type) {
        DocumentNumbering documentNumber = getDocumentNumber();
        if (type == PRODUCT) {
            documentNumber.setProduct(incrementAndGet(documentNumber.getProduct()));
        } else if (type == CUSTOMER) {
            documentNumber.setCustomer(incrementAndGet(documentNumber.getCustomer()));
        } else if (type == ORDER) {
            documentNumber.setOrder(incrementAndGet(documentNumber.getOrder()));
        } else if (type == TRANSACTION) {
            documentNumber.setTransaction(incrementAndGet(documentNumber.getTransaction()));
        }
        documentNumberingRepository.save(documentNumber);
    }

    private String incrementAndGet(String id) {
        String[] split = id.split("-");
        String formattedNumber = String.format("%05d", (Integer.parseInt(split[1]) + 1));
        return split[0] + "-" + formattedNumber;
    }
}
