package com.digital.library.data.repository;

import com.digital.library.data.model.UserMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMemberRepository  extends JpaRepository<UserMember, Integer> {
}
