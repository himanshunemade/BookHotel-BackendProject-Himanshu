package com.sweethome.bookingservice.service;

import com.sweethome.bookingservice.dto.TransactionDto;
import com.sweethome.bookingservice.entity.BookInfoEntity;
import com.sweethome.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    RestTemplate template;

    @Value("${payment.service.url}")
    String paymentUrl;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookInfoEntity bookHotel(BookInfoEntity bookInfo){
        Period period = Period.between(bookInfo.getFromDate().toLocalDate(), bookInfo.getToDate().toLocalDate());
        int roomPrice =1000*bookInfo.getNumOfRooms()*period.getDays();
        bookInfo.setRoomPrice(roomPrice);
        String roomNumbers = getRoomNumbers(bookInfo.getNumOfRooms()).stream()
                .map(Object::toString).collect(Collectors.joining(","));
        bookInfo.setRoomNumbers(roomNumbers);
        bookInfo.setBookedOn(Date.valueOf(LocalDate.now()));
        BookInfoEntity bookingEntity = bookingRepository.save(bookInfo);
        return bookingEntity;
    }

    public static ArrayList<String> getRoomNumbers(int count){
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String>numberList = new ArrayList<String>();

        for (int i=0; i<count; i++){
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }

        return numberList;
    }

    @Transactional
    public BookInfoEntity createTransaction(Integer bookingId, TransactionDto transactionDto) throws Exception {
        Optional<BookInfoEntity> bookInfo = bookingRepository.findById(bookingId);
        if(!bookInfo.isPresent()){
            throw new Exception("Invalid Booking Id");
        }
        //call to payment service
        Integer transactionId = (template.postForObject(paymentUrl,transactionDto,Integer.class));
        String message = "Booking confirmed for user with aadhaar number: "
                + bookInfo.get().getAadharNumber()
                +    "    |    "
                + "Here are the booking details:    " + bookInfo.get().toString();
        System.out.println(message);
        BookInfoEntity entity = bookInfo.get();
        entity.setTransactionId(transactionId);
        return bookingRepository.save(entity);
    }
}
