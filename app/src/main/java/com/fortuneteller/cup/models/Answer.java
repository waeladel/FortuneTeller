package com.fortuneteller.cup.models;

import android.text.TextUtils;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Answer {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String message;
    //@ColumnInfo(name = "If i want to change the name of the column")
    private int position;
    private long created;

    public Answer(String message, int position) {
        this.message = message;
        this.position = position;
        this.created = System.currentTimeMillis();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;

        Log.d("Notifications equals", "Answer equals . Message ="+message +" answer1.Message= "+answer1.message + " value=" + TextUtils.equals(message, answer1.message));
        Log.d("Notifications equals", "Answer equals . created ="+created +" answer1.Message= "+answer1.created + " value=" + (created == answer1.created) );

        return (TextUtils.equals(message, answer1.message)) &&
                (position == answer1.position) &&
                (created == answer1.created);

        //sent == answer1.sent || (sent!=null && sent.equals(answer1.sent)
    }

    @Override
    public int hashCode() {
        //return Objects.hash(senderName, senderAvatar, seen, sent);
        int result = 1;
        result = 31 * result + (message == null ? 0 : message.hashCode());
        result = 31 * result + (position == 0 ? 0 : 1);
        result = 31 * result + (created == 0 ? 0 : 1);
        return result;
    }
}
