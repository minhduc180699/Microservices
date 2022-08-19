package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.service.ICategoryService;
import com.saltlux.deepsignal.web.service.dto.CategoryDTO;
import com.saltlux.deepsignal.web.util.ObjectMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category Management", description = "The category management API")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("/getCategoryFaq")
    @Operation(summary = "Get All Category By Type", tags = { "Category Management" })
    public List<CategoryDTO> getCategoryFaq(@RequestParam("type") Integer type) {
        return ObjectMapperUtils.mapAll(iCategoryService.findByTypeOrderByCodeAsc(type), CategoryDTO.class);
    }
}
