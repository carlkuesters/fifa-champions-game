package com.carlkuesters.fifachampions.ai;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class DataSource {

    public DataSource(int inputCount, int outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }
    @Getter
    protected int inputCount;
    @Getter
    protected int outputCount;

    public abstract List<float[][]> load() throws Exception;

    protected String readFile(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
