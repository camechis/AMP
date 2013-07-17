package amp.anubis.ldap;

import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

import java.util.HashSet;
import java.util.Set;

public class IncludeDomainUsersLdapAuthoritiesPopulator extends DefaultLdapAuthoritiesPopulator {


    public IncludeDomainUsersLdapAuthoritiesPopulator(ContextSource contextSource, String groupSearchBase) {
        super(contextSource, groupSearchBase);
    }


    @Override
    protected Set<GrantedAuthority> getAdditionalRoles(DirContextOperations user, String username) {

        Set<GrantedAuthority> additionalAuthorities = new HashSet<GrantedAuthority>();

        // get the primaryGroupToken of Domain Users
        String primaryGroup = user.getStringAttribute("primaryGroupID");

        if (primaryGroup.equalsIgnoreCase("513")) {
            additionalAuthorities.add(new SimpleGrantedAuthority("Domain Users"));
        }

        return additionalAuthorities;
    }
}
