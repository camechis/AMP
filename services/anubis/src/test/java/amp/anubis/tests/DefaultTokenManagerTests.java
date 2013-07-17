package amp.anubis.tests;


import amp.anubis.core.NamedToken;
import amp.anubis.services.DefaultTokenManager;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.attribute.UserDefinedFileAttributeView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class DefaultTokenManagerTests {

    public static class GenerateTokenTests {

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_requestor_is_null() {
            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.generateToken(null);

            fail("Should have thrown an IllegalArgumentException but didn't.");
        }

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_username_is_null() {

            // create
            UserDetails mockUser = mock(UserDetails.class);

            // setup
            when(mockUser.getUsername()).thenReturn(null);

            // test
            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.generateToken(mockUser);

            // assert
            fail("Should have thrown an IllegalArgumentException but didn't.");
        }

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_username_is_empty() {

            // create
            UserDetails mockUser = mock(UserDetails.class);

            // setup
            when(mockUser.getUsername()).thenReturn("");

            // test
            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.generateToken(mockUser);

            // assert
            fail("Should have thrown an IllegalArgumentException but didn't.");
        }

        @Test
        public void generates_passwords_of_the_configured_length() {

            // create
            UserDetails mockUser = mock(UserDetails.class);
            DefaultTokenManager tokenManager18Length = new DefaultTokenManager();
            DefaultTokenManager tokenManager128Length = new DefaultTokenManager();

            // setup
            when(mockUser.getUsername()).thenReturn("testUser");
            tokenManager18Length.setPasswordSize(18);
            tokenManager128Length.setPasswordSize(128);

            // test
            NamedToken token18 = tokenManager18Length.generateToken(mockUser);
            NamedToken token128 = tokenManager128Length.generateToken(mockUser);

            byte[] decoded18 = Base64.decodeBase64(token18.getToken());
            byte[] decoded128 = Base64.decodeBase64(token128.getToken());

            // assert
            assertEquals(18, decoded18.length);
            assertEquals(128, decoded128.length);
        }
    }



    public static class VerifyTokenTests {

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_token_is_null() {

            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.verifyToken(null);

            fail("Should have thrown an IllegalArgumentException but didn't.");
        }

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_token_has_null_identity() {

            // create
            NamedToken fakeToken = new NamedToken(null, null);

            // test
            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.verifyToken(fakeToken);

            // assert
            fail("Should have thrown an IllegalArgumentException but didn't.");
        }

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_token_has_empty_identity() {

            // create
            NamedToken fakeToken = new NamedToken("", null);

            // test
            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.verifyToken(fakeToken);

            // assert
            fail("Should have thrown an IllegalArgumentException but didn't.");
        }


        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_token_has_null_token() {

            // create
            NamedToken fakeToken = new NamedToken("testUser", null);

            // test
            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.verifyToken(fakeToken);

            // assert
            fail("Should have thrown an IllegalArgumentException but didn't.");
        }

        @Test(expected = IllegalArgumentException.class)
        public void should_throw_IllegalArgumentException_if_token_has_empty_token() {

            // create
            NamedToken fakeToken = new NamedToken("testUser", "");

            // test
            DefaultTokenManager tokenManager = new DefaultTokenManager();
            tokenManager.verifyToken(fakeToken);

            // assert
            fail("Should have thrown an IllegalArgumentException but didn't.");
        }
    }
}
