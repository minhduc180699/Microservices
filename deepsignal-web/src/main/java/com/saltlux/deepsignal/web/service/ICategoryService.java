package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.domain.Category;
import java.util.List;

public interface ICategoryService extends GeneralService<Category, String> {
    public List<Category> findByTypeOrderByCodeAsc(Integer type);
}
