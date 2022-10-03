package com.wangkai.myapplication.bean;

public class DataBean {
        private int id;
        private String channel;
        private String version;
        private String cv;
        private String content;
        private String down_url;
        private String filesize;
        private long create_time;
        private int kind;
        private int is_mustup;
        private int status;
        private String ip;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
        public String getChannel() {
            return channel;
        }

        public void setVersion(String version) {
            this.version = version;
        }
        public String getVersion() {
            return version;
        }

        public void setCv(String cv) {
            this.cv = cv;
        }
        public String getCv() {
            return cv;
        }

        public void setContent(String content) {
            this.content = content;
        }
        public String getContent() {
            return content;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }
        public String getDown_url() {
            return down_url;
        }

        public void setFilesize(String filesize) {
            this.filesize = filesize;
        }
        public String getFilesize() {
            return filesize;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }
        public long getCreate_time() {
            return create_time;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }
        public int getKind() {
            return kind;
        }

        public void setIs_mustup(int is_mustup) {
            this.is_mustup = is_mustup;
        }
        public int getIs_mustup() {
            return is_mustup;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
        public String getIp() {
            return ip;
        }
}
