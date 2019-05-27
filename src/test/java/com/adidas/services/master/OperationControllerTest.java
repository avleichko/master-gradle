package com.adidas.services.master;

import com.adidas.services.master.dto.Brand;
import com.adidas.services.master.dto.MigrationFlow;
import com.adidas.services.master.dto.MigrationType;
import com.adidas.services.master.dto.WorkerStarterDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.adidas.services.master.util.JsonUtil.objectToJson;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class OperationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        WorkerStarterDto workerStarterDto = new WorkerStarterDto();
        workerStarterDto.setBrand(Brand.ADIDAS);
        workerStarterDto.setFlow(MigrationFlow.FULL);
        workerStarterDto.setMigrationType(MigrationType.BAZAAR_VOICE);
        //workerStarterDto.setLocale("en-US");
        this.mockMvc.perform(post("/run")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectToJson(workerStarterDto)
                ))
                .andDo(print()).andExpect(status().isAccepted())
                .andDo(document("index", requestFields(
                        fieldWithPath("locale").description("filed is used for passinf locales"),
                        fieldWithPath("uuid").description("unique uuid"),
                        fieldWithPath("brand").description("brand filed is used for passing brand (adidas or reebok)"),
                        fieldWithPath("flow").description("Migration Flow: FULL or S3_SFTP"),
                        fieldWithPath("migrationType").description(" OLAPIC,\n" +
                                "    BAZAAR_VOICE,\n" +
                                "    INVENTORY")

                )));
    }

}
