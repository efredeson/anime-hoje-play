package com.animehoje.play.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Calendário independente consultando grade pública AniList GraphQL.
 * Exibe horários de TRANSMISSÃO, não garante disponibilidade para assistir.
 * Datas e horários apresentados em America/Porto_Velho (UTC-4).
 */
public final class AHPProgramacaoView extends LinearLayout {
    private static final int BG=Color.BLACK,INK=Color.WHITE,
        SECONDARY=0xffA5A5AF,PURPLE=0xff8727CC,PANEL=0xff111115;
    private static final TimeZone TZ=TimeZone.getTimeZone("America/Porto_Velho");
    private final Handler main=new Handler(Looper.getMainLooper());
    private final List<Air> airings=new ArrayList<>();
    private final List<String> dates=new ArrayList<>();
    private String selected="";
    private final LinearLayout dayButtons,list;
    private final TextView status;
    private boolean loading=false;
    private final OpenAnime openAnime;

    public interface OpenAnime {
        void onAnime(String anilistId, String title);
    }
    public AHPProgramacaoView(Context context, OpenAnime callback) {
        super(context);
        openAnime=callback;
        setOrientation(VERTICAL);setBackgroundColor(BG);
        int pad=dp(18);setPadding(pad,pad,pad,pad);
        TextView title=text("Programação",24,INK,true);
        addView(title);
        TextView help=text("Horários previstos · Porto Velho (UTC−4)",12,SECONDARY,false);
        help.setPadding(0,dp(5),0,dp(14));addView(help);
        LinearLayout top=new LinearLayout(context);
        top.setGravity(Gravity.CENTER_VERTICAL);
        status=text("Aguardando atualização",12,SECONDARY,false);
        top.addView(status,new LayoutParams(0,dp(40),1));
        Button refresh=new Button(context);
        refresh.setText("Atualizar");refresh.setTextColor(INK);
        refresh.setTextSize(12);refresh.setAllCaps(false);
        refresh.setBackground(background(PANEL,dp(12),0xff383841));
        top.addView(refresh,new LayoutParams(dp(100),dp(40)));
        addView(top);
        refresh.setOnClickListener(v->refresh());

        HorizontalScrollView scroll=new HorizontalScrollView(context);
        scroll.setHorizontalScrollBarEnabled(false);
        dayButtons=new LinearLayout(context);
        dayButtons.setOrientation(HORIZONTAL);scroll.addView(dayButtons);
        LayoutParams tabs=new LayoutParams(-1,dp(62));
        tabs.topMargin=dp(13);tabs.bottomMargin=dp(12);
        addView(scroll,tabs);

        ScrollView content=new ScrollView(context);
        content.setFillViewport(true);
        list=new LinearLayout(context);
        list.setOrientation(VERTICAL);content.addView(list);
        addView(content,new LayoutParams(-1,0,1));

        TextView note=text(
            "Fonte: AniList. Horários podem mudar e não garantem disponibilidade no aplicativo.",
            11,SECONDARY,false);
        note.setPadding(0,dp(13),0,dp(3));addView(note);
        initializeDays();draw();refresh();
    }
    private int dp(float n){
        return (int)(getResources().getDisplayMetrics().density*n+0.5f);
    }
    private GradientDrawable background(int color,int radius,int border){
        GradientDrawable d=new GradientDrawable();
        d.setColor(color);d.setCornerRadius(radius);
        if(border!=0)d.setStroke(dp(1),border);
        return d;
    }
    private TextView text(String v,int sp,int color,boolean bold){
        TextView t=new TextView(getContext());
        t.setText(v);t.setTextSize(sp);t.setTextColor(color);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        return t;
    }
    private String dateKey(Date d){
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        f.setTimeZone(TZ);return f.format(d);
    }
    private String dayName(Date d){
        SimpleDateFormat f=new SimpleDateFormat("EEE\ndd/MM",new Locale("pt","BR"));
        f.setTimeZone(TZ);return f.format(d).replace(".","");
    }
    private String time(Date d){
        SimpleDateFormat f=new SimpleDateFormat("HH:mm",new Locale("pt","BR"));
        f.setTimeZone(TZ);return f.format(d);
    }
    private void initializeDays(){
        Calendar c=Calendar.getInstance(TZ);
        c.set(Calendar.HOUR_OF_DAY,0);c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);c.set(Calendar.MILLISECOND,0);
        dates.clear();
        for(int i=0;i<7;i++){
            dates.add(dateKey(c.getTime()));
            c.add(Calendar.DAY_OF_MONTH,1);
        }
        selected=dates.get(0);
    }
    private void draw(){
        dayButtons.removeAllViews();
        for(String day:dates){
            Calendar cal=Calendar.getInstance(TZ);
            String[] p=day.split("-");
            cal.set(Integer.parseInt(p[0]),Integer.parseInt(p[1])-1,
                Integer.parseInt(p[2]));
            Button b=new Button(getContext());
            b.setAllCaps(false);b.setText(dayName(cal.getTime()));
            b.setTextSize(12);b.setTextColor(INK);
            b.setBackground(background(day.equals(selected)?PURPLE:PANEL,
                dp(10),day.equals(selected)?PURPLE:0xff33333B));
            LayoutParams lp=new LayoutParams(dp(72),dp(55));
            lp.rightMargin=dp(8);dayButtons.addView(b,lp);
            b.setOnClickListener(v->{selected=day;draw();});
        }
        list.removeAllViews();int count=0;
        for(Air a:airings){
            if(!dateKey(new Date(a.when*1000L)).equals(selected))continue;
            count++;
            LinearLayout item=new LinearLayout(getContext());
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setPadding(dp(12),dp(12),dp(12),dp(12));
            item.setBackground(background(PANEL,dp(12),0xff292933));
            TextView timeView=text(time(new Date(a.when*1000L)),16,PURPLE,true);
            item.addView(timeView,new LayoutParams(dp(64),-2));
            LinearLayout body=new LinearLayout(getContext());
            body.setOrientation(VERTICAL);
            body.addView(text(a.title,14,INK,true));
            body.addView(text("Episódio "+a.episode+" · horário previsto",
                12,SECONDARY,false));
            item.addView(body,new LayoutParams(0,-2,1));
            if(openAnime!=null)
                item.setOnClickListener(v->openAnime.onAnime(a.id,a.title));
            LayoutParams lp=new LayoutParams(-1,-2);
            lp.bottomMargin=dp(8);list.addView(item,lp);
        }
        if(count==0){
            TextView empty=text(loading?"Consultando a programação…":
                "Sem episódios listados para este dia.",14,SECONDARY,false);
            empty.setPadding(dp(8),dp(30),0,0);list.addView(empty);
        }
    }
    private static final class Air {
        String id,title;int episode;long when;
        Air(String i,String t,int e,long w){
            id=i;title=t;episode=e;when=w;
        }
    }
    public void refresh(){
        if(loading)return;
        loading=true;status.setText("Consultando horários…");draw();
        new Thread(()->{
            List<Air> found=new ArrayList<>();String error=null;
            try{
                Calendar base=Calendar.getInstance(TZ);
                base.set(Calendar.HOUR_OF_DAY,0);
                base.set(Calendar.MINUTE,0);
                base.set(Calendar.SECOND,0);
                base.set(Calendar.MILLISECOND,0);
                long from=base.getTimeInMillis()/1000-1;
                long to=from+7L*86400L+1;
                String query="query($page:Int,$from:Int,$to:Int){Page(page:$page,perPage:50){pageInfo{hasNextPage} airingSchedules(airingAt_greater:$from,airingAt_lesser:$to,sort:TIME){episode airingAt media{id title{romaji english} isAdult}}}}";
                for(int page=1;page<=4;page++){
                    JSONObject vars=new JSONObject();
                    vars.put("page",page);
                    vars.put("from",from);
                    vars.put("to",to);
                    JSONObject body=new JSONObject();
                    body.put("query",query);body.put("variables",vars);
                    HttpURLConnection conn=(HttpURLConnection)
                        new URL("https://graphql.anilist.co").openConnection();
                    try{
                        conn.setConnectTimeout(10000);
                        conn.setReadTimeout(10000);
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Type","application/json");
                        try(OutputStream out=conn.getOutputStream()){
                            out.write(body.toString().getBytes(StandardCharsets.UTF_8));
                        }
                        if(conn.getResponseCode()!=200)
                            throw new Exception("HTTP "+conn.getResponseCode());
                        StringBuilder raw=new StringBuilder();
                        try(BufferedReader r=new BufferedReader(
                            new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8))){
                            String line;
                            while((line=r.readLine())!=null)raw.append(line);
                        }
                        JSONObject response=new JSONObject(raw.toString())
                            .getJSONObject("data").getJSONObject("Page");
                        JSONArray rows=response.getJSONArray("airingSchedules");
                        for(int j=0;j<rows.length();j++){
                            JSONObject a=rows.getJSONObject(j);
                            JSONObject m=a.getJSONObject("media");
                            if(m.optBoolean("isAdult",false))continue;
                            JSONObject titles=m.optJSONObject("title");
                            String title=titles==null?"Anime":
                                titles.optString("english","");
                            if(title.isEmpty()||title.equals("null"))
                                title=titles==null?"Anime":
                                    titles.optString("romaji","Anime");
                            found.add(new Air(String.valueOf(m.optInt("id")),
                                title,a.optInt("episode"),a.optLong("airingAt")));
                        }
                        if(!response.getJSONObject("pageInfo")
                            .optBoolean("hasNextPage",false))break;
                    }finally{conn.disconnect();}
                }
            }catch(Exception ex){error=ex.getMessage();}
            final String resultError=error;
            main.post(()->{
                loading=false;
                if(resultError==null){
                    airings.clear();airings.addAll(found);
                    status.setText("Grade atualizada · hora local");
                }else{
                    status.setText("Sem conexão · tente atualizar");
                }
                draw();
            });
        },"AHP-calendario").start();
    }
}
