package server.standalone;

import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import server.feed.FeedBuilder;
import server.matching.PercentMatchUserMatcher;
import server.matching.UserMatcher;
import data.proxy.PostStore;
import data.proxy.UserProfileStore;
import data.structure.Post;
import data.structure.UserProfile;

/**
 * This class acts as a standalone runner for the Chatter server. It also surfaces a simple CLI.
 */
public class SimpleServerCLI {
    
    /**
     * The set of allowed commands for this simple CLI.
     */
    enum COMMAND {
        LOGIN, SET, POST, FEED
    };
    
    private static final double DEFAULT_PERCENT_MATCH = 0.5;
    
    private static final Scanner in = new Scanner(System.in);
    private static UserProfile currentUser;
    
    /**
     * Main driver method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        final UserProfileStore userStore = new UserProfileStore();
        final PostStore postStore = new PostStore();
        final UserMatcher userMatcher = new PercentMatchUserMatcher(DEFAULT_PERCENT_MATCH);
        final Predicate<Post> postPredicate = new Predicate<Post>() {
            @Override
            public boolean test(Post post) {
                return true;
            }
        };
        final FeedBuilder feedBuilder = new FeedBuilder(postStore, userStore, userMatcher,
                postPredicate);
        
        printGreeting();
        
        while (true) {
            System.out.print(">> ");
            String[] line = parseCommand(in.nextLine());
            
            if (line.length == 0) {
                continue;
            }
            
            COMMAND cmd = null;
            try {
                cmd = COMMAND.valueOf(line[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(String.format("Invalid command \"%s\". Valid options are %s",
                        line[0], COMMAND.values().toString()));
                continue;
            }
            
            switch (cmd) {
            case LOGIN:
                login(userStore, line);
                break;
            case SET:
                setAttribute(userStore, line);
                break;
            case POST:
                post(postStore, line);
                break;
            case FEED:
                getFeed(feedBuilder);
                break;
            default:
            }
        }
    }
    
    /**
     * Prints the opening message.
     */
    private static void printGreeting() {
        System.out.println("=======================================");
        System.out.println("Welcome to the Chatter SimpleServerCLI!");
        System.out.println("=======================================");
        System.out.println("**Available commands (Group multi-word args with double quotes):");
        System.out.println(">> login {username}");
        System.out.println(">> set {attribute name} {attribute value}");
        System.out.println(">> post {post content}");
        System.out.println(">> feed");
        System.out.println("=======================================\r\n");
    }
    
    private static final int USER_ID_INDEX = 1;
    
    /**
     * Logs in with the specified user profile or creates a new profile if necessary.
     * 
     * @param userStore
     * @param line
     */
    private static void login(UserProfileStore userStore, String[] line) {
        String userId = line[USER_ID_INDEX];
        
        currentUser = userStore.getProfile(userId);
        
        if (currentUser == null) {
            System.out.println(String.format("Creating new user %s.", userId));
            userStore.write(new UserProfile(userId));
            currentUser = userStore.getProfile(userId);
        }
        
        System.out.println(String.format("Logged in as %s.", userId));
    }
    
    private static final int ATTRIBUTE_NAME_INDEX = 1;
    private static final int ATTRIBUTE_VALUE_INDEX = 2;
    
    /**
     * Set an attribute for the current user.
     * 
     * @param userStore
     * @param line
     */
    private static void setAttribute(UserProfileStore userStore, String[] line) {
        if (isLoggedIn()) {
            String attributeName = line[ATTRIBUTE_NAME_INDEX];
            String attributeValue = line[ATTRIBUTE_VALUE_INDEX];
            
            currentUser.setAttribute(attributeName, attributeValue);
            userStore.write(currentUser);
            
            System.out.println(String.format("Set attribute \"%s\" to \"%s\".", attributeName,
                    attributeValue));
        }
    }
    
    private static final int POST_CONTENT_INDEX = 1;
    
    /**
     * Create a post for the current user.
     * 
     * @param postStore
     * @param line
     */
    private static void post(PostStore postStore, String[] line) {
        if (isLoggedIn()) {
            String postContent = line[POST_CONTENT_INDEX];
            Post post = new Post(currentUser.getId(), postContent);
            
            postStore.write(post);
            
            System.out.println(String.format("Created post \"%s\".", postContent));
        }
    }
    
    /**
     * Prints the feed for the current user.
     * 
     * @param feedBuilder
     */
    private static void getFeed(FeedBuilder feedBuilder) {
        if (isLoggedIn()) {
            List<Post> feed = feedBuilder.getFeedForUser(currentUser);
            
            System.out.println(String.format("Feed for user %s:", currentUser.getId()));
            
            for (Post post : feed) {
                System.out.println(String.format("\r\n\t%s >> %s", post.getUser(),
                        post.getContent()));
            }
        }
    }
    
    /**
     * Checks to see if the user is currently logged in.
     * 
     * @return
     */
    private static boolean isLoggedIn() {
        if (currentUser == null) {
            System.out.println("Please log in first.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Parses the entire command into arguments, respecting double-quote groupings by user.
     * 
     * @param line
     * @return
     */
    private static String[] parseCommand(String line) {
        final String ARG_SEPARATOR = "~!~";
        StringBuilder finalArgs = new StringBuilder();
        
        // Split by double quotes
        String[] quoteSplit = line.split("\"");
        
        // The first segment must begin with the command itself, and
        // any other components in here are also single-word arguments.
        String[] starterSegment = quoteSplit[0].split("\\s");
        for (String component : starterSegment) {
            finalArgs.append(component);
            finalArgs.append(ARG_SEPARATOR);
        }
        
        // Now, iterate through the rest of the segments, flipping
        // quote groupings on and off as appropriate
        boolean insideQuotes = true;
        for (int i = 1; i < quoteSplit.length; i++) {
            if (insideQuotes) {
                // If we are currently inside quotes, store the entire segment as an argument.
                finalArgs.append(quoteSplit[i]);
                finalArgs.append(ARG_SEPARATOR);
            } else {
                // If we are not inside quotes, we want to split by whitespace.
                String[] singleWordArgs = quoteSplit[i].split("\\s");
                for (String component : singleWordArgs) {
                    finalArgs.append(component);
                    finalArgs.append(ARG_SEPARATOR);
                }
            }
            
            // We want to flip our insideQuotes flag for each quote-split seqment.
            insideQuotes = !insideQuotes;
        }
        
        return finalArgs.toString().split(ARG_SEPARATOR);
        
    }
}
