import java.lang.Math;
import java.util.ArrayList;

import medianProblem.*;

public class App {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int FACILITYNUMBER = 5;
        ArrayList<Integer> indexInformation = new ArrayList<>();

        Operators operator = new Operators();
        double[][] facs_coordinates = operator.createDistanceMatrix(5, 2);
        double[][] points_coordinates = operator.createDistanceMatrix(20, 2);
        Facility[] facs = new Facility[5];
        Point[] points = new Point[20];
        for (int i = 0; i < facs.length; i++) {
            Facility f1 = new Facility(i, facs_coordinates[i][0], facs_coordinates[i][1], 20 * Math.random() + 50);
            facs[i] = f1;
        }
        for (int i = 0; i < points.length; i++) {
            Point p1 = new Point(i, points_coordinates[i][0], points_coordinates[i][1], 2 * Math.random() + 1);
            points[i] = p1;
        }
        double[][] distanceMatrix = operator.distanceMatrix(facs, points);
        ArrayList<Facility> openedFacilities = new ArrayList<>();
        ArrayList<Point> unassignedFacilities = new ArrayList<>();
        ArrayList<ArrayList<Point>> assignmentList = new ArrayList<>();

        // get first min index and add them
        int minimumIndex = MinimumTotalDist(distanceMatrix, facs, points, indexInformation);

        openedFacilities.add(facs[minimumIndex]);
        indexInformation.add(minimumIndex);

        // assign first point
        ArrayList<Point> assignedPoints = new ArrayList<>();
        for (int k = 0; k < points.length; k++) {
            if (points[k].getDemand() < facs[minimumIndex].getSupply())
                assignedPoints.add(points[k]);
            else
                unassignedFacilities.add(points[k]);
        }
        assignmentList.add(assignedPoints);

        // assign others until the max facility number in a greedy way
        while (openedFacilities.size() < FACILITYNUMBER) {
            assignmentList.clear();
            int min = MinimumTotalDist(distanceMatrix, facs, points, indexInformation);
            openedFacilities.add(facs[min]);
            indexInformation.add(min);
            for (int i = 0; i < indexInformation.size(); i++)
                assignmentList.add(new ArrayList<Point>());
            for (int k = 0; k < points.length; k++) {
                double minDist = distanceMatrix[0][k];
                int index = 0;
                for (int i = 0; i < indexInformation.size(); i++) {
                    if (distanceMatrix[i][k] < minDist) {
                        minDist = distanceMatrix[i][k];
                        index = i;
                    }
                }
                if (openedFacilities.get(index).getSupply() > points[k].getDemand())
                    assignmentList.get(index).add(points[k]);
            }

        }

        long end = System.currentTimeMillis();
        // operator.print2d(distanceMatrix);
        // assignment list prİnting
        for (int j = 0; j < assignmentList.size(); j++) {
            for (int i = 0; i < assignmentList.get(j).size(); i++) {
                System.out.println((assignmentList.get(j).get(i).x + " " + assignmentList.get(j).get(i).y));
            }

        }
        System.out.println((end - start + "ms"));

    }

    public static int MinimumTotalDist(double[][] distanceMatrix, Facility[] facs, Point[] points,
            ArrayList<Integer> indexInfo) {
        boolean flag = true;
        int index = 0;
        int min = 0;
        for (int i = 0; i < facs.length; i++) {
            int sum = 0;
            for (int j = 0; j < points.length; j++) {
                sum += distanceMatrix[i][j];
            }
            if (sum < min && !indexInfo.contains(i)) {
                min = sum;
                index = i;
            }
            if (flag) {
                min = sum;
                flag = false;
            }
        }
        return index;
    }
}
