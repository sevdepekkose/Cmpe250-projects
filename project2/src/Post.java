public class Post implements Comparable<Post>{
    public User user;
    public String ID;
    public String content;
    public int like;
    public Post(String id,String content,User user){
        like=0;
        ID = id;
        this.content =content;
        this.user = user;
    }
    // compare posts with theirs number of like, if their number of like is equal compare according to their lexicographical order
    @Override
    public int compareTo(Post post){
        if (like>post.like){
            return 1;
        }
        else if (like<post.like){
            return -1;
        }
        else{
            if (ID.compareTo(post.ID)>0){
                return 1;
            } else if (ID.compareTo(post.ID)<0) {
                return -1;
            }
            return 0;
        }
    }
    @Override
    public String toString(){
        return ID;
    }


}
