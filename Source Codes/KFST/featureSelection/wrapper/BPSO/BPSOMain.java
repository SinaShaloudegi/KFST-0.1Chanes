package KFST.featureSelection.wrapper.BPSO;

import KFST.dataset.DatasetInfo;
import KFST.featureSelection.wrapper.WrapperApproach;

import java.io.IOException;

/**
 * Created by sina on 7/30/2017.
 */
public class BPSOMain  implements WrapperApproach {
    BPSOSwarm swarm;
    int numIterates;
    int numFeatures;
    int numSwarmPopulation;
    int numSelectedFeatures;
    String pathData;
    String pathTestData;
    BPSOFitCalculator bpsoFitCalculator;
    int num=0;

    public BPSOMain(int numSelectedFeatures, int numItertion, int numSwarmPopulation) {
        this.numIterates = numItertion;
        this.numSwarmPopulation = numSwarmPopulation;
        this.numSelectedFeatures = numSelectedFeatures;

    }

    private void run() throws Exception {
        for (int i = 0; i < numIterates; i++) {
            swarm.calculateFitness();

            if(swarm.update()){
                num=0;
            }else {
               num++;
            }

            swarm.uniformCombination(num);

        }
    }

    private void init() throws IOException {
        bpsoFitCalculator = new BPSOFitCalculator(pathData, pathTestData);
        swarm = new BPSOSwarm(numFeatures, numSwarmPopulation, bpsoFitCalculator, numSelectedFeatures);
        swarm.initialize();
    }

    @Override
    public void loadDataSet(DatasetInfo ob) {
        numFeatures = ob.getNumFeature();
        pathData = ob.getPathData();
        pathTestData = ob.getPathTestSet();
    }

    @Override
    public void loadDataSet(double[][] data, int numFeat, int numClasses) {

    }

    @Override
    public void evaluateFeatures() throws Exception {
        init();
        run();
    }

    @Override
    public int[] getSelectedFeatureSubset() {
        return new int[0];
    }

    @Override
    public double[] getValues() {
        return new double[0];
    }
}
