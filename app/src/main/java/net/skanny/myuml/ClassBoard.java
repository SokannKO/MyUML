package net.skanny.myuml;

public class ClassBoard {
    private int no;
    private String title;
    private String content;
    private String date;
    private int hit;
    private String id;
    private String pw;


    public ClassBoard(){}

    public ClassBoard(int no, String title, String content, String date, int hit, String id, String pw) {
        super();
        this.no = no;
        this.title = title;
        this.content = content;
        this.date = date;
        this.hit = hit;
        this.id = id;
        this.pw = pw;

    }
    public int getNo() {
        return no;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {return content;}

    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getHit() {
        return hit;
    }
    public void setHit(int hit) {
        this.hit = hit;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }

}
