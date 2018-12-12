public class MineFieldTester {

    public static void main(String[] args) {

        boolean[][] smallMineField = {{false, false, false, false},
                {true, false, false, false},
                {false, true, true, false},
                {false, true, false, true}};

        MineField MF = new MineField(smallMineField);

        System.out.println(MF.numMines() + " *********** Should be 5.");
        System.out.println(MF.numCols() + " *********** Should be 4.");
        System.out.println(MF.numRows() + " *********** Should be 4.");
        System.out.println(MF.hasMine(0, 1) + " ************ Should be false.");
        System.out.println(MF.hasMine(2, 1) + " ************ Should be true.");
        System.out.println(MF.numAdjacentMines(3, 2) + " ************ Should be 4.");
        System.out.println(MF.numAdjacentMines(1, 1) + " ************ Should be 3.");
        System.out.println();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(MF.hasMine(i, j) + " ");
            }
            System.out.println("\n");
        }

        MF.resetEmpty();
        System.out.println(MF.hasMine(2, 1) + " ************ Should be false.");
        System.out.println();

        MF.populateMineField(0, 0);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(MF.hasMine(i, j) + " ");
            }
            System.out.println("\n");
        }

    }
}
