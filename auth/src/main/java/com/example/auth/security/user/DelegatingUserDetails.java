package com.example.auth.security.user;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author Arfat A. Chaus
 * since 2025-10-24
 */
public class DelegatingUserDetails extends User {

    private String totpSecret;
    private boolean totpEnabled = true;
    private boolean totpSetup = false;

    public DelegatingUserDetails(String username, @Nullable String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public String getTotpSecret() {
        return this.totpSecret;
    }

    public void setTotpSecret(String totpSecret) {
        this.totpSecret = totpSecret;
    }

    private boolean isTotpEnabled() {
        return this.totpEnabled;
    }

    public void setTotpEnabled(boolean totpEnabled) {
        Assert.notNull(totpEnabled, "totpEnabled must not be null");
        this.totpEnabled = totpEnabled;
    }

    public boolean isTotpSetup() {
        return totpSetup;
    }

    public void setTotpSetup(boolean totpSetup) {
        this.totpSetup = totpSetup;
    }


    public static Builder username(String username) {
        return getBuilder().username(username);
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String username;
        private String password;
        private List<GrantedAuthority> authorities = new ArrayList();
        private String totpSecret;
        private boolean totpEnabled = true;
        private boolean totpSetup = false;
        private PasswordEncoder passwordEncoder;

        public Builder username(String username) {
            Assert.notNull(username, "username must not be null");
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            Assert.notNull(password, "password must not be null");
            this.password = password;
            return this;
        }

        public Builder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList(roles.length);

            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> role + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }

            return this.authorities(authorities);
        }

        private Builder authorities(Collection authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            this.authorities = new ArrayList(authorities);
            return this;
        }

        public Builder totpSecret(String secret) {
            Assert.notNull(secret, "TOTP secret cannot be null");
            this.totpSecret = secret;
            return this;
        }

        public Builder totpSetup(boolean setupCompleted) {
            Assert.notNull(setupCompleted, "setupCompleted cannot be null");
            this.totpSetup = setupCompleted;
            return this;
        }


        public Builder totpEnabled(boolean totpEnabled) {
            Assert.notNull(totpEnabled, "totpEnabled cannot be null");
            this.totpEnabled = totpEnabled;
            return this;
        }

        public Builder passwordEncoder(PasswordEncoder encoder){
            this.passwordEncoder = encoder;
            return this;
        }
        public DelegatingUserDetails build() {
            String encodedPassword = this.password != null ? this.passwordEncoder.encode(this.password) : null;

            var delegatingUserDetails = new DelegatingUserDetails(this.username, encodedPassword, this.authorities);
            delegatingUserDetails.setTotpSetup(this.totpSetup);
            delegatingUserDetails.setTotpSecret(this.totpSecret);
            delegatingUserDetails.setTotpEnabled(this.totpEnabled);
            return delegatingUserDetails;
        }

    }


    @Override
    public String toString() {
        return "DelegatingUserDetails{" +
                "totpSecret='" + totpSecret + '\'' +
                ", totpEnabled=" + totpEnabled +
                ", totpSetup=" + totpSetup +
                "} " + super.toString();
    }
}
