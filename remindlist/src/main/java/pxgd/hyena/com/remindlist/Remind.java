package pxgd.hyena.com.remindlist;

public class Remind {

    //备忘录ID
    private int mId;
    //备忘录文本内容
    private String mContent;
    //备忘录重要性（1=重要，0=不重要）
    private int mImportant;

    public Remind(int id, String content, int important) {
        mId = id;
        mImportant = important;
        mContent = content;
    }
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }
    public int getImportant() {
        return mImportant;
    }
    public void setImportant(int important) {
        mImportant = important;
    }
    public String getContent() {
        return mContent;
    }
    public void setContent(String content) {
        mContent = content;
    }
}
