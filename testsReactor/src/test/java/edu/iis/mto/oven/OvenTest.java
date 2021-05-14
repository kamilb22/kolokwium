package edu.iis.mto.oven;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    @Mock
    Fan fan;
    @Mock
    HeatingModule heatingModule;

    Oven oven;

    @BeforeEach
    void setUp(){
        oven = new Oven(heatingModule,fan);
    }

    @Test
    void ifHeatTypeIsThermoCirculationFanShouldBeTurnedOn() {
        int anyTemp = 20;
        ProgramStage programStage = ProgramStage.builder()
                .withHeat(HeatType.THERMO_CIRCULATION)
                .withStageTime(20)
                .withTargetTemp(200)
                .build();
        List<ProgramStage> stagesList = List.of(programStage);
        BakingProgram bakingProgram = BakingProgram.builder().withInitialTemp(anyTemp).withStages(stagesList).build();
        oven.start(bakingProgram);
        Mockito.verify(fan).on();
    }


}
