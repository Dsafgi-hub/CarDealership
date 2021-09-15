package ru.bachinin.cardealership.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.bachinin.cardealership.entities.Order;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    Order findOrderByIdAndCreatedBy_Id(Long id, Long id_user);

    Order findOrderById(Long id);

    Page<Order> findAllByCreatedBy_Id(Long id, Pageable pageable);
}
