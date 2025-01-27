import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        SeparateChainingHashTable<User> users = new SeparateChainingHashTable<>();
        SeparateChainingHashTable<Post> posts = new SeparateChainingHashTable<>();
        SeparateChainingHashTable<Entry> seenPosts = new SeparateChainingHashTable<>();
        File input = new File(args[0]);
        File output = new File(args[1]);
        // to write a file control the file whether it exists
        PrintStream stream;
        try {
            stream = new PrintStream(output);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        // to read the file control the file whether it exists
        Scanner scanner;
        try {
            scanner = new Scanner(input);
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }
        // While there is a line in input file, read the file. According to the reading call the appropriate method
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineSplit = line.split(" ");
            if (lineSplit[0].equals("create_user")){
                create_user((lineSplit[1]),stream,users);
            }
            if (lineSplit[0].equals("follow_user")){
                follow_user((lineSplit[1]),(lineSplit[2]),stream,users);
            }
            if (lineSplit[0].equals("unfollow_user")){
                unfollow_user((lineSplit[1]),(lineSplit[2]),stream,users);
            }
            if (lineSplit[0].equals("create_post")){
                create_post(lineSplit[1],lineSplit[2],lineSplit[3],users,stream,posts);
            }
            if (lineSplit[0].equals("see_post")){
                see_post((lineSplit[1]),(lineSplit[2]),stream,users,posts,seenPosts);
            }
            if (lineSplit[0].equals("see_all_posts_from_user")){
                see_all_posts_from_user((lineSplit[1]),(lineSplit[2]),stream,users,seenPosts);
            }
            if (lineSplit[0].equals("toggle_like")){
                toggle_like(lineSplit[1],lineSplit[2],stream,users,posts,seenPosts);
            }
            if (lineSplit[0].equals("generate_feed")){
                generate_feed((lineSplit[1]),Integer.parseInt(lineSplit[2]),stream,seenPosts,users);
            }
            if (lineSplit[0].equals("scroll_through_feed")){
                int number = Integer.parseInt(lineSplit[2]);
                int[] array = new int[number];
                for (int i=0;i<number;i++){
                    array[i]= Integer.parseInt(lineSplit[3+i]);
                }
                scroll_through_feed(lineSplit[1],number,array,stream,users,seenPosts);
            }
            if (lineSplit[0].equals("sort_posts")){
                sort_posts(lineSplit[1],users,stream);
            }
        }
    }
    // If there is no user with given Id create user, else log
    public static void create_user(String userId,PrintStream stream,SeparateChainingHashTable<User> hashTable){
        int index = hashTable.myHash(userId);    // If the user has been created before, the index that user should be in the table
        User user = hashTable.theLists[index].contains(userId);
        if (user!=null){
            stream.println("Some error occurred in create_user.");
        }
        else{
            User newUser = new User(userId);
            hashTable.insert(newUser);
            stream.println("Created user with Id "+userId+".");
        }
    }
    // controls that necessary conditions are satisfied for following, if they are satisfied follow the user
    public static void follow_user(String userId1,String userId2,PrintStream stream,SeparateChainingHashTable<User> hashTable){
        // If users have been created before, the indexes that users should be in the table
        int index1 = hashTable.myHash(userId1);
        int index2 = hashTable.myHash(userId2);
        // find users
        User user1 = hashTable.theLists[index1].contains(userId1);
        User user2 = hashTable.theLists[index2].contains(userId2);
        if (user1== null){
            stream.println("Some error occurred in follow_user.");
            return;
        }
        if (user2==null){
            stream.println("Some error occurred in follow_user.");
            return;
        }
        if (user1.equals(user2)){
            stream.println("Some error occurred in follow_user.");
            return;
        }
        if (user1.followed.contains(user2)){    // control that whether user1 is already following the user2
            stream.println("Some error occurred in follow_user.");
            return;
        }
        // if user1 and user2 exist, user1 is not following user2, and user1 is not user2 then user1 follows user2
        user1.followed.insert(user2);
        user1.followedUsers.add(user2);  // add user2 to users followed by user1
        stream.println(userId1 + " followed " + userId2+".");
    }
    // controls that necessary conditions are satisfied for unfollowing, if they are satisfied unfollow the user
    public static void unfollow_user(String userId1,String userId2,PrintStream stream,SeparateChainingHashTable<User> hashTable){
        // If users have been created before, the indexes that users should be in the table
        int index1 = hashTable.myHash(userId1);
        int index2 = hashTable.myHash(userId2);
        User user1 = hashTable.theLists[index1].contains(userId1);
        User user2 = hashTable.theLists[index2].contains(userId2);
        if (user1==null){
            stream.println("Some error occurred in unfollow_user.");
            return;
        }
        if (user2==null){
            stream.println("Some error occurred in unfollow_user.");
            return;
        }
        if (user1.equals(user2)){
            stream.println("Some error occurred in unfollow_user.");
            return;
        }
        boolean isFollowed = user1.followed.contains(user2);
        if (!isFollowed){
            stream.println("Some error occurred in unfollow_user.");
            return;
        }
        // if user1 and user2 exist, user1 is following user2, and user1 is not user2 then user1 unfollows user2
        user1.followed.remove(user2);
        user1.followedUsers.remove(user2);   // remove user2 from users followed by user1
        stream.println(userId1 + " unfollowed " + userId2+".");

    }
    // If there is no post with given Id and there is user with given Id create post, else log
    public static void create_post(String userId,String postId,String content,SeparateChainingHashTable<User> users,PrintStream stream,SeparateChainingHashTable<Post> posts){
        // If the user has been created before, the index that user should be in the table
        int index1 = users.myHash(userId);
        User user = users.theLists[index1].contains(userId);
        if (user==null){
            stream.println("Some error occurred in create_post.");
            return;
        }
        // If the post has been created before, the index that post should be in the table
        int index = posts.myHash(postId);
        Post post = posts.theLists[index].contains(postId);
        if(post != null){
            stream.println("Some error occurred in create_post.");
            return;
        }
        // create new post with given Id and then insert this to user's own posts
        Post newPost = new Post(postId,content,user);
        posts.insert(newPost);
        user.ownPosts.insert(newPost);
        stream.println(userId +" created a post with Id "+ postId+".");

    }
    // If there is a post and user with given Id , user see that post
    public static void see_post(String userId, String postId, PrintStream stream,SeparateChainingHashTable<User>users, SeparateChainingHashTable<Post> posts,SeparateChainingHashTable<Entry> seenPosts){
        // If the user has been created before, the index that user should be in the table
        int index1 = users.myHash(userId);
        User user = users.theLists[index1].contains(userId);
        if (user==null){
            stream.println("Some error occurred in see_post.");
            return;
        }
        // If the post has been created before, the index that post should be in the table
        int index = posts.myHash(postId);
        Post post = posts.theLists[index].contains(postId);
        if(post == null){
            stream.println("Some error occurred in see_post.");
            return;
        }
        else{
            // If user saw that post, mark that post as seen
            Entry entry = new Entry(userId,postId);
            seenPosts.markPostAsSeen(entry);
            stream.println(userId+" saw " +postId+ ".");
        }

    }
    public static  void see_all_posts_from_user(String userId1,String userId2,PrintStream stream,SeparateChainingHashTable<User> users,SeparateChainingHashTable<Entry> seenPosts){
        // If users have been created before, the indexes that users should be in the table
        int index1 = users.myHash(userId1);
        int index2 = users.myHash(userId2);
        User user1 = users.theLists[index1].contains(userId1);
        User user2 = users.theLists[index2].contains(userId2);
        if (user1==null){
            stream.println("Some error occurred in see_all_posts_from_user.");
            return;
        }
        if (user2==null){
            stream.println("Some error occurred in see_all_posts_from_user.");
            return;
        }
        for (int i =1;i<=user2.ownPosts.currentSize;i++){
            Entry entry = new Entry(userId1,user2.ownPosts.array[i].ID);
            // If user saw that post, mark that post as seen
            seenPosts.markPostAsSeen(entry);
        }
        stream.println(userId1+" saw all posts of "+ userId2+".");
    }
    // If that post is liked before unlike it, else like it
    public static void toggle_like(String userId, String postId, PrintStream stream,SeparateChainingHashTable<User> users,SeparateChainingHashTable<Post> posts,SeparateChainingHashTable<Entry> seenPosts){
        int index1 = users.myHash(userId);
        User user = users.theLists[index1].contains(userId);
        if (user==null){
            stream.println("Some error occurred in toggle_like.");
            return;
        }
        int index = posts.myHash(postId);
        Post post = posts.theLists[index].contains(postId);
        if(post == null){
            stream.println("Some error occurred in toggle_like.");
            return;
        }
        if (user.likedPosts.contains(post)){   // if that post is liked before, unlike it
            post.like= post.like-1;
            user.likedPosts.remove(post);
            stream.println(userId+ " unliked " +postId+".");
        }
        else{
            //if that post is not liked before, like it
            post.like =post.like+1;
            user.likedPosts.insert(post);
            Entry entry = new Entry(userId,postId);
            // If post is liked then this post has been seen.
            seenPosts.markPostAsSeen(entry);
            stream.println(userId+ " liked " +postId+".");
        }
    }
    // generate feed which has given number of post for user
    public static void generate_feed(String userId,int number,PrintStream stream,SeparateChainingHashTable<Entry> seenPosts,SeparateChainingHashTable<User> users) throws Exception {
        int index1 = users.myHash(userId);
        User user = users.theLists[index1].contains(userId);
        if (user==null){
            stream.println("Some error occurred in generate_feed.");
            return;
        }
        // for feed generation insert posts which are not seen before to heap to make it easier to choose based on the number of likes
        user.followedPosts = new BinaryHeap();
        for(int i = 0;i<user.followedUsers.size();i++){
            for (int j = 1;j<=user.followedUsers.get(i).ownPosts.currentSize;j++){
                Post post = user.followedUsers.get(i).ownPosts.array[j];
                Entry entry = new Entry(userId, post.ID);
                boolean isSeen = seenPosts.hasUserSeenPost(entry);
                if (!isSeen) {
                    user.followedPosts.insert(post);
                }
            }
        }
        int numberOfPost = user.followedPosts.currentSize;
        if (numberOfPost<number){
            stream.println("Feed for "+ userId+":");
            for (int i =0;i<numberOfPost;i++){
                Post post = user.followedPosts.array[i+1];
                stream.println("Post ID: "+ post.ID+", Author: "+post.user.ID+", Likes: "+post.like);
            }
            stream.println("No more posts available for "+userId+".");
        }
        else{
            stream.println("Feed for "+ userId+":");
            for (int i =0;i<number;i++){
                Post post = user.followedPosts.array[i+1];
                stream.println("Post ID: "+ post.ID+", Author: "+post.user.ID+", Likes: "+post.like);
            }
        }
        user.followedPosts = null;
    }
    // generate feed and user see that post in order by number of likes
    public static void scroll_through_feed(String userId, int number, int[] array, PrintStream stream, SeparateChainingHashTable<User> users, SeparateChainingHashTable<Entry> seenPosts) throws Exception {
        int index1 = users.myHash(userId);
        User user = users.theLists[index1].contains(userId);
        if (user==null){
            stream.println("Some error occurred in scroll_through_feed.");
            return;
        }
        // for feed generation insert posts which are not seen before to heap to make it easier to choose based on the number of likes
        user.followedPosts = new BinaryHeap();
        for(int i = 0;i<user.followedUsers.size();i++){
            int x = user.followedUsers.get(i).ownPosts.currentSize;
            for (int j = 1 ; j<=x ; j++){
                Post post = user.followedUsers.get(i).ownPosts.array[j];
                Entry entry = new Entry(user.ID, post.ID);
                boolean isSeen = seenPosts.hasUserSeenPost(entry);
                if (!isSeen) {
                    user.followedPosts.insert(post);
                }
            }
        }
        stream.println(userId+" is scrolling through feed:");
        int numberOfPost = user.followedPosts.currentSize;
        if (numberOfPost<number){
            for (int i =0;i<numberOfPost;i++){
                Post post = user.followedPosts.deleteMax();
                if (array[i]==0){
                    // user only sees post
                    Entry entry = new Entry(userId,post.ID);
                    seenPosts.insert(entry);
                    seenPosts.markPostAsSeen(entry);
                    stream.println(userId+" saw "+post.ID+" while scrolling.");
                }
                else{
                    // user sees post and likes it
                    Entry entry = new Entry(userId,post.ID);
                    seenPosts.insert(entry);
                    seenPosts.markPostAsSeen(entry);
                    post.like =post.like+1;
                    user.likedPosts.insert(post);
                    stream.println(userId+" saw "+post.ID+" while scrolling and clicked the like button.");
                }
            }
            stream.println("No more posts in feed.");
        }
        else{
            for (int i =0;i<number;i++){
                Post post = user.followedPosts.deleteMax();
                if (array[i]==0){
                    // user only sees post
                    Entry entry = new Entry(userId,post.ID);
                    seenPosts.markPostAsSeen(entry);
                    stream.println(userId+" saw "+post.ID+" while scrolling.");
                }
                else{
                    // user sees post and likes it
                    Entry entry = new Entry(userId,post.ID);
                    seenPosts.markPostAsSeen(entry);
                    post.like =post.like+1;
                    user.likedPosts.insert(post);
                    stream.println(userId+" saw "+post.ID+" while scrolling and clicked the like button.");
                }
            }
        }
        user.followedPosts = null;
    }
    // find user and sort its post from largest to smallest according to the number of likes
    public static void sort_posts(String userId,SeparateChainingHashTable<User> users,PrintStream stream) throws Exception {
        int index1 = users.myHash(userId);
        User user = users.theLists[index1].contains(userId);
        if (user==null){
            stream.println("Some error occurred in sort_posts.");
            return;
        }
        int postNumber = user.ownPosts.currentSize;
        if (postNumber ==0){
            stream.println("No posts from "+ userId+".");
            return;
        }
        // create new binary heap and copy the post because they are deleted sequentially while taking the largest
        BinaryHeap copy = new BinaryHeap();
        stream.println("Sorting "+userId+"'s posts:");
        for (int i = 1;i<=postNumber;i++){
            copy.insert(user.ownPosts.array[i]);
        }
        for (int i = 0;i<postNumber;i++){
            Post post = copy.deleteMax();
            stream.println(post.ID+", Likes: " +post.like);
        }
    }

}
