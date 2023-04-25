package com.sweethome.bookingservice;

import com.sweethome.bookingservice.dto.TransactionDto;
import com.sweethome.bookingservice.entity.BookInfoEntity;
import com.sweethome.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
public class BookingController {

    private final BookingService boookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.boookingService = bookingService;
    }

    @PostMapping("/booking")
    public ResponseEntity<BookInfoEntity> bookHotel(@RequestBody BookInfoEntity bookInfo){
        BookInfoEntity entity = boookingService.bookHotel(bookInfo);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PostMapping("booking/{bookingId}/transaction")
    public ResponseEntity<BookInfoEntity> bookHotel(@PathVariable("bookingId") String bookingId, @RequestBody TransactionDto transactionDto) throws Exception {
        int id = Integer.parseInt(bookingId);
        if(transactionDto.getPaymentMode().equals("UPI") || transactionDto.getPaymentMode().equals("CARD")) {
            if (transactionDto.getPaymentMode().equals("CARD") && (transactionDto.getCardNumber() == null || transactionDto.getCardNumber().isBlank())) {
                throw new Exception("Card number must be provided");
            } else if (transactionDto.getPaymentMode().equals("UPI") && (transactionDto.getUpiId() == null || transactionDto.getUpiId().isBlank())) {
                throw new Exception("UPI Id must be provided");
            } else{
                BookInfoEntity entity = boookingService.createTransaction(id,transactionDto);
                return new ResponseEntity<>(entity, HttpStatus.CREATED);
            }
        } else{
            throw new Exception("Invalid mode of payment");
        }

    }


}
