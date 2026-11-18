package com.animehoje.play.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Home AMOLED de 5 abas. Dados do catálogo e ações originais entram via Host.
 * APK de prévia é independente do Tomato, sem acesso ao player original.
 */
public final class AHPHomeView extends LinearLayout {
    private static final int BG=Color.BLACK,PANEL=0xff101014,PURPLE=0xff8727CC,
            TEXT=Color.WHITE,MUTED=0xffA6A6AE;

    public static final class Anime {
        public final String id,title,subtitle,artUrl;
        public Anime(String id,String title,String subtitle,String artUrl){
            this.id=id;this.title=title;this.subtitle=subtitle;this.artUrl=artUrl;
        }
    }

    public interface Host {
        List<Anime> featured();
        List<Anime> continueWatching();
        List<Anime> recentEpisodes();
        List<Anime> favorites();
        List<Anime> catalog();
        void search(String query);
        void openAnime(Anime anime);
        void openAniListAnime(String anilistId,String title);
        void openProfile();
        void openOriginalCatalog();
    }

    private final Host host;
    private static final LruCache<String,Bitmap> IMAGE_CACHE=new LruCache<>(64);
    private final Handler main=new Handler(Looper.getMainLooper());
    private final FrameLayout content;
    private final LinearLayout nav;
    private String tab="inicio";

    public AHPHomeView(Context context, Host controller) {
        super(context);
        this.host=controller;
        setOrientation(VERTICAL);setBackgroundColor(BG);setFitsSystemWindows(true);
        content=new FrameLayout(context);addView(content,new LayoutParams(-1,0,1));
        nav=new LinearLayout(context);nav.setGravity(Gravity.CENTER);
        nav.setBackgroundColor(0xff08080A);
        addView(nav,new LayoutParams(-1,dp(64)));
        open("inicio");
    }

    private int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+0.5f);}
    private GradientDrawable bg(int color,int radius,int border){
        GradientDrawable g=new GradientDrawable();
        g.setColor(color);g.setCornerRadius(dp(radius));
        if(border!=0)g.setStroke(dp(1),border);
        return g;
    }
    private TextView text(String s,int sp,int col,boolean bold){
        TextView t=new TextView(getContext());
        t.setText(s);t.setTextColor(col);t.setTextSize(sp);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        return t;
    }
    private Bitmap asset(String name) {
        try(InputStream in=getContext().getAssets().open(name)){
            return BitmapFactory.decodeStream(in);
        }catch(Exception ignored){return null;}
    }
    private List<Anime> list(List<Anime> a){return a==null?Collections.emptyList():a;}

    private void open(String route) {
        tab=route;
        content.removeAllViews();
        nav.removeAllViews();
        if("programacao".equals(route)){
            content.addView(new AHPProgramacaoView(getContext(),
                (id,title)->{if(host!=null)host.openAniListAnime(id,title);}),
                new FrameLayout.LayoutParams(-1,-1));
        } else if("perfil".equals(route)){
            LinearLayout group=stack();
            heading(group,"Perfil");
            hint(group,"Sua conta e configurações.");
            Button action=action("Abrir perfil");
            group.addView(action);
            action.setOnClickListener(v->{if(host!=null)host.openProfile();});
            content.addView(group);
        } else if("inicio".equals(route)) createHome();
        else if("catalogo".equals(route)) createCatalog();
        else createFavorites();

        String[][] items={
            {"inicio","⌂","Início"},
            {"catalogo","▦","Catálogo"},
            {"favoritos","♡","Favoritos"},
            {"programacao","▤","Programação"},
            {"perfil","○","Perfil"}
        };
        for(String[] item:items){
            LinearLayout block=new LinearLayout(getContext());
            block.setGravity(Gravity.CENTER);block.setOrientation(VERTICAL);
            TextView glyph=text(item[1],23,route.equals(item[0])?PURPLE:TEXT,false);
            block.addView(glyph);
            TextView name=text(item[2],10,route.equals(item[0])?PURPLE:MUTED,
                route.equals(item[0]));
            block.addView(name);
            nav.addView(block,new LayoutParams(0,-1,1));
            block.setOnClickListener(v->open(item[0]));
        }
    }

    private LinearLayout stack() {
        LinearLayout c=new LinearLayout(getContext());
        c.setOrientation(VERTICAL);c.setPadding(dp(15),dp(12),dp(15),dp(16));
        c.setBackgroundColor(BG);
        return c;
    }

    private LinearLayout scrollRoot() {
        ScrollView scroll=new ScrollView(getContext());
        scroll.setFillViewport(false);scroll.setVerticalScrollBarEnabled(false);
        LinearLayout body=stack();
        scroll.addView(body);
        content.addView(scroll,new FrameLayout.LayoutParams(-1,-1));
        return body;
    }

    private void heading(LinearLayout root,String s){
        TextView h=text(s,22,TEXT,true);
        h.setPadding(0,dp(18),0,dp(11));root.addView(h);
    }
    private void hint(LinearLayout root,String s){
        TextView h=text(s,13,MUTED,false);
        h.setPadding(0,0,0,dp(14));root.addView(h);
    }
    private Button action(String s){
        Button b=new Button(getContext());b.setText(s);
        b.setTextSize(14);b.setAllCaps(false);
        b.setTextColor(TEXT);b.setBackground(bg(PURPLE,14,0));
        return b;
    }
    private ImageView picture(String asset,int height){
        ImageView img=new ImageView(getContext());
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap bitmap=asset(asset);
        if(bitmap!=null)img.setImageBitmap(bitmap);
        img.setBackground(bg(PANEL,14,0));
        img.setLayoutParams(new LayoutParams(-1,dp(height)));
        return img;
    }
    private void search(LinearLayout root){
        EditText input=new EditText(getContext());
        input.setSingleLine(true);input.setTextSize(14);
        input.setTextColor(TEXT);input.setHintTextColor(MUTED);
        input.setHint("⌕   Buscar animes e episódios…");
        input.setPadding(dp(16),0,dp(16),0);
        input.setBackground(bg(PANEL,16,0xff3E3E45));
        LayoutParams lp=new LayoutParams(-1,dp(47));
        lp.bottomMargin=dp(14);root.addView(input,lp);
        input.setOnEditorActionListener((v,id,event)->{
            if(host!=null)host.search(input.getText().toString());
            return true;
        });
    }
    private void brand(LinearLayout root){
        LinearLayout top=new LinearLayout(getContext());
        top.setGravity(Gravity.CENTER_VERTICAL);
        ImageView logo=new ImageView(getContext());
        Bitmap bitmap=asset("ahp_logo.png");
        if(bitmap!=null)logo.setImageBitmap(bitmap);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        top.addView(logo,new LayoutParams(dp(44),dp(44)));
        TextView label=text("  ANIME HOJE\n  P L A Y",16,TEXT,true);
        top.addView(label,new LayoutParams(0,dp(50),1));
        TextView profile=text("●",22,PURPLE,true);
        top.addView(profile);profile.setOnClickListener(v->open("perfil"));
        LayoutParams lp=new LayoutParams(-1,dp(62));
        lp.bottomMargin=dp(2);root.addView(top,lp);
    }

    private void createHome(){
        LinearLayout root=scrollRoot();
        brand(root);search(root);
        FrameLayout hero=new FrameLayout(getContext());
        hero.setBackground(bg(PANEL,15,0xff33213A));
        hero.setClipToOutline(true);
        ImageView image=picture("ahp_hero.jpg",208);
        hero.addView(image,new FrameLayout.LayoutParams(-1,dp(208)));
        LinearLayout copy=new LinearLayout(getContext());
        copy.setOrientation(VERTICAL);
        copy.setPadding(dp(16),dp(15),0,dp(13));
        copy.addView(text("EM DESTAQUE",11,0xffD17AFF,true));
        copy.addView(text("Tensei Shitara\nSlime Datta Ken",21,TEXT,true));
        TextView shortText=text("Fantasia · Isekai",12,TEXT,false);
        shortText.setPadding(0,dp(8),0,dp(6));
        copy.addView(shortText);
        Button discover=action("Descobrir");
        copy.addView(discover,new LayoutParams(dp(135),dp(44)));
        hero.addView(copy,new FrameLayout.LayoutParams(-1,-2,Gravity.BOTTOM));
        discover.setOnClickListener(v->{
            if(host!=null)host.search("Tensei Shitara Slime Datta Ken");
        });
        root.addView(hero,new LayoutParams(-1,dp(208)));
        Button nativeLibrary=action("Abrir catálogo original");
        LayoutParams lpNative=new LayoutParams(-1,dp(44));
        lpNative.topMargin=dp(15);
        root.addView(nativeLibrary,lpNative);
        nativeLibrary.setOnClickListener(v->{if(host!=null)host.openOriginalCatalog();});
        section(root,"Continuar assistindo",host==null?null:host.continueWatching());
        section(root,"Novos episódios",host==null?null:host.recentEpisodes());
        section(root,"Minha lista",host==null?null:host.favorites());
        TextView week=text("Programação da semana   ›",18,TEXT,true);
        week.setPadding(0,dp(18),0,dp(12));
        root.addView(week);week.setOnClickListener(v->open("programacao"));
        LinearLayout days=new LinearLayout(getContext());
        for(String name:new String[]{"SEG","TER","QUA","QUI","SEX","SÁB","DOM"}){
            TextView day=text(name,10,TEXT,false);day.setGravity(Gravity.CENTER);
            day.setBackground(bg(PANEL,10,0xff3A3A41));
            LayoutParams lp=new LayoutParams(0,dp(39),1);lp.rightMargin=dp(3);
            days.addView(day,lp);
            day.setOnClickListener(v->open("programacao"));
        }
        root.addView(days);
        section(root,"Lançamentos",host==null?null:host.featured());
    }

    private void loadCover(ImageView image,String url){
        if(url==null||!url.startsWith("https://"))return;
        Bitmap cached=IMAGE_CACHE.get(url);
        if(cached!=null){image.setImageBitmap(cached);return;}
        new Thread(()->{
            HttpURLConnection con=null;
            try{
                con=(HttpURLConnection)new URL(url).openConnection();
                con.setConnectTimeout(6500);con.setReadTimeout(6500);
                try(InputStream in=con.getInputStream()){
                    Bitmap b=BitmapFactory.decodeStream(in);
                    if(b!=null){
                        IMAGE_CACHE.put(url,b);
                        main.post(()->{
                            if(url.equals(image.getTag()))image.setImageBitmap(b);
                        });
                    }
                }
            }catch(Exception ignored){}
            finally{if(con!=null)con.disconnect();}
        },"AHP-capa").start();
    }
    private void section(LinearLayout root,String name,List<Anime> items){
        heading(root,name);items=list(items);
        if(items.isEmpty()){
            TextView blank=text("Os títulos aparecerão após conectar o catálogo original.",12,MUTED,false);
            blank.setPadding(0,0,0,dp(8));root.addView(blank);return;
        }
        HorizontalScrollView scroll=new HorizontalScrollView(getContext());
        scroll.setHorizontalScrollBarEnabled(false);
        LinearLayout row=new LinearLayout(getContext());
        for(Anime anime:items){
            LinearLayout card=new LinearLayout(getContext());
            card.setOrientation(VERTICAL);
            ImageView thumb=new ImageView(getContext());
            thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumb.setBackground(bg(0xff17141C,12,0xff31213D));
            thumb.setTag(anime.artUrl);
            card.addView(thumb,new LayoutParams(dp(156),dp(102)));
            loadCover(thumb,anime.artUrl);
            TextView title=text(anime.title,13,TEXT,true);
            title.setMaxLines(2);title.setPadding(0,dp(7),0,0);
            card.addView(title);
            TextView sub=text(anime.subtitle==null?"":anime.subtitle,11,MUTED,false);
            card.addView(sub);
            LayoutParams lp=new LayoutParams(dp(156),-2);
            lp.rightMargin=dp(11);row.addView(card,lp);
            card.setOnClickListener(v->{if(host!=null)host.openAnime(anime);});
        }
        scroll.addView(row);root.addView(scroll);
    }
    private void createCatalog(){
        LinearLayout root=scrollRoot();heading(root,"Catálogo");search(root);
        Button openNative=action("Abrir catálogo do aplicativo");
        LayoutParams nativeLp=new LayoutParams(-1,dp(46));
        nativeLp.bottomMargin=dp(12);
        root.addView(openNative,nativeLp);
        openNative.setOnClickListener(v->{if(host!=null)host.openOriginalCatalog();});
        section(root,"Animes",host==null?null:host.catalog());
    }
    private void createFavorites(){
        LinearLayout root=scrollRoot();heading(root,"Minha lista");
        section(root,"Favoritos",host==null?null:host.favorites());
    }
    public void refreshData(){open(tab);}
}
