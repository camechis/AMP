package amp.anubis.tests;

import java.util.ArrayList;
import amp.anubis.core.AttributedNamedToken;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributedNameTokenTests {

    @Test(expected = IllegalArgumentException.class)
    public void null_NamedToken_disallowed() {
        AttributedNamedToken token = new AttributedNamedToken(null, new ArrayList<String>());

        fail("An IllegalArgumentException should have been thrown, but it wasn't.");
    }

    @Test
    public void null_attributes_results_in_empty_collection() {
        AttributedNamedToken token = new AttributedNamedToken("testuser", "testpassword", null);

        assertNotNull(token.getAttributes());
        assertEquals(0, token.getAttributes().size());
    }
}
