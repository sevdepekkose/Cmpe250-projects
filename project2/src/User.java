import java.util.ArrayList;

public class User {
    public String ID;
    public SeparateChainingHashTable<Post> likedPosts;
    public BinaryHeap ownPosts;
    public BinaryHeap followedPosts;
    public SeparateChainingHashTable<User> followed;
    public ArrayList<User> followedUsers;
    public User(String userId){
        ID = userId;
        followed = new SeparateChainingHashTable<>();
        ownPosts = new BinaryHeap();
        likedPosts =new SeparateChainingHashTable<>();
        followedUsers = new ArrayList<>();
    }
    @Override
    public String toString() {
        return "user ıd "+ ID;
    }
}
