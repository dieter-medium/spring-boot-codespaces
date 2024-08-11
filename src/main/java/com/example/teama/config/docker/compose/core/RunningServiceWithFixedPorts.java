package com.example.teama.config.docker.compose.core;

import lombok.experimental.Delegate;
import org.springframework.boot.docker.compose.core.ConnectionPorts;
import org.springframework.boot.docker.compose.core.RunningService;

import java.util.List;

public class RunningServiceWithFixedPorts implements RunningService {
    @Delegate(types = RunningService.class)
    private final RunningService runningService;

    public RunningServiceWithFixedPorts(RunningService runningService) {
        this.runningService = runningService;
    }

    @Override
    public ConnectionPorts ports() {
        return new ConnectionPorts() {
            @Override
            public int get(int containerPort) {
                try {
                    return runningService.ports().get(containerPort);
                } catch (IllegalStateException e) {
                    return containerPort;
                }

            }

            @Override
            public List<Integer> getAll() {
                return runningService.ports().getAll();
            }

            @Override
            public List<Integer> getAll(String protocol) {
                return runningService.ports().getAll(protocol);
            }
        };
    }
}
