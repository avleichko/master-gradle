package com.adidas.services.master.aop;

import com.adidas.services.master.dto.MigrationFlow;
import com.adidas.services.master.dto.MigrationType;
import com.adidas.services.master.dto.WorkerStarterDto;
import com.adidas.services.master.exception.MasterServiceValidationException;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Aspect
@Configuration
@Slf4j
public class OperationControllerValidator {

    @After("execution(* com.adidas.services.master.controller.OperationController.runMigration(..)))")
    public void afterMethod(JoinPoint joinPoint){
        WorkerStarterDto workerStarterDto =  (WorkerStarterDto)joinPoint.getArgs()[0];
        if (!StringUtils.isEmpty(workerStarterDto.getLocale()) && workerStarterDto.getMigrationType() == MigrationType.BAZAAR_VOICE){
            throw new MasterServiceValidationException(MigrationType.BAZAAR_VOICE + " Can not be used with not empty locale");
        }
        if (workerStarterDto.getFlow()== MigrationFlow.DELTA && (workerStarterDto.getMigrationType() == MigrationType.BAZAAR_VOICE || workerStarterDto.getMigrationType()== MigrationType.OLAPIC)){
            throw new MasterServiceValidationException(MigrationType.BAZAAR_VOICE + " and "+ MigrationType.OLAPIC+ " Can not be used with " +MigrationFlow.DELTA);
        }
    }
}
