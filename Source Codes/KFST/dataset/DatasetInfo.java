/*
 * Kurdistan Feature Selection Tool (KFST) is an open-source tool, developed
 * completely in Java, for performing feature selection process in different
 * areas of research.
 * For more information about KFST, please visit:
 *     http://kfst.uok.ac.ir/index.html
 *
 * Copyright (C) 2016 KFST development team at University of Kurdistan,
 * Sanandaj, Iran.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package KFST.dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import KFST.util.ArraysFunc;

/**
 * This java class is used to keep the input data, split input data
 * to test/train sets and crate CSV file format.
 *
 * @author Sina Tabakhi
 */
public class DatasetInfo {

    private int numData;
    private int numTrainSet;
    private int numTestSet;
    private int numFeature;
    private int numClass;
    private boolean checkDataSet; //checks the format of dataset file
    private boolean checkTrainTestSet; //checks that train and test sets are compatible
    private boolean checkClassLabels; //checks the format of class labels file
    private boolean checkSamplesClass; //checks that the class labels of the samples is valid
    private String[] Classlabel;
    private String allNameFeatures;
    private String[] nameFeatures;
    private String pathData;
    private String pathTestSet;
    private String pathLabel;
    private String[] originalData;
    private double[][] allData;
    private double[][] trainSet;
    private double[][] testSet;
//    private int seedValue = 0;
//    private Random rand = new Random(seedValue);
    private Random rand = new Random();

    /**
     * This method checks that the input string don't contain the semicolon(;)
     * or tab characters. Also, its checks that the input string contains
     * the comma character
     *
     * @param str the input string
     * 
     * @return true if the input string is in the correct format
     */
    private boolean isCorrectString(String str) {
        if (str.indexOf(',') != -1 && str.indexOf(';') == -1 && str.indexOf('	') == -1) {
            return true;
        }
        return false;
    }

    /**
     * This method sets the dataset and the class labels of samples
     *
     * @param path1 the path of the dataset file
     * @param path2 the path of the class label file
     */
    private void init(String path1, String path2) {
        checkDataSet = checkClassLabels = checkTrainTestSet = checkSamplesClass = true;
        BufferedReader br1 = null;
        BufferedReader br2 = null;

        //reads the dataset file and checks the correct format of them
        try {
            br1 = new BufferedReader(new FileReader(path1));
            String tempStr = br1.readLine();
            if (!isCorrectString(tempStr)) {
                checkDataSet = false;
            } else {
                int sizeAllFeatures = tempStr.split(",").length;
                numFeature = sizeAllFeatures - 1;
                numData = 0;
                while (br1.ready()) {
                    int tempSize = br1.readLine().split(",").length;
                    if (tempSize != sizeAllFeatures) {
                        checkDataSet = false;
                        break;
                    }
                    numData++;
                }
            }
            br1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //reads the class label file and checks the correct format of them
        try {
            br2 = new BufferedReader(new FileReader(path2));
            numClass = 0;
            while (br2.ready()) {
                br2.readLine();
                numClass++;
            }
            br2.close();
            if (numClass == 0) {
                checkClassLabels = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (checkDataSet && checkClassLabels) {
            numTrainSet = (int) (numData * 0.66);
            numTestSet = numData - numTrainSet;
            pathData = path1;
            pathLabel = path2;
            Classlabel = new String[numClass];
            nameFeatures = new String[numFeature + 1];
            originalData = new String[numData];
            allData = new double[numData][numFeature + 1];
            trainSet = new double[numTrainSet][numFeature + 1];
            testSet = new double[numTestSet][numFeature + 1];
        }
    }

    /**
     * This method read and set the train/test sets and class labels of samples
     *
     * @param path1 the path of the train set file
     * @param path2 the path of the test set file
     * @param path3 the path of the class labels of samples
     */
    private void init(String path1, String path2, String path3) {
        checkDataSet = checkClassLabels = checkTrainTestSet = checkSamplesClass = true;
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        BufferedReader br3 = null;

        //reads the train set file and checks the correct format of them
        try {
            br1 = new BufferedReader(new FileReader(path1));
            String tempStr = br1.readLine();
            if (!isCorrectString(tempStr)) {
                checkDataSet = false;
            } else {
                int sizeAllFeatures = tempStr.split(",").length;
                numFeature = sizeAllFeatures - 1;
                numTrainSet = 0;
                while (br1.ready()) {
                    int tempSize = br1.readLine().split(",").length;
                    if (tempSize != sizeAllFeatures) {
                        checkDataSet = false;
                        break;
                    }
                    numTrainSet++;
                }
            }
            br1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //reads the test set file and checks the correct format of them
        if (checkDataSet) {
            try {
                br2 = new BufferedReader(new FileReader(path2));
                int sizeAllFeatures = br2.readLine().split(",").length;
                numTestSet = 0;
                while (br2.ready()) {
                    int tempSize = br2.readLine().split(",").length;
                    if (tempSize != sizeAllFeatures) {
                        checkDataSet = false;
                        break;
                    }
                    numTestSet++;
                }
                br2.close();
                if (checkDataSet && sizeAllFeatures != (numFeature + 1)) {
                    checkTrainTestSet = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //reads the class label file and checks the correct format of them
        try {
            br3 = new BufferedReader(new FileReader(path3));
            numClass = 0;
            while (br3.ready()) {
                br3.readLine();
                numClass++;
            }
            br3.close();
            if (numClass == 0) {
                checkClassLabels = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (checkDataSet && checkClassLabels && checkTrainTestSet) {
            numData = numTrainSet + numTestSet;
            pathData = path1;
            pathTestSet = path2;
            pathLabel = path3;
            Classlabel = new String[numClass];
            nameFeatures = new String[numFeature + 1];
            originalData = new String[numData];
            allData = new double[numData][numFeature + 1];
            trainSet = new double[numTrainSet][numFeature + 1];
            testSet = new double[numTestSet][numFeature + 1];
        }
    }

    /**
     * This method reads the dataset
     */
    private void readExample1() {
        int count = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(pathData));
            allNameFeatures = br.readLine();
            while (br.ready()) {
                originalData[count++] = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads the train/test sets
     */
    private void readExample2() {
        int count = 0;
        BufferedReader br1 = null;
        BufferedReader br2 = null;

        //reads the train set file
        try {
            br1 = new BufferedReader(new FileReader(pathData));
            allNameFeatures = br1.readLine();
            while (br1.ready()) {
                originalData[count++] = br1.readLine();
            }
            br1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //reads the test set file
        try {
            br2 = new BufferedReader(new FileReader(pathTestSet));
            br2.readLine();
            while (br2.ready()) {
                originalData[count++] = br2.readLine();
            }
            br2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads the class labels of the samples
     */
    private void readLabel() {
        int count = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(pathLabel));
            while (br.ready()) {
                Classlabel[count++] = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method converts the String name of class labels to the integer value
     *
     * @param nameClass the name of the class labels as String
     * 
     * @return the index of the class
     */
    private int indexClass(String nameClass) {
        for (int i = 0; i < numClass; i++) {
            if (nameClass.equals(Classlabel[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method converts and sets the string values of the input data
     * to the double values
     */
    private void splitFeatureWithLabel() {
        checkSamplesClass = true;
        nameFeatures = allNameFeatures.split(",");
        for (int i = 0; i < numData; i++) {
            String res[] = originalData[i].split(",");
            for (int k = 0; k < numFeature; k++) {
                allData[i][k] = Double.parseDouble(res[k]);
            }
            allData[i][numFeature] = indexClass(res[numFeature]);
            if (allData[i][numFeature] == -1) {
                checkSamplesClass = false;
                break;
            }
        }
    }

    /**
     * This method randomly splits the input dataset to the train/test sets
     * 2/3 of the dataset is used as train set and
     * 1/3 of the dataset is used as test set
     */
    private void splitDataSetToTrainAndTest1() {
        int countTest = 0;
        int countTrain = 0;
        for (int i = 0; i < numData; i++) {
            if ((countTrain < numTrainSet) && (countTest < numTestSet)) {
                if (rand.nextDouble() <= 0.66) {
                    trainSet[countTrain++] = Arrays.copyOf(allData[i], allData[i].length);
                } else {
                    testSet[countTest++] = Arrays.copyOf(allData[i], allData[i].length);
                }
            } else if (countTrain < numTrainSet) {
                trainSet[countTrain++] = Arrays.copyOf(allData[i], allData[i].length);
            } else {
                testSet[countTest++] = Arrays.copyOf(allData[i], allData[i].length);
            }
        }
    }

    /**
     * This method sets the train/test sets
     * train and test sets previously are split by user
     */
    private void splitDataSetToTrainAndTest2() {
        for (int i = 0; i < numTrainSet; i++) {
            trainSet[i] = Arrays.copyOf(allData[i], allData[i].length);
        }
        for (int i = numTrainSet; i < numData; i++) {
            testSet[i - numTrainSet] = Arrays.copyOf(allData[i], allData[i].length);
        }
    }

    /**
     * This is used to read dataset and class label files, split datasets and
     * set their values
     *
     * @param path1 the path of the datasets
     * @param path2 the path of the class labels
     */
    public void preProcessing(String path1, String path2) {
        init(path1, path2); //checks the correct format of the input files
        if (checkDataSet && checkClassLabels) {
            readExample1(); //reads the dataset file as string
            readLabel(); //reads the class label file
            splitFeatureWithLabel(); //converts string values of the input data to the double values
            if (checkSamplesClass) {
                splitDataSetToTrainAndTest1(); //randomly splits the input dataset to the train/test sets
            }
        }
    }

    /**
     * This is used to read datasets and class labels, split datasets and
     * set their values
     *
     * @param path1 the path of the train set
     * @param path2 the path of the test set
     * @param path3 the path of the class labels
     */
    public void preProcessing(String path1, String path2, String path3) {
        init(path1, path2, path3); //checks the correct format of the input files
        if (checkDataSet && checkClassLabels && checkTrainTestSet) {
            readExample2(); //reads the train/test set files as string
            readLabel(); //reads the class label file
            splitFeatureWithLabel(); //converts string values of the input data to the double values
            if (checkSamplesClass) {
                splitDataSetToTrainAndTest2(); //setts the train/test sets
            }
        }
    }

    /**
     * This is used to return the status of the dataset
     *
     * @return true if the dataset file is in the correct format
     */
    public boolean isCorrectDataset() {
        return checkDataSet;
    }

    /**
     * This is used to return the status of the class label file
     *
     * @return true if the class labels file is in the correct format
     */
    public boolean isCorrectClassLabel() {
        return checkClassLabels;
    }

    /**
     * This is used to return the status of the samples' class
     *
     * @return true if the the class labels of the samples is valid
     */
    public boolean isCorrectSamplesClass() {
        return checkSamplesClass;
    }

    /**
     * This is used to return the status of train/test sets
     * 
     * @return true if the train and test sets are compatible
     */
    public boolean isCompatibleTrainTestSet() {
        return checkTrainTestSet;
    }

    /**
     * This is used to return number of samples in the dataset(train set +
     * test set)
     * 
     * @return number of samples 
     */
    public int getNumData() {
        return numData;
    }

    /**
     * This is used to return number of features in each sample
     *
     * @return number of features
     */
    public int getNumFeature() {
        return numFeature;
    }

    /**
     * This is used to return number of samples in the train set
     *
     * @return number of samples in the train set
     */
    public int getNumTrainSet() {
        return numTrainSet;
    }

    /**
     * This is used to return number of samples in the test set
     *
     * @return number of samples in the test set
     */
    public int getNumTestSet() {
        return numTestSet;
    }

    /**
     * This is used to return number of classes in the dataset
     *
     * @return number of classes in the dataset
     */
    public int getNumClass() {
        return numClass;
    }

    /**
     * This is used to return the names of class labels
     *
     * @return the array of class labels' names
     */
    public String[] getClassLabel() {
        return Arrays.copyOf(Classlabel, Classlabel.length);
    }

    /**
     * This is used to return the train set values
     *
     * @return the matrix of train set
     */
    public double[][] getTrainSet() {
        return ArraysFunc.copyDoubleArray2D(trainSet);
    }

    /**
     * This is used to return the test set values
     *
     * @return the matrix of test set
     */
    public double[][] getTestSet() {
        return ArraysFunc.copyDoubleArray2D(testSet);
    }

    /**
     * This is used to return the names of features
     *
     * @return the array of features' names
     */
    public String[] getNameFeatures() {
        return Arrays.copyOf(nameFeatures, nameFeatures.length);
    }

    /**
     * This method creates a string of the names of features in the selected
     * feature array
     *
     * @param array the array of indices of the selected features
     * 
     * @return a string of the integer array
     */
    public String createFeatNames(int[] array) {
        String res = array.length + " features = {" + nameFeatures[array[0]];
        for (int i = 1; i < array.length; i++) {
            res += ", " + nameFeatures[array[i]];
        }
        res += "}";
        return res;
    }

    /**
     * This method creates a CSV (Comma delimited) file of the input data
     *
     * @param oldData the input data
     * @param selectedFeature the list of selected Feature
     * @param name name of the path for created CSV file
     */
    public void createCSVFile(double[][] oldData, int[] selectedFeature, String name) {
        int numSamples = oldData.length;
        int sizeFeatureSet = selectedFeature.length;
        FileWriter fw = null;
        try {
            fw = new FileWriter(name, false);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < sizeFeatureSet; i++) {
                pw.print(nameFeatures[selectedFeature[i]] + ",");
            }
            pw.println(nameFeatures[numFeature]);
            for (int i = 0; i < numSamples; i++) {
                for (int j = 0; j < sizeFeatureSet; j++) {
                    pw.print(oldData[i][selectedFeature[j]] + ",");
                }
                int index = (int) oldData[i][numFeature];
                pw.println(Classlabel[index]);
            }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(DatasetInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getPathData() {
        return pathData;
    }

    public void setPathData(String pathData) {
        this.pathData = pathData;
    }

    public String getPathTestSet() {
        return pathTestSet;
    }

    public void setPathTestSet(String pathTestSet) {
        this.pathTestSet = pathTestSet;
    }

    /**
     * This method creates a CSV (Comma delimited) file of the input data
     *
     * @param oldData the input data
     * @param selectedFeature the list of selected Feature
     * @param name name of the path for created CSV file
     * @param featureNames a string array of the names of features
     */
    public void createCSVFile(double[][] oldData, int[] selectedFeature, String name, String[] featureNames) {
        int numSamples = oldData.length;
        int sizeFeatureSet = selectedFeature.length;
        int numFeats = oldData[0].length - 1;
        FileWriter fw = null;
        try {
            fw = new FileWriter(name, false);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < sizeFeatureSet; i++) {
                pw.print(featureNames[selectedFeature[i]] + ",");
            }
            pw.println(featureNames[numFeats]);
            for (int i = 0; i < numSamples; i++) {
                for (int j = 0; j < sizeFeatureSet; j++) {
                    pw.print(oldData[i][selectedFeature[j]] + ",");
                }
                int index = (int) oldData[i][numFeats];
                pw.println(index);
            }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(DatasetInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
