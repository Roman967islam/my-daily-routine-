package com.example.mydailyroutine
import android.content.*
class BootReceiver:BroadcastReceiver(){override fun onReceive(c:Context,i:Intent){if(i.action==Intent.ACTION_BOOT_COMPLETED||i.action==Intent.ACTION_MY_PACKAGE_REPLACED){val p=c.getSharedPreferences("routine",0);MainDefaults.v.indices.forEach{id->val d=MainDefaults.v[id];Scheduler(c).set(Routine(id,p.getInt("h$id",d.hour),p.getInt("m$id",d.minute),p.getString("t$id",d.title)?:d.title,d.emoji,p.getBoolean("e$id",true)))}}}}
