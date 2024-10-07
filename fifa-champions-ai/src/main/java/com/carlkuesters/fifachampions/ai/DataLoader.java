package com.carlkuesters.fifachampions.ai;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Collections;
import java.util.List;

public class DataLoader {

    public static DataSet load(DataSource source) throws Exception {
        List<float[][]> allData = source.load();
        System.out.println("Finished load (total " + allData.size() + " entries).");

        // TODO: Support the whole thing :(
        Collections.shuffle(allData);
        allData = allData.subList(0, allData.size() / 5);

        System.out.println("Starting preparation...");
        INDArray trainingInputs = Nd4j.zeros(allData.size(), source.getInputCount());
        INDArray trainingOutputs = Nd4j.zeros(allData.size(), source.getOutputCount());

        int dataIndex = 0;
        for (float[][] data : allData) {
            for (int input = 0; input < source.getInputCount(); input++) {
                trainingInputs.putScalar(new int[] { dataIndex, input }, data[0][input]);
            }
            for (int output = 0; output < source.getOutputCount(); output++) {
                trainingOutputs.putScalar(new int[] { dataIndex, output }, data[1][output]);
            }
            dataIndex++;
            if ((dataIndex % 10000) == 0) {
                System.out.println("Prepared " + dataIndex + " total entries...");
            }
        }

        return new DataSet(trainingInputs, trainingOutputs);
    }
}
