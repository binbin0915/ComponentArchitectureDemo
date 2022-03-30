package com.model.home.api;
import java.util.List;

public class MyData {

    private int code;
    private String msg;
    private List<Data> data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

    public static class List1 {

        private int id;
        private String img;
        private String url;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

    }
    public static class Data {

        private int kind;
        private String title;
        private List<List1> list;
        public void setKind(int kind) {
            this.kind = kind;
        }
        public int getKind() {
            return kind;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setList(List<List1> list) {
            this.list = list;
        }
        public List<List1> getList() {
            return list;
        }

    }

}