package com.seyfullahpolat.rahatlaticisesler.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalpreloaders.widgets.CrystalPreloader;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seyfullahpolat.rahatlaticisesler.R;
import com.seyfullahpolat.rahatlaticisesler.adapter.CategoryDetailListAdapter;
import com.seyfullahpolat.rahatlaticisesler.adapter.CategoryListAdapter;
import com.seyfullahpolat.rahatlaticisesler.adapter.FavoriteListAdapter;
import com.seyfullahpolat.rahatlaticisesler.item.CategoryItem;
import com.seyfullahpolat.rahatlaticisesler.item.PlayerItem;
import com.seyfullahpolat.rahatlaticisesler.item.SoundItem;
import com.seyfullahpolat.rahatlaticisesler.services.RestClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import devlight.io.library.ntb.NavigationTabBar;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by seyfullahpolat on 15/07/2018.
 */

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Map<String, Object> map;
    CrystalPreloader favCrystalPreloader;
    CrystalPreloader categCrystalPreloader;
    private CategoryItem currentCategory;

    private RecyclerView fawRecyclerView;
    private RecyclerView categRecyclerView;
    private RecyclerView categDetsilRecyclerView;

    private Toolbar main_toolbar;
    private TextView title;

    private FavoriteListAdapter favoriteListAdapter;
    private CategoryListAdapter categoryListAdapter;
    private CategoryDetailListAdapter categoryDetailListAdapter;

    private ArrayList<SoundItem> categoryDetailList = new ArrayList<>();
    private ArrayList<SoundItem> favList = new ArrayList<>();
    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();
    private ArrayList<PlayerItem> playerItems = new ArrayList<>();

    ViewPager viewPager;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitViews();
        initUI();
    }

    // viewlere değer atanır
    private void setInitViews() {
        context = MainActivity.this;
        main_toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        title = (TextView) findViewById(R.id.category_name);

        sharedPref = getApplicationContext().getSharedPreferences("Teknasyon", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }


    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {

                final View view;
                if (position == 0) {
                    title.setText("Favoriler");
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.favorite_list_view, null, false);
                    fawRecyclerView = (RecyclerView) view.findViewById(R.id.favorite_list);
                    favCrystalPreloader = (CrystalPreloader) view.findViewById(R.id.progress);

                    try {
                        getFavorite();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    title.setText("Kitaplık");

                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.category_view, null, false);
                    categRecyclerView = (RecyclerView) view.findViewById(R.id.category_list);
                    categDetsilRecyclerView = (RecyclerView) view.findViewById(R.id.category_detail_list);
                    categCrystalPreloader = (CrystalPreloader) view.findViewById(R.id.progress);

                    getCategory();
                }

                container.addView(view);

                return view;
            }
        });


        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(getResources().getDrawable(R.drawable.ic_favorite_black_24dp),
                        Color.parseColor("#FF4081"))
                        .selectedIcon(MainActivity.this.getResources().getDrawable(R.drawable.ic_favorite_black_24dp))
                        .title("Favoriler")
                        .badgeTitle("F")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(getResources().getDrawable(R.drawable.ic_library_music_black_24dp),
                        Color.parseColor("#FF4081"))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_library_music_black_24dp))
                        .title("Kitaplık")
                        .badgeTitle("K")
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                categRecyclerView.setVisibility(View.VISIBLE);
                categDetsilRecyclerView.setVisibility(View.GONE);

                if (position == 0) title.setText("Favoriler");
                else {
                    title.setText("Kitaplık");
                    for (int i = 0; i < playerItems.size(); i++) {

                        if (playerItems.get(i).getSimpleExoPlayer().getPlayWhenReady()) {
                            playerItems.get(i).getSimpleExoPlayer().setPlayWhenReady(false);

                        }
                    }
                }
            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();

                        }
                    }, i * 100);
                }
            }
        }, 500);

    }

    // bellekte tutulan favoriler listesi çağrılır.
    private void getFavorite() throws IOException {
        Gson gson = new Gson();
        String json = sharedPref.getString("PlayList", "");
        Type type = new TypeToken<ArrayList<SoundItem>>() {
        }.getType();
        favList = gson.fromJson(json, type);
        try {
            if (favList == null)
                favList = new ArrayList<>();
            playerList();
            setFavorite();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // favorilere eklenen sesler view set edilir ; play_pause,  volume  ve favorilerden çıkarma işlemleri yapılır.
    private void setFavorite() throws IOException {
        favoriteListAdapter = new FavoriteListAdapter(favList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        fawRecyclerView.setLayoutManager(mLayoutManager);
        fawRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fawRecyclerView.setAdapter(favoriteListAdapter);
        favoriteListAdapter.setClickListener(new FavoriteListAdapter.ClickListener() {
            @Override
            public void onClickPlayPause(View view, int position, ImageView play_pause) {

                if (!playerItems.get(position).getSimpleExoPlayer().getPlayWhenReady()) {
                    playerItems.get(position).getSimpleExoPlayer().setPlayWhenReady(true);
                    playerItems.get(position).getSimpleExoPlayer().setRepeatMode(Player.REPEAT_MODE_ONE);
                    play_pause.setImageResource(R.drawable.ic_pause_black_24dp);
                    float volume = (float) (1 - (Math.log(100 - audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) / Math.log(100)));
                    playerItems.get(position).getSimpleExoPlayer().setVolume(volume);
                    //buttonPlayPauseBar.setImageResource(R.drawable.ic_pause_black_24dp);
                } else {
                    //
                    playerItems.get(position).getSimpleExoPlayer().setPlayWhenReady(false);
                    play_pause.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                }
            }

            @Override
            public void onClickVolume(int position, SeekBar seekBar, int i) {
                if (playerItems.get(position).getSimpleExoPlayer().getPlayWhenReady()) {
                    // playerItems.get(position).getSimpleExoPlayer().setVolume(i);
                    float volume = (float) (1 - (Math.log(100 - i) / Math.log(100)));
                    playerItems.get(position).getSimpleExoPlayer().setVolume(volume);
                }
            }

            @Override
            public void onClickFav(final int position, final ImageView play_pause) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(null);
                builder.setMessage(favList.get(position).getSound_name() + " Kaldır");
                builder.setPositiveButton("Kaldır", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        playerItems.get(position).getSimpleExoPlayer().setPlayWhenReady(false);
                        playerItems.get(position).getSimpleExoPlayer().stop();
                        favList.remove(position);
                        Gson gson = new Gson();
                        String json = gson.toJson(favList);
                        editor.putString("PlayList", json);
                        editor.commit();
                        favoriteListAdapter.notifyDataSetChanged();
                        play_pause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        for (int i = 0; i < playerItems.size(); i++) {

                            if (playerItems.get(i).getSimpleExoPlayer().getPlayWhenReady()) {
                                playerItems.get(i).getSimpleExoPlayer().setPlayWhenReady(false);

                            }
                        }
                        try {
                            getFavorite();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
    }

    // favorilere eklenen sesler için player oluşturulur
    public void playerList() {
        playerItems.clear();

        for (int i = 0; i < favList.size(); i++) {
            PlayerItem playerItem = new PlayerItem();
            try {
                playerItem.getPlayer().setDataSource(favList.get(i).getSound_url());

                DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
                        MainActivity.this,
                        null,
                        DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                );

                TrackSelector trackSelector = new DefaultTrackSelector();
                SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        renderersFactory,
                        trackSelector
                );

                String userAgent = Util.getUserAgent(this, "Play Audio");
                ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                        Uri.parse(favList.get(i).getSound_url()),
                        new DefaultDataSourceFactory(this, userAgent),
                        new DefaultExtractorsFactory(),
                        null,
                        null

                );
                simpleExoPlayer.prepare(mediaSource);
                playerItem.setSimpleExoPlayer(simpleExoPlayer);
                playerItems.add(playerItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // kategori listesi servisten çağırılır.
    private void getCategory() {
        categCrystalPreloader.setVisibility(View.VISIBLE);
        RestClient.Sound service = RestClient.getClient();
        Call<ArrayList<CategoryItem>> call = service.getCategory();
        call.enqueue(new Callback<ArrayList<CategoryItem>>() {
            @Override
            public void onResponse(Response<ArrayList<CategoryItem>> response) {
                if (response.body() != null) {
                    categoryItems = response.body();
                    Log.d("Sonuç Kontrol ", "" + response.body().toString());
                    try {
                        setCategory();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                categCrystalPreloader.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("ds", "");
                categCrystalPreloader.setVisibility(View.GONE);
            }
        });

    }

    //kategori listesi view set edilir.
    private void setCategory() throws IOException {
        categoryListAdapter = new CategoryListAdapter(categoryItems, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        categRecyclerView.setLayoutManager(mLayoutManager);
        categRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categRecyclerView.setAdapter(categoryListAdapter);
        categoryListAdapter.setClickListener(new CategoryListAdapter.ClickListener() {
            @Override
            public void onClickDetail(View view, int position) {
                currentCategory = categoryItems.get(position);
                categRecyclerView.setVisibility(View.GONE);
                categDetsilRecyclerView.setVisibility(View.VISIBLE);
                getCategoryDetail(currentCategory.getC_id());
                title.setText(currentCategory.getC_name());
            }
        });
    }

    // kategory detay categori id'si ile servisten çağrılır.
    private void getCategoryDetail(int c_id) {
        categCrystalPreloader.setVisibility(View.VISIBLE);
        HashMap map = new HashMap();
        map.put("c_id", c_id);
        RestClient.Sound service = RestClient.getClient();
        Call<ArrayList<SoundItem>> call = service.getCategoryDetail(map);
        call.enqueue(new Callback<ArrayList<SoundItem>>() {
            @Override
            public void onResponse(Response<ArrayList<SoundItem>> response) {

                categoryDetailList.clear();
                if (response.body() != null) {
                    categoryDetailList = response.body();
                    Log.d("Sonuç Kontrol ", "" + response.body().toString());
                    try {
                        setCategoryDetail();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    categoryDetailListAdapter.notifyDataSetChanged();
                }

                if (categoryDetailList.size() > 0)
                    categCrystalPreloader.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("ds", "");
                categCrystalPreloader.setVisibility(View.GONE);
            }
        });

    }

    //Kategory detayı view set edilir
    private void setCategoryDetail() throws IOException {
        categoryDetailListAdapter = new CategoryDetailListAdapter(categoryDetailList, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        categDetsilRecyclerView.setLayoutManager(mLayoutManager);
        categDetsilRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categDetsilRecyclerView.setAdapter(categoryDetailListAdapter);
        categoryDetailListAdapter.setClickListener(new CategoryDetailListAdapter.ClickListener() {
            @Override
            public void onClickFav(int position) {
                boolean control = false;
                for (int i = 0; i < favList.size(); i++) {
                    if (favList.get(i).getS_id() == categoryDetailList.get(position).getS_id()) {
                        control = true;
                        break;
                    }
                }
                if (!control) {
                    favList.add(categoryDetailList.get(position));
                    Gson gson = new Gson();
                    String json = gson.toJson(favList);
                    editor.putString("PlayList", json);
                    editor.commit();
                    favoriteListAdapter.notifyDataSetChanged();
                    playerList();
                    Toast.makeText(MainActivity.this, categoryDetailList.get(position).getSound_name() + " Favorilere Eklendi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, categoryDetailList.get(position).getSound_name() + "  Eklenmiş", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
