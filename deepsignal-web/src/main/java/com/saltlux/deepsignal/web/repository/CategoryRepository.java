package com.saltlux.deepsignal.web.repository;

import com.saltlux.deepsignal.web.domain.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    public List<Category> findByTypeOrderByCodeAsc(Integer type);
}
