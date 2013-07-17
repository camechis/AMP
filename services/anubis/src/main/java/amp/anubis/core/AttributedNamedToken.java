package amp.anubis.core;

import java.util.ArrayList;
import java.util.Collection;

public class AttributedNamedToken {

    private NamedToken _namedToken;
    private Collection<String> _attributes;


    public String getIdentity() { return _namedToken.getIdentity(); }
    public String getToken() { return _namedToken.getToken(); }
    public NamedToken getNamedToken() { return _namedToken; }
    public Collection<String> getAttributes() { return _attributes; }


    public AttributedNamedToken(String username, String token, Collection<String> attributes) {
        this(new NamedToken(username, token), attributes);
    }

    public AttributedNamedToken(NamedToken namedToken, Collection<String> attributes) {

        if (null == namedToken)
            throw new IllegalArgumentException("namedToken cannot be null.");


        _namedToken = namedToken;
        _attributes = (null == attributes) ? new ArrayList<String>() : attributes;
    }


    public boolean hasAttribute(String attribute) {
        return _attributes.contains(attribute);
    }

    public boolean hasAtLeastOneAttribute(String... attributes) {

        boolean hasOne = false;

        for (String attribute : attributes) {
            if (_attributes.contains(attribute)) {
                hasOne = true;
                break;
            }
        }

        return hasOne;
    }
}
