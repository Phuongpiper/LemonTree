package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Supplier;
import com.example.jparepository.SupplierRepository;
import com.example.service.DistributorService;

@Service
public class DistributorServiceImpl implements DistributorService{
    @Autowired
    private SupplierRepository supplierRepository;
    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier create(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier findById(Integer id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        supplierRepository.deleteById(id);
    }

    @Override
    public Supplier update(Supplier supplier) {
        // Kiá»ƒm tra xem Brand cĂ³ tá»“n táº¡i trong cÆ¡ sá»Ÿ dá»¯ liá»‡u khĂ´ng
        Supplier existingBrand = supplierRepository.findById(supplier.getId()).orElse(null);
        if (existingBrand != null) {
            // Thá»±c hiá»‡n cáº­p nháº­t thĂ´ng tin cá»§a Brand
            existingBrand.setName(supplier.getName());
            existingBrand.setAddress(supplier.getAddress());
            existingBrand.setContactInfo(supplier.getContactInfo());

            return supplierRepository.save(existingBrand);
        } else {
            // Náº¿u Brand khĂ´ng tá»“n táº¡i, khĂ´ng thá»±c hiá»‡n cáº­p nháº­t vĂ  tráº£ vá» null hoáº·c thĂ´ng bĂ¡o lá»—i tĂ¹y vĂ o logic á»©ng dá»¥ng cá»§a báº¡n
            return null;
        }
    }
}

