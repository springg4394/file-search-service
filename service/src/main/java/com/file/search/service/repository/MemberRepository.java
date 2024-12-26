package com.file.search.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.search.service.model.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

}
