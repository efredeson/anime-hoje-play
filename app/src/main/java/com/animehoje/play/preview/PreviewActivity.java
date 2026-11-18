package com.animehoje.play.preview;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import com.animehoje.play.ui.AHPHomeView;
import java.util.Collections;
import java.util.List;

/**
 * Aplicativo independente para testar o layout do ANIME HOJE PLAY 3.1.
 * Não tem integração ao login/catálogo/player do Tomato.
 */
public final class PreviewActivity extends Activity implements AHPHomeView.Host {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(0xff000000);
        getWindow().setNavigationBarColor(0xff000000);
        setContentView(new AHPHomeView(this,this));
    }
    @Override public List<AHPHomeView.Anime> featured() {
        return Collections.emptyList();
    }
    @Override public List<AHPHomeView.Anime> continueWatching() {
        return Collections.emptyList();
    }
    @Override public List<AHPHomeView.Anime> recentEpisodes() {
        return Collections.emptyList();
    }
    @Override public List<AHPHomeView.Anime> favorites() {
        return Collections.emptyList();
    }
    @Override public List<AHPHomeView.Anime> catalog() {
        return Collections.emptyList();
    }
    @Override public void search(String term) {
        notice("A busca do catálogo original ainda precisa ser conectada.");
    }
    @Override public void openAnime(AHPHomeView.Anime anime) {
        notice("O player original ainda precisa ser conectado.");
    }
    @Override public void openAniListAnime(String id,String title) {
        notice(title+"\n\nHorário de transmissão consultado no AniList. "+
            "O catálogo e o player do Tomato ainda não estão conectados.");
    }
    @Override public void openProfile() {
        notice("O perfil original ainda precisa ser conectado.");
    }
    private void notice(String message) {
        new AlertDialog.Builder(this)
            .setTitle("ANIME HOJE PLAY — Prévia")
            .setMessage(message)
            .setPositiveButton("Entendi",null)
            .show();
    }
}
