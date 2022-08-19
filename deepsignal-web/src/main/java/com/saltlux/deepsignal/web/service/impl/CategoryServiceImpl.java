package com.saltlux.deepsignal.web.service.impl;

import com.saltlux.deepsignal.web.domain.Category;
import com.saltlux.deepsignal.web.repository.CategoryRepository;
import com.saltlux.deepsignal.web.service.ICategoryService;
import com.saltlux.deepsignal.web.service.dto.CategoryDTO;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(String s) {
        return categoryRepository.findById(s);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void remove(String s) {
        categoryRepository.deleteById(s);
    }

    @Override
    public List<Category> findByTypeOrderByCodeAsc(Integer type) {
        return categoryRepository.findByTypeOrderByCodeAsc(type);
    }
}
