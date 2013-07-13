package amp.anubis.ldap;


import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;


/**
 * Author: jar349
 * Creation Date: 7/10/13
 */
public class NoSpacesFilterBasedLdapUserSearch extends FilterBasedLdapUserSearch {

    public NoSpacesFilterBasedLdapUserSearch(String searchBase, String searchFilter, BaseLdapPathContextSource contextSource) {
        super(searchBase, searchFilter, contextSource);
    }

    @Override
    public DirContextOperations searchForUser(String username) {

        // builder is not synchronized, which we don't need
        StringBuilder buffer = new StringBuilder();

        String[] tokens = username.split(",");
        for (String token : tokens) {
            buffer.append(token.trim());
            buffer.append(",");
        }

        // due to the way I loop, there's an extra comma which we need to remove
        buffer.deleteCharAt(buffer.length() - 1);

        // return the result of the parent class with the new de-spaced string
        return super.searchForUser(buffer.toString());
    }
}
