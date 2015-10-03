package data.proxy.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.document.Item;

import data.structure.UserProfile;

/**
 * Tests the functionality of the DDBUserProfileAdapter class.
 */
public class DDBUserProfileAdapterTest {
    
    private static final String TEST_USER_ID = "TestUser";
    private static final String LOCATION_ATTRIBUTE_NAME = "location";
    private static final String LOCATION_ATTRIBUTE_VALUE = "New York, NY";
    
    private UserProfile testProfile;
    private Item testModel;
    
    /**
     * Creates the UserProfile object and corresponding DynamoDB model to be used in the subsequent
     * tests.
     */
    @Before
    public void setup() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(UserProfile.getNormalizedAttributeString(LOCATION_ATTRIBUTE_NAME),
                UserProfile.getNormalizedAttributeString(LOCATION_ATTRIBUTE_VALUE));
        testProfile = new UserProfile(TEST_USER_ID, attributes);
        testModel = new Item()
                .withPrimaryKey(DDBUserProfileAdapter.USER_ID_ATTRIBUTE, TEST_USER_ID).withMap(
                        DDBUserProfileAdapter.ATTRIBUTE_MAP_ATTRIBUTE, attributes);
    }
    
    /**
     * Tests that the toObject() method cannot be called without first setting the DynamoDB Item.
     */
    @Test
    public void testMissingModel() {
        DDBUserProfileAdapter adapter = new DDBUserProfileAdapter();
        
        boolean thrown = false;
        
        try {
            adapter.toObject();
        } catch (IllegalStateException e) {
            thrown = true;
        }
        
        assertTrue(
                "The adapter was asked to provide a UserProfile object without a DynamoDB Item, but no exception was thrown!",
                thrown);
    }
    
    /**
     * Tests that the toDBModel() method cannot be called without first setting the UserProfile
     * object.
     */
    @Test
    public void testMissingObject() {
        DDBUserProfileAdapter adapter = new DDBUserProfileAdapter();
        
        boolean thrown = false;
        
        try {
            adapter.toDBModel();
        } catch (IllegalStateException e) {
            thrown = true;
        }
        
        assertTrue(
                "The adapter was asked to provide a DynamoDB Item without a UserProfile object, but no exception was thrown!",
                thrown);
    }
    
    /**
     * Tests the conversion of a UserProfile object to a DynamoDB model.
     */
    @Test
    public void testToDBModel() {
        DDBUserProfileAdapter adapter = new DDBUserProfileAdapter().withObject(testProfile);
        Item result = adapter.toDBModel();
        
        assertEquals("The returned Item did not have the expected ID!",
                testModel.getString(DDBUserProfileAdapter.USER_ID_ATTRIBUTE),
                result.getString(DDBUserProfileAdapter.USER_ID_ATTRIBUTE));
        
        assertEquals("The returned Item did not have the expected attributes!",
                testModel.getMap(DDBUserProfileAdapter.ATTRIBUTE_MAP_ATTRIBUTE),
                result.getMap(DDBUserProfileAdapter.ATTRIBUTE_MAP_ATTRIBUTE));
    }
    
    /**
     * Tests the conversion of a DynamoDB model to a UserProfile object.
     */
    @Test
    public void testToObject() {
        DDBUserProfileAdapter adapter = new DDBUserProfileAdapter().withDBModel(testModel);
        UserProfile result = adapter.toObject();
        
        assertEquals("The returned UserProfile did not have the expected ID!", testProfile.getId(),
                result.getId());
        
        assertEquals("The returned UserProfile did not have the expected attributes!",
                testProfile.getAttributes(), result.getAttributes());
    }
}
