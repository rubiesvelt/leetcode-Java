package solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMachineSystem {

    public int num;

    public List<Machine> machineList;

    public DataMachineSystem(int num) {
        this.num = num;
        this.machineList = new ArrayList<>();
        for (int id = 1; id <= num; id++) {
            machineList.add(new Machine(id));
        }
    }

    public int transferData(int machineA, int machineB, int dataId) {
        Data data = getData(machineA, dataId);
        Machine machineEntityB = machineList.get(machineB - 1);
        if (machineEntityB.dataMap.containsKey(dataId)) {
            return 0;
        }
        raiseContribution(data);
        Data replicaData = new Data(data);
        replicaData.getSpreadChain().add(machineB);
        machineEntityB.dataMap.put(dataId, replicaData);
        return 1;
    }

    public int transferDataToAll(int machine, int dataId) {
        Data data = getData(machine, dataId);
        int count = 0;
        for (Machine currentMachine : machineList) {
            if (currentMachine.id == machine) {
                continue;
            } else if (currentMachine.dataMap.containsKey(dataId)) {
                continue;
            }
            raiseContribution(data);
            Data replicaData = new Data(data);
            replicaData.getSpreadChain().add(currentMachine.id);
            currentMachine.dataMap.put(dataId, replicaData);
            count++;
        }
        return count;
    }

    public int queryContribution(int machine) {
        Machine machineEntity = machineList.get(machine - 1);
        return machineEntity.contribution;
    }

    public void raiseContribution(Data data) {
        for (Integer machineId : data.getSpreadChain()) {
            Machine machineEntity = machineList.get(machineId - 1);
            machineEntity.contribution += 10;
        }
    }

    public Data getData(int machine, int dataId) {
        Machine machineEntity = machineList.get(machine - 1);
        Data data;
        if (machineEntity.dataMap.containsKey(dataId)) {
            data = machineEntity.dataMap.get(dataId);
        } else {
            data = new Data();
            data.getSpreadChain().add(machine);
        }
        machineEntity.dataMap.put(dataId, data);
        return data;
    }

    public static class Machine {

        public int id;

        public Map<Integer, Data> dataMap = new HashMap();

        public int contribution = 0;

        public Machine(int id) {
            this.id = id;
        }
    }

    public static class Data {

        private final List<Integer> spreadChain = new ArrayList<>();

        public Data() {
        }

        public Data(Data data) {
            spreadChain.addAll(data.spreadChain);
        }

        public List<Integer> getSpreadChain() {
            return spreadChain;
        }
    }
}
