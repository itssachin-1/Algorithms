/**
 * gradle test --info --tests
 * "com.williamfiset.algorithms.datastructures.segmenttree.SumQueryAssignUpdateSegmentTreeTest"
 */
package com.williamfiset.algorithms.datastructures.segmenttree;

import static com.google.common.truth.Truth.assertThat;

import com.williamfiset.algorithms.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class SumQueryAssignUpdateSegmentTreeTest {

  static int ITERATIONS = 100;

  @Before
  public void setup() {}

  @Test
  public void testSumQuery() {
    long[] values = {1, 2, 3, 4, 5};
    SumQueryAssignUpdateSegmentTree st = new SumQueryAssignUpdateSegmentTree(values);

    assertThat(st.rangeQuery1(0, 1)).isEqualTo(3);
    assertThat(st.rangeQuery1(2, 2)).isEqualTo(3);
    assertThat(st.rangeQuery1(0, 4)).isEqualTo(15);
  }

  @Test
  public void testAllSumQueries() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    SumQueryAssignUpdateSegmentTree st = new SumQueryAssignUpdateSegmentTree(ar);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        long bfSum = bruteForceSum(ar, i, j);
        long segTreeSum = st.rangeQuery1(i, j);
        assertThat(bfSum).isEqualTo(segTreeSum);
      }
    }
  }

  @Test
  public void testSimpleAdditionRangeUpdate() {
    //           0, 1, 2, 3, 4
    long[] ar = {1, 2, 1, 2, 1};
    SumQueryAssignUpdateSegmentTree st = new SumQueryAssignUpdateSegmentTree(ar);

    // Do multiple range updates
    st.rangeUpdate1(0, 1, 5);
    st.rangeUpdate1(3, 4, 2);
    st.rangeUpdate1(0, 4, 3);

    // Point queries
    assertThat(st.rangeQuery1(0, 0)).isEqualTo(1 + 3 + 5);
    assertThat(st.rangeQuery1(1, 1)).isEqualTo(2 + 3 + 5);
    assertThat(st.rangeQuery1(2, 2)).isEqualTo(1 + 3);
    assertThat(st.rangeQuery1(3, 3)).isEqualTo(2 + 3 + 2);
    assertThat(st.rangeQuery1(4, 4)).isEqualTo(2 + 3 + 1);

    // Range queries
    assertThat(st.rangeQuery1(0, 1)).isEqualTo(2 * 5 + 2 * 3 + 1 + 2);
    assertThat(st.rangeQuery1(0, 2)).isEqualTo(2 * 5 + 3 * 3 + 1 + 2 + 1);
    assertThat(st.rangeQuery1(3, 4)).isEqualTo(2 * 2 + 2 * 3 + 2 + 1);
    assertThat(st.rangeQuery1(0, 4)).isEqualTo(2 * 5 + 2 * 2 + 3 * 5 + 1 + 1 + 1 + 2 + 2);
  }

  @Test
  public void testRandomRangeAssignUpdatesWithSumRangeQueries() {
    for (int n = 5; n < ITERATIONS; n++) {
      long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
      SumQueryAssignUpdateSegmentTree st = new SumQueryAssignUpdateSegmentTree(ar);

      for (int i = 0; i < n; i++) {
        System.out.printf("n = %d, i = %d\n", n, i);
        int j = TestUtils.randValue(0, n - 1);
        int k = TestUtils.randValue(0, n - 1);
        int i1 = Math.min(j, k);
        int i2 = Math.max(j, k);

        // Range query
        long bfMin = bruteForceMin(ar, i1, i2);
        long segTreeMin = st.rangeQuery1(i1, i2);
        assertThat(bfMin).isEqualTo(segTreeMin);

        // Range update
        j = TestUtils.randValue(0, n - 1);
        k = TestUtils.randValue(0, n - 1);
        int i3 = Math.min(j, k);
        int i4 = Math.max(j, k);
        long randValue = TestUtils.randValue(-1000, 1000);
        st.rangeUpdate1(i3, i4, randValue);
        bruteForceAssignRangeUpdate(ar, i3, i4, randValue);
      }
    }
  }

  // Finds the sum in an array between [l, r] in the `values` array
  private static long bruteForceSum(long[] values, int l, int r) {
    long s = 0;
    for (int i = l; i <= r; i++) {
      s += values[i];
    }
    return s;
  }

  // Finds the min value in an array between [l, r] in the `values` array
  private static long bruteForceMin(long[] values, int l, int r) {
    long m = values[l];
    for (int i = l; i <= r; i++) {
      m = Math.min(m, values[i]);
    }
    return m;
  }

  // Finds the max value in an array between [l, r] in the `values` array
  private static long bruteForceMax(long[] values, int l, int r) {
    long m = values[l];
    for (int i = l; i <= r; i++) {
      m = Math.max(m, values[i]);
    }
    return m;
  }

  private static void bruteForceSumRangeUpdate(long[] values, int l, int r, long x) {
    for (int i = l; i <= r; i++) {
      values[i] += x;
    }
  }

  private static void bruteForceMulRangeUpdate(long[] values, int l, int r, long x) {
    for (int i = l; i <= r; i++) {
      values[i] *= x;
    }
  }

  private static void bruteForceAssignRangeUpdate(long[] values, int l, int r, long x) {
    for (int i = l; i <= r; i++) {
      values[i] = x;
    }
  }
}
