package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Brand;
import com.example.jparepository.BrandRepository;
import com.example.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService{
     @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand create(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand findById(Integer id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        brandRepository.deleteById(id);
    }

    @Override
    public Brand update(Brand brand) {
        // Kiểm tra xem Brand có tồn tại trong cơ sở dữ liệu không
        Brand existingBrand = brandRepository.findById(brand.getId()).orElse(null);
        if (existingBrand != null) {
            // Thực hiện cập nhật thông tin của Brand
            existingBrand.setName(brand.getName());
            existingBrand.setResponsibleName(brand.getResponsibleName());
            existingBrand.setOrigin(brand.getOrigin());

            return brandRepository.save(existingBrand);
        } else {
            // Nếu Brand không tồn tại, không thực hiện cập nhật và trả về null hoặc thông báo lỗi tùy vào logic ứng dụng của bạn
            return null;
        }
    }
}
