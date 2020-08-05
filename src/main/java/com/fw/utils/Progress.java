package com.fw.utils;

import java.text.DecimalFormat;

/**
 * @author yqf
 */
public class Progress {

    private int processLength;
    private  StringBuilder body;
    private String oldPrint="";

    public Progress(int processLength) {
        this.processLength = processLength;
        initBody(processLength);
    }

    private  void initBody(int processLength){
        body = new StringBuilder("[");
        for (int i = 0; i < processLength; i++) {
            body.append("-");
            if(i==processLength-1){
                body.append("]");
            }
        }

    }

    public void setSchedule(Double progress){
        String format = new DecimalFormat("0").format(progress*processLength);

        for (int i = 1; i <=Integer.parseInt(format); i++) {
            body.replace(i,i+1,">");

        }

        print(body.toString()+Integer.parseInt(format)+"%");


    }

    private void print(String string){

        if(oldPrint.length()!=0){
            for (int i = 0; i < oldPrint.length(); i++) {
                System.out.print("\b");
            }
        }

        System.out.print(string);
        oldPrint = string;
    }

    public StringBuilder getBody() {
        return body;
    }
}
