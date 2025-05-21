import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ABCSortingTest.class,
        BucketSortTest.class,
        HeapSortTest.class,
        RandomizedQuickSortTest.class
})
public class Project5Test {
    // This class combines all other tests in a suite so that you can run them all together
}