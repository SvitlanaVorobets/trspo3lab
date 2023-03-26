package com.example.lab3.contollers;

import com.example.lab3.dao.AccountDao;
import com.example.lab3.dao.CreditCardDao;
import com.example.lab3.dao.CustomerDao;
import com.example.lab3.dto.NewCreditCardDto;
import com.example.lab3.models.Account;
import com.example.lab3.models.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/credit-card")
public class CreditCardController {
    @Autowired
    CreditCardDao creditCardDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    CustomerDao customerDao;

    @GetMapping("")
    public ResponseEntity<Iterable<CreditCard>> findAll() {
        List<CreditCard> creditCard = creditCardDao.findAll();
        if (creditCardDao.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(creditCard, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> findById(@PathVariable("id") long id) {
        Optional<CreditCard> creditCard = creditCardDao.findById(id);
        return creditCard.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<CreditCard> createCreditCard(@RequestBody NewCreditCardDto newCreditCard){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 5);

        CreditCard creditCard = new CreditCard();
        creditCard.setCustomer(customerDao.findById(newCreditCard.getCustomer_id()).get());
        creditCard.setCardNumber(newCreditCard.getCardNumber());
        creditCard.setExpirationDate(String.valueOf(cal.getTime()));
        creditCard.setCardholderName(newCreditCard.getCardholderName());
        creditCard.setBillingAddress(newCreditCard.getBillingAddress());
        creditCard.setPaymentMethodType(newCreditCard.getPaymentMethodType());
        CreditCard created = creditCardDao.save(creditCard);

        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = new Date();
        Account account = new Account();
        account.setBalance(0.0);
        account.setBlocked(false);
        account.setLastTopUpDate(dateFormat.format(date));
        account.setCreditCard(created);
        accountDao.save(account);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CreditCard> updateCreditCard(@PathVariable("id") long id, @RequestBody NewCreditCardDto newCreditCard) {
        Optional<CreditCard> foundCreditCard = creditCardDao.findById(id);

        if (foundCreditCard.isPresent()) {
            CreditCard creditCard = foundCreditCard.get();
            creditCard.setCardholderName(newCreditCard.getCardholderName());
            creditCard.setBillingAddress(newCreditCard.getBillingAddress());
            creditCard.setPaymentMethodType(newCreditCard.getPaymentMethodType());
            return new ResponseEntity<>(creditCardDao.save(creditCard), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCreditCard(@PathVariable("id") long id) {
        try {
            creditCardDao.deleteById(id);
            accountDao.delete(accountDao.findByCardId(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
