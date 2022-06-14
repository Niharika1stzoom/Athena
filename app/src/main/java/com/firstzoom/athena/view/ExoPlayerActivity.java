package com.firstzoom.athena.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.firstzoom.athena.R;
import com.firstzoom.athena.databinding.ActivityExoPlayerBinding;
import com.firstzoom.athena.model.Item;
import com.firstzoom.athena.util.AppConstants;
import com.firstzoom.athena.util.AppUtil;
import com.firstzoom.athena.util.DateUtil;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.Util;




public class ExoPlayerActivity extends Activity implements StyledPlayerControlView.VisibilityListener {
    protected StyledPlayerView playerView;
    protected @Nullable
    ExoPlayer player;
    ActivityExoPlayerBinding binding;
    Boolean group;
    Item item;

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }



    protected void releasePlayer() {
        if (player != null) {

            player.release();
            player = null;
            playerView.setPlayer(/* player= */ null);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityExoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        item= (Item) intent.getSerializableExtra(AppConstants.KEY_ITEM);
        group=intent.getBooleanExtra(AppConstants.KEY_FOR_GROUP,false);
        playerView=binding.playerView;
        playerView.requestFocus();
        playerView.setControllerVisibilityListener(this);

        if(group!=null && !group)
            binding.textContainer.setVisibility(View.GONE);
        else{
            binding.textContainer.setVisibility(View.VISIBLE);
            binding.name.setText(item.getName());
            binding.itemTime.setText(DateUtil.getDisplayDate(item.getCreated_at()));
            binding.user.setText(item.getUpload_by());
            if (item.getDescription() == null)
                binding.description.setVisibility(View.GONE);
            else
                binding.description.setText(item.getDescription());
        }
       // playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    private void initializePlayer() {
        String filePath="";
        if(item!=null)
            filePath=item.getFile();
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        // Build the media item.
        try {
            Uri uri = Uri.parse(filePath);
            MediaItem mediaItem = MediaItem.fromUri(uri);
// Set the media item to be played.
            player.setMediaItem(mediaItem);
// Prepare the player.
            player.prepare();
// Start the playback.
            player.play();
        }catch (Exception e){
            Log.d("Video","Exc"+e.getLocalizedMessage());
        }

    }

    @Override
    public void onVisibilityChange(int visibility) {

    }


   /* private class PlayerErrorMessageProvider implements ErrorMessageProvider<PlaybackException> {

        @SuppressLint("StringFormatInvalid")
        @Override
        public Pair<Integer, String> getErrorMessage(PlaybackException e) {
            String errorString = getString(R.string.error_generic) +e.getLocalizedMessage();
            Throwable cause = e.getCause();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.codecInfo == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString =
                                getString(
                                        R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                    } else {
                        errorString =
                                getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                    }
                } else {
                    errorString =
                            getString(
                                    R.string.error_instantiating_decoder,
                                    decoderInitializationException.codecInfo.name);
                }
            }
            return Pair.create(0, errorString);
        }
    }*/
}