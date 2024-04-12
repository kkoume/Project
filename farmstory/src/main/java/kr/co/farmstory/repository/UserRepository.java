package kr.co.farmstory.repository;

import com.querydsl.core.Tuple;
import kr.co.farmstory.dto.UserDTO;
import kr.co.farmstory.dto.UserPageRequestDTO;
import kr.co.farmstory.entity.User;
import kr.co.farmstory.repository.custum.UserRepositoryCustom;
import kr.co.farmstory.repository.impl.UserRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

    // 이름과 이메일 찾기 (아이디 찾기용)
    public Optional<User> findUserByNameAndEmail(String name, String email);

    // 아이디와 이메일 찾기 (비밀번호 찾기용)
    public Optional<User> findUserByUidAndEmail(String uid, String email);

    public Page<Tuple> selectsUsers(UserPageRequestDTO userPageRequestDTO, Pageable pageable);
    public Tuple selectUser(String uid);

}