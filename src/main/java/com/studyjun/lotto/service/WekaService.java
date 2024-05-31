package com.studyjun.lotto.service;

import com.studyjun.lotto.dto.lottoNum.response.PredictNumbersNeuralNetworkResponse;
import com.studyjun.lotto.dto.lottoNum.response.PredictNumbersRegressionResponse;
import org.springframework.stereotype.Service;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.RegressionByDiscretization;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WekaService {
    private Instances data;
    private RegressionByDiscretization regression;
    private MultilayerPerceptron neuralNetwork;
    private Random random;

    public WekaService() throws Exception {
        DataSource source = new DataSource("src/main/resources/lottoInfoWeka.arff");
        data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        regression = new RegressionByDiscretization();
        regression.buildClassifier(data);

        neuralNetwork = new MultilayerPerceptron();
        neuralNetwork.setHiddenLayers("5");
        neuralNetwork.setLearningRate(0.1);
        neuralNetwork.setMomentum(0.2);
        neuralNetwork.setTrainingTime(100000);
        neuralNetwork.buildClassifier(data);

        random = new Random();
    }

    public PredictNumbersRegressionResponse predictNumbersRegression(String date) throws Exception {
        List<Integer> numbers = predictNumbers(regression, date);
        return new PredictNumbersRegressionResponse(true, numbers);
    }

    public PredictNumbersNeuralNetworkResponse predictNumbersNeuralNetwork(String date) throws Exception {
        List<Integer> numbers = predictNumbers(neuralNetwork, date);
        return new PredictNumbersNeuralNetworkResponse(true, numbers);
    }

    private List<Integer> predictNumbers(weka.classifiers.Classifier classifier, String date) throws Exception {
        int numberOfPredictions = 100000;
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int i = 0; i < numberOfPredictions; i++) {
            int[] predictedNumbers = singlePrediction(classifier, date);
            for (int num : predictedNumbers) {
                frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
            }
        }

        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int[] singlePrediction(weka.classifiers.Classifier classifier, String date) throws Exception {
        Instances predictionData = new Instances(data, 0);
        double[] instanceValue = new double[predictionData.numAttributes()];

        instanceValue[0] = Double.parseDouble(date.replaceAll("-", ""));

        DenseInstance newInstance = new DenseInstance(1.0, instanceValue);
        newInstance.setDataset(predictionData);
        predictionData.add(newInstance);

        double[] predictedValues = classifier.distributionForInstance(predictionData.instance(0));

        int[] predictedNumbers = Arrays.stream(predictedValues)
                .mapToInt(d -> (int) Math.round(d))
                .filter(num -> num >= 1 && num <= 45)
                .distinct()
                .limit(6)
                .toArray();

        // Fill in remaining numbers randomly if needed
        while (predictedNumbers.length < 6) {
            int randomNum = random.nextInt(45) + 1;
            if (!Arrays.stream(predictedNumbers).anyMatch(num -> num == randomNum)) {
                predictedNumbers = Arrays.copyOf(predictedNumbers, predictedNumbers.length + 1);
                predictedNumbers[predictedNumbers.length - 1] = randomNum;
            }
        }

        return predictedNumbers;
    }
}