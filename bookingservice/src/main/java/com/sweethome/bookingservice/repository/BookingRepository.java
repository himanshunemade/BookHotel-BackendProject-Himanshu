package com.sweethome.bookingservice.repository;

import com.sweethome.bookingservice.entity.BookInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookInfoEntity, Integer> {
}
