package com.pay1oad.homepage.config;

import com.pay1oad.homepage.model.board.Category;
import com.pay1oad.homepage.persistence.board.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("보안 소식"));
                categoryRepository.save(new Category("발표 자료 게시판"));
                categoryRepository.save(new Category("과제 제출 게시판"));
                categoryRepository.save(new Category("정보 게시판"));
            }
        };
    }
}
