package airlanetickets.model;


import airlanetickets.model.enumerations.Role;
import lombok.Data;
import org.junit.function.ThrowingRunnable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "arilinetickets_users")
public class User implements UserDetails, ThrowingRunnable {


    @Id
    private String username;

    private String password;

    private String name;

    private String surname;

    private  String email;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    @Enumerated(value = EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    public User() {
    }

    public User(String username, String password, String name,
                String surname,String email, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void run() throws Throwable {

    }

    @Override
    public String toString() {
        return "User{}";
    }
}
