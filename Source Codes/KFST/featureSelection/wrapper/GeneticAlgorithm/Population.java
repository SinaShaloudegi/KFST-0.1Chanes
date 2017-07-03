package KFST.featureSelection.wrapper.GeneticAlgorithm;

import weka.core.Instances;

import java.util.Random;

/**
 * Created by sina on 6/2/2017.
 */
public class Population {

    Individual individuals[];
    int numPopulation;
    private int numFeatures;
    Instances data;
    int[] S;
    int[] D;

    public Population(int numPopulation, int numFeatures, int[] S, int[] D) {

        this.numPopulation = numPopulation;
        individuals = new Individual[numPopulation];
        this.numFeatures = numFeatures;
        this.S = S;
        this.D = D;

    }


    public Individual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }

    public void init(int numSelectedFeature) {

        for (int i = 0; i < numPopulation; i++) {
            individuals[i] = new Individual();

            Individual insInstance = new Individual();
            this.individuals[i] = insInstance.randomIndividual(numFeatures);

        }

        refineNumOfOnes(numSelectedFeature);


    }

    public void refineNumOfOnes(int numSelectedFeature) {

        for (int i = 0; i < individuals.length; i++) {
            int ones = numOfOnes(this.individuals[i].getGene());
            if (ones != numSelectedFeature) {
                refine(i, numSelectedFeature, ones);

            }
        }
    }


    private void refine(int i, int numSelectedFeatures, int ones) {

        if (ones > numSelectedFeatures) {


            int temp = ones;
            while (temp != numSelectedFeatures) {
                Random rand = new Random();
                int rnd = rand.nextInt(numFeatures);
                if (this.individuals[i].gene[rnd] == 1) {
                    this.individuals[i].gene[rnd] = 0;
                    temp--;
                }

            }

        } else if (ones < numSelectedFeatures) {
            int temp = numSelectedFeatures;
            while (temp != ones) {
                Random rand = new Random();
                int rnd = rand.nextInt(numFeatures);
                if (this.individuals[i].gene[rnd] == 0) {
                    this.individuals[i].gene[rnd] = 1;
                    temp--;
                }

            }
        }

    }

    private void refineHGAFS(int i, int numSelectedFeatures, int ones) {
//TODO HGAFS for refine
        if (ones > numSelectedFeatures) {


        } else if (ones < numSelectedFeatures) {

        }

    }


    private int numOfOnes(byte[] gene) {
        int counter = 0;
        for (int i = 0; i < gene.length; i++) {
            if (gene[i] == 1) {
                counter++;
            }
        }
        return counter;

    }

    public int getNumPopulation() {
        return numPopulation;
    }

    public void setNumPopulation(int numPopulation) {
        this.numPopulation = numPopulation;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public void setNumFeatures(int numFeatures) {
        this.numFeatures = numFeatures;
    }

    public Individual best() {
        double max = Double.MIN_VALUE;
        Individual best = null;
        for (int i = 0; i < this.individuals.length; i++) {
            if (this.individuals[i].fitness > max) {
                max = this.individuals[i].fitness;
                best = this.individuals[i];
            }

        }
        return best;
    }
}