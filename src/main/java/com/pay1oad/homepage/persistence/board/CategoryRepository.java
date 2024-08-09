package com.pay1oad.homepage.persistence.board;

import com.pay1oad.homepage.model.board.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
