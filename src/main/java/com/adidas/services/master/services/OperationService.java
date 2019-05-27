package com.adidas.services.master.services;


import com.adidas.services.master.dto.WorkerStarterDto;

public interface OperationService {
    String launchWorker();

    void run(WorkerStarterDto workerStarterDto);

    void launchWorkerBV();

    void launchWorkerOlapic();

    void launchWorkerInventory();
}
