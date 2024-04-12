package kr.co.farmstory.repository.custum;

import com.querydsl.core.Tuple;
import kr.co.farmstory.dto.UserDTO;
import kr.co.farmstory.dto.UserPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    public Page<Tuple> selectsUsers(UserPageRequestDTO userPageRequestDTO, Pageable pageable);
    public Tuple selectUser(String uid);

}
