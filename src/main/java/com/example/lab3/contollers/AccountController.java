package com.example.lab3.contollers;

import com.example.lab3.dao.AccountDao;
import com.example.lab3.models.Account;
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
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountDao accountDao;

    @GetMapping("")
    public ResponseEntity<Iterable<Account>> findAll() {
        List<Account> accounts = accountDao.findAll();
        if (accountDao.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(@PathVariable("id") long id) {
        Optional<Account> account = accountDao.findById(id);
        return account.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/block/{id}")
    public ResponseEntity<Account> blockById(@PathVariable("id") long id) {
        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = new Date();

        Optional<Account> account = accountDao.findById(id);
        account.get().setBlocked(true);
        account.get().setLastTopUpDate(dateFormat.format(date));
        return new ResponseEntity<>(accountDao.save(account.get()), HttpStatus.CREATED);
    }

    @GetMapping("/admin/unblock/{id}")
    public ResponseEntity<Account> unblockById(@PathVariable("id") long id) {
        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = new Date();

        Optional<Account> account = accountDao.findById(id);
        account.get().setBlocked(false);
        account.get().setLastTopUpDate(dateFormat.format(date));
        return new ResponseEntity<>(accountDao.save(account.get()), HttpStatus.CREATED);
    }

    @PutMapping("/add/{id}")
    public ResponseEntity<Account> addMoney(@PathVariable("id") long id, @RequestBody String money) {
        DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = new Date();

        Optional<Account> foundAccount = accountDao.findById(id);

        if (foundAccount.isPresent() && !foundAccount.get().isBlocked()) {
            foundAccount.get().setLastTopUpDate(dateFormat.format(date));
            foundAccount.get().setBalance(foundAccount.get().getBalance() + Double.parseDouble(money));
            return new ResponseEntity<>(accountDao.save(foundAccount.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
