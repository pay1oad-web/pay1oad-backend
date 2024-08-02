package com.pay1oad.homepage.persistence.board;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pay1oad.homepage.model.board.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}