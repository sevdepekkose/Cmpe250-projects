public class Entry {
    public String userId;
    public String postId;
    public Entry(String userId, String postId){
        this.postId= postId;
        this.userId = userId;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entry entry = (Entry) obj;
        return userId.equals(entry.userId) && postId.equals(entry.postId);
    }
    @Override
    public int hashCode() {
        return userId.hashCode() + postId.hashCode();
    }
    @Override
    public String toString(){
        return userId +" " + postId;
    }

}
