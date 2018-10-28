import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by zxj on 10/27/18.
 */
class TopTenCalculator {

    public void count(String fileName) {

        // for top_10_occupations.txt
        Map<String, Integer> certifiedOccupationsCount = new HashMap<>();

        // for top_10_states.txt
        Map<String, Integer> certifiedStatesCount = new HashMap<>();

        // for both
        int[] totalCertified = new int[1];

        readData(fileName, certifiedOccupationsCount, certifiedStatesCount, totalCertified);


        // top 10 occupations
        String[] topTenOccupations = getTopK(certifiedOccupationsCount, 10);

        // top 10 states
        String[] topTenStates = getTopK(certifiedStatesCount, 10);

        // output for top_10_occupations.txt
        StringBuilder occupationsOutputSb = new StringBuilder();
        occupationsOutputSb.append("TOP_OCCUPATIONS;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE").append("\n");
        constructOutput(topTenOccupations, certifiedOccupationsCount, totalCertified[0], occupationsOutputSb);

        output(".//output//top_10_occupations.txt", occupationsOutputSb.toString());

        // output for top_10_states.txt
        StringBuilder statesOutputSb = new StringBuilder();
        statesOutputSb.append("TOP_STATES;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE").append("\n");
        constructOutput(topTenStates, certifiedStatesCount, totalCertified[0], statesOutputSb);

        output(".//output//top_10_states.txt", statesOutputSb.toString());

    }


    private void readData(String fileName, Map<String, Integer> certifiedOccupationsCount, Map<String, Integer> certifiedStatesCount, int[] totalCertified) {
        StringUtil util = new StringUtil();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            // pre - process:
            // get fields index

            // CASE_STATUS is for 2015 and 2016
            // STATUS is for 2014
            Map<String, Integer> fieldsIndex = new HashMap<>();
            String[] statusNames = new String[]{"CASE_STATUS", "STATUS"};
            for (String name : statusNames) {
                fieldsIndex.put(name, null);
            }


            // SOC_NAME is for 2015 and 2016
            // LCA_CASE_SOC_NAME is for 2014
            String[] socNameList = new String[]{"SOC_NAME", "LCA_CASE_SOC_NAME"};
            for (String socName : socNameList) {
                fieldsIndex.put(socName, null);
            }


            // WORKSITE_STATE is for 2015 and 2016
            // LCA_CASE_WORKLOC1_STATE is for 2014
            String[] statesNameList = new String[]{"WORKSITE_STATE", "LCA_CASE_WORKLOC1_STATE"};
            for (String state_name : statesNameList) {
                fieldsIndex.put(state_name, null);
            }


            // split the data by delimiter
            // but ignore the delimiter inside quotations
            List<String> fields = util.split(br.readLine().toUpperCase(), ';');

            // get field index
            for (int i = 0; i < fields.size(); i++) {
                if (fieldsIndex.containsKey(fields.get(i))) {
                    fieldsIndex.put(fields.get(i), i);
                }
            }

            Integer case_status_index = null;
            for (String status_name : statusNames) {
                if (fieldsIndex.get(status_name) != null) {
                    case_status_index = fieldsIndex.get(status_name);
                    break;
                }
            }
            if (case_status_index == null)
                throw new IllegalArgumentException("STATUS field name is unknown");


            Integer soc_name_index = null;
            for (String soc_name : socNameList) {
                if (fieldsIndex.get(soc_name) != null) {
                    soc_name_index = fieldsIndex.get(soc_name);
                    break;
                }
            }
            if (soc_name_index == null)
                throw new IllegalArgumentException("SOC_NAME field name is unknown");


            Integer employment_state_index = null;
            for (String state_name : statesNameList) {
                if (fieldsIndex.get(state_name) != null) {
                    employment_state_index = fieldsIndex.get(state_name);
                    break;
                }

            }
            if (employment_state_index == null)
                throw new IllegalArgumentException("WORK_STATE field name is unknown");


            // count - step:
            String line;
            while ((line = br.readLine()) != null) {

                // split the data by delimiter
                // but ignore the delimiter inside quotations
                List<String> data = util.split(line, ';');


                // this is for test
                // we are trying to make sure that the data is in the same format with headings
                if (fields.size() != data.size()) {
                    throw new IllegalArgumentException("data and headings do not match with each other");
                }


                String keyOccupation = util.trim(data.get(soc_name_index));
                String keyStates = util.trim(data.get(employment_state_index));


                // count for certified occupations
                if (data.get(case_status_index).equals("CERTIFIED")) {
                    if (!keyOccupation.isEmpty())
                        certifiedOccupationsCount.put(keyOccupation, certifiedOccupationsCount.getOrDefault(keyOccupation, 0) + 1);
                    if (!keyOccupation.isEmpty())
                        certifiedStatesCount.put(keyStates, certifiedStatesCount.getOrDefault(keyStates, 0) + 1);
                    totalCertified[0]++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getTopK(Map<String, Integer> map, int K) {
        // reverse order top K PriorityQueue
        PriorityQueue<Map.Entry<String, Integer>> topK_PQ = new PriorityQueue<>(
            (e1, e2) -> e1.getValue().equals(e2.getValue()) ? e2.getKey().compareTo(e1.getKey()) : e1.getValue() - e2.getValue()
        );

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            topK_PQ.offer(entry);
            if (topK_PQ.size() > K) {
                topK_PQ.poll();
            }
        }

        String[] topK = new String[Math.min(topK_PQ.size(), K)];
        for (int i = topK.length - 1; i >= 0; i--) {
            topK[i] = topK_PQ.poll().getKey();
        }
        return topK;
    }



    private String constructOutput(String[] topK, Map<String, Integer> certifiedCount, int total, StringBuilder sb) {
        for (String field : topK) {
            int certifiedCountForThisField = certifiedCount.get(field);
            float percentageOfTotal = certifiedCountForThisField * 100f / total;

            sb.append(field).append(";");
            sb.append(certifiedCountForThisField).append(";");
            sb.append(percentageOfTotal).append("%");
            sb.append("\n");

        }
        // for test
        sb.append(LocalDateTime.now());
        return sb.toString();
    }

    private void output(String fileName, String output) {
        Path outputPath = Paths.get(fileName);

        if (!Files.exists(outputPath)) {
            try {
                Files.createFile(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.write(outputPath, output.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}