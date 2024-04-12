package kr.co.farmstory.security;

import kr.co.farmstory.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class MyUserDetails implements UserDetails, OAuth2User {
    //entity  선언
    private User user;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPass();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

    @Override
    public boolean isAccountNonExpired() {
        //계정 만료 여부 (true = 만료 안됨, false = 만료됨)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 휴면 계정? 계정 잠김 여부(true: 잠김안됨 false: 잠김)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부 (3개월 마다 바꾸라는 그거)
        // true: 만료안됨 false: 만료
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부 (true: 활성화, false: 비활성화)
        // 회원 탈퇴시 비활성화 시켜야함
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
