package jatin.demoapps.com.exoplayer2udpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.google.android.exoplayer2.util.TimestampAdjuster;

import static com.google.android.exoplayer2.extractor.ts.TsExtractor.MODE_SINGLE_PMT;

public class MainActivity extends AppCompatActivity {
    PlayerView playerView;
    Button playVideoBtn;
    TextView videoURL;
    private SimpleExoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.player_view);
        playVideoBtn = findViewById(R.id.playVideo);
        videoURL = findViewById(R.id.url);
        playVideoBtn.setOnClickListener(v -> initPlayer(videoURL.getText().toString()));
    }

    private void initPlayer(String url){
        if (url.isEmpty())
            Toast.makeText(this,"Please enter a url",Toast.LENGTH_SHORT).show();
        Uri videoUri = Uri.parse(url);

        //Create a default TrackSelector
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this, new AdaptiveTrackSelection.Factory());

        //Create the player
        player = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();

        // set player in playerView
        playerView.setPlayer(player);
        playerView.requestFocus();

        //Create default UDP Datasource
        DataSource.Factory factory = () -> new UdpDataSource(3000, 100000);
        ExtractorsFactory tsExtractorFactory = () -> new TsExtractor[]{new TsExtractor(MODE_SINGLE_PMT,
                new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory())};
        MediaItem mediaItem = new MediaItem.Builder().setUri(videoUri).build();
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(factory,tsExtractorFactory).createMediaSource(mediaItem);
        player.addMediaSource(mediaSource);
        player.prepare();

        // start play automatically when player is ready.
        player.setPlayWhenReady(true);
    }
}
