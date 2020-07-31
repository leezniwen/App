package com.example.msiqlab;

public class little_supper_supper_assist {
    String content_assist;
    little_supper_supper lss = new little_supper_supper();
    public String assist_get_content(String content){
        content_assist = lss.getContent(content);
        return content_assist;
    }
    public String catch_content(){
        return content_assist;
    }
}
