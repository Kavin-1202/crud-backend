package com.example.crud.controller;

import com.example.crud.entity.Item;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.repo.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepo itemRepository;

    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElse(null);
        if(item==null){
            throw new ResourceNotFoundException("Item not found");
        }
        return ResponseEntity.ok(item);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handleSetNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElse(null);
        if(item==null){
            throw new ResourceNotFoundException("Item not found");
        }
        item.setName(itemDetails.getName());
        item.setTopic(itemDetails.getTopic());

        final Item updatedItem = itemRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteItem(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElse(null);
        if(item==null){
            throw new ResourceNotFoundException("Item not found");
        }
        itemRepository.delete(item);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
