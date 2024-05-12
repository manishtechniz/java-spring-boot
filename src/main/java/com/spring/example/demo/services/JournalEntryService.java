package com.spring.example.demo.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.bson.types.ObjectId;
import com.spring.example.demo.entity.JournalEntry;
import com.spring.example.demo.repositories.JournalEntryRepository;

@Component
public class JournalEntryService {
    @Autowired
    protected JournalEntryRepository journalEntryRepository;

    public JournalEntry saveEntry(JournalEntry journalEntry)
    {
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> listEntries()
    {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id)
    {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id)
    {
        journalEntryRepository.deleteById(id);
    }
}
