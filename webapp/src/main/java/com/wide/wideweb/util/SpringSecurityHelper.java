package com.wide.wideweb.util;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * @author Attila Cs.
 */
@Component
public class SpringSecurityHelper implements Serializable {

    private static final long serialVersionUID = -1350167235501593117L;

    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;

    /**
     * Check whether if user has the given role.
     * 
     * @param role role to check.
     * @return
     */
    public static boolean hasRole(String role)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return false;
        } else {
            User user = (User) auth.getPrincipal();
            return user.getAuthorities().contains(new SimpleGrantedAuthority(role));

        }

    }

    /**
     * Authenticate User with a given name or password, if fails, throw exception.
     * 
     * @param user username plain text
     * @param password password plain text
     */
    public void authenticate(String user, String password) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, password);
        Authentication authenticatedUser = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

    }

    /**
     * Check whether the user is logged in.
     * 
     * @return boolean
     */
    public static boolean isAuthenticated() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() != "anonymousUser") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Log out the User.
     */
    public void unauthenticate() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            auth.setAuthenticated(false);
        }
    }
}
