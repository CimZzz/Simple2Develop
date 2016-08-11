package com.virtualightning.simple2develop.quickdb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.virtualightning.library.simple2develop.quickdb.core.QuickDB;
import com.virtualightning.simple2develop.R;
import com.virtualightning.simple2develop.quickdb.entity.Entity1;
import com.virtualightning.simple2develop.quickdb.entity.User1;

import java.util.Calendar;
import java.util.List;

/**
 * Created by CimZzz on 16/8/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.4<br>
 * Description:<br>
 */
public class QuickDBActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);

        QuickDB.initQuickDB(this, R.raw.quickdb);

        addUser();

        addEntity();

        List<User1> user1s = QuickDB.getInstance().query(User1.class);
        List<Entity1> entity1s = QuickDB.getInstance().query(Entity1.class,"where id1=? and id2=?","1","2");

        for(User1 user1 : user1s)
            textView.setText(textView.getText() + user1.toString());
        for(Entity1 entity1 : entity1s)
            textView.setText(textView.getText() + entity1.toString());

    }

    private void addUser()
    {
        User1 user1 = new User1();
        user1.setName("这是User");
        user1.setBirth(Calendar.getInstance().getTime());
        user1.setBoy(true);
        user1.setWeight(15.5);
        user1.setId(101);

        QuickDB.getInstance().saveOrUpdate(user1);
    }

    private void addEntity()
    {
        Entity1 entity1 = new Entity1();
        entity1.setId1(1);
        entity1.setId2(2);

        QuickDB.getInstance().saveOrUpdate(entity1);
    }
}
