package hh.task;

import java.util.*;
import java.util.stream.Collectors;

public class Skyscrapers {

    private final int[][] INDEX_COL_ROW = {{0, 17}, {1, 16}, {2, 15}, {3, 14}, {4, 13}, {5, 12}, {6, 23}, {7, 22}, {8, 21}, {9, 20}, {10, 19}, {11, 18}};

    private List<Integer> heights = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

    private List<List<Integer>> quarter = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0)),
            new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0)),
            new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0)),
            new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0)),
            new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0)),
            new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0))
    ));

    private int setSkyscrapersWithLimitation(Map<Integer, List<List<Integer>>> allVariationArrangeLimitation,
                                             List<List<Integer>> quarter, List<Integer> numbersLimitation, int iterRecur) {
        if (iterRecur < numbersLimitation.size()) {
            int numberLimitation = numbersLimitation.get(iterRecur);
            int prevIterRecur = iterRecur;
            List<List<Integer>> variationArrangeLimitation = allVariationArrangeLimitation.get(numberLimitation);
            List<List<Integer>> quarterCurrent = new ArrayList<>(quarter);
            if (numberLimitation < 6) {
                for (List<Integer> values : variationArrangeLimitation) {
                    int countInsert = 0;
                    for (int i = 0; i < values.size(); i++) {
                        if (!containsValue(quarter, numberLimitation, i, values.get(i))) {
                            quarterCurrent.get(i).set(numberLimitation, values.get(i));
                            countInsert++;
                        }
                    }
                    if (countInsert == values.size()) {
                        iterRecur = setSkyscrapersWithLimitation(allVariationArrangeLimitation, quarterCurrent, numbersLimitation, ++iterRecur);
                        if (iterRecur != numbersLimitation.size()) {
                            iterRecur = prevIterRecur;
                        } else {
                            break;
                        }
                    }
                }

                return iterRecur;
            } else {
                for (List<Integer> values : variationArrangeLimitation) {
                    int indexInsertRow = numberLimitation - 6;
                    if (isInsertRow(quarter, values, indexInsertRow)) {
                        quarterCurrent.set(indexInsertRow, values);
                        iterRecur = setSkyscrapersWithLimitation(allVariationArrangeLimitation, quarterCurrent, numbersLimitation, ++iterRecur);
                        if (iterRecur != numbersLimitation.size()) {
                            iterRecur = prevIterRecur;
                        } else {
                            break;
                        }
                    }
                }

                return iterRecur;
            }
        } else {
            this.quarter = quarter;
            return iterRecur;
        }
    }

    private int setSkyscrapersWithOutLimitation(List<List<Integer>> quarter, int iterRecurRow, int iterRecurCol) {
        if (iterRecurRow != quarter.size()) {
            if (iterRecurCol != quarter.get(iterRecurRow).size()) {
                int iterRecurColPrev = iterRecurCol;
                if (quarter.get(iterRecurRow).get(iterRecurCol) == 0) {
                    for (int height : heights) {
                        if (!containValueCol(quarter, iterRecurCol, height) && !quarter.get(iterRecurRow).contains(height)) {
                            quarter.get(iterRecurRow).set(iterRecurCol, height);
                            iterRecurRow = setSkyscrapersWithOutLimitation(quarter, iterRecurRow, ++iterRecurCol);
                            if (iterRecurRow != quarter.size()) {
                                iterRecurCol = iterRecurColPrev;
                            } else {
                                break;
                            }
                        }
                    }

                    return iterRecurRow;
                } else {
                    setSkyscrapersWithOutLimitation(quarter, iterRecurRow, ++iterRecurCol);
                    return iterRecurRow;
                }
            } else {
                setSkyscrapersWithOutLimitation(quarter, ++iterRecurRow, 0);
                return iterRecurRow;
            }
        } else {
            return iterRecurRow;
        }
    }

    private void arrangeSkyscrapers(String confines) {
        Integer[] reqArr = Arrays.stream(confines.split(","))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
        Map<Integer, List<List<Integer>>> allVariationArrangeLimitation = new LinkedHashMap<>();
        List<Integer> numberLimitation = new ArrayList<>();
        for (int i = 0; i < reqArr.length / 2; i++) {
            int firstLimitation;
            int secondLimitation;
            if (i < 6) {
                firstLimitation = reqArr[i];
                secondLimitation = reqArr[INDEX_COL_ROW[i][1]];
            } else {
                firstLimitation = reqArr[INDEX_COL_ROW[i][1]];
                secondLimitation = reqArr[i];
            }
            if (firstLimitation != 0 || secondLimitation != 0) {
                List<List<Integer>> variationArrange = new ArrayList<>();
                allArrangeLimitation(heights, 0,
                        variationArrange, firstLimitation, secondLimitation);
                allVariationArrangeLimitation.put(i, variationArrange);
                numberLimitation.add(i);
            }
        }
        setSkyscrapersWithLimitation(allVariationArrangeLimitation, quarter, numberLimitation, 0);
        setSkyscrapersWithOutLimitation(quarter, 0, 0);
    }

    private void allArrangeLimitation(List<Integer> arr, int k, List<List<Integer>> variationArrange,
                                      int firstLimitation, int secondLimitation) {
        for (int i = k; i < arr.size(); i++) {
            Collections.swap(arr, i, k);
            allArrangeLimitation(arr, k + 1, variationArrange, firstLimitation, secondLimitation);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            if (checkArrangementFirst(arr, firstLimitation) && checkArrangementSecond(arr, secondLimitation)) {
                List<Integer> subArr = new ArrayList<>(arr);
                variationArrange.add(subArr);
            }
        }
    }

    private boolean isInsertRow(List<List<Integer>> quarter, List<Integer> insertRow, int indexInsertRow) {
        for (int row = 0; row < quarter.size(); row++) {
            for (int col = 0; col < quarter.get(row).size(); col++) {
                if (indexInsertRow != row) {
                    if (quarter.get(row).get(col) != 0 && quarter.get(row).get(col).equals(insertRow.get(col))) {
                        return false;
                    }
                } else {
                    if (quarter.get(row).get(col) != 0 && !quarter.get(row).get(col).equals(insertRow.get(col))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkArrangementFirst(List<Integer> arr, int firstLimitation) {
        if (firstLimitation > 0) {
            int countVisibleSkyscraper = 0;
            int visibleHeight = 0;
            for (int i = 0; i < arr.size(); i++) {
                if (i == 0) {
                    countVisibleSkyscraper++;
                    visibleHeight = arr.get(i);
                } else {
                    if (visibleHeight < arr.get(i)) {
                        countVisibleSkyscraper++;
                        visibleHeight = arr.get(i);
                    }
                }
            }
            return countVisibleSkyscraper == firstLimitation;
        } else {
            return true;
        }
    }

    private boolean checkArrangementSecond(List<Integer> arr, int secondLimitation) {
        if (secondLimitation > 0) {
            int countVisibleSkyscraper = 0;
            int visibleHeight = 0;
            for (int i = arr.size() - 1; i >= 0; i--) {
                if (i == arr.size() - 1) {
                    countVisibleSkyscraper++;
                    visibleHeight = arr.get(i);
                } else {
                    if (visibleHeight < arr.get(i)) {
                        countVisibleSkyscraper++;
                        visibleHeight = arr.get(i);
                    }
                }
            }
            return countVisibleSkyscraper == secondLimitation;
        } else {
            return true;
        }
    }

    private boolean containsValue(List<List<Integer>> quarter, int numberLimitation, int row, int value) {
        for (int col = 0; col < quarter.size(); col++) {
            if (col < numberLimitation) {
                if (quarter.get(row).get(col) == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containValueCol(List<List<Integer>> quarter, int col, int value) {
        for (List<Integer> integers : quarter) {
            if (integers.get(col) == value) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String restriction = new Scanner(System.in).nextLine();
        Skyscrapers main = new Skyscrapers();
        main.arrangeSkyscrapers(restriction);
        String allSkyscraper = main.quarter.stream().flatMap(List::stream).collect(Collectors.toList()).toString();
        System.out.println(allSkyscraper.substring(1, allSkyscraper.length() - 1).replaceAll(" ", ""));
    }
}
