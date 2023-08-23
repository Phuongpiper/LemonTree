package com.example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Supplier;
import com.example.service.DistributorService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/distributors")
public class DistributorRestController {
    @Autowired
    DistributorService distributorService;

     @GetMapping()
    public List<Supplier> getAll() {
        return distributorService.findAll();
    }

    @GetMapping("{id}")
    public Supplier getOne(@PathVariable("id") Integer id) {
        return distributorService.findById(id);
    }
    @PostMapping()
    public Supplier create(@RequestBody Supplier supplier) {
        return distributorService.create(supplier);
    }
    @PutMapping("{id}")
    public Supplier update(@PathVariable("id") Integer id, @RequestBody Supplier supplier) {
        return distributorService.update(supplier);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) {
        distributorService.deleteById(id);
    }
}

