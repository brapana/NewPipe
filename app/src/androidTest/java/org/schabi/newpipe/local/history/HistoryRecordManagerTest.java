package org.schabi.newpipe.local.history;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.schabi.newpipe.database.history.model.SearchHistoryEntry;
import org.schabi.newpipe.database.history.model.StreamHistoryEntry;
import org.schabi.newpipe.extractor.InfoItem;
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


    // BELOW ARE TESTS FOR EXPANDING COVERAGE

    /**
     * Tests deleting a particular history item as well as inserting a list of items at a time.
     */
    @Test
    public void insertAndDeleteStreamHistory() {
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

        // delete all history items from watchhistory
        historyManager.deleteStreamHistory(historyList).blockingGet();

        // get the current history list and ensure the list is now empty
        assertEquals(0, history.blockingFirst().size());

        // insert the stream history
        historyManager.insertStreamHistory(historyList).blockingGet();

        // get the current history list and ensure the items have been added back in
        final List<StreamHistoryEntry> newHistoryList = history.blockingFirst();
        assertEquals("https://www.youtube.com/watch?v=usNsCeOV4GM",
                newHistoryList.get(0).component1().getUrl());

        assertEquals("https://www.youtube.com/watch?v=c-fvFv8uprA",
                newHistoryList.get(1).component1().getUrl());

        assertEquals("https://www.youtube.com/watch?v=o5behEieBDA",
                newHistoryList.get(2).component1().getUrl());

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

    }


    /**
     * Tests adding search queries to the search history and deleting both a specific and
     * the entire search history.
     */
    @Test
    public void searchHistory() {
        // delete search history
        historyManager.deleteCompleteSearchHistory().blockingGet();


        // search for "John Lennon"
        historyManager.onSearched(0, "John Lennon").blockingGet();

        // search for "Beatles"
        historyManager.onSearched(0, "Beatles").blockingGet();

        // get related searches for an empty query (defaults to most recent searches)
        final Flowable<List<SearchHistoryEntry>> relatedSearches = historyManager
                .getRelatedSearches("", 5, 5);

        // assert the most recent search is "Beatles"
        assertEquals("Beatles", relatedSearches.blockingFirst().get(0).getSearch());
        // delete the "Beatles" search and assert the most recent search is now "John Lennon"
        historyManager.deleteSearchHistory("Beatles").blockingGet();
        assertEquals("John Lennon", relatedSearches.blockingFirst().get(0).getSearch());

        // delete search history
        historyManager.deleteCompleteSearchHistory().blockingGet();

        // assert search history is now empty
        assertEquals(0, historyManager
                .getRelatedSearches("", 5, 5)
                .blockingFirst().size());
    }

    /**
     * Tests adding and loading stream state objects in StreamInfo, ItemInfo,
     * and ItemInfo List form.
     */
    @Test
    public void streamStateHistory() {

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

        try {
            final StreamInfo vidInfo1 = StreamInfo
                    .getInfo("https://www.youtube.com/watch?v=o5behEieBDA");

            final StreamInfo vidInfo2 = StreamInfo
                    .getInfo("https://www.youtube.com/watch?v=usNsCeOV4GM");


            // save vid 1 stream state with 30 second progress time
            historyManager.saveStreamState(vidInfo1, 30000)
                    .blockingAwait();

            // save vid 2 stream state with 50 second progress time
            historyManager.saveStreamState(vidInfo2, 50000)
                    .blockingAwait();

            // assert that the stored progress time for video 1 is correct
            assertEquals(30000, historyManager.loadStreamState(vidInfo1).blockingGet()
                    .getProgressTime());

            // assert that the stored progress time for video 2 is correct
            assertEquals(50000, historyManager.loadStreamState(vidInfo2).blockingGet()
                    .getProgressTime());


            // get and save an InfoItem from the related videos of vid1
            final List<InfoItem> infoItemsList = vidInfo1.getRelatedStreams();
            final InfoItem vidRelated1 = infoItemsList.get(0);
            final StreamInfo vidInfo3 = StreamInfo
                    .getInfo(vidRelated1.getUrl());

            // save the StreamInfo representation of the InfoItem with 40 second progress time
            historyManager.saveStreamState(vidInfo3, 40000).blockingAwait();
            // assert that loading a stream state from an InfoItem returns the correct progress time
            assertEquals(40000,
                    historyManager.loadStreamState(vidRelated1).blockingGet()[0].getProgressTime());


            // assert that loading a stream state from a list of InfoItems containing the above
            // InfoItem returns the correct progress time
            assertEquals(40000,
                    historyManager.loadStreamStateBatch(infoItemsList)
                            .blockingGet().get(0).getProgressTime());

        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ExtractionException e) {
            e.printStackTrace();
        }

        // delete watchhistory
        historyManager.deleteWholeStreamHistory().blockingGet();

    }

}
