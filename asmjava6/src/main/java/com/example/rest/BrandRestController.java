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

import com.example.entity.Brand;
import com.example.service.BrandService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/brands")
public class BrandRestController {
    @Autowired
    BrandService brandService;

    @GetMapping()
    public List<Brand> getAll() {
        return brandService.findAll();
    }

    @GetMapping("{id}")
    public Brand getOne(@PathVariable("id") Integer id) {
        return brandService.findById(id);
    }
    @PostMapping()
    public Brand create(@RequestBody Brand brand) {
        return brandService.create(brand);
    }
    @PutMapping("{id}")
    public Brand update(@PathVariable("id") Integer id, @RequestBody Brand brand) {
        return brandService.update(brand);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) {
        brandService.deleteById(id);
    }
}
