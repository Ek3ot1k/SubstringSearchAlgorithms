import com.search.benchmark.algorithms.*;
import com.search.benchmark.hash.*;
import com.search.benchmark.stats.AlgorithmStats;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlgorithmTest {

    @Test
    public void testNaiveSearch() {
        String text = "abcabcabc";
        String pattern = "abc";

        NaiveSearch algorithm = new NaiveSearch();
        AlgorithmStats stats = algorithm.search(text, pattern);

        assertEquals(3, stats.getFoundCount());
        assertTrue(stats.getPositions().contains(0));
        assertTrue(stats.getPositions().contains(3));
        assertTrue(stats.getPositions().contains(6));
    }

    @Test
    public void testRabinKarpWithSimpleHash() {
        String text = "abcabcabc";
        String pattern = "abc";

        RabinKarpSearch algorithm = new RabinKarpSearch(new SimpleHash());
        AlgorithmStats stats = algorithm.search(text, pattern);

        assertEquals(3, stats.getFoundCount());
        assertEquals(0, stats.getCollisions()); // Должно быть без коллизий
    }

    @Test
    public void testPatternNotFound() {
        String text = "abcdefgh";
        String pattern = "xyz";

        NaiveSearch algorithm = new NaiveSearch();
        AlgorithmStats stats = algorithm.search(text, pattern);

        assertEquals(0, stats.getFoundCount());
        assertTrue(stats.getTimeMillis() >= 0);
    }

    @Test
    public void testEmptyPattern() {
        String text = "abcdefgh";
        String pattern = "";

        BoyerMooreSearch algorithm = new BoyerMooreSearch();
        AlgorithmStats stats = algorithm.search(text, pattern);

        assertEquals(0, stats.getFoundCount());
    }
}