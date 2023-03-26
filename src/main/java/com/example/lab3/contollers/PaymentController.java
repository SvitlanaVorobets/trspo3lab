package com.example.lab3.contollers;

import com.example.lab3.dao.CreditCardDao;
import com.example.lab3.dao.PaymentDao;
import com.example.lab3.dto.NewPaymentDto;
import com.example.lab3.models.CreditCard;
import com.example.lab3.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentDao paymentDao;

    @Autowired
    CreditCardDao creditCardDao;

    @GetMapping("")
    public ResponseEntity<Iterable<Payment>> findAll() {
        List<Payment> payments = paymentDao.findAll();
        if (paymentDao.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable("id") long id) {
        Optional<Payment> payment = paymentDao.findById(id);
        return payment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody NewPaymentDto newPayment){
        Payment payment = new Payment();

        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = new Date();

        CreditCard creditCard = creditCardDao.findById(newPayment.getCredit_card_id()).get();

        if(!creditCard.getAccount().isBlocked()){
            payment.setAmount(newPayment.getAmount());
            payment.setType(newPayment.getType());
            payment.setCreditCard(creditCard);
            payment.setDate(dateFormat.format(date));

            Double currentAmount = creditCard.getAccount().getBalance();
            if(currentAmount > newPayment.getAmount()){
                creditCard.getAccount().setBalance(currentAmount - newPayment.getAmount());
                creditCard.getAccount().setLastTopUpDate(dateFormat.format(date));
                payment.setStatus("Success");
            } else payment.setStatus("Failure");

        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(paymentDao.save(payment), HttpStatus.CREATED);
    }
}
