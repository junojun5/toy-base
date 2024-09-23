package com.xiilab.data_learnway.user.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xiilab.data_learnway.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// 이메일로 사용자 조회
	@Query("select u from User u where u.useStatus = 'U' and u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);

	// 이메일 중복검사
	Boolean existsByEmail(String userEmail);
}
