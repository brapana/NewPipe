package org.schabi.newpipe.player.playback;

import android.os.Looper;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.player.Player;
import org.schabi.newpipe.player.playqueue.PlayQueue;
import org.schabi.newpipe.player.playqueue.SinglePlayQueue;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class PlayerMediaSessionTest {

    private static PlayerMediaSession playerMediaSession;

    private static Player player;

    @BeforeClass
    public static void instantiateErrorActivity() {
        // Looper thread creation necessary to use
        Looper.prepare();
    }


    /**
     * Tests PlayerMediaSession with a mocked version of the Player class, which is passed in
     * to the class. Tests that player.selectQueueItem() is called exactly once by
     * playerMediaSession's getCurrentPlayingIndex()
     */
    @Test
    public void testPlayerMediaSession() {

        // create mock of the Player class
        player = mock(Player.class);
        try {
            // create a PlayQueue with a single item
            final PlayQueue playQueue = new SinglePlayQueue(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=B41O9PO6Zw0"));

            // set the mocked player's getPlayQueue to return the above playQueue
            when(player.getPlayQueue()).thenReturn(playQueue);

            // create a PlayerMediaSession object and pass in the mocked player object
            playerMediaSession = new PlayerMediaSession(player);

            // call getCurrentPlayingIndex() and getQueueSize() and verify getPlayQueue()
            // is called exactly 4 times from these methods
            playerMediaSession.getCurrentPlayingIndex();
            playerMediaSession.getQueueSize();
            verify(player, times(4)).getPlayQueue();

            // invoke playItemAtIndex(0)
            playerMediaSession.playItemAtIndex(0);

            // verify that the above line causes the player's selectQueueItem method to be called
            // exactly once, specifically with the argument of playQueue's first item
            verify(player, times(1))
                    .selectQueueItem(playQueue.getItem(0));

        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ExtractionException e) {
            e.printStackTrace();
        }

    }

}
