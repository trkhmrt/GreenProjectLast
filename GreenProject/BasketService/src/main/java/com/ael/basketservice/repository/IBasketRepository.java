package com.ael.basketservice.repository;

import com.ael.basketservice.model.Basket;
import com.ael.basketservice.model.BasketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;


@Repository
public interface IBasketRepository extends JpaRepository<Basket, Integer> {


    @Query("Select  b.basketStatus from Basket b WHERE b.basketId = :basketId")
    BasketStatus getBasketStatus(@Param("basketId") Integer basketId);



    Basket findBasketByCustomerId(Integer customerId);
    Basket findBasketByBasketId(Integer basketId);
    Optional<Basket> findByCustomerIdAndBasketStatus_BasketStatusId(Integer customerId, Integer basketStatusId);


    @Query("SELECT b FROM Basket b LEFT JOIN FETCH b.products WHERE " +
            "(:basketId IS NULL OR b.basketId = :basketId) AND " +
            "(:customerId IS NULL OR b.customerId = :customerId) AND " +
            "(:statusId IS NULL OR b.basketStatus.basketStatusId = :statusId)")
    List<Basket> findBasketsByCriteria(
            @Param("basketId") Integer basketId,
            @Param("customerId") Integer customerId,
            @Param("statusId") Integer statusId);

}
