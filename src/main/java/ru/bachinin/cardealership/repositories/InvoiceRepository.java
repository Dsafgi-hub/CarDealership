package ru.bachinin.cardealership.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
