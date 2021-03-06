package com.adidas.services.master.service;


import com.adidas.product.worker.schema.WorkerLaunch;
import com.adidas.services.master.dto.Brand;
import com.adidas.services.master.dto.MigrationFlow;
import com.adidas.services.master.dto.MigrationType;
import com.adidas.services.master.dto.WorkerStarterDto;
import com.adidas.services.master.exception.CommonMasterServiceException;
import com.adidas.services.master.property.AdidasLocales;
import com.adidas.services.master.property.ReebokLocales;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OperationServiceImpl implements OperationService {



    private final KafkaProducer kafkaProducer;


    private final AdidasLocales adidasLocales;


    private final ReebokLocales reebokLocales;

    @Autowired
    public OperationServiceImpl(KafkaProducer kafkaProducer, AdidasLocales adidasLocales, ReebokLocales reebokLocales) {
        this.kafkaProducer = kafkaProducer;
        this.adidasLocales = adidasLocales;
        this.reebokLocales = reebokLocales;
    }


    @Scheduled(cron = "${olap.full.feed.gen.schedule}")
    @Override
    public void launchWorkerOlapic() {
        log.warn("OLAPIC full feed started");
        WorkerStarterDto workerStarterDto = new WorkerStarterDto();
        Map<String, String> locales = adidasLocales.getLocales();
        final Map<String, String> localesReebok = reebokLocales.getLocales();
        locales.putAll(localesReebok);

        locales.forEach((key, value) -> {
            workerStarterDto.setLocale(value);
            if (localesReebok.containsKey(key)){
                workerStarterDto.setBrand(Brand.REEBOK);
            }else{
                workerStarterDto.setBrand(Brand.ADIDAS);
            }
            workerStarterDto.setFlow(MigrationFlow.FULL);
            workerStarterDto.setMigrationType(MigrationType.OLAPIC);
            run(workerStarterDto);
        });
        log.warn("OLAPIC full feed ended");
    }

    @Scheduled(cron = "${inventory.full.feed.gen.schedule}")
    @Override
    public void launchWorkerInventory() {
        log.warn("inventory feed started");
        WorkerStarterDto  workerStarterDto = new WorkerStarterDto();
        workerStarterDto.setFlow(MigrationFlow.FULL);
        workerStarterDto.setMigrationType(MigrationType.INVENTORY);
        run(workerStarterDto);
        log.warn("inventory full feed ended");
    }


    @Scheduled(cron = "${bv.full.feed.gen.schedule}")
    @Override
    public void launchWorkerBV() {
        log.warn("BV full feed started");
        WorkerStarterDto  workerStarterDto = new WorkerStarterDto();

        workerStarterDto.setFlow(MigrationFlow.FULL);
        workerStarterDto.setMigrationType(MigrationType.BAZAAR_VOICE);

        for (Brand value : Brand.values()) {
            workerStarterDto.setBrand(value);
            run(workerStarterDto);
        }
        log.warn("BV full feed ended");
    }

    @Override
    public String launchWorker() {
        return null;
    }

    @Override
    public void run(WorkerStarterDto workerStarterDto) {
        log.warn("event sending start");
        if (workerStarterDto.getMigrationType() == MigrationType.INVENTORY){
            throw new CommonMasterServiceException("functionality is not implemented yet");
        }
        kafkaProducer.sendMessage(toLaunchWorkerMessage(workerStarterDto));
        log.warn("event sending end");
    }


    private WorkerLaunch toLaunchWorkerMessage(WorkerStarterDto dto) {
        return WorkerLaunch.newBuilder()
                .setBrand(dto.getBrand().getBrandCode())
                .setId(dto.getUuid())
                .setFlow(dto.getFlow().name())
                .setConsumer(dto.getMigrationType().name())
                .setLocale(getLocale(dto))
                .build();

    }

    private List<String> getLocale(WorkerStarterDto dto) {
        final List<String> collect = new ArrayList<>();
                if (!StringUtils.isBlank(dto.getLocale())) {
                   return Arrays.stream(dto.getLocale().split(","))
                            .map(s -> s.replaceAll("[\\[\\]]", StringUtils.EMPTY))
                            .map(s -> s.replaceAll("\"", StringUtils.EMPTY))
                            .collect(Collectors.toList());
                }
        return collect;
    }
}
