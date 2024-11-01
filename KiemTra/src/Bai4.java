import java.util.Scanner;

public class Bai4 {
    private long[] segmentTree;
    private long[] array;

    public  Bai4(int n) {
        int size = 1;
        while (size < n) {
            size *= 2;
        }
        segmentTree = new long[size * 2];
        array = new long[n];
    }


    public void build(int[] arr, int x, int lx, int rx) {
        if (rx - lx == 1) {
            if (lx < arr.length) {
                segmentTree[x] = arr[lx];
            }
            return;
        }
        int mid = (lx + rx) / 2;
        build(arr, 2 * x + 1, lx, mid);
        build(arr, 2 * x + 2, mid, rx);
        segmentTree[x] = segmentTree[2 * x + 1] + segmentTree[2 * x + 2];
    }


    public long queryTree(int x, int lx, int rx, int L, int R) {
        if (lx >= R || L >= rx) return 0;
        if (lx >= L && rx <= R) return segmentTree[x];
        int mid = (lx + rx) / 2;
        long leftSum = queryTree(2 * x + 1, lx, mid, L, R);
        long rightSum = queryTree(2 * x + 2, mid, rx, L, R);
        return leftSum + rightSum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        Bai4 segmentTreeSum = new Bai4(n);
        segmentTreeSum.build(arr, 0, 0, n);

        int q = scanner.nextInt();
        for (int i = 0; i < q; i++) {
            int L = scanner.nextInt() - 1;
            int R = scanner.nextInt();
            long sum = segmentTreeSum.queryTree(0, 0, n, L, R);
            System.out.println(sum);
        }

    }
}
