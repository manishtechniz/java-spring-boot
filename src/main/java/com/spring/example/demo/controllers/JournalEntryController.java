package com.spring.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.example.demo.entity.JournalEntry;
import com.spring.example.demo.services.JournalEntryService;

@RestController
@RequestMapping("journal")
public class JournalEntryController {
    @Autowired
    protected JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<?> listJournalEntries()
    {
        List<JournalEntry> list = new ArrayList<JournalEntry>();
        list.addAll(journalEntryService.listEntries());

        if (list.isEmpty()) {
            HashMap<String, Object> error = new HashMap<>();

            error.put("message", "Data not found");

            error.put("status", 404);

            return new ResponseEntity<HashMap>(error, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List>(list, HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId id)
    {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);

        if (journalEntry.isPresent()) {
            return new ResponseEntity<JournalEntry>(journalEntry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("create")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry)
    {
        journalEntry.setDate(LocalDateTime.now());

        try {
            return new ResponseEntity<JournalEntry>(journalEntryService.saveEntry(journalEntry), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id)
    {
        try {
            journalEntryService.deleteById(id);

            return new ResponseEntity<String>("Journal Entry has been deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry updateEntry)
    {
        Optional<JournalEntry> entry = journalEntryService.findById(id);

        if (! entry.isPresent()) {
            return new ResponseEntity<String>("Record not found", HttpStatus.BAD_REQUEST);
        }

        if (updateEntry.getContent() != null) {
            entry.get().setContent(updateEntry.getContent());
        }

        if (updateEntry.getTitle() != null) {
            entry.get().setTitle(updateEntry.getTitle());
        }

        journalEntryService.saveEntry(entry.get());

        return new ResponseEntity<JournalEntry>(entry.get(), HttpStatus.OK);
    }
}