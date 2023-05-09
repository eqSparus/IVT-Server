package ru.example.ivtserver.security.details;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.example.ivtserver.entities.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Класс, представляющий собой реализацию {@link UserDetails} для Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouchUserDetails implements UserDetails {
    User user;

    public CouchUserDetails(User user) {
        this.user = user;
    }

    public static CouchUserDetails of(User user) {
        return new CouchUserDetails(user);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
