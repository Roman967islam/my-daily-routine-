package com.example.mydailyroutine
import android.app.*; import android.content.*; import android.os.*; import android.provider.Settings; import android.net.Uri; import android.widget.*; import java.util.*

data class Routine(val id:Int,var hour:Int,var minute:Int,var title:String,var emoji:String,var enabled:Boolean=true)

class MainActivity:Activity(){
 private val prefs by lazy{getSharedPreferences("routine",0)}
 private val ds=listOf(
  Routine(0,6,30,"ঘুম থেকে ওঠা","⏰"),Routine(1,6,40,"ফ্রেশ হওয়া","🧼"),Routine(2,7,0,"নামাজ/প্রার্থনা ও শান্ত সময়","🕌"),Routine(3,7,30,"হাঁটা/ব্যায়াম","🏃"),Routine(4,8,0,"নাশতা","🍳"),Routine(5,8,30,"আজকের কাজের পরিকল্পনা","📋"),Routine(6,9,0,"প্রধান কাজ/পড়াশোনা","💼"),Routine(7,13,0,"দুপুরের খাবার","🍛"),Routine(8,13,30,"বিশ্রাম","😌"),Routine(9,14,0,"কাজ/পড়াশোনা","💼"),Routine(10,17,0,"নাস্তা + বিশ্রাম","☕"),Routine(11,17,30,"হাঁটা/ব্যক্তিগত কাজ","🚶"),Routine(12,18,30,"প্রয়োজনীয় ফোন/অনলাইন কাজ","📱"),Routine(13,19,30,"নামাজ/প্রার্থনা","🕌"),Routine(14,20,0,"রাতের খাবার","🍽️"),Routine(15,20,30,"পরিবার/নিজের সময়","👨‍👩‍👦"),Routine(16,21,30,"ফোন কমানো","📱"),Routine(17,22,0,"ঘুমের প্রস্তুতি","🧼"),Routine(18,22,30,"হালকা পড়া/পরের দিনের পরিকল্পনা","📖"),Routine(19,23,0,"ঘুম","😴"))
 override fun onCreate(b:Bundle?){super.onCreate(b);if(Build.VERSION.SDK_INT>=33)requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"),10);ui()}
 private fun get(i:Int)=ds[i].let{Routine(i,prefs.getInt("h$i",it.hour),prefs.getInt("m$i",it.minute),prefs.getString("t$i",it.title)?:it.title,it.emoji,prefs.getBoolean("e$i",true))}
 private fun ui(){val box=LinearLayout(this).apply{orientation=1;setPadding(24,24,24,24)};box.addView(TextView(this).apply{text="My Daily Routine";textSize=28f});box.addView(TextView(this).apply{text="প্রতিটি কাজের সময় Edit করে বদলাতে পারবেন।"})
  ds.indices.forEach{i->val r=get(i);val row=LinearLayout(this).apply{orientation=0};val cb=CheckBox(this).apply{text="${r.emoji} ${"%02d:%02d".format(r.hour,r.minute)}  ${r.title}";isChecked=r.enabled;textSize=16f}
   cb.setOnCheckedChangeListener{_,v->prefs.edit().putBoolean("e$i",v).apply();Scheduler(this).set(get(i).copy(enabled=v))}
   row.addView(cb,LinearLayout.LayoutParams(0,-2,1f));row.addView(Button(this).apply{text="Edit";setOnClickListener{edit(i)}});box.addView(row)}
  box.addView(Button(this).apply{text="⏰ Alarm permission";setOnClickListener{if(Build.VERSION.SDK_INT>=31)startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,Uri.parse("package:$packageName")))}})
  box.addView(Button(this).apply{text="🔔 সব Alarm আবার সেট করুন";setOnClickListener{ds.indices.forEach{i->Scheduler(this).set(get(i))};Toast.makeText(this,"সব Alarm সেট হয়েছে",0).show()}})
  setContentView(ScrollView(this).apply{addView(box)})}
 private fun edit(i:Int){val r=get(i);val l=LinearLayout(this).apply{orientation=1};val t=EditText(this).apply{setText(r.title)};val p=TimePicker(this).apply{hour=r.hour;minute=r.minute;setIs24HourView(true)};l.addView(t);l.addView(p)
  AlertDialog.Builder(this).setTitle("Routine পরিবর্তন").setView(l).setPositiveButton("Save"){_,_->prefs.edit().putString("t$i",t.text.toString()).putInt("h$i",p.hour).putInt("m$i",p.minute).apply();Scheduler(this).set(get(i));ui()}.setNegativeButton("Cancel",null).show()}
}
private fun Routine.copy(enabled:Boolean)=Routine(id,hour,minute,title,emoji,enabled)
class Scheduler(val c:Context){fun set(r:Routine){val a=c.getSystemService(Context.ALARM_SERVICE) as AlarmManager;val pi=PendingIntent.getBroadcast(c,r.id,Intent(c,AlarmReceiver::class.java).putExtra("id",r.id).putExtra("title","${r.emoji} এখন ${r.title} করার সময়"),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE);if(!r.enabled){a.cancel(pi);return};val x=Calendar.getInstance().apply{set(Calendar.HOUR_OF_DAY,r.hour);set(Calendar.MINUTE,r.minute);set(Calendar.SECOND,0);set(Calendar.MILLISECOND,0);if(timeInMillis<=System.currentTimeMillis())add(Calendar.DAY_OF_YEAR,1)};if(Build.VERSION.SDK_INT>=31&&a.canScheduleExactAlarms())a.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,x.timeInMillis,pi)else a.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,x.timeInMillis,pi)}}
