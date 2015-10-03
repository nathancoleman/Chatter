package data.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

import data.proxy.adapter.DDBUserProfileAdapter;
import data.structure.UserProfile;

/**
 * Tests the functionality of the DDBUserProfileStore class.
 */
public class DDBUserProfileStoreTest {
    
    private static final String USER_TABLE_NAME = "UserProfiles";
    private static final String NONEXISTENT_TABLE_NAME = "PigsThatHaveFlown";
    private static final String INVALID_SCHEMA_TABLE_NAME = "UserPosts";
    private static final String INVALID_SCHEMA_KEY_NAME = "PostID";
    private static final String TEST_USER_NAME = "TestUser";
    private DynamoDB ddbClient;
    
    /**
     * Creates the basic mock DynamoDB object.
     */
    @Before
    public void setup() {
        ddbClient = createMock(DynamoDB.class);
    }
    
    /**
     * Tests the constructor requirement for a non-null DynamoDB client.
     */
    @Test
    public void testConstructorNullClient() {
        boolean thrown = false;
        
        try {
            new DDBUserProfileStore(null, USER_TABLE_NAME);
        } catch (NullPointerException e) {
            thrown = true;
        }
        
        assertTrue("A null DynamoDB client was passed in, but no exception was thrown!", thrown);
    }
    
    /**
     * Tests the constructor requirement for a valid DynamoDB table name.
     */
    @Test
    public void testConstructorInvalidTableName() {
        expectNonExistentTable();
        replay(ddbClient);
        
        boolean thrown = false;
        
        try {
            new DDBUserProfileStore(ddbClient, NONEXISTENT_TABLE_NAME);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        
        assertTrue("An invalid table name was passed in, but no exception was thrown!", thrown);
        
        verify(ddbClient);
    }
    
    /**
     * Tests the constructor requirement for a valid DynamoDB table schema.
     */
    @Test
    public void testConstructorInvalidTableSchema() {
        Table tableToTest = expectInvalidSchemaTable();
        replay(tableToTest);
        replay(ddbClient);
        
        boolean thrown = false;
        
        try {
            new DDBUserProfileStore(ddbClient, INVALID_SCHEMA_TABLE_NAME);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        
        assertTrue("A table with an invalid schema was passed in, but no exception was thrown!",
                thrown);
        
        verify(tableToTest);
        verify(ddbClient);
    }
    
    /**
     * Tests successful instantiation.
     */
    @Test
    public void testSuccessfulConstruction() {
        Table tableToTest = expectValidTable();
        replay(tableToTest);
        replay(ddbClient);
        
        boolean thrown = false;
        
        try {
            new DDBUserProfileStore(ddbClient, USER_TABLE_NAME);
        } catch (Exception e) {
            thrown = true;
        }
        
        assertFalse("Valid arguments were passed in, but an exception was thrown!", thrown);
        
        verify(tableToTest);
        verify(ddbClient);
    }
    
    /**
     * Tests that the write() method of DDBUserProfileStore calls table.putItem() once.
     */
    @Test
    public void testWrite() {
        Table tableToTest = expectValidTable();
        expect(tableToTest.putItem(isA(Item.class))).andReturn(
                new PutItemOutcome(new PutItemResult())).once();
        replay(tableToTest);
        replay(ddbClient);
        
        DDBUserProfileStore store = new DDBUserProfileStore(ddbClient, USER_TABLE_NAME);
        store.write(new UserProfile("test"));
        
        verify(tableToTest);
        verify(ddbClient);
    }
    
    /**
     * Tests that the write() method of DDBUserProfileStore calls table.putItem() once.
     */
    @Test
    public void testGet() {
        Table tableToTest = expectValidTable();
        Item testUserItem = new Item().withPrimaryKey(DDBUserProfileAdapter.USER_ID_ATTRIBUTE,
                TEST_USER_NAME);
        expect(tableToTest.getItem(DDBUserProfileAdapter.USER_ID_ATTRIBUTE, TEST_USER_NAME))
                .andReturn(testUserItem).once();
        replay(tableToTest);
        replay(ddbClient);
        
        DDBUserProfileStore store = new DDBUserProfileStore(ddbClient, USER_TABLE_NAME);
        UserProfile testUserProfile = store.getProfile(TEST_USER_NAME);
        
        assertNotNull("The getProfile() method did not return a profile!", testUserProfile);
        assertEquals("The returned profile did not have the correct ID!", TEST_USER_NAME,
                testUserProfile.getId());
        
        verify(tableToTest);
        verify(ddbClient);
    }
    
    /**
     * Tests that the delete() method of DDBUserProfileStore calls table.deleteItem() once.
     */
    @Test
    public void testDelete() {
        Table tableToTest = expectValidTable();
        expect(tableToTest.deleteItem(DDBUserProfileAdapter.USER_ID_ATTRIBUTE, TEST_USER_NAME))
                .andReturn(new DeleteItemOutcome(new DeleteItemResult())).once();
        replay(tableToTest);
        replay(ddbClient);
        
        DDBUserProfileStore store = new DDBUserProfileStore(ddbClient, USER_TABLE_NAME);
        store.delete(TEST_USER_NAME);
        
        verify(tableToTest);
        verify(ddbClient);
    }
    
    /**
     * Performs setup to expect a valid table.
     * 
     * @return
     */
    private Table expectValidTable() {
        KeySchemaElement keySchema = new KeySchemaElement(DDBUserProfileAdapter.USER_ID_ATTRIBUTE,
                KeyType.HASH);
        TableDescription tableDesc = new TableDescription().withTableName(USER_TABLE_NAME)
                .withKeySchema(keySchema);
        
        Table userTable = createMock(Table.class);
        expect(userTable.describe()).andReturn(tableDesc).atLeastOnce();
        
        expect(ddbClient.getTable(USER_TABLE_NAME)).andReturn(userTable).atLeastOnce();
        
        return userTable;
    }
    
    /**
     * Performs setup to expect a non-existent table.
     */
    private void expectNonExistentTable() {
        expect(ddbClient.getTable(NONEXISTENT_TABLE_NAME)).andThrow(
                new ResourceNotFoundException("Resource Not Found!")).atLeastOnce();
    }
    
    /**
     * Performs setup to expect an existing table that has an invalid table schema.
     * 
     * @return
     */
    private Table expectInvalidSchemaTable() {
        KeySchemaElement invalidKeySchema = new KeySchemaElement(INVALID_SCHEMA_KEY_NAME,
                KeyType.HASH);
        TableDescription invalidTableDesc = new TableDescription().withTableName(
                INVALID_SCHEMA_TABLE_NAME).withKeySchema(invalidKeySchema);
        
        Table invalidTable = createMock(Table.class);
        expect(invalidTable.describe()).andReturn(invalidTableDesc).atLeastOnce();
        
        expect(ddbClient.getTable(INVALID_SCHEMA_TABLE_NAME)).andReturn(invalidTable).atLeastOnce();
        
        return invalidTable;
    }
}
