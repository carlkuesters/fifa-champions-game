package com.carlkuesters.fifachampions.ai;

import com.carlkuesters.fifachampions.ai.sources.metrica.DataSource_Metrica;
import com.carlkuesters.fifachampions.ai.sources.statsbomb.DataSource_Statsbomb;
import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;

public class Training {

    public static void main(String[] args) throws Exception {
        DataSource source = new DataSource_Metrica();
        // source = new DataSource_Statsbomb();

        int nodes = 32;
        int hiddenLayers = 8;
        nodes = 128;
        hiddenLayers = 8;

        NeuralNetConfiguration.ListBuilder configurationBuilder = new NeuralNetConfiguration.Builder()
                .seed(42)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam())
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(source.getInputCount())
                        .nOut(nodes)
                        .activation(Activation.TANH)
                        .build());

        for (int i = 0; i < hiddenLayers; i++) {
            configurationBuilder = configurationBuilder.layer(new DenseLayer.Builder()
                    .nIn(nodes)
                    .nOut(nodes)
                    .activation(Activation.TANH)
                    .build());
        }

        MultiLayerConfiguration configuration = configurationBuilder
                .layer(new OutputLayer.Builder()
                        .nOut(source.getOutputCount())
                        .activation(Activation.IDENTITY)
                        .lossFunction(LossFunctions.LossFunction.MSE)
                        .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(configuration);
        network.init();

        UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();
        uiServer.attach(statsStorage);
        network.setListeners(new StatsListener(statsStorage));

        System.out.println("Starting loading data...");
        DataSet allData = DataLoader.load(source);
        System.out.println("Splitting training and test data...");
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.8);
        DataSet trainData = testAndTrain.getTrain();
        DataSet testData = testAndTrain.getTest();

        System.out.println("Starting training...");
        for (int i = 0; i < 100000; i++) {
            System.out.println("Shuffling...");
            trainData.shuffle();
            System.out.println("Training...");
            ListDataSetIterator<DataSet> trainDataIterator = new ListDataSetIterator<>(trainData.asList(), 1000);
            network.fit(trainDataIterator);
            System.out.println("Trained " + (i + 1) + " total epochs.");
            if ((i % 5) == 0) {
                network.save(new File("./network.dl4j"));
                System.out.println("Eval training data:");
                System.out.println(network.evaluateRegression(trainDataIterator).stats());
                System.out.println("Eval test data:");
                ListDataSetIterator<DataSet> testDataIterator = new ListDataSetIterator<>(testData.asList(), 1000);
                System.out.println(network.evaluateRegression(testDataIterator).stats());

                INDArray verifyInputs = Nd4j.zeros(2, source.getInputCount());
                verifyInputs.putScalar(new int[] { 0, 0 }, 0.9f);
                verifyInputs.putScalar(new int[] { 0, 1 }, 0.9f);
                verifyInputs.putScalar(new int[] { 0, 2 }, 0);
                verifyInputs.putScalar(new int[] { 0, 3 }, 0.75f);
                verifyInputs.putScalar(new int[] { 1, 0 }, -0.95f);
                verifyInputs.putScalar(new int[] { 1, 1 }, 0);
                verifyInputs.putScalar(new int[] { 1, 2 }, -0.7f);
                verifyInputs.putScalar(new int[] { 1, 3 }, 0.25f);
                System.out.println("-------------------");
                System.out.println("Input:\t" + verifyInputs);
                System.out.println("Predicted:\t" + network.output(verifyInputs));
                System.out.println("-------------------");
            }
        }
        System.out.println("Finished training.");
    }
}
