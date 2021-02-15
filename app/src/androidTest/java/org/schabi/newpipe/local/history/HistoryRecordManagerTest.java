package org.schabi.newpipe.local.history;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.schabi.newpipe.database.history.model.StreamHistoryEntry;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.stream.StreamInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class HistoryRecordManagerTest {

    // Android context generated as required to access HistoryRecordManager.java
    private Context context = ApplicationProvider.getApplicationContext();
    private HistoryRecordManager historyManager = new HistoryRecordManager(context);
    private Flowable<List<StreamHistoryEntry>> history = historyManager.getStreamHistory();

    /**
     * Tests viewing two videos (plus one viewed twice between them) from an empty list while
     * sorting by last played. Ensures that first video is the most recently viewed (v=usNsCeOV4GM)
     * and that changing the sorting to highest viewed returns the highest viewed as the
     * first item (v=c-fvFv8uprA). Also tests that deleting the watch history returns an empty
     * list.
     */
    @Test
    public void fillSortByLastPlayed() {

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

        // view two videos once, and one video being viewed twice between them
        try {
            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=o5behEieBDA"))
                    .blockingGet();

            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=c-fvFv8uprA"))
                    .blockingGet();

            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=c-fvFv8uprA"))
                    .blockingGet();

            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=usNsCeOV4GM"))
                    .blockingGet();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ExtractionException e) {
            e.printStackTrace();
        }
        // get history list
        final List<StreamHistoryEntry> historyList = history.blockingFirst();

        // assert that the first youtube video in the list is the most recently watched
        assertEquals("https://www.youtube.com/watch?v=usNsCeOV4GM",
                historyList.get(0).component1().getUrl());


        // sort history list by most watched
        Collections.sort(historyList, (o1, o2) -> ((Long) o2.getRepeatCount())
                .compareTo(o1.getRepeatCount()));

        // assert that the first youtube video in the list is now the most viewed
        assertEquals("https://www.youtube.com/watch?v=c-fvFv8uprA",
                historyList.get(0).component1().getUrl());

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();


        // get the current history list and ensure its empty
        final List<StreamHistoryEntry> emptyHistoryList = history.blockingFirst();
        assertEquals(0, emptyHistoryList.size());

    }

    /**
     * Tests viewing two videos (plus one viewed twice between them) from an empty list while
     * sorting by most played. Ensures that first video is the highest viewed (v=c-fvFv8uprA)
     * and that changing the sorting to most recently viewed returns the most recently viewed as the
     * first item (v=usNsCeOV4GM). Also tests that deleting the watch history returns an empty
     * list.
     */
    @Test
    public void fillSortByMostPlayed() {

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

        // view two videos once, and one video being viewed twice between them
        try {
            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=o5behEieBDA"))
                    .blockingGet();

            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=c-fvFv8uprA"))
                    .blockingGet();

            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=c-fvFv8uprA"))
                    .blockingGet();

            historyManager.onViewed(
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=usNsCeOV4GM"))
                    .blockingGet();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ExtractionException e) {
            e.printStackTrace();
        }
        // get history list
        final List<StreamHistoryEntry> historyList = history.blockingFirst();

        // sort history list by most watched
        Collections.sort(historyList, (o1, o2) -> ((Long) o2.getRepeatCount())
                .compareTo(o1.getRepeatCount()));

        // assert that the first youtube video in the list is the most viewed
        assertEquals("https://www.youtube.com/watch?v=c-fvFv8uprA",
                historyList.get(0).component1().getUrl());


        // sort history list by most recently watched
        Collections.sort(historyList, (o1, o2) -> (o2.getAccessDate())
                .compareTo(o1.getAccessDate()));

        // assert that the first youtube video in the list is now the most recently watched
        assertEquals("https://www.youtube.com/watch?v=usNsCeOV4GM",
                historyList.get(0).component1().getUrl());


        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

        // get the current history list and ensure its empty
        final List<StreamHistoryEntry> emptyHistoryList = history.blockingFirst();

        // sort empty history list by most recently watched
        Collections.sort(emptyHistoryList, (o1, o2) -> (o2.getAccessDate())
                .compareTo(o1.getAccessDate()));

        assertEquals(0, emptyHistoryList.size());
    }

    /**
     * Tests an empty watch list beginning with sorting by last played
     * and ensures changing sort methods results in the same empty list
     * and that deleting the stream history also results in the same empty list.
     */
    @Test
    public void emptySortByBoth() {

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

        // assert that the list is empty
        assertEquals(0, history.blockingFirst().size());

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

        // get the current history list and assert that the list is still empty
        final List<StreamHistoryEntry> emptyHistoryList = history.blockingFirst();
        assertEquals(0, emptyHistoryList.size());

        // sort history list by most watched
        Collections.sort(emptyHistoryList, (o1, o2) -> ((Long) o2.getRepeatCount())
                .compareTo(o1.getRepeatCount()));

        // assert that the list is still empty
        assertEquals(0, emptyHistoryList.size());

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

        // get the current history list and assert that the list is still empty
        final List<StreamHistoryEntry> emptyHistoryList2 = history.blockingFirst();
        assertEquals(0, emptyHistoryList2.size());

        // sort current history list by most recently watched and assert the list is still empty
        Collections.sort(emptyHistoryList2, (o1, o2) -> (o2.getAccessDate())
                .compareTo(o1.getAccessDate()));
        assertEquals(0, emptyHistoryList2.size());
    }

}
