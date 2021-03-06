package fr.luc_nopre.projet;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TodoItem implements Serializable {

    public enum Tags {
        Faible("Faible"), Normal("Normal"), Important("Important");

        private String desc;
        Tags(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    private String label;
    private Tags tag;
    private int id;
    private boolean done;
    private int position;
    private LocalDateTime date;


    public TodoItem(Tags tag, String label, LocalDateTime date) {
        this.tag = tag;
        this.label = label;
        this.done = false;
        this.date = date;
    }


    public TodoItem(String label, Tags tag, boolean done, int position, LocalDateTime date) {
        this.label = label;
        this.tag = tag;
        this.done = done;
        this.position = position;
        this.date = date;
    }

    public static Tags getTagFor(String desc) {
        for (Tags tag : Tags.values()) {
            if (desc.compareTo(tag.getDesc()) == 0)
                return tag;
        }

        return Tags.Faible;
    }

    public String getLabel() {
        return label;
    }

    public Tags getTag() {
        return tag;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){ return this.id;}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int compareTo(TodoItem td){
        int res;
        if(this.getPosition() > td.getPosition()){
            res = 1;
        }else if(this.getPosition() < td.getPosition()){
            res = -1;
        }else{
            res = 0;
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoItem)) return false;
        TodoItem item = (TodoItem) o;
        return getPosition() == item.getPosition();
    }

}
