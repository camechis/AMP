package amp.anubis.services;

import amp.anubis.core.NamedToken;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class DefaultTokenManager implements ITokenManager {

    private static final Logger Log = LoggerFactory.getLogger(DefaultTokenManager.class);

    private static final int DEFAULT_PASSWORD_SIZE = 16;
    private static final int DEFAULT_CACHE_ACCESS_TIMEOUT_DURATION = 10;
    private static final TimeUnit DEFAULT_CACHE_ACCESS_TIMEOUT_UNITS = TimeUnit.MINUTES;
    private static final int DEFAULT_CACHE_ABSOLUTE_EXPIRATION_DURATION = 8;
    private static final TimeUnit DEFAULT_CACHE_ABSOLUTE_EXPIRATION_UNITS = TimeUnit.HOURS;

    private Cache<String, Collection<String>> _tokenCache;
    private int _passwordSize;
    private int _cacheAccessTimeoutDuration;
    private TimeUnit _cacheAccessTimeoutUnits;
    private int _cacheAbsoluteExpirationDuration;
    private TimeUnit _cacheAbsoluteExpirationUnits;


    public void setPasswordSize(int value) { _passwordSize = value; }
    public void setCacheAccessTimeoutDuration(int value) { _cacheAccessTimeoutDuration = value; }
    public void setCacheAccessTimeoutUnits(TimeUnit value) { _cacheAccessTimeoutUnits = value; }
    public void setCacheAbsoluteExpirationDuration(int value) { _cacheAbsoluteExpirationDuration = value; }
    public void setDefaultCacheAbsoluteExpirationUnits(TimeUnit value) { _cacheAbsoluteExpirationUnits = value; }


    public DefaultTokenManager() {

        _passwordSize = DEFAULT_PASSWORD_SIZE;
        _cacheAccessTimeoutDuration = DEFAULT_CACHE_ACCESS_TIMEOUT_DURATION;
        _cacheAccessTimeoutUnits = DEFAULT_CACHE_ACCESS_TIMEOUT_UNITS;
        _cacheAbsoluteExpirationDuration = DEFAULT_CACHE_ABSOLUTE_EXPIRATION_DURATION;
        _cacheAbsoluteExpirationUnits = DEFAULT_CACHE_ABSOLUTE_EXPIRATION_UNITS;

        _tokenCache = CacheBuilder.newBuilder()
                .expireAfterAccess(_cacheAccessTimeoutDuration, _cacheAccessTimeoutUnits)
                .expireAfterWrite(_cacheAbsoluteExpirationDuration, _cacheAbsoluteExpirationUnits)
                .build();
    }


    @Override
    public NamedToken generateToken(UserDetails requestor) throws IllegalArgumentException {

        // make sure we have a requestor identity
        if ( (null == requestor) || (null == requestor.getUsername()) || (requestor.getUsername().length() == 0) )
            throw new IllegalArgumentException("Username must be provided in order to generate a token.");


        // the actor for whom we're generating a token
        String identity = requestor.getUsername();
        Log.debug("Now generating a {} byte token for {}", _passwordSize, identity);

        // the 128 byte password we're going to generate
        byte[] password = new byte[_passwordSize];

        // generate a secure, random 128-bit (16 byte) password
        SecureRandom passwordGenerator = new SecureRandom();
        passwordGenerator.setSeed(passwordGenerator.generateSeed(16));
        passwordGenerator.nextBytes(password);

        Log.debug("Token successfully generated.  Encoding into Base64.");
        // use the identity and password to create a named token
        NamedToken token = new NamedToken(identity, Base64.encodeBase64String(password));
        Log.debug("Token successfully Base64 encoded");

        // add the token to the user's list of tokens
        this.cacheToken(token);
        Log.debug("Token added to authentication cache");

        return token;
    }

    @Override
    public boolean verifyToken(NamedToken token) throws IllegalArgumentException {

        // make sure we have something to verify
        if (this.isNullOrEmpty(token))
            throw new IllegalArgumentException("Cannot verify a null or empty token.");

        // get the identity of the token
        String identity = token.getIdentity();
        Log.debug("Verifying a token from {}", identity);

        boolean verified = false;

        Collection<String> tokens = _tokenCache.getIfPresent(identity);

        if (null != tokens) {
            if (tokens.contains(token.getToken())) {
                verified = true;
            }
        }

        Log.debug("Was user '{}' token valid? {}", verified);
        return verified;
    }


    public void cacheToken(NamedToken token) {

        Collection<String> tokens = _tokenCache.getIfPresent(token.getIdentity());

        // there may be no entry in the cache
        if (null == tokens) {

            // and if not, create it
            tokens = new ArrayList<String>();
            tokens.add(token.getToken());

            _tokenCache.put(token.getIdentity(), tokens);
        }
        else {

            // merely add this new token to the existing collection of tokens
            tokens.add(token.getToken());
        }
    }

    public boolean isNullOrEmpty(NamedToken token) {

        boolean nullOrEmpty = true;

        if (null != token) {
            String identity = token.getIdentity();
            String password = token.getToken();

            if ( (null != identity) && (identity.length() > 0) ) {

                if ( (null != password) && (password.length() > 0) ) {
                    nullOrEmpty = false;
                }
            }
        }

        return nullOrEmpty;
    }
}
