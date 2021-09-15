package ru.bachinin.cardealership.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.Invoice;

@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long> {
    Invoice getInvoiceById(Long id);

    Page<Invoice> findAllByCreatedBy_Id(Long id, Pageable pageable);

}
